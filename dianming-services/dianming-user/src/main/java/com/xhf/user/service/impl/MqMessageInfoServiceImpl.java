package com.xhf.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xhf.common.utils.R;
import com.xhf.model.user.entity.MqMessageInfo;
import com.xhf.model.user.entity.MqWaringInfo;
import com.xhf.user.dao.MqMessageInfoDao;
import com.xhf.user.service.MqMessageInfoService;
import com.xhf.user.service.MqWaringInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 *
 */
@Service
@Slf4j
public class MqMessageInfoServiceImpl extends ServiceImpl<MqMessageInfoDao, MqMessageInfo>
    implements MqMessageInfoService{

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 存储发布到mq的信息, 并插入/更新数据库消息发布情况
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void saveInfo(CorrelationData correlationData, boolean ack, String cause) {
        Message returnedMessage = correlationData.getReturnedMessage();

        try {
            log.info("correlationData:{}, ack:{}, cause:{}", objectMapper.writeValueAsString(correlationData), ack, cause);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 初始化
        MqMessageInfo mqMessageInfo = initMqMessageInfo(correlationData, returnedMessage);

        if (ack) {
            // 消息发布成功
            mqMessageInfo.setPublishStatus(true);
            mqMessageInfo.setPublishEndTime(LocalDateTime.now());
            log.info("消息发布成功, 更新数据库");
        }else {
            // 消息发布失败
            mqMessageInfo.setPublishStatus(false);
            mqMessageInfo.setErrorInfo(cause);
            log.error("消息发布失败, 更新数据库");
        }

        // 存储数据
        this.saveOrUpdate(mqMessageInfo);
    }

    /**
     * 根据correlationData, Message初始化mqMessageInfo
     * @param correlationData
     * @param returnedMessage
     * @return
     */
    private MqMessageInfo initMqMessageInfo(CorrelationData correlationData, Message returnedMessage) {
        MqMessageInfo mqMessageInfo = new MqMessageInfo();
        mqMessageInfo.setMqMsgId(correlationData.getId());

        if (returnedMessage != null) {
            MessageProperties properties = returnedMessage.getMessageProperties();
            mqMessageInfo.setMessage(new String(returnedMessage.getBody(), StandardCharsets.UTF_8));
            mqMessageInfo.setExchangeName(properties.getReceivedExchange());
            mqMessageInfo.setRoutingKey(properties.getReceivedRoutingKey());
            mqMessageInfo.setPublishStartTime(LocalDateTime.now());
        }
        return mqMessageInfo;
    }

    @Autowired
    private MqWaringInfoService mqWaringInfoService;

    /**
     * mq_message_info跟新
     *
     *
     * @param mqMsgId
     * @param errorInfo
     * @return
     */
    @Override
    public R updateMessageInfo(String mqMsgId, String errorInfo, boolean consumerStatus) throws JsonProcessingException {
        MqMessageInfo messageInfo = new MqMessageInfo();
        messageInfo.setMqMsgId(mqMsgId);
        messageInfo.setErrorInfo(errorInfo);
        messageInfo.setConsumerStatus(consumerStatus);

        LambdaUpdateWrapper<MqMessageInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .setSql("consumer_status = " + consumerStatus)
                .eq(MqMessageInfo::getMqMsgId, mqMsgId);
        if (consumerStatus) {
            updateWrapper.setSql("error_info = ''");
        }
        updateWrapper.setSql("retry_num = retry_num + 1");
        this.update(updateWrapper);
        return R.ok();
    }

    /**
     * waring_queue信息处理
     * 通知chat_queue无法被路由, 同时修改已发布消息的状态
     */
    @Override
    @Transactional
    public void waringQueueHandler(MqWaringInfo waringInfo, MqMessageInfo messageInfo) {
        // 存储mq_waring_info
        waringInfo.setStartTime(LocalDateTime.now());
        mqWaringInfoService.save(waringInfo);
        // 根据id修改mq_message_info
        this.updateById(messageInfo);
    }
}