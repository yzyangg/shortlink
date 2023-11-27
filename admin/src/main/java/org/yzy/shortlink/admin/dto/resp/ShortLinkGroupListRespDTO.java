package org.yzy.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/26 12:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkGroupListRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;

}
