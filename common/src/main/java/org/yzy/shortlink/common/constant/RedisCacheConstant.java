package org.yzy.shortlink.common.constant;

/**
 * 短链接后管 Redis 缓存常量类
 */
public class RedisCacheConstant {

    public static final String LOCK_USER_REGISTER_KEY = "shortLink:lock:user-register:";
    public static final String USER_LOGIN_KEY = "shortLink:user:login_";

    /**
     * 短链接跳转
     */
    public static final String GOTO_SHORTLINK_KEY = "shortLink:go-to-url:";
    /**
     * 短链接跳转锁
     */
    public static final String LOCK_GOTO_SHORTLINK_KEY = "shortLink:lock:go-to-url:";

    /**
     * 短链接空跳转
     */
    public static final String GOTO_IS_NULL_SHORTLINK_KEY = "shortLink:null_go-to-url:";
}
