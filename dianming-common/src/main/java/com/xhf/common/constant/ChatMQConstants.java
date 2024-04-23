package com.xhf.common.constant;

public interface ChatMQConstants {
    /*-----交换机-----*/
    String EXCHANGE_NAME = "chat_exchange";
    String BACKUP_EXCHANGE_NAME = "backup_chat_exchange";
    String DEAD_EXCHANGE_NAME = "chat_dead_exchange";
    /*-----队列-----*/
    String QUEUE_NAME = "chat_queue";
    String DEAD_QUEUE_NAME = "chat_dead_queue";
    String BACKUP_QUEUE_NAME = "backup_chat_queue";
    String WARING_QUEUE_NAME = "waring_chat_queue";
    /*-----路由key-----*/
    String ROUTING_KEY = "chat_routing_key";
    String DEAD_ROUTING_KEY = "chat_dead_routing_key";
    /*-----headers key-----*/
    String HEADER_KEY = "spring_returned_message_correlation";
}
