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
 * MQ报警信息存储
 * @TableName mq_waring_info
 */
@TableName(value ="mq_waring_info")
@Data
public class MqWaringInfo implements Serializable {
    public MqWaringInfo() {}
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer mqWaringId;

    /**
     * 发送的消息
     */
    private String message;

    /**
     * 报警信息
     */
    private String waringInfo;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 状态
     */
    private Boolean status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}