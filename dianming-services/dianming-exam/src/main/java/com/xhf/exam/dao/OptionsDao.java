package com.xhf.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.exam.entity.OptionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Mapper
public interface OptionsDao extends BaseMapper<OptionsEntity> {

    List<OptionsEntity>	selectOptionsById(@Param("id") Long id);
}
