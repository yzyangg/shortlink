package org.yzy.shortlink.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.yzy.shortlink.project.BaseTest;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;

import java.time.LocalDateTime;

class ShortLinkServiceImplTest extends BaseTest {

    @Resource
    private ShortLinkServiceImpl shortLinkService;

    @Test
    public void testJob() {
        // 删除数据哭内半年前的数据
        // 删除所有数据
        LambdaQueryWrapper<ShortLinkDO> wrapper = new LambdaQueryWrapper<ShortLinkDO>().lt(ShortLinkDO::getCreateTime, LocalDateTime.now());
        shortLinkService.remove(wrapper);
    }

    @Test
    public void testJob2() {
        // 删除数据哭内半年前的数据
        // 删除所有数据
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, "55")
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);

        for (ShortLinkDO shortLinkDO : shortLinkService.list(lambdaQueryWrapper)) {
            System.out.println(shortLinkDO);
        }
    }
}