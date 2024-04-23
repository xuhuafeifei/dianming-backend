package com.xhf.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.model.exam.entity.ExamRepoEntity;
import com.xhf.utils.common.PageUtils;

import java.util.Map;

/**
 * 考试题库
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
public interface ExamRepoService extends IService<ExamRepoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

