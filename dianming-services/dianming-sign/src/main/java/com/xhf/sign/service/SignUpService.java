package com.xhf.sign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.common.utils.R;
import com.xhf.model.sign.dto.SignUpFormDto;
import com.xhf.model.sign.entity.SignUpEntity;
import com.xhf.utils.common.PageUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * 签到表
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:26
 */
public interface SignUpService extends IService<SignUpEntity> {

    PageUtils queryPage(Map<String, Object> params);

    @Override
    boolean saveOrUpdate(SignUpEntity entity);

    /**
     * 返回二维码
     * @param id 教师id
     * @param semId 课程id
     * @param response
     * @return
     */
    void getImage(Long id, Long semId, HttpServletResponse response) throws NoSuchAlgorithmException;

    /**
     * 接收签到表单, 执行签到逻辑
     * @param dto
     * @return
     */
    R signUp(SignUpFormDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException;

    /**
     * 获取绫华图片
     * @return
     */
    R getLinghua();
}

