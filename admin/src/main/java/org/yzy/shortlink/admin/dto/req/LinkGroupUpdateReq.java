package org.yzy.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/26 14:12
 */
@Data
public class LinkGroupUpdateReq {
    /**
     * ID
     */
    private Long id;

    /**
     * 分组名称
     */
    private String name;
}
