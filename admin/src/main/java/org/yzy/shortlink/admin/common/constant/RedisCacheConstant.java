package org.yzy.shortlink.admin.common.constant;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/20 1:08
 */
public class RedisCacheConstant {
    /**
     * 用户注册缓存穿透布隆过滤器
     */
    public static final String USER_REGISTER_CACHE_PENETRATION_BLOOM_FILTER = "user_register_cache_penetration_bloom_filter";

    public static final String USER_REGISTER_LOCK = "short_link_lock";

    public static final String USER_LOGIN_TOKEN = "short_link_token";
}
