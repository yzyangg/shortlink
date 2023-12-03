package org.yzy.shortlink.project.dto.req;


import lombok.Data;

/**
 * 短链接分页请求参数
 */
@Data
public class ShortLinkPageReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    protected long size;

    protected long current;
}
