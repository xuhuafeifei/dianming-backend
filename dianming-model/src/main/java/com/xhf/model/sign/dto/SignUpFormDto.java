package com.xhf.model.sign.dto;

import lombok.Data;

@Data
public class SignUpFormDto {
    /**
     * 学号
     */
    private String stuSno;
    /**
     * 学生姓名
     */
    private String stuName;
    /**
     * 请求Id: requestId
     */
    private String requestId;
    /**
     * 课程Id: semId
     */
    private Long semId;
    /**
     * 教师id
     */
    private String teacherId;
}
