package org.yzy.shortlink.admin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.admin.biz.user.UserContext;
import org.yzy.shortlink.admin.dao.entity.UserDO;
import org.yzy.shortlink.admin.dao.mapper.UserMapper;
import org.yzy.shortlink.admin.dto.request.UserLoginReqDTO;
import org.yzy.shortlink.admin.dto.request.UserRegisterReqDTO;
import org.yzy.shortlink.admin.dto.request.UserUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.yzy.shortlink.admin.dto.resp.UserRespDTO;
import org.yzy.shortlink.admin.service.UserService;
import org.yzy.shortlink.admin.toolkit.JWTUtil;
import org.yzy.shortlink.common.convention.exception.ClientException;
import org.yzy.shortlink.common.enums.BaseErrorCode;
import org.yzy.shortlink.common.enums.UserErrorCodeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.yzy.shortlink.common.constant.RedisCacheConstant.USER_LOGIN_KEY;


/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        Optional.ofNullable(userDO).orElseThrow(() -> new ClientException(UserErrorCodeEnum.USER_NULL));
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        Optional.ofNullable(requestParam).orElseThrow(() -> new ClientException(BaseErrorCode.PARAM_EMPTY));
        String username = requestParam.getUsername();
        if (hasUsername(username)) {
            throw new ClientException(UserErrorCodeEnum.USER_EXIST);
        }

        RLock lock = redissonClient.getLock(USER_LOGIN_KEY + username);
        try {
            if (lock.tryLock()) {
                UserDO userDO = BeanUtil.toBean(requestParam, UserDO.class);
                baseMapper.insert(userDO);
                userRegisterCachePenetrationBloomFilter.add(username);
            } else {
                throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
            }
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void update(UserUpdateReqDTO requestParam) {
        Optional.ofNullable(requestParam).orElseThrow(() -> new ClientException(BaseErrorCode.PARAM_EMPTY));
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class).eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // TODO 为什么这行报错
//        if (Objects.isNull(UserContext.getUser())) throw new ClientException("用户已登录");

        LambdaQueryWrapper<UserDO> lambdaQueryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, requestParam.getUsername()).eq(UserDO::getPassword, requestParam.getPassword()).eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(lambdaQueryWrapper);
        Optional.ofNullable(userDO).orElseThrow(() -> new ClientException(UserErrorCodeEnum.USER_NULL));


        Map<String, String> map = new HashMap<>();
        map.put("username", userDO.getUsername());
        String token = JWTUtil.genToken(map);
        Map<String, String> userMap = new DecoratingStringHashMapper<>(new Jackson2HashMapper(true)).toHash(userDO);
        String redisKey = USER_LOGIN_KEY + token;
        if (checkLogin(token)) throw new ClientException("用户已登录");
        stringRedisTemplate.opsForHash().putAll(redisKey, userMap);
        stringRedisTemplate.expire(redisKey, 30, TimeUnit.DAYS);

        return new UserLoginRespDTO(token);
    }

    @Override
    public Boolean checkLogin(String token) {
        String key = USER_LOGIN_KEY + token;
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    public void logout(String token) {
        Optional.ofNullable(token).orElseThrow(() -> new ClientException(BaseErrorCode.TOKEN_EMPTY));
        String key = USER_LOGIN_KEY + token;
        if (checkLogin(token)) {
            stringRedisTemplate.delete(key);
            UserContext.removeUser();
            return;
        }
        throw new ClientException("用户token不存在或用户未登录");
    }
}




