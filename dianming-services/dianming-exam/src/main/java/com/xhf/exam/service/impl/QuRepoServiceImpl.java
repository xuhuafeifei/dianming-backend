package com.xhf.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhf.exam.dao.QuRepoDao;
import com.xhf.exam.service.QuRepoService;
import com.xhf.model.exam.entity.QuRepoEntity;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("quRepoService")
public class QuRepoServiceImpl extends ServiceImpl<QuRepoDao, QuRepoEntity> implements QuRepoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuRepoEntity> page = this.page(
                new Query<QuRepoEntity>().getPage(params),
                new QueryWrapper<QuRepoEntity>()
        );

        return new PageUtils(page);
    }

}