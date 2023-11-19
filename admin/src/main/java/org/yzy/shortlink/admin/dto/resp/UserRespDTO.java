package org.yzy.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.yzy.shortlink.admin.serialize.PhoneDesensitizationSerializer;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 17:56
 */
@Data
public class UserRespDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     * 使用自定义的序列化器
     */
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

}
