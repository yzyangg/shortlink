package org.yzy.shortlink.common.convention.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 全局返回实体
 *
 * @param <T> 泛型 返回的数据类型
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "0";
    private static final long serialVersionUID = 5679018624309023727L;
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    private String requestId;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }


}
