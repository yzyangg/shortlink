package com.nageoffer.shortlink.project.mq.producer;

import com.nageoffer.shortlink.project.mq.config.RabbitMQConfig;
import com.nageoffer.shortlink.project.mq.vo.SendVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.nageoffer.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler.IDEMPOTENT_KEY_PREFIX;

/**
 * @author yzy
 * @date 2024/3/21 21:48
 */

@Slf4j
@Component
public class ShortLinkRabbitMQProducer implements RabbitTemplate.ConfirmCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;



    public void send(SendVO sendVO) {
        log.debug("producer 生产消息[{}]", sendVO);
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.LINK_EXCHANGE
                , RabbitMQConfig.LINK_ROUTING_KEY, sendVO);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        if (!ack) {
            log.info("消费者发送异常! {}", correlationData);
        } else {
            log.info("消费者发送成功! {}", correlationData);
        }
    }
}
