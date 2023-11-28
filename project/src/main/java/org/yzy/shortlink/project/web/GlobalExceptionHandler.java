package org.yzy.shortlink.project.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yzy.shortlink.project.common.convention.errorcode.BaseErrorCode;
import org.yzy.shortlink.project.common.convention.exception.AbstractException;
import org.yzy.shortlink.project.common.convention.result.Result;
import org.yzy.shortlink.project.common.convention.result.Results;

/**
 * @author yzy
 * @version 1.0
 * @description 全局异常处理器
 * @date 2023/11/19 22:06
 */
@Component
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidationExceptions(
            HttpServletRequest request, MethodArgumentNotValidException ex) {
        String exceptionStr = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("");
        logError(request, ex, exceptionStr);
        return ResponseEntity.badRequest().body(Results.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr));
    }

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<Result<Void>> handleAbstractException(
            HttpServletRequest request, AbstractException ex) {
        logError(request, ex, ex.toString());
        return ResponseEntity.badRequest().body(Results.failure(ex));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Result<Void>> handleDefaultError(
            HttpServletRequest request, Throwable throwable) {
        log.error("[{}] {}", request.getMethod(), getUrl(request), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Results.failure());
    }

    private void logError(HttpServletRequest request, Throwable ex, String message) {
        String url = getUrl(request);
        if (ex.getCause() != null) {
            log.error("[{}] {} [ex] {}", request.getMethod(), url, message, ex.getCause());
        } else {
            log.error("[{}] {} [ex] {}", request.getMethod(), url, message);
        }
    }

    private String getUrl(HttpServletRequest request) {
        if (StringUtils.isEmpty(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
