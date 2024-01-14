package org.yzy.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.yzy.shortlink.common.constant.RedisCacheConstant;
import org.yzy.shortlink.common.convention.exception.ServiceException;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.dao.mapper.ShortLinkMapper;
import org.yzy.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import org.yzy.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import org.yzy.shortlink.project.dto.req.ShortLinkPageReqDTO;
import org.yzy.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import org.yzy.shortlink.project.service.RecycleBinService;

import java.util.Optional;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2024/1/14 14:10
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {
    public final StringRedisTemplate stringRedisTemplate;
    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO req) {
        Optional.ofNullable(this.getOne(Wrappers.lambdaQuery(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, req.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, req.getGid())
                        .eq(ShortLinkDO::getEnableStatus, 0)
                        .eq(ShortLinkDO::getDelFlag, 0)))
                .orElseThrow(() -> new ServiceException("短链接不存在或已放入回收站"));

        LambdaUpdateWrapper<ShortLinkDO> wrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, req.getFullShortUrl())
                .eq(ShortLinkDO::getGid, req.getGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .enableStatus(1)
                .build();

        // 删除缓存
        this.update(shortLinkDO, wrapper);
        Boolean delete = stringRedisTemplate.delete(RedisCacheConstant.GOTO_SHORTLINK_KEY + req.getFullShortUrl());
        log.info("删除缓存结果：{}", delete);
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageRecycleBin(ShortLinkPageReqDTO shortLinkPageReqDTO) {
        LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getGid, shortLinkPageReqDTO.getGid())
                .orderByDesc(ShortLinkDO::getCreateTime);
        Page<ShortLinkDO> page = new Page<ShortLinkDO>().setCurrent(shortLinkPageReqDTO.getCurrent())
                .setSize(shortLinkPageReqDTO.getSize());
        return baseMapper.selectPage(page, wrapper).convert(shortLinkDO -> {
            ShortLinkPageRespDTO shortLinkPageRespDTO = new ShortLinkPageRespDTO();
            BeanUtil.copyProperties(shortLinkDO, shortLinkPageRespDTO);
            return shortLinkPageRespDTO;
        });


    }

    @Override
    public void restoreRecycleBin(RecycleBinRecoverReqDTO req) {
        Optional.ofNullable(this.getOne(Wrappers.lambdaQuery(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, req.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, req.getGid())
                        .eq(ShortLinkDO::getEnableStatus, 1)
                        .eq(ShortLinkDO::getDelFlag, 0)))
                .orElseThrow(() -> new ServiceException("短链接不存在"));

        LambdaUpdateWrapper<ShortLinkDO> wrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, req.getFullShortUrl())
                .eq(ShortLinkDO::getGid, req.getGid())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .enableStatus(0)
                .build();

        // 也可以不用重建缓存 从业务上分析 一个已经被放入过回收站的链接，不可能是热点数据
        this.update(shortLinkDO, wrapper);
    }
}
