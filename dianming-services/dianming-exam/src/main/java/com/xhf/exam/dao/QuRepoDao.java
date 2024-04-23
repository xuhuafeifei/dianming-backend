package com.xhf.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.exam.entity.QuRepoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 试题题库关联
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Mapper
public interface QuRepoDao extends BaseMapper<QuRepoEntity> {
	
}
