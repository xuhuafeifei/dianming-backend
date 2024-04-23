package com.xhf.user.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.xhf.common.constant.ChatMQConstants.*;

@Configuration
public class TTLConfig {


    // 创建交换机
    @Bean("chatExchange")
    public DirectExchange chatExchange() {
        return ExchangeBuilder
                .directExchange(EXCHANGE_NAME)
                .alternate(BACKUP_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 创建死信交换机
    @Bean("chatDeadExchange")
    public DirectExchange chatDeadExchange() {
        return ExchangeBuilder
                .directExchange(DEAD_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 创建队列
    @Bean("chatQueue")
    public Queue chatQueue() {
        return QueueBuilder
                .durable(QUEUE_NAME)
                .deadLetterExchange(DEAD_EXCHANGE_NAME)
                .deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .ttl(40 * 1000)
                .build();
    }

    // 创建死信队列
    @Bean("chatDeadQueue")
    public Queue chatDeadQueue() {
        return QueueBuilder
                .durable(DEAD_QUEUE_NAME)
                .build();
    }

    // 绑定chat_exchange -> chat_queue
    @Bean
    public Binding chatExchangeBindingToChatQueue(
            @Qualifier("chatExchange") DirectExchange chatExchange,
            @Qualifier("chatQueue") Queue chatQueue
    ) {
        return BindingBuilder.bind(chatQueue).to(chatExchange).with(ROUTING_KEY);
    }

    // 绑定chat_dead_exchange -> chat_dead_queue
    @Bean
    public Binding chatDeadExchangeBindingToChatQueue(
            @Qualifier("chatDeadExchange") DirectExchange chatDeadExchange,
            @Qualifier("chatDeadQueue") Queue chatDeadQueue
    ) {
        return BindingBuilder.bind(chatDeadQueue).to(chatDeadExchange).with(DEAD_ROUTING_KEY);
    }

    // 创建备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return ExchangeBuilder
                .fanoutExchange(BACKUP_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    // 创建警告队列
    @Bean("waringQueue")
    public Queue waringQueue() {
        return QueueBuilder
                .durable(WARING_QUEUE_NAME)
                .build();
    }

    // 创建备份队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder
                .durable(BACKUP_QUEUE_NAME)
                .build();
    }

    // 绑定backupExchange -> waringQueue
    @Bean
    public Binding backupExchangeBindingToWaringQueue(
            @Qualifier("backupExchange") FanoutExchange backupExchange,
            @Qualifier("waringQueue") Queue waringQueue
    ) {
        return BindingBuilder.bind(waringQueue).to(backupExchange);
    }

    // 绑定backupExchange -> backupQueue
    @Bean
    public Binding backupExchangeBindingToBackupQueue(
            @Qualifier("backupExchange") FanoutExchange backupExchange,
            @Qualifier("backupQueue") Queue backupQueue
    ) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
}
