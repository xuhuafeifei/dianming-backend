package com.xhf.model.sign.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 签到表
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:26
 */
@Data
@TableName("sign_up")
public class SignUpEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Long signId;
    /**
     * 学生学号
     */
    private String stuSno;
    /**
     * 关联学期课程名称
     */
    private Long semId;
    /**
     * 签到时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime signTime;

}
