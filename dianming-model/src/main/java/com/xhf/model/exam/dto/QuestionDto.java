package com.xhf.model.exam.dto;

import com.xhf.model.exam.entity.OptionsEntity;
import com.xhf.model.exam.entity.QuestionsEntity;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto extends QuestionsEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 选项列表
     */
    private List<OptionsEntity> answerList;
    /**
     * 关联的题库
     */
    private List<Long> repoIds;

    /**
     * 转换为entity
     */
    public QuestionsEntity toEntity() {
        QuestionsEntity entity = new QuestionsEntity();
        entity.setId(getId());
        entity.setContent(getContent());
        entity.setQuType(getQuType());
        entity.setDrawNum(getDrawNum());
        entity.setRightNum(getRightNum());
        entity.setAnalysis(getAnalysis());
        entity.setImage(getImage());
        entity.setCreateTime(getCreateTime());
        entity.setUpdateTime(getUpdateTime());
        entity.setRemark(getRemark());
        entity.setChapter(getChapter());
        entity.setLevel(getLevel());
        entity.setStatus(isStatus());
        return entity;
    }
}
