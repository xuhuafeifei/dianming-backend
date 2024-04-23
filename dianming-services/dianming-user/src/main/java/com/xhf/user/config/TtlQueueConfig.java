package com.xhf.user.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * TTL队列
 */
//@Configuration
public class TtlQueueConfig {

    //普通交换机名称
    public static final String X_CHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_CHANGE = "Y";
    //普通队列
    public static final String QUEUE_B = "QB";
    //死信队列
    public static final String DEAD_QUEUE_D = "QD";

    //声明普通交换机
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_CHANGE);
    }
    //声明死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_CHANGE);
    }

    @Bean("queueB")
    public Queue queueB(){
        return QueueBuilder.durable(QUEUE_B)
                .deadLetterExchange(Y_DEAD_CHANGE) //死信交换机
                .deadLetterRoutingKey("YD")  //死信RoutingKey
                .ttl(40 * 1000)  //消息过期时间40s过期
                .build();
    }

    //死信队列
    @Bean("queueD")
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_QUEUE_D).build();
    }

    //绑定  X_CHANGE绑定queueB
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,@Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    //绑定  Y_CHANGE绑定queueD
    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,@Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}

