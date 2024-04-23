package com.xhf.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

@Slf4j
public class MQUtils {
    public static void convertAndSend(String exchange, String routingKey, final String msg,
                                      RabbitTemplate rabbitTemplate) {
        log.info("------------------------消息发送mq----------------------------");
        CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReceivedExchange(exchange);
        messageProperties.setReceivedRoutingKey(routingKey);
        data.setReturnedMessage(new Message(msg.getBytes(), messageProperties));
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, data);
        log.info("------------------------消息发送mq结束----------------------------");
    }

    public static void convertAndSend(String exchange, String routingKey, final String msg, String id,
                                      RabbitTemplate rabbitTemplate) {
        log.info("------------------------消息发送mq----------------------------");
        log.info("exchange:{}, routingKey:{}, msg:{}", exchange, routingKey, msg);
        CorrelationData data = new CorrelationData(id);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setReceivedExchange(exchange);
        messageProperties.setReceivedRoutingKey(routingKey);
        data.setReturnedMessage(new Message(msg.getBytes(), messageProperties));
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, data);
        log.info("------------------------消息发送mq结束----------------------------");
    }
}
