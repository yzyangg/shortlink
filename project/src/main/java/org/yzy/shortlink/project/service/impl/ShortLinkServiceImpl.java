package org.yzy.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.common.constant.RedisCacheConstant;
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
import org.yzy.shortlink.project.toolkit.LinkUtil;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

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
            // 缓存预热，创建时就放进缓存之中
            stringRedisTemplate.opsForValue().set(
                    RedisCacheConstant.GOTO_SHORTLINK_KEY + fullShortUrl,
                    requestParam.getOriginUrl(),
                    LinkUtil.getLinkCacheValidDate(requestParam.getValidDate()), TimeUnit.MILLISECONDS);
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

    /**
     * 通过短链接拿到原始链接 (暂时只返回原始连接)
     * TODO 真实的跳转功能
     */
    @Override
    public String restoreUrl(String shortUri, ServletRequest request, ServletResponse response) {
        String serverName = request.getServerName();
        String fullShortUrl = StrBuilder.create(serverName).append("/").append(shortUri).toString();
        String originUrl;
        // Redis 中查找完整短链接对应的原始链接
        originUrl = stringRedisTemplate.opsForValue().get(RedisCacheConstant.GOTO_SHORTLINK_KEY + fullShortUrl);
        if (StrUtil.isNotBlank(originUrl)) {
            return originUrl;
        }
        // 防止缓存穿透（布隆过滤器方式），在短链接创建时存入的fullShortUrl
        if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
            throw new ServiceException("短链接不存在");
        }


        // 防止缓存击穿 （加锁重建缓存）
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_GOTO_SHORTLINK_KEY + fullShortUrl);
        lock.lock();
        try {
            // 双重判断
            originUrl = stringRedisTemplate.opsForValue().get(RedisCacheConstant.GOTO_SHORTLINK_KEY + fullShortUrl);
            if (StrUtil.isBlank(originUrl)) {
                LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class).eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
                ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoService.getOne(queryWrapper);
                Optional.of(shortLinkGotoDO).orElseThrow(() -> new ServiceException("短链接路由不存在"));
                // 通过分组id和完整短链接去找到原始链接
                String gid = shortLinkGotoDO.getGid();
                LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                        .eq(ShortLinkDO::getGid, gid)
                        .eq(ShortLinkDO::getEnableStatus, 0)
                        .eq(ShortLinkDO::getDelFlag, 0)
                        .eq(ShortLinkDO::getFullShortUrl, fullShortUrl);
                ShortLinkDO shortLinkDO = baseMapper.selectOne(lambdaQueryWrapper);
                Optional.ofNullable(shortLinkDO).orElseThrow(() -> new ServiceException("短链接不存在"));
                // 是否过期
                if (shortLinkDO.getValidDate() != null && shortLinkDO.getValidDate().after(new Date())) {
                    return "";
                }
                // TODO 正儿八经的设置过期时间
                stringRedisTemplate.opsForValue().set(RedisCacheConstant.GOTO_SHORTLINK_KEY + fullShortUrl, shortLinkDO.getOriginUrl(), 30, TimeUnit.MINUTES);
                originUrl = shortLinkDO.getOriginUrl();
            }
        } finally {
            lock.unlock();
        }

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
