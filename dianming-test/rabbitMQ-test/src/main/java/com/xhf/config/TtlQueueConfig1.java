package com.xhf.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;


/**
 * TTL队列
 */
//@Configuration
public class TtlQueueConfig1 {

    //普通交换机名称
    public static final String TEST_EXCHANGE= "testExchange";
    //普通队列
    public static final String TEST_QUEUE = "testQueue";

    //声明普通交换机
    @Bean("testExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(TEST_EXCHANGE);
    }

    @Bean("testQueue")
    public Queue queueB(){
        return QueueBuilder.durable(TEST_QUEUE)
                .build();
    }

    //绑定  X_CHANGE绑定queueB
    @Bean
    public Binding queueBBindingX(@Qualifier("testQueue") Queue queue, @Qualifier("testExchange") DirectExchange testExchange){
        return BindingBuilder.bind(queue).to(testExchange).with("");
    }
}

