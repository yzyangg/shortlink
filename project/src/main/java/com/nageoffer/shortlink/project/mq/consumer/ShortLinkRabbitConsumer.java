package com.nageoffer.shortlink.project.mq.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nageoffer.shortlink.project.common.convention.exception.ServiceException;
import com.nageoffer.shortlink.project.dao.entity.*;
import com.nageoffer.shortlink.project.dao.mapper.*;
import com.nageoffer.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.nageoffer.shortlink.project.mq.TaskHandler;
import com.nageoffer.shortlink.project.mq.config.RabbitMQConfig;
import com.nageoffer.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler;
import com.nageoffer.shortlink.project.mq.vo.SendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.nageoffer.shortlink.project.common.constant.ShortLinkConstant.AMAP_REMOTE_URL;

/**
 * @author yzy
 * @date 2024/3/20 16:31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShortLinkRabbitConsumer {
    private final ShortLinkMapper shortLinkMapper;
    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;
    private final TaskHandler taskHandler;

    // 高德获取地理位置API KEY
    @Value("${short-link.stats.locale.amap-key}")
    private String statsLocaleAmapKey;

    /**
     * 拿RabbitMQ来说的话，消费者在消费完成一条消息之后会向MQ回复一个ACK（可以配置自动ACK或者手动ACK） 来告诉MQ这条消息已经消费了。
     * 假如当消费者消费完数据后，准备回执ACK时，系统挂掉了，MQ是不知道该条消息已经被消费了。
     * 所以重启之后MQ会再次发送该条消息，导致消息被重复消费，如果此时没有做幂等性处理，可能就会导致数据错误等问题。
     */
    @RabbitListener(queues = RabbitMQConfig.LINK_QUEUE)
    public void onMessage(SendVO vo) {
        taskHandler.submitTask(() -> handle(vo));
    }

    public void handle(SendVO vo) {
        long start = System.currentTimeMillis();
        log.info("消费者收到消息[{}]", vo);
        Map<String, String> producerMap = vo.getInfoMap();
        String id = producerMap.get("id");
        try {
            // 加上幂等标识
            if (!messageQueueIdempotentHandler.setAndCheckMessageProcessed(id)) {
                // 防止 消费未完成并且 未删除幂等标识
                // 必须在消费完成之后 才算 消息消费成功
                if (messageQueueIdempotentHandler.isAccomplish(id)) return;
                throw new ServiceException("消息未完成流程，需要消息队列重试");
            }
            String fullShortUrl = Optional.ofNullable(producerMap.get("fullShortUrl")).orElseThrow(() -> new ServiceException("找不到短链接"));
            String gid = producerMap.get("gid");
            ShortLinkStatsRecordDTO statsRecord = JSON.parseObject(producerMap.get("statsRecord"), ShortLinkStatsRecordDTO.class);
            actualSaveShortLinkStats(fullShortUrl, gid, statsRecord);
        } catch (ServiceException e) {
            log.info("消费者消费异常[{ }],即将重试", e);
            messageQueueIdempotentHandler.delMessageProcessed(id);
            throw new ServiceException("需要消息队列重试");
        }
        messageQueueIdempotentHandler.setAccomplish(id);
        long end = System.currentTimeMillis();
        log.info("当前消息{},消费耗时{}ms", id, end - start);
    }

    // 更新监控信息
    public void actualSaveShortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO statsRecord) {
        fullShortUrl = Optional.ofNullable(fullShortUrl).orElse(statsRecord.getFullShortUrl());
        LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
        ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(queryWrapper);
        gid = shortLinkGotoDO.getGid();
        int hour = DateUtil.hour(new Date(), true);
        Week week = DateUtil.dayOfWeekEnum(new Date());
        int weekValue = week.getIso8601Value();
        LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                .pv(1)
                .uv(statsRecord.getUvFirstFlag() ? 1 : 0)
                .uip(statsRecord.getUipFirstFlag() ? 1 : 0)
                .hour(hour)
                .weekday(weekValue)
                .fullShortUrl(fullShortUrl)
                .gid(gid)
                .date(new Date())
                .build();
        linkAccessStatsMapper.shortLinkStats(linkAccessStatsDO);
        Map<String, Object> localeParamMap = new HashMap<>();
        localeParamMap.put("key", statsLocaleAmapKey);
        localeParamMap.put("ip", statsRecord.getRemoteAddr());
        String localeResultStr = HttpUtil.get(AMAP_REMOTE_URL, localeParamMap);
        JSONObject localeResultObj = JSON.parseObject(localeResultStr);
        String infoCode = localeResultObj.getString("infocode");
        String actualProvince = "未知";
        String actualCity = "未知";
        if (StrUtil.isNotBlank(infoCode) && StrUtil.equals(infoCode, "10000")) {
            String province = localeResultObj.getString("province");
            boolean unknownFlag = StrUtil.equals(province, "[]");
            LinkLocaleStatsDO linkLocaleStatsDO = LinkLocaleStatsDO.builder()
                    .province(actualProvince = unknownFlag ? actualProvince : province)
                    .city(actualCity = unknownFlag ? actualCity : localeResultObj.getString("city"))
                    .adcode(unknownFlag ? "未知" : localeResultObj.getString("adcode"))
                    .cnt(1)
                    .fullShortUrl(fullShortUrl)
                    .country("中国")
                    .gid(gid)
                    .date(new Date())
                    .build();
            linkLocaleStatsMapper.shortLinkLocaleState(linkLocaleStatsDO);
        }
        LinkOsStatsDO linkOsStatsDO = LinkOsStatsDO.builder()
                .os(statsRecord.getOs())
                .cnt(1)
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .date(new Date())
                .build();
        linkOsStatsMapper.shortLinkOsState(linkOsStatsDO);
        LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder()
                .browser(statsRecord.getBrowser())
                .cnt(1)
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .date(new Date())
                .build();
        linkBrowserStatsMapper.shortLinkBrowserState(linkBrowserStatsDO);
        LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder()
                .device(statsRecord.getDevice())
                .cnt(1)
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .date(new Date())
                .build();
        linkDeviceStatsMapper.shortLinkDeviceState(linkDeviceStatsDO);
        LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder()
                .network(statsRecord.getNetwork())
                .cnt(1)
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .date(new Date())
                .build();
        linkNetworkStatsMapper.shortLinkNetworkState(linkNetworkStatsDO);
        LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder()
                .user(statsRecord.getUv())
                .ip(statsRecord.getRemoteAddr())
                .browser(statsRecord.getBrowser())
                .os(statsRecord.getOs())
                .network(statsRecord.getNetwork())
                .device(statsRecord.getDevice())
                .locale(StrUtil.join("-", "中国", actualProvince, actualCity))
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .build();
        linkAccessLogsMapper.insert(linkAccessLogsDO);
        shortLinkMapper.incrementStats(gid, fullShortUrl, 1, statsRecord.getUvFirstFlag() ? 1 : 0, statsRecord.getUipFirstFlag() ? 1 : 0);
        LinkStatsTodayDO linkStatsTodayDO = LinkStatsTodayDO.builder()
                .todayPv(1)
                .todayUv(statsRecord.getUvFirstFlag() ? 1 : 0)
                .todayUip(statsRecord.getUipFirstFlag() ? 1 : 0)
                .gid(gid)
                .fullShortUrl(fullShortUrl)
                .date(new Date())
                .build();
        linkStatsTodayMapper.shortLinkTodayState(linkStatsTodayDO);
    }
}
