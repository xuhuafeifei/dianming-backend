package com.xhf.model.sign.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学期课程表
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:27
 */
@Data
@TableName("term_schedule")
public class TermScheduleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long semesterId;
    /**
     * 学期
     */
    private String semester;
    /**
     * 课程
     */
    private String course;
    /**
     * 课堂次序，第几节课
     */
    private Integer classNumber;
    /**
     * 签到开启时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime signStartTime;
    /**
     * 签到持续时间
     */
    private Integer signLastTime;
    /**
     * 签到是否结束
     */
    private boolean finished;
    /**
     * 签到次数
     */
    private Integer count;

}
