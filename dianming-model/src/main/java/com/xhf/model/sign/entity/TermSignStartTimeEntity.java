package com.xhf.model.sign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TermSignStartTimeEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 学期课程id
     */
    private Long semId;
    /**
     * 签到开始时间
     */
    private LocalDateTime startTime;
    /**
     * 课程签到次数
     */
    private Integer count;
}
