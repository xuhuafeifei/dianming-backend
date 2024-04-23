package com.xhf.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.exam.entity.ExamRepoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考试题库
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Mapper
public interface ExamRepoDao extends BaseMapper<ExamRepoEntity> {
	
}
