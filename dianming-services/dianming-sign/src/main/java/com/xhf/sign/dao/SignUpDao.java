package com.xhf.sign.dao;

import com.xhf.model.sign.entity.SignUpEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 签到表
 * 
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:26
 */
@Mapper
public interface SignUpDao extends BaseMapper<SignUpEntity> {
	
}
