package com.xhf.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhf.exam.dao.ExamRepoDao;
import com.xhf.exam.service.ExamRepoService;
import com.xhf.model.exam.entity.ExamRepoEntity;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("examRepoService")
public class ExamRepoServiceImpl extends ServiceImpl<ExamRepoDao, ExamRepoEntity> implements ExamRepoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamRepoEntity> page = this.page(
                new Query<ExamRepoEntity>().getPage(params),
                new QueryWrapper<ExamRepoEntity>()
        );

        return new PageUtils(page);
    }

}