package com.xhf.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xhf.common.utils.R;
import com.xhf.model.user.entity.MqMessageInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.model.user.entity.MqWaringInfo;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 *
 */
public interface MqMessageInfoService extends IService<MqMessageInfo> {
    /**
     * 存储发布到mq的信息
     * @param correlationData
     * @param ack
     * @param cause
     */
    void saveInfo(CorrelationData correlationData, boolean ack, String cause);

    /**
     * waring_queue信息处理
     * @param waringInfo
     * @param messageInfo
     */
    void waringQueueHandler(MqWaringInfo waringInfo, MqMessageInfo messageInfo);

    /**
     * mq_message_info跟新
     *
     * @param mqMsgId
     * @param errorInfo
     * @param customStatus
     * @return
     */
    R updateMessageInfo(String mqMsgId, String errorInfo, boolean customStatus) throws JsonProcessingException;
}
