package com.xhf.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.model.exam.entity.OptionsEntity;
import com.xhf.utils.common.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
public interface OptionsService extends IService<OptionsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

