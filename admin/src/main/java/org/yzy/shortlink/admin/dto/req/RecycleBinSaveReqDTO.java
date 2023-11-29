package org.yzy.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 回收站保存
 */
@Data
public class RecycleBinSaveReqDTO {
    /**
     * 分组标识
     */
    private  String gid;
    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
