package org.yzy.shortlink.admin.biz.user;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.yzy.shortlink.common.constant.RedisCacheConstant;
import org.yzy.shortlink.common.constant.UserConstant;
import org.yzy.shortlink.common.convention.exception.ClientException;
import org.yzy.shortlink.common.enums.UserErrorCodeEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户信息传输过滤器
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    public static final String POST = "POST";
    private static final List<String> IGNORE_URI = Lists.newArrayList(
            "/api/shortLink/admin/v1/user/login",
            "/api/shortLink/admin/v1/user/has-username"
    );
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        if (!IGNORE_URI.contains(requestURI)) {
            String method = httpServletRequest.getMethod();
            // 不是登录接口，需要校验用户信息
            if (!(Objects.equals(requestURI, "/api/shortLink/admin/v1/user") && Objects.equals(method, POST))) {
                String token = httpServletRequest.getHeader(UserConstant.USER_TOKEN_KEY);
                if (!StrUtil.isAllNotBlank(token)) {
                    throw new ClientException(UserErrorCodeEnum.USER_TOKEN_FAIL);
                }
                String key = RedisCacheConstant.USER_LOGIN_KEY + token;

                UserInfoDTO userInfoDTO;
                try {
                    Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
                    if (userMap.isEmpty()) throw new ClientException(UserErrorCodeEnum.USER_TOKEN_FAIL);
                    userInfoDTO = BeanUtil.mapToBean(userMap, UserInfoDTO.class, false);
                    if (userInfoDTO == null) throw new ClientException(UserErrorCodeEnum.USER_TOKEN_FAIL);
                } catch (Exception ex) {
                    throw new ClientException(UserErrorCodeEnum.USER_TOKEN_FAIL);
                }
                UserContext.setUser(userInfoDTO);
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
