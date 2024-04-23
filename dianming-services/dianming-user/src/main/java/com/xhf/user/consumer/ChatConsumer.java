package com.xhf.user.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.xhf.common.exception.RRException;
import com.xhf.common.utils.R;
import com.xhf.common.utils.log.LogAnnotation;
import com.xhf.model.user.entity.MqMessageInfo;
import com.xhf.model.user.entity.MqWaringInfo;
import com.xhf.user.service.ChatHistoryService;
import com.xhf.user.service.MqMessageInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.xhf.common.constant.ChatMQConstants.*;

/**
 * 监听redis中chat信息同步服务
 */
@Component
@Slf4j
public class ChatConsumer {
    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private MqMessageInfoService mqMessageInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * dead, backup
     * 监听chat_queue, backup_chat_queue 进行redis-db数据同步
     * @param message
     * @param channel
     * @throws InterruptedException
     * @throws IOException
     */
    @RabbitListener(queues = {DEAD_QUEUE_NAME, BACKUP_QUEUE_NAME})
    @LogAnnotation(module = "chat消费者", operation = "监听chat_dead_queue, backup_chat_queue, 备份redis-mq",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public void receiveMessage(Message message, Channel channel) throws InterruptedException, IOException {
        log.info("------------------开始通知数据备份......------------------");
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        MessageProperties properties = message.getMessageProperties();
        String mqMsgId = (String) properties.getHeaders().get(HEADER_KEY);
        // 需要远程传递的参数
        String msg = new String(message.getBody());

        try {
            // Thread.sleep(1000);
            log.info("msg: {}", msg);
            R flag = chatHistoryService.synchronization(msg);
            log.info("同步结果 : {}", flag);
            // 消息同步异常, mq消息回退
            if (0 != Integer.parseInt(String.valueOf(flag.get("code")))) {
                channel.basicReject(deliveryTag, true);
                throw new RRException("redis聊天信息同步db异常", 500);
            }

            // 手动确认
            channel.basicAck(deliveryTag, true);
            // 修改mq_message_info信息
            R r = mqMessageInfoService.updateMessageInfo(mqMsgId, "", true);
            log.info("mq_message_info 修改结果:{}", r);
        } catch (Exception e) {
            e.printStackTrace();
            // 消息确认, 并重新入队
            channel.basicReject(deliveryTag, true);
            mqMessageInfoService.updateMessageInfo(mqMsgId, e.getMessage(), false);
            log.error("消息重新入队: {}", msg);
        } finally {
            log.info("------------------消息备份结束------------------");
        }
    }

    /**
     * waring
     * MQ走备份交换机(主交换机异常). 进行信息警告, 并且存储数据库
     * @param message
     * @param channel
     */
    @RabbitListener(queues = WARING_QUEUE_NAME)
    @LogAnnotation(module = "chat消费者", operation = "监听chat_waring_queue, 发出警告信息",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public void ChatWarningHandler(Message message, Channel channel) {
        log.info("==============================警告记录开始========================");
        MessageProperties properties = message.getMessageProperties();
        long deliveryTag = properties.getDeliveryTag();
        // 获取发布至mq中消息的主键id
        String mqMsgId = (String) properties.getHeaders().get(HEADER_KEY);

        try {
            MqWaringInfo waringInfo = new MqWaringInfo();
            waringInfo.setMessage(new String(message.getBody(), StandardCharsets.UTF_8));
            waringInfo.setStatus(false);
            waringInfo.setWaringInfo("交换机路由异常, 请检查. exchange:"
                    + properties.getReceivedExchange() + " routing-key:" + properties.getReceivedRoutingKey()
                    + " consumer queue:" + properties.getConsumerQueue()
            );

            MqMessageInfo messageInfo = new MqMessageInfo();
            messageInfo.setErrorInfo("交换机路由异常, 请检查. exchange:"
                    + properties.getReceivedExchange() + " routing-key:" + properties.getReceivedRoutingKey()
                    + " consumer queue:" + properties.getConsumerQueue());
            messageInfo.setMqMsgId(mqMsgId);
            // 存储警告信息, 修改存储MQ的发布信息
            mqMessageInfoService.waringQueueHandler(waringInfo, messageInfo);

            // 消息确认
            channel.basicAck(deliveryTag, false);
            log.info("==============================警告记录成功========================");
        } catch (Exception e) {
            e.printStackTrace();
            // 消息拒绝确认
            try {
                channel.basicReject(deliveryTag, false);
                log.error("==============================警告记录失败========================");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
