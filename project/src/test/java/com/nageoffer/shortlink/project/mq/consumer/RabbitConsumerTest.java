package com.nageoffer.shortlink.project.mq.consumer;


import com.nageoffer.shortlink.project.mq.producer.ShortLinkRabbitMQProducer;
import com.nageoffer.shortlink.project.mq.vo.SendVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class RabbitConsumerTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Resource
    ShortLinkRabbitMQProducer producer;

    @Test
    public void Producer_topics_springbootTest() {
        SendVO vo = new SendVO();
        HashMap<String, String> infoMap = new HashMap();
        infoMap.put("nihao", "nihao");
        vo.setInfoMap(infoMap);
        producer.send(vo);

    }

}