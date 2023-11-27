package org.yzy.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dao.mapper.ShortLinkMapper;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.project.service.ShortLinkService;
import org.yzy.shortlink.project.util.HashUtil;


/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        String shortURI = generateSuffix(shortLinkCreateReqDTO);
        ShortLinkDO shortLinkDO = BeanUtil.copyProperties(shortLinkCreateReqDTO, ShortLinkDO.class);
        shortLinkDO.setShortUri(shortURI);
        shortLinkDO.setEnableStatus(0);
        shortLinkDO.setFullShortUrl(shortLinkCreateReqDTO.getDomain() + "/" + shortURI);
        baseMapper.insert(shortLinkDO);

        return BeanUtil.copyProperties(shortLinkDO, ShortLinkCreateRespDTO.class);
    }

    public static String generateSuffix(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {
        return HashUtil.generateShortLink(shortLinkCreateReqDTO.getOriginUrl());
    }
}
