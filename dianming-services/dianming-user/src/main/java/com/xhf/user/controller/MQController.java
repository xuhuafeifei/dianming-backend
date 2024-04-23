package com.xhf.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xhf.common.utils.R;
import com.xhf.common.utils.log.LogAnnotation;
import com.xhf.model.user.entity.MqMessageInfo;
import com.xhf.model.user.entity.MqWaringInfo;
import com.xhf.user.service.MqMessageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
@RequestMapping("mq/mqserver")
public class MQController {
    @Autowired
    private MqMessageInfoService mqMessageInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * waring_queue信息处理
     * @param params
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/waring")
    @LogAnnotation(module = "mq服务", operation = "处理waring_queue发送的警报信息",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public R waringQueueHandler(@RequestBody Map<String, String> params) throws JsonProcessingException {
        String json1 = params.get("waringInfo");
        String json2 = params.get("messageInfo");
        MqWaringInfo mqWaringInfo = objectMapper.readValue(json1, MqWaringInfo.class);
        MqMessageInfo mqMessageInfo = objectMapper.readValue(json2, MqMessageInfo.class);
        this.mqMessageInfoService.waringQueueHandler(mqWaringInfo, mqMessageInfo);
        return R.ok();
    }

    /**
     * mq信息消费记录修改
     */
    @PostMapping("/updateMqMessageInfo")
    @LogAnnotation(module = "mq服务", operation = "处理消费信息, 修改mq_message_info",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public R updateMessageInfo(@RequestParam("mqMsgId") String mqMsgId, @RequestParam("errorInfo") String errorInfo, @RequestParam("customStatus") boolean customStatus) throws JsonProcessingException {
        return mqMessageInfoService.updateMessageInfo(mqMsgId, errorInfo, customStatus);
    }
}
