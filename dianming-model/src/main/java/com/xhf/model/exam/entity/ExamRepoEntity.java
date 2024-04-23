package com.xhf.model.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 考试题库
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Data
@TableName("exam_repo")
public class ExamRepoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 考试ID
	 */
	private Long examId;
	/**
	 * 题库ID
	 */
	private Long repoId;
	/**
	 * 题库名称
	 */
	private String repoName;
	/**
	 * 单选题数量
	 */
	private Integer radioCount;
	/**
	 * 单选题分数
	 */
	private Integer radioScore;
	/**
	 * 多选题数量
	 */
	private Integer multiCount;
	/**
	 * 多选题分数
	 */
	private Integer multiScore;
	/**
	 * 判断题数量
	 */
	private Integer judgeCount;
	/**
	 * 判断题分数
	 */
	private Integer judgeScore;
	/**
	 * 简答题数量
	 */
	private Integer saqCount;
	/**
	 * 简答题分数
	 */
	private Integer saqScore;

}
