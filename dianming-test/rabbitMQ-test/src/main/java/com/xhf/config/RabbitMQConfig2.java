package com.xhf.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQConfig2 {

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME="confirm_exchange";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME="confirm_queue";

    /**
     * 创建一个队列
     * @return
     */
    @Bean("confirmQueue")
    Queue confirmQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /**
     * 创建一个交换机
     * @return
     */
    @Bean("confirmExchange")
    Exchange confirmExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    /**
     *
     * @param exchange 交换机对象
     * @param queue 队列对象
     * @return
     */
    @Bean
    Binding confirmExchange(@Qualifier("confirmExchange") Exchange exchange,@Qualifier("confirmQueue") Queue queue){
        //bind(queue) 绑定队列 to(exchange) 绑定交换机 with("") routingKey这里绑定的是空白可以按照自己的要求绑定  noargs() 没有参数
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
}
