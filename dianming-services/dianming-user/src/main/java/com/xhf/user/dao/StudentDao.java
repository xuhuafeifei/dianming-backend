package com.xhf.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.user.entity.StudentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:15:36
 */
@Mapper
public interface StudentDao extends BaseMapper<StudentEntity> {
    /**
     * 按照sno修改
     * @param studentList
     */
    void updateImages(@Param("stus") List<StudentEntity> studentList);
}
