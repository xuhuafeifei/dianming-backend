package com.xhf.model.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:45:57
 */
@Data
@TableName("user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.INPUT)
    private Long id;
	/**
	 * 
	 */
	private String uname;
	/**
	 * 
	 */
	private String passwd;

	private String roles;

}
