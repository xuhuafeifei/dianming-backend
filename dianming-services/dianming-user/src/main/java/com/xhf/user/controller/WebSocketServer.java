package com.xhf.user.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xhf.common.redis.CacheService;
import com.xhf.common.redis.utils.RedisConstant;
import com.xhf.common.utils.MQUtils;
import com.xhf.common.utils.log.LogAnnotation;
import com.xhf.user.callback.ChatConfirmCallback;
import com.xhf.user.service.MqMessageInfoService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.xhf.common.constant.ChatMQConstants.EXCHANGE_NAME;
import static com.xhf.common.constant.ChatMQConstants.ROUTING_KEY;

/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/user/ws/{id}")
@Component
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 记录当前在线连接数
     */
    public static final Map<Long, Session> sessionMap = new ConcurrentHashMap<>();

    private static RabbitTemplate rabbitTemplate;

    private static CacheService cacheService;

    private static Redisson redisson;

    private static MqMessageInfoService messageInfoService;

    @Autowired
    public void setMessageInfoService(MqMessageInfoService messageInfoService) {
        WebSocketServer.messageInfoService = messageInfoService;
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        WebSocketServer.cacheService = cacheService;
    }

    @Autowired
    public void setRedissonConfig(Redisson redisson) {
        WebSocketServer.redisson = redisson;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        WebSocketServer.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void initRabbitTemplate() {
        WebSocketServer.rabbitTemplate.setConfirmCallback(new ChatConfirmCallback(WebSocketServer.messageInfoService));
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    @LogAnnotation(module = "chat_websocket", operation = "建立连接",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public void onOpen(Session session, @PathParam("id") Long id) {
        sessionMap.put(id, session);
        log.info("有新用户加入，id={}, 当前在线人数为：{}", id, sessionMap.size());
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.set("users", array);
        for (Object key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("username", key);
            // {"username", "zhang", "username": "admin"}
            array.add(jsonObject);
        }
        // {"users": [{"username": "zhang"},{ "username": "admin"}]}
        // sendAllMessage(JSONUtil.toJsonStr(result));  // 后台发送消息给所有的客户端
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    @LogAnnotation(module = "chat_websocket", operation = "关闭连接",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public void onClose(Session session, @PathParam("id") Long id) {
        sessionMap.remove(id);
        log.info("有一连接关闭，移除id={}的用户session, 当前在线人数为：{}", id, sessionMap.size());
    }
    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    @LogAnnotation(module = "chat_websocket", operation = "发送消息",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public void onMessage(String message, Session session, @PathParam("id") Long id) {
        log.info("服务端收到用户id={}的消息:{}", id, message);
        JSONObject obj = JSONUtil.parseObj(message);
        Long toUserId = Long.valueOf(obj.getStr("toUserId")); // to表示发送给哪个用户，比如 admin
        String text = obj.getStr("content"); // 发送的消息文本  hello
        // {"to": "admin", "text": "聊天文本"}
        Session toSession = sessionMap.get(toUserId); // 根据 to用户名来获取 session，再通过session发送消息文本
        // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
        // {"from": "zhang", "text": "hello"}
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("fromUserId", id);  // from 是 zhang
        jsonObject.set("content", text);  // text 同上面的text
        jsonObject.set("toUserId", toUserId);
        jsonObject.set("mark", RedisConstant.getChatMark(id, toUserId));
        String json = jsonObject.toString();
        if (toSession == null) {
            log.info("发送失败，未找到用户id={}的session", toUserId);
        }else {
            this.sendMessage(json, toSession);
        }

        log.info("发送给用户id={}，消息：{}", toUserId, jsonObject.toString());

        // todo: MQP 发送消息, redis 存储
        saveInRedisAndMQ(id, toUserId, json);
    }

    /**
     * 聊天消息存储redis, 同时通知MQ延迟同步
     * @param id
     * @param toUserId
     * @param json
     */
    public void saveInRedisAndMQ(Long id, Long toUserId, String json) {
        String lockKey = RedisConstant.getChatLockKey(id, toUserId);
        String redisKey = RedisConstant.getChatKey(id, toUserId);

        log.info("开始上锁:{}", lockKey);

        RLock lock = redisson.getLock(lockKey);
        try{
            // debug
            log.info("redisKey = {}, json = {}, locakKey  = {}", redisKey, json, lockKey);
            lock.lock();

            cacheService.lRightPush(redisKey, json);
            // 判断是否需处于定时状态
            String isCount = cacheService.get(redisKey + "_TIME_COUNT");
            if (StringUtils.isBlank(isCount)) {
                log.info("通知MQ");
                MQUtils.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, redisKey, rabbitTemplate);
                // rabbitTemplate.convertAndSend(TtlQueueConfig.X_CHANGE, "XB", redisKey);
                // todo: 定时, 未来30s钟内, 同一个对话的redis数据不发送数据同步请求
                cacheService.setEx(redisKey + "_TIME_COUNT", "倒计时", 30, TimeUnit.SECONDS);
            }else {
                log.info("处于计时状态, 不通知MQ");
            }
        } catch (Exception e) {
            log.error("", e);
            sendMessage(createErrorMessage("数据存储异常, 请重新发送消息"), sessionMap.get(id));
        } finally {
            lock.unlock();
            log.info("解锁: {}", lockKey);
        }
    }

    /**
     * 编写异常信息类
     * @param errorMsg
     * @return
     * @throws JsonProcessingException
     */
    private String createErrorMessage(String errorMsg) {
        return "{\"errorMsg\":\"" + errorMsg + "\"}";
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }
    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
