package com.xhf.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.model.exam.dto.QuestionDto;
import com.xhf.model.exam.entity.QuestionsEntity;
import com.xhf.utils.common.PageUtils;

import java.util.Map;

/**
 * 
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
public interface QuestionsService extends IService<QuestionsEntity> {

    /**
     * 分页条件查询关联repoId的题目
     * @param params
     * repoId: 题库id
     * quId: 根据题目id
     * content: 根据题目内容查询
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存题目
     * @param questionDto
     */
    void saveQu(QuestionDto questionDto);

    /**
     * 根据repoId, 生成相关的随机题目
     * 选取当前没有被抽取过的题目, 并且优先选择历史抽过次数小的题目
     * 如果没有题目,抛出异常. 如果有,抽取,同时修改抽取的历史次数,和题目当前状态
     * @param params
     * @return
     */
    QuestionDto randomQu(Map<String, Object> params);

    /**
     * 根据id查询题目
     * @param id
     * @return
     */
    QuestionDto getQuById(String id);

    /**
     * 修改题目
     * @param quFormDto
     */
    void updateQu(QuestionDto quFormDto);

    /**
     * 清空题目被选择状态
     */
    void clearStatus();
}

