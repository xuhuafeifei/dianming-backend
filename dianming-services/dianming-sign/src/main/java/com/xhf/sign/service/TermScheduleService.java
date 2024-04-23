package com.xhf.sign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.model.sign.entity.TermScheduleEntity;
import com.xhf.utils.common.PageUtils;

import java.util.Map;

/**
 * 学期课程表
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:27
 */
public interface TermScheduleService extends IService<TermScheduleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改签到开启时间, 签到次数
     */
    boolean updateInfo(TermScheduleEntity entity);
}

