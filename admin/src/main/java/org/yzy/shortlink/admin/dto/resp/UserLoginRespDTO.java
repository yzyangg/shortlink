package org.yzy.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 17:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRespDTO {
    /**
     * token
     */
    String token;

}
