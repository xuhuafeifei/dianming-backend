package com.xhf.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xhf.model.exam.dto.QuestionDto;
import com.xhf.model.exam.entity.QuestionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 
 * 
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@Mapper
public interface QuestionsDao extends BaseMapper<QuestionsEntity> {

    /**
     * 清除choose状态
     */
    @Update("update questions set choose = 0 where choose = 1")
    void clearStatus();

    /**
     * 修改choose状态为true, draw_num次数+1
     * choose: 当天是否被抽中
     * draw_num: 历史抽中次数
     */
    @Update("update questions set choose = 1, draw_num = draw_num + 1 where id = #{quId}")
    void setStatus(@Param("quId") Long quId);

    /**
     * 获取题目
     */
    List<QuestionDto> getQu(@Param("repoId") Long repoId);
}
