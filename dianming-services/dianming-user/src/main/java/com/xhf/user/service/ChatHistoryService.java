package com.xhf.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.common.utils.R;
import com.xhf.model.user.entity.ChatHistoryEntity;
import com.xhf.utils.common.PageUtils;

import java.util.Map;

/**
 * 聊天历史记录
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-24 08:45:34
 */
public interface ChatHistoryService extends IService<ChatHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 将redis中, rediskey的value同步至Mysql
     * @param redisKey
     * @return
     */
    R synchronization(String redisKey);

    /**
     * 聊天数据初始化, 先查询redis数据, 然后查询MySQL
     * @param redisKey
     * @return
     */
    R chatInit(String redisKey);

    /**
     * 查询历史数据
     * @return
     */
    R getHistory(Map<String, Object> params);
}

