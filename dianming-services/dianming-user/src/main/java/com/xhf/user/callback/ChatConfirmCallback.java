package com.xhf.user.callback;

import com.xhf.user.service.MqMessageInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class ChatConfirmCallback implements RabbitTemplate.ConfirmCallback {
    private final MqMessageInfoService messageInfoService;

    public ChatConfirmCallback(MqMessageInfoService messageInfoService) {
        this.messageInfoService = messageInfoService;
    }

    /**
     * Confirmation callback.
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 如果发布失败, 或者路由转发失败, 存储失败数据
        messageInfoService.saveInfo(correlationData, ack, cause);
    }
}
