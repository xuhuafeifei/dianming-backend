package com.xhf.model.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Data
@TableName("options")
public class OptionsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 选项
	 */
	private String content;
	/**
	 * 图片
	 */
	private String image;
	/**
	 * 关联问题id
	 */
	private Long quId;
	/**
	 * 正确选项
	 */
	private boolean correct;
	/**
	 * 答案分析
	 */
	private String analysis;

}
