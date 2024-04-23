package com.xhf.sign.dao;

import com.xhf.model.sign.entity.TermScheduleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 学期课程表
 * 
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:27
 */
@Mapper
public interface TermScheduleDao extends BaseMapper<TermScheduleEntity> {

     /**
      * 跟新学期课程, 签到开始时间, 签到总次数
      * @param entity
      * @return
      */
     boolean updateInfo(@Param("entity") TermScheduleEntity entity);
}
