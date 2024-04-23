package com.xhf.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xhf.apis.fallback.ChatFeignServiceFallback;
import com.xhf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component
@FeignClient(name = "user", fallback = ChatFeignServiceFallback.class)
public interface ChatFeignService {
    /**
     * redis数据同步至数据库
     */
    @GetMapping("/api/user/chathistory/synchronization")
    R synchronization(@RequestParam String redisKey);

    /**
     * 存储MQ主交换机异常信息, 更新mq_message_info, 并进行警告
     */
    @PostMapping("/api/mq/mqserver/waring")
    R waringQueueHandler(@RequestBody Map<String, String> params) throws JsonProcessingException;

    /**
     * mq信息消费记录修改
     */
    @PostMapping("/api/mq/mqserver/updateMqMessageInfo")
    R updateMessageInfo(@RequestParam("mqMsgId") String mqMsgId, @RequestParam("errorInfo") String errorInfo, @RequestParam("customStatus") boolean customStatus) throws JsonProcessingException;
}