package org.yzy.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

/**
 * 短链接接口层
 */
public interface ShortLinkService extends IService<ShortLinkDO> {


    /**
     * 创建短链接
     *
     * @param shortLinkCreateReqDTO 短链接创建请求参数
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO shortLinkCreateReqDTO);
}
