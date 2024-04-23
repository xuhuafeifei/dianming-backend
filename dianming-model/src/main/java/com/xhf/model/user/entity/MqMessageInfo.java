package com.xhf.model.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * MQ信息发布存储，记录发送给mq所有信息状态
 * @TableName mq_message_info
 */
@TableName(value ="mq_message_info")
@Data
public class MqMessageInfo implements Serializable {
    public MqMessageInfo() {}
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String mqMsgId;

    /**
     * 发送消息
     */
    private String message;

    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 重试次数
     */
    private Integer retryNum;

    /**
     * 交换机名称
     */
    private String exchangeName;

    /**
     * 路由key
     */
    private String routingKey;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishStartTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishEndTime;

    /**
     * 消息发布状态 1:解决 /0:未解决
     */
    private Boolean publishStatus;

    /**
     * 消息消费状态 1:解决 /0:未解决
     */
    private Boolean consumerStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}