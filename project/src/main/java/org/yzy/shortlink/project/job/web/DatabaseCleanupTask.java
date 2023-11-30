package org.yzy.shortlink.project.job.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yzy.shortlink.project.dao.entity.ShortLinkDO;
import org.yzy.shortlink.project.service.ShortLinkService;

import java.time.LocalDateTime;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/30 9:12
 */
@Component
@Slf4j
public class DatabaseCleanupTask {

    private final ShortLinkService shortLinkService;

    @Autowired
    public DatabaseCleanupTask(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @Scheduled(fixedRate = 2000) // 每两秒执行一次
    public void cleanupDatabase() {
        log.info("开始清理数据库");
        shortLinkService.remove(new QueryWrapper<ShortLinkDO>().lt("create_time", LocalDateTime.now().minusMonths(6)));
        log.info("清理数据库完成");
    }
}