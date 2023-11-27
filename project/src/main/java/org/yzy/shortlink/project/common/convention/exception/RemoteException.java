package org.yzy.shortlink.project.common.convention.exception;


import org.yzy.shortlink.project.common.convention.errorcode.BaseErrorCode;
import org.yzy.shortlink.project.common.convention.errorcode.IErrorCode;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 22:00
 */
public class RemoteException extends AbstractException {

    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }

}
