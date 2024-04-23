package com.xhf.model.exam.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xhf.model.exam.dto.QuestionDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Data
@TableName("questions")
public class QuestionsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 问题
	 */
	private String content;
	/**
	 * 类型
	 */
	private Integer quType;
	/**
	 * 抽中次数
	 */
	private Integer drawNum;
	/**
	 * 回答正确次数
	 */
	private Integer rightNum;
	/**
	 * 题目分析
	 */
	private String analysis;
	/**
	 * 图片
	 */
	private String image;
	/**
	 *
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	/**
	 *
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;
	/**
	 * 题目备注
	 */
	private String remark;
	/**
	 * 今天是否被抽中
	 */
	private boolean choose;
	/**
	 * 难度
	 */
	private Integer level;
	/**
	 * 章节
	 */
	private String chapter;
	/**
	 * 题目是否有效
	 */
	private boolean status;

	public QuestionDto toDto() {
		QuestionDto questionDto = new QuestionDto();
		questionDto.setId(this.id);
		questionDto.setContent(this.content);
		questionDto.setQuType(this.quType);
		questionDto.setAnalysis(this.analysis);
		questionDto.setImage(this.image);
		questionDto.setCreateTime(this.createTime);
		questionDto.setUpdateTime(this.updateTime);
		questionDto.setRemark(this.remark);
		questionDto.setLevel(this.level);
		questionDto.setChapter(this.chapter);
		return questionDto;
	}
}
