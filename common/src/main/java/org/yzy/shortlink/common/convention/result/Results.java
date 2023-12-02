package org.yzy.shortlink.common.convention.result;


import org.yzy.shortlink.common.convention.exception.AbstractException;
import org.yzy.shortlink.common.enums.BaseErrorCode;

import java.util.Optional;

/**
 * 全局返回对象构造器
 */
public final class Results {

    /**
     * 构造成功响应
     */
    public static Result<Void> success() {
        return new Result<Void>()
                .setMessage("操作成功")
                .setCode(Result.SUCCESS_CODE);
    }

    /**
     * 构造成功响应
     */
    public static Result<Void> success(String message) {
        return new Result<Void>()
                .setMessage(message)
                .setCode(Result.SUCCESS_CODE);
    }

    /**
     * 构造带返回数据的成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setMessage("操作成功")
                .setCode(Result.SUCCESS_CODE)
                .setData(data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<T>()
                .setMessage(message)
                .setCode(Result.SUCCESS_CODE)
                .setData(data);
    }

    /**
     * 构建服务端失败响应
     */
    public static Result<Void> failure() {
        return new Result<Void>()
                .setCode(BaseErrorCode.SERVICE_ERROR.code())
                .setMessage(BaseErrorCode.SERVICE_ERROR.message());
    }

    /**
     * 通过 {@link AbstractException} 构建失败响应
     */
    public static Result<Void> failure(AbstractException abstractException) {
        String errorCode = Optional.ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.CLIENT_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    /**
     * 构建失败响应
     *
     * @param errorCode 错误码
     * @return 失败响应
     */
    public static Result<Void> failure(BaseErrorCode errorCode) {
        return new Result<Void>()
                .setCode(errorCode.code())
                .setMessage(errorCode.message());
    }

    /**
     * 通过 errorMessage 构建失败响应
     *
     * @param errorMessage 错误信息
     * @return 失败响应
     */
    public static Result<Void> failure(String code, String errorMessage) {
        return new Result<Void>()
                .setCode(code)
                .setMessage(errorMessage);
    }


}