package org.yzy.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.admin.common.convention.exception.ClientException;
import org.yzy.shortlink.admin.common.enums.UserErrorCodeEnum;
import org.yzy.shortlink.admin.dao.entity.UserDO;
import org.yzy.shortlink.admin.dao.mapper.UserMapper;
import org.yzy.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.yzy.shortlink.admin.dto.resp.UserRespDTO;
import org.yzy.shortlink.admin.service.UserService;

import java.util.Optional;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 17:50
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    /**
     * 用户注册缓存穿透布隆过滤器
     */
    public final RBloomFilter<String> USER_REGISTER_CACHE_PENETRATION_BLOOM_FILTER;

    public final RedissonClient redissonClient;

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
            lock.unlock();
        }
    }
}
