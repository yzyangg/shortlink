package org.yzy.shortlink.admin.common.biz.user;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.Optional;

import static org.yzy.shortlink.admin.service.impl.UserServiceImpl.LOGIN;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/26 13:12
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class UserTransmitFilter implements Filter {
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String username = request.getHeader("username");
        String token = request.getHeader("token");
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/short-link/user/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        Object userInfoJsonStr = stringRedisTemplate.opsForHash().get(LOGIN + username, token);
        Optional.ofNullable(userInfoJsonStr).orElseThrow(() -> new RuntimeException("用户未登录"));

        UserInfoDTO userInfoDTO = JSONObject.parseObject(userInfoJsonStr.toString(), UserInfoDTO.class);
        UserContext.setUser(userInfoDTO);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.remove();
        }


    }
}
