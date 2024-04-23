package com.xhf.model.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 聊天历史记录
 * 
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-24 08:45:34
 */
@Data
@TableName("chat_history")
public class ChatHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer chatId;
	/**
	 * 消息来源
	 */
	private String fromUserId;
	/**
	 * 消息去向
	 */
	private String toUserId;
	/**
	 * 聊天内容
	 */
	private String content;
	/**
	 * 标记，小id_大id
	 */
	private String mark;

}
