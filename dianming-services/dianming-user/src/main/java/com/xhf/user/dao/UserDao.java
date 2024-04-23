package com.xhf.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:45:57
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	
}
