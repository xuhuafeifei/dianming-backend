package com.xhf.sign.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.sign.entity.TermSignStartTimeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TermSignStartHistoryDao extends BaseMapper<TermSignStartTimeEntity> {

    /**
     * 记录历史信息
     * @param historyEntity
     */
    void saveInfo(@Param("entity") TermSignStartTimeEntity historyEntity);
}
