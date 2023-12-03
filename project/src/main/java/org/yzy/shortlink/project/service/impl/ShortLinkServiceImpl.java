package org.yzy.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.common.convention.exception.ServiceException;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dao.mapper.ShortLinkMapper;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.project.service.ShortLinkService;
import org.yzy.shortlink.project.toolkit.HashUtil;

import java.net.URL;
import java.util.Optional;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/30 9:06
 */
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String favicon = getFavicon(requestParam.getOriginUrl());
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(requestParam.getDomain())
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(requestParam.getDomain())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createType(requestParam.getCreateType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortUrl)
                .favicon(favicon)
                .build();
        ShortLinkCreateRespDTO shortLinkCreateRespDTO = new ShortLinkCreateRespDTO();
        BeanUtil.copyProperties(requestParam, shortLinkCreateRespDTO);
        baseMapper.insert(shortLinkDO);
        return shortLinkCreateRespDTO;
    }


    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String originUrl = requestParam.getOriginUrl();
        String shortUri;
        do {
            if (customGenerateCount > 10) throw new ServiceException("短链接生成频繁");
            originUrl += System.currentTimeMillis();
            shortUri = HashUtil.hashToBase62(originUrl);
            if (!shortUriCreateCachePenetrationBloomFilter.contains(requestParam.getDomain() + "/" + shortUri)) break;
            else customGenerateCount++;
        } while (true);
        return shortUri;
    }

    @SneakyThrows
    private String getFavicon(String requestUrl) {
        URL url = new URL(requestUrl);
        Optional.of(url).orElseThrow(() -> new ServiceException("URL存在问题"));
        String protocol = url.getProtocol();
        String host = url.getHost();
        return protocol + "://" + host + "/favicon.ico";
    }
}
