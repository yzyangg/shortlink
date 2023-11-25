package org.yzy.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/20 0:09
 */
@Data
public class UserLoginReqDTO {
    /**
     * 用户名
     */
    private String username;
    // TODO 根据mail或者phone登录

    /**
     * 密码
     */
    private String password;
}
