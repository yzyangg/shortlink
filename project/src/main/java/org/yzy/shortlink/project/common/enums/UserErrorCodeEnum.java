package org.yzy.shortlink.project.common.enums;


import org.yzy.shortlink.project.common.convention.errorcode.IErrorCode;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 18:41
 */
public enum UserErrorCodeEnum implements IErrorCode {
    USER_NOT_LOGIN("B000201", "用户未登录"),
    USER_NOT_EXIST("B000200", "用户不存在"),
    USER_NOT_AUTH("B000202", "用户无权限"),

    USER_EXIST("B000203", "用户已存在"),
    USER_REGISTER_FAIL("B000204", "用户注册失败"),
    USER_ALREADY_LOGIN("B000205", "用户已登录");


    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    public final String message;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
