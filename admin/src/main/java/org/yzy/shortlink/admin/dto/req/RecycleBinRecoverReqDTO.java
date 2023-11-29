package org.yzy.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 回收站恢复
 */
@Data
public class RecycleBinRecoverReqDTO {
    /**
     * 分组标识
     */
    private  String gid;
    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
