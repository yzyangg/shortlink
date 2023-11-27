package org.yzy.shortlink.project.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 23:51
 */
@Configuration
public class RBloomFilterConfiguration {
    /**
     * 用户注册缓存穿透布隆过滤器
     *
     * @param redissonClient redisson客户端
     * @return 布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter =
                redissonClient.getBloomFilter("RBloomFilterConfiguration");
        // 预计元素个数 : 误判率
        cachePenetrationBloomFilter.tryInit(10000000L, 0.001);
        return cachePenetrationBloomFilter;
    }
}
