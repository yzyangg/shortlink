package com.nageoffer.shortlink.project.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yzy
 * @date 2024/3/20 16:21
 */
@Configuration
public class RabbitMQConfig {
    public static final String LINK_EXCHANGE = "link_exchange";
    public static final String LINK_QUEUE = "link_queue";

    public static final String LINK_ROUTING_KEY = "link.#.key";

    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(RabbitMQConfig.LINK_EXCHANGE);
    }


    @Bean
    public Queue notifyMsgQueue() {
        return new Queue(RabbitMQConfig.LINK_QUEUE, true);
    }


    @Bean
    public Binding notifyMsgQueueBinding() {
        return BindingBuilder
                .bind(notifyMsgQueue())
                .to(defaultExchange())
                .with(RabbitMQConfig.LINK_ROUTING_KEY);

    }

}
