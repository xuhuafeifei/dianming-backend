package com.xhf.sign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhf.model.sign.entity.TermSignStartTimeEntity;
import com.xhf.sign.dao.TermSignStartHistoryDao;
import com.xhf.sign.service.TermSignStartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TermSignStartServiceImpl extends ServiceImpl<TermSignStartHistoryDao, TermSignStartTimeEntity> implements TermSignStartService {

    @Autowired
    private TermSignStartHistoryDao termSignStartHistoryDao;

    /**
     * 记录签到开启历史
     *
     * @param historyEntity
     */
    @Override
    public void saveInfo(TermSignStartTimeEntity historyEntity) {
        termSignStartHistoryDao.saveInfo(historyEntity);
    }
}
