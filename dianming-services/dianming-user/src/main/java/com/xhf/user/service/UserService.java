package com.xhf.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xhf.common.utils.R;
import com.xhf.model.user.entity.StudentEntity;
import com.xhf.model.user.entity.UserEntity;
import com.xhf.model.user.vo.LoginVo;
import com.xhf.utils.common.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:45:57
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    LoginVo login(String username, String password);

    R getInfo(Integer userId) throws JsonProcessingException;

    void saveByStu(StudentEntity student);

    void saveByStuList(List<StudentEntity> studentEntities);

    boolean update(String id, String usedPassword, String newPassword, String secNewPassword);

    void saveByStu(List<StudentEntity> studentList);
}

