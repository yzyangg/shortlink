package org.yzy.shortlink.admin.common.convention.exception;

import lombok.Getter;
import org.springframework.util.StringUtils;
import org.yzy.shortlink.admin.common.convention.errorcode.IErrorCode;

import java.util.Optional;

/**
 * @author yzy
 * @version 1.0
 * @description 抽象异常
 * @date 2023/11/19 21:51
 */
@Getter
public abstract class AbstractException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(StringUtils.hasLength(message) ? message : null).orElse(errorCode.message());
    }
}
