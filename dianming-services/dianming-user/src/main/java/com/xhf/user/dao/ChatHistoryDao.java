package com.xhf.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.user.entity.ChatHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天历史记录
 * 
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-24 08:45:34
 */
@Mapper
public interface ChatHistoryDao extends BaseMapper<ChatHistoryEntity> {
	
}
