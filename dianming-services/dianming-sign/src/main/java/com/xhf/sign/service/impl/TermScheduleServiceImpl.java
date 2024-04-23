package com.xhf.sign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhf.model.sign.entity.TermScheduleEntity;
import com.xhf.sign.dao.TermScheduleDao;
import com.xhf.sign.service.TermScheduleService;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("termScheduleService")
public class TermScheduleServiceImpl extends ServiceImpl<TermScheduleDao, TermScheduleEntity> implements TermScheduleService {

    @Autowired
    private TermScheduleDao termScheduleDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TermScheduleEntity> page = this.page(
                new Query<TermScheduleEntity>().getPage(params),
                new QueryWrapper<TermScheduleEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 修改签到开启时间, 签到次数
     * @return
     */
    public boolean updateInfo(TermScheduleEntity entity) {
        return termScheduleDao.updateInfo(entity);
    }
}