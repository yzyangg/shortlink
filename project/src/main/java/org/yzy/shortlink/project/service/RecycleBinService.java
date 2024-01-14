package org.yzy.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.yzy.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2024/1/14 14:09
 */
public interface RecycleBinService extends IService<ShortLinkDO> {
    void saveRecycleBin(RecycleBinSaveReqDTO req);

    IPage<ShortLinkPageRespDTO> pageRecycleBin(ShortLinkPageReqDTO shortLinkPageReqDTO);

    void restoreRecycleBin(RecycleBinRecoverReqDTO recycleBinSaveReqDTO);
}
