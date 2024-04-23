package com.xhf.model.user.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * 
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:15:36
 */
@Data
@TableName("student")
public class StudentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	@ExcelProperty("学生id")
	private Long id;
	/**
	 * 
	 */
	@ExcelProperty("班级")
	private String sclass;
	/**
	 * 
	 */
	@ExcelProperty("学生学号")
	private String sno;
	/**
	 * 
	 */
	@ExcelProperty("学生姓名")
	private String sname;
	/**
	 * 
	 */
	@ExcelProperty("学生个性")
	private String label;
	/**
	 * 
	 */
	@ExcelProperty("缺勤次数")
	private Integer absent;
	/**
	 *
	 */
	@ExcelProperty("头像地址")
	private String sportrait;
	/**
	 * 锁定开始时间
	 */
	private LocalDateTime LockTime;
	/**
	 * 是否被锁定
	 */
	private boolean locked;
	/**
	 * 年龄
 	 */
	private Integer age;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 电话
	 */
	private String phoneNumber;
	/**
	 * 兴趣爱好
	 */
	private String interest;
	/**
	 * 注册时间
	 */
	private Date registerTime;
}
