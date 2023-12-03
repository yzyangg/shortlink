package org.yzy.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/30 9:05
 */
public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 创建短链接
     *
     * @param requestParam 请求参数
     * @return 短链接
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);
}
