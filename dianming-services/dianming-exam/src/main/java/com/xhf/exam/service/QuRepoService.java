package com.xhf.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.model.exam.entity.QuRepoEntity;
import com.xhf.utils.common.PageUtils;

import java.util.Map;

/**
 * 试题题库关联
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
public interface QuRepoService extends IService<QuRepoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

