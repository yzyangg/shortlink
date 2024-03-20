package com.nageoffer.shortlink.project.mq.consumer;

import com.nageoffer.shortlink.project.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author yzy
 * @date 2024/3/20 16:31
 */
@Component
public class RabbitConsumer {
    @RabbitListener(queues = {RabbitMQConfig.SHORT_LINK_MSG})
    public void onMessage(Object msg, Message message, Channel channel) {
        System.out.println("QUEUE_INFORM_EMAIL msg" + msg);
    }
}
