package com.xhf.user.time;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.ConnectionFactory;
import com.xhf.common.utils.MQUtils;
import com.xhf.model.user.entity.MqMessageInfo;
import com.xhf.user.service.MqMessageInfoService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MQTimer {
    @Autowired
    private MqMessageInfoService messageInfoService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 定时监测mq消息发布情况
     */
    // 每10min执行一次
    @Scheduled(cron = "0 0/10 * * * ?")
    // 每20s执行一次
//    @Scheduled(cron = "0/20 * * * * ?")
    public void MQPublishedDetect() {
        log.info("----------------------监测mq消息发布情况----------------------");
        // 获取未发布的消息
        List<MqMessageInfo> list = messageInfoService.list(new LambdaQueryWrapper<MqMessageInfo>()
                .eq(MqMessageInfo::getPublishStatus, false)
                .select(MqMessageInfo::getMqMsgId)
        );
        // 转发消息至mq中
        list.forEach(e -> MQUtils.convertAndSend(e.getExchangeName(), e.getRoutingKey(), e.getMessage(), e.getMqMsgId(), rabbitTemplate));
        log.info("----------------------mq消息监测结束----------------------");
    }

//    /**
//     * 定时监测customer消费情况
//     */
//    @Scheduled(cron = "0 0/15 * * * ?")
//    public void MQConsumerDetect() {
//        log.info("----------------------监听消费情况----------------------");
//        // 获取消费失败的消息(retry >= 1 && consumer_status = false)
//        List<MqMessageInfo> list = messageInfoService.list(new LambdaQueryWrapper<MqMessageInfo>()
//                .eq(MqMessageInfo::getConsumerStatus, false)
//                .ge(MqMessageInfo::getRetryNum, 1)
//        );
//        // 转发至mq中
//        list.forEach(e -> MQUtils.convertAndSend(e.getExchangeName(), e.getRoutingKey(), e.getMessage(), rabbitTemplate));
//        log.info("----------------------监听消费情况结束----------------------");
//    }
}
