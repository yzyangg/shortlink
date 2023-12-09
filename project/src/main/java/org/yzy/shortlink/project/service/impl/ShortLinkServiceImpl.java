package org.yzy.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.RBloomFilter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.common.convention.exception.ServiceException;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dao.entity.ShortLinkGotoDO;
import org.yzy.shortlink.project.dao.mapper.ShortLinkMapper;
import org.yzy.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.yzy.shortlink.project.service.ShortLinkGotoService;
import org.yzy.shortlink.project.service.ShortLinkService;
import org.yzy.shortlink.project.toolkit.HashUtil;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final ShortLinkGotoService shortLinkGotoService;
    private final StringRedisTemplate stringRedisTemplate;:

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
        try {
            baseMapper.insert(shortLinkDO);
            shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
            // 创建短链接路由记录
            ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder().fullShortUrl(fullShortUrl).gid(requestParam.getGid()).build();
            shortLinkGotoService.save(shortLinkGotoDO);
        } catch (Exception e) {
            throw new ServiceException("短链接创建失败");
        }

        ShortLinkCreateRespDTO shortLinkCreateRespDTO = new ShortLinkCreateRespDTO();
        BeanUtil.copyProperties(shortLinkDO, shortLinkCreateRespDTO);
        shortLinkCreateRespDTO.setFullShortLink("http:/" + fullShortUrl);
        return shortLinkCreateRespDTO;
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        Page<ShortLinkDO> page = new Page<ShortLinkDO>().setCurrent(requestParam.getCurrent()).setSize(requestParam.getSize());
        IPage<ShortLinkDO> shortLinkDOIPage = baseMapper.selectPage(page, lambdaQueryWrapper);
        return shortLinkDOIPage.convert(shortLinkDO -> {
            ShortLinkPageRespDTO shortLinkPageRespDTO = new ShortLinkPageRespDTO();
            BeanUtil.copyProperties(shortLinkDO, shortLinkPageRespDTO);
            return shortLinkPageRespDTO;
        });
    }

    @Override
    public ShortLinkDO updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0);


        ShortLinkDO one = this.baseMapper.selectOne(queryWrapper);
        Optional.ofNullable(one).orElseThrow(() -> new ServiceException("短链接不存在"));
        if (Objects.equals(requestParam.getGid(), one.getGid())) {
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, requestParam.getGid())
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .set(Objects.equals(requestParam.getValidDateType(), 0), ShortLinkDO::getValidDate, null);
            BeanUtil.copyProperties(requestParam, one);
            int update = baseMapper.update(one, updateWrapper);
            if (update == 1) {
                return one;
            } else {
                throw new ServiceException("更新失败");
            }
        } else {
            // 分组发生了变化，先删除后插
            LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, one.getFullShortUrl())
                    .eq(ShortLinkDO::getGid, one.getGid())
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .eq(ShortLinkDO::getDelFlag, 0);
            baseMapper.delete(updateWrapper);
            BeanUtil.copyProperties(requestParam, one);
            one.setId(null);
            int insert = baseMapper.insert(one);
            if (insert == 1) {
                return one;
            } else {
                throw new ServiceException("更新失败");
            }
        }

    }

    @Override
    public List<ShortLinkGroupCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam) {
        QueryWrapper<ShortLinkDO> wrapper = Wrappers.query(new ShortLinkDO())
                .select("gid as gid,count(*) as shortLinkCount")
                .in("gid", requestParam)
                .eq("enable_status", 0)
                .groupBy("gid");
        List<Map<String, Object>> shortLinkDOList = baseMapper.selectMaps(wrapper);
        return BeanUtil.copyToList(shortLinkDOList, ShortLinkGroupCountQueryRespDTO.class);
    }

    @Override
    public String restoreUrl(String shortUri, ServletRequest request, ServletResponse response) {
        // TODO 真实的跳转功能
        String serverName = request.getServerName();
        String fullShortUrl = StrBuilder.create(serverName).append("/").append(shortUri).toString();
        if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
            throw new ServiceException("短链接不存在");
        }
        LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class).eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
        ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoService.getOne(queryWrapper);
        Optional.ofNullable(shortLinkGotoDO).orElseThrow(() -> new ServiceException("短链接路由不存在"));

        // 通过分组id和完整短链接去找到原始链接
        String gid = shortLinkGotoDO.getGid();
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, gid)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getFullShortUrl, fullShortUrl);
        ShortLinkDO shortLinkDO = baseMapper.selectOne(lambdaQueryWrapper);

        if (Objects.isNull(shortLinkDO)) {
            throw new ServiceException("短链接不存在");
        }
        String originUrl = shortLinkDO.getOriginUrl();
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        return originUrl;

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
