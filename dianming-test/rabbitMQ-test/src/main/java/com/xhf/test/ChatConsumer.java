package com.xhf.test;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

/**
 * 监听redis中chat信息同步服务
 */
//@Component
@Slf4j
public class ChatConsumer {

//    @RabbitListener(queues = "testQueue")
    public void receiveMessage(Message message, Channel channel){
        String msg = new String(message.getBody());
        System.out.println(msg);
    }
}
