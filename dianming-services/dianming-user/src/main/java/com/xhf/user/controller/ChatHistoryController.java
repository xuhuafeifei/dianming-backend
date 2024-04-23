package com.xhf.user.controller;

import com.xhf.common.redis.utils.RedisConstant;
import com.xhf.common.utils.R;
import com.xhf.common.utils.log.LogAnnotation;
import com.xhf.model.user.entity.ChatHistoryEntity;
import com.xhf.user.service.ChatHistoryService;
import com.xhf.utils.common.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 聊天历史记录
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-24 08:45:34
 */
@RestController
@RequestMapping("user/chathistory")
public class ChatHistoryController {
    @Autowired
    private ChatHistoryService chatHistoryService;

    /**
     * 查询历史记录
     */
    @GetMapping("/history")
    @LogAnnotation(module = "chatHistory", operation = "查询历史聊天记录",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public R getHistory(@RequestParam Map<String, Object> params) {
        return chatHistoryService.getHistory(params);
    }

    /**
     * 聊天数据初始化
     */
    @GetMapping("/init")
    @LogAnnotation(module = "chatHistory", operation = "聊天数据初始化",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public R chatInit(@RequestParam Long id, @RequestParam Long toUserId) {
        return chatHistoryService.chatInit(RedisConstant.getChatKey(id, toUserId));
    }

    /**
     * redis数据同步至数据库
     */
    @GetMapping("/synchronization")
    @LogAnnotation(module = "chatHistory", operation = "redis数据同步db",
            methodType = LogAnnotation.MethodType.CONTROLLER
    )
    public R synchronization(@RequestParam String redisKey) {
        return chatHistoryService.synchronization(redisKey);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = chatHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{chatId}")
    public R info(@PathVariable("chatId") Integer chatId){
		ChatHistoryEntity chatHistory = chatHistoryService.getById(chatId);

        return R.ok().put("chatHistory", chatHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ChatHistoryEntity chatHistory){
		chatHistoryService.save(chatHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ChatHistoryEntity chatHistory){
		chatHistoryService.updateById(chatHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] chatIds){
		chatHistoryService.removeByIds(Arrays.asList(chatIds));

        return R.ok();
    }

}
