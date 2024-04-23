//package com.xhf;
//
//import com.xhf.common.redis.CacheService;
//import com.xhf.config.RabbitMQConfig2;
//import com.xhf.config.TtlQueueConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
//public class MQTest {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Test
//    public void test() {
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean b, String s) {
//                System.out.println(correlationData);
//                System.out.println(s);
//                if (b) {
//                    System.out.println("成功");
//                }else {
//                    System.out.println("失败");
//                }
//            }
//        });
//        System.out.println("发送消息");
//        rabbitTemplate.convertAndSend(TtlQueueConfig.TEST_EXCHANGE, "", "hello rabbitMQ");
//    }
//
//    @Autowired
//    private CacheService cacheService;
//
//    @Test
//    public void test2() {
//        cacheService.lRightPush("CHAT_FACE_TO_FACE_1_1565", "{\"fromUserId\":1565,\"content\":\"1\",\"mark\":\"1_1565\"}");
//    }
//
//    @Test
//    public void test3(){
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            /**
//             *
//             * @param correlationData 相关配置信息
//             * @param ack 交换机是否成功收到消息
//             * @param cause 错误信息
//             */
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                if (ack){
//                    log.info("消息成功发送");
//                }else {
//                    log.info("消息发送失败");
//                    log.info("错误原因"+cause);
//                }
//            }});
//        //第一个参数 交换机名称  第二个参数 routingKey 也就是路由key 第三个参数 消息
//        //填写正确的交换机名即可  看见效果 如果想看见失败填写一个错误的交换机名称
//        rabbitTemplate.convertAndSend(RabbitMQConfig2.EXCHANGE_NAME, "", "什么鬼");
//    }
//}
