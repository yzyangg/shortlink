package org.yzy.shortlink.admin.common.convention.errorcode;

/**
 * @author yzy
 * @version 1.0
 * @description 异常码
 * @date 2023/11/19 18:36
 */
public interface IErrorCode {
    /**
     * 返回码
     *
     * @return
     */
    String code();

    /**
     * 返回消息
     *
     * @return
     */
    String message();

}
