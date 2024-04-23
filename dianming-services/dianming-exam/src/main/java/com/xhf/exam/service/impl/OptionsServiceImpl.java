package com.xhf.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhf.exam.dao.OptionsDao;
import com.xhf.exam.service.OptionsService;
import com.xhf.model.exam.entity.OptionsEntity;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("optionsService")
public class OptionsServiceImpl extends ServiceImpl<OptionsDao, OptionsEntity> implements OptionsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OptionsEntity> page = this.page(
                new Query<OptionsEntity>().getPage(params),
                new QueryWrapper<OptionsEntity>()
        );

        return new PageUtils(page);
    }

}