package org.yzy.shortlink.admin.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author yzy
 * @version 1.0
 * @description 访问日志拦截器
 * @date 2023/12/1 0:45
 */
@Slf4j
public class AccessLogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 在请求处理之前调用，记录请求开始时间
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求处理完成后调用，记录耗时信息
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        log.info("Request URL: " + requestUrl + ", Method: " + method + ", Duration: " + duration + "ms");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

}
