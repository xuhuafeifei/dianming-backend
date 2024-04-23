package com.xhf.model.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 试题题库关联
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Data
@TableName("qu_repo")
public class QuRepoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 试题
	 */
	private Long quId;
	/**
	 * 归属题库
	 */
	private Long repoId;
	/**
	 * 题目类型
	 */
	private Integer quType;
	/**
	 * 排序
	 */
	private Integer sort;

}
