package com.xhf.sign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.model.sign.entity.TermSignStartTimeEntity;

public interface TermSignStartService extends IService<TermSignStartTimeEntity> {

    /**
     * 记录签到开启历史
     * @param historyEntity
     */
    void saveInfo(TermSignStartTimeEntity historyEntity);
}
