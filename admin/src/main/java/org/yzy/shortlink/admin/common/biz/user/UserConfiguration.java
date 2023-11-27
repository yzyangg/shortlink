package org.yzy.shortlink.admin.common.biz.user;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/26 13:16
 */
@Configuration
public class UserConfiguration {
    /**
     * 用户信息传递过滤器
     */
    @Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter(StringRedisTemplate stringRedisTemplate) {
        FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UserTransmitFilter(stringRedisTemplate));
        registration.addUrlPatterns("/*");
        // 排除登录接口
        registration.addInitParameter("exclusions", "/api/short-link/user/login");
        registration.setOrder(0);
        return registration;
    }
}
