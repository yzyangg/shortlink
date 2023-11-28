package org.yzy.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.yzy.shortlink.admin.common.convention.result.Results;
import org.yzy.shortlink.admin.common.enums.UserErrorCodeEnum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.yzy.shortlink.admin.service.impl.UserServiceImpl.LOGIN;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/26 13:12
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserTransmitFilter implements Filter {
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 不需要登录的接口
     */
    public static List<String> IGNORE_URLS = Arrays.asList("/api/short-link/user/login", "/api/short-link/user/register", "/api/short-link/user/has-username");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String username = request.getHeader("username");
        String token = request.getHeader("token");
        String requestURI = request.getRequestURI();

        if (!IGNORE_URLS.contains(requestURI)) {
            if (!StrUtil.isAllNotBlank(username, token)) {
                setResponse(servletResponse, JSONObject.toJSONString(Results.failure(UserErrorCodeEnum.USER_NOT_LOGIN)));
                return;
            }
            Object userInfoJsonStr = stringRedisTemplate.opsForHash().get(LOGIN + username, token);
            if (userInfoJsonStr == null) {
                setResponse(servletResponse, JSONObject.toJSONString(Results.failure(UserErrorCodeEnum.USER_NOT_LOGIN)));
                return;
            }

            UserInfoDTO userInfoDTO = JSONObject.parseObject(userInfoJsonStr.toString(), UserInfoDTO.class);
            UserContext.setUser(userInfoDTO);
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.remove();
        }
    }

    /**
     * 设置响应
     *
     * @param response
     * @param json
     */
    private void setResponse(ServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        } catch (IOException e) {
            log.error("设置响应失败", e);
        }
    }
}
