package org.yzy.shortlink.project.dto.req;

import lombok.Data;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2024/1/14 14:02
 */
@Data
public class RecycleBinRecoverReqDTO {
    String fullShortUrl;
    String gid;
}
