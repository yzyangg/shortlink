package org.yzy.shortlink.admin.biz.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    /**
     * userID
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
     * 密码
     */
    private String password;
}
