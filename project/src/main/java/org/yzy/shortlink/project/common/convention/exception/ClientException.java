package org.yzy.shortlink.project.common.convention.exception;


import org.yzy.shortlink.project.common.convention.errorcode.BaseErrorCode;
import org.yzy.shortlink.project.common.convention.errorcode.IErrorCode;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 21:53
 */
public class ClientException extends AbstractException {


    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }

}
