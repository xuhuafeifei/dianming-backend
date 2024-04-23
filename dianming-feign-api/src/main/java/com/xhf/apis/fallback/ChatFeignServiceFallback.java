package com.xhf.apis.fallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xhf.apis.ChatFeignService;
import com.xhf.common.exception.ErrorCode;
import com.xhf.common.exception.RRException;
import com.xhf.common.utils.R;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component
public class ChatFeignServiceFallback implements ChatFeignService {

    /**
     * redis数据同步至数据库
     *
     * @param redisKey
     */
    @Override
    public R synchronization(String redisKey) {
        throw new RRException(ErrorCode.SYSTEM_ERROR, "redis聊天记录同步MySql异常");
    }

    /**
     * 存储MQ主交换机异常信息, 更新mq_message_info, 并进行警告
     */
    @Override
    public R waringQueueHandler(@RequestBody Map<String, String> params) {
        throw new RRException(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * mq信息消费记录修改
     *
     * @param mqMsgId
     * @param errorInfo
     * @param customStatus
     */
    @Override
    public R updateMessageInfo(String mqMsgId, String errorInfo, boolean customStatus) throws JsonProcessingException {
        throw new RRException(ErrorCode.SYSTEM_ERROR);
    }
}
