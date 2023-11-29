package org.yzy.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.project.common.convention.errorcode.BaseErrorCode;
import org.yzy.shortlink.project.common.convention.exception.ServiceException;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dao.mapper.ShortLinkMapper;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.yzy.shortlink.project.service.ShortLinkService;
import org.yzy.shortlink.project.util.HashUtil;

import java.util.Objects;
import java.util.Optional;


/**
 * 短链接接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortURICreateCachePenetrationBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        Optional.ofNullable(requestParam).orElseThrow(() -> new ServiceException(BaseErrorCode.PARAM_EMPTY_ERROR));

        String shortURI = generateSuffix(requestParam);

        String fullShortUrl = StrBuilder.create(requestParam.getDomain()).append("/").append(shortURI).toString();

        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .originUrl(requestParam.getOriginUrl())
                .domain(requestParam.getDomain())
                .gid(requestParam.getGid())
                .createType(requestParam.getCreateType())
                .describe(requestParam.getDescribe())
                .validDate(requestParam.getValidDate())
                .validDateType(requestParam.getValidDateType())
                .shortUri(shortURI).enableStatus(0).fullShortUrl(fullShortUrl).build();
        // TODO 是否只允许一个短连接对应一个原始链接 ?
        try {
            baseMapper.insert(shortLinkDO);
        } catch (DuplicateKeyException e) {
            log.info("短链接已存在 {}", fullShortUrl);
            throw new ServiceException("短链接已存在，请勿重复创建");
        }
        shortURICreateCachePenetrationBloomFilter.add(fullShortUrl);
        return BeanUtil.copyProperties(shortLinkDO, ShortLinkCreateRespDTO.class);
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        Optional.ofNullable(requestParam).orElseThrow(() -> new ServiceException(BaseErrorCode.PARAM_EMPTY_ERROR));

        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.<ShortLinkDO>lambdaQuery()
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0);
        IPage<ShortLinkDO> shortLinkDOIPage = baseMapper.selectPage(requestParam.convert(), queryWrapper);
        return shortLinkDOIPage.convert(shortLinkDO -> BeanUtil.copyProperties(shortLinkDO, ShortLinkPageRespDTO.class));


    }

    /**
     * 生成短链接后缀
     *
     * @param shortLinkCreateReqDTO 短链接创建请求参数
     * @return 短链接后缀
     */
    public String generateSuffix(ShortLinkCreateReqDTO shortLinkCreateReqDTO) {

        String shortURI = "";
        String originUrl = shortLinkCreateReqDTO.getOriginUrl();
        String domain = shortLinkCreateReqDTO.getDomain();
        int maxCount = 10;
        int currentCount = 0;
        while (true) {
            if (currentCount >= maxCount) {
                throw new ServiceException("生成短链接失败");
            }
            shortURI = HashUtil.generateShortLink(originUrl);
            shortURI += System.currentTimeMillis();
            if (!shortURICreateCachePenetrationBloomFilter.contains(domain + "/" + shortURI)) {
                break;
            }
            currentCount++;
        }
        return HashUtil.generateShortLink(Objects.requireNonNull(shortURI));
    }
}
