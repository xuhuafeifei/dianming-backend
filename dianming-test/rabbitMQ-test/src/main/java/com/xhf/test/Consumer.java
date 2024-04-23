package com.xhf.test;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Consumer {
    @RabbitListener(queues = "confirm_queue")
    public void test(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println(new String(message.getBody()));
            // 手动签收
            channel.basicAck(tag, true);
        } catch (Exception e) {
            // 消息打回
            channel.basicNack(tag, true, true);
        }
    }
}
