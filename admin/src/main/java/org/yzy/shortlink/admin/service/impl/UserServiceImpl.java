package org.yzy.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
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
import org.springframework.stereotype.Service;
import org.yzy.shortlink.admin.common.convention.exception.ClientException;
import org.yzy.shortlink.admin.common.enums.UserErrorCodeEnum;
import org.yzy.shortlink.admin.dao.entity.UserDO;
import org.yzy.shortlink.admin.dao.mapper.UserMapper;
import org.yzy.shortlink.admin.dto.req.UserLoginReqDTO;
import org.yzy.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.yzy.shortlink.admin.dto.req.UserUpdateReqDTO;
import org.yzy.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.yzy.shortlink.admin.dto.resp.UserRespDTO;
import org.yzy.shortlink.admin.service.UserService;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 17:50
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    public static final String LOGIN = "login_";
    /**
     * 用户注册缓存穿透布隆过滤器
     */
    public final RBloomFilter<String> USER_REGISTER_CACHE_PENETRATION_BLOOM_FILTER;

    public final RedissonClient redissonClient;

    public final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(wrapper);
        Optional.ofNullable(userDO).orElseThrow(() -> new ClientException(UserErrorCodeEnum.USER_NOT_EXIST));
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
        return USER_REGISTER_CACHE_PENETRATION_BLOOM_FILTER.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO userRegisterReqDTO) {
        String username = userRegisterReqDTO.getUsername();

        if (hasUsername(username)) {
            throw new ClientException(UserErrorCodeEnum.USER_EXIST);
        }

        // 分布式锁
        RLock lock = redissonClient.getLock(username);
        try {
            if (lock.tryLock()) {
                int success = baseMapper.insert(BeanUtil.copyProperties(userRegisterReqDTO, UserDO.class));
                if (success < 1) {
                    throw new ClientException(UserErrorCodeEnum.USER_REGISTER_FAIL);
                }
                USER_REGISTER_CACHE_PENETRATION_BLOOM_FILTER.add(username);
                return;
            }
            throw new ClientException(UserErrorCodeEnum.USER_REGISTER_FAIL);

        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDTO userUpdateReqDTO) {
        // TODO 校验用户是否存在
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class).eq(UserDO::getUsername, userUpdateReqDTO.getUsername());
        baseMapper.update(BeanUtil.copyProperties(userUpdateReqDTO, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO) {
        // TODO 校验用户是否存在
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, userLoginReqDTO.getUsername())
                .eq(UserDO::getPassword, userLoginReqDTO.getPassword());
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        Optional.ofNullable(userDO).orElseThrow(() -> new ClientException(UserErrorCodeEnum.USER_NOT_EXIST));

        // 不能重复登陆
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(LOGIN + userLoginReqDTO.getUsername()))) {
            throw new ClientException(UserErrorCodeEnum.USER_ALREADY_LOGIN);
        }

        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(LOGIN + userLoginReqDTO.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(LOGIN + userLoginReqDTO.getUsername(), 30, TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String token, String username) {
        String redisTokenValue = (String) stringRedisTemplate.opsForHash().get(LOGIN + username, token);

        // 检查token是否一致 一致则刷新过期时间
        if (redisTokenValue != null) {
            stringRedisTemplate.expire(LOGIN + username, 30, TimeUnit.DAYS);
            return true;
        }
        return false;
    }

    @Override
    public void logout(String token, String username) {
        if (checkLogin(token, username)) {
            stringRedisTemplate.opsForHash().delete(LOGIN + username, token);
        }
    }


}
