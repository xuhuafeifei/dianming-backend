package com.xhf.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhf.common.exception.ErrorCode;
import com.xhf.common.exception.RRException;
import com.xhf.exam.dao.QuRepoDao;
import com.xhf.exam.dao.QuestionsDao;
import com.xhf.exam.service.OptionsService;
import com.xhf.exam.service.QuRepoService;
import com.xhf.exam.service.QuestionsService;
import com.xhf.file.service.OSSService;
import com.xhf.model.exam.dto.QuestionDto;
import com.xhf.model.exam.entity.OptionsEntity;
import com.xhf.model.exam.entity.QuRepoEntity;
import com.xhf.model.exam.entity.QuestionsEntity;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("questionsService")
@Slf4j
public class QuestionsServiceImpl extends ServiceImpl<QuestionsDao, QuestionsEntity> implements QuestionsService {
    @Autowired
    private OptionsService optionsService;

    @Resource
    private QuRepoDao quRepoDao;

    @Autowired
    private QuRepoService quRepoService;

    @Resource
    private QuestionsDao questionsDao;

    @Autowired
    private OSSService ossService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 获取关联题库
        String repoId = (String) params.get("repoId");
        String page = (String) params.get("page");
        String limit = (String) params.get("limit");
        String quId = (String) params.get("quId");
        String content = (String) params.get("content");

        if (repoId == null) {
            throw new RRException(ErrorCode.DATA_ERROR);
        }

        // 分页处理
        int pageNumber = 1, pageSize = 10;
        if (!StringUtils.isBlank(page)) {
            pageNumber = Integer.parseInt(page);
        }
        if (!StringUtils.isBlank(limit)) {
            pageSize = Integer.parseInt(limit);
        }

        // 查询和repoId关联的所有quId
        List<Long> quIdList = quRepoDao.selectList(
                new LambdaQueryWrapper<QuRepoEntity>()
                        .eq(QuRepoEntity::getRepoId, Long.valueOf(repoId)))
                .stream().map(QuRepoEntity::getQuId).collect(Collectors.toList());
        // 设置分页查询
        PageHelper.startPage(pageNumber, pageSize);
        // 查询所有题目
        List<QuestionsEntity> questionsList = this.questionsDao.selectList(
                new LambdaQueryWrapper<QuestionsEntity>()
                        .in(QuestionsEntity::getId, quIdList)
                        .orderByAsc(QuestionsEntity::getDrawNum)
                        .eq(StringUtils.isNotBlank(quId), QuestionsEntity::getId, quId)
                        .like(StringUtils.isNotBlank(content), QuestionsEntity::getContent, content)
        );

        // 题目顺序修改


        PageInfo pageInfo = new PageInfo(questionsList, pageSize);

        return new PageUtils(pageInfo);
    }

    @Override
    public void saveQu(QuestionDto questionDto) {
        try {
            // 1.获取题目
            QuestionsEntity questionsEntity = questionDto.toEntity();
            // 存储图床
            if (questionsEntity.getImage() != null && questionsEntity.getImage().length() > 0) {
                // todo: 存储题目图片逻辑
                // questionsEntity.setImage(ossService.uploadFile(questionsEntity.getImage()));
            }
            // 2.存储题目
            this.save(questionsEntity);
            // 3.1.获取题目id,提取选项列表
            Long quId = questionsEntity.getId();
            // 3.2.存储中间表(qu_repo)
            List<QuRepoEntity> quRepoList = questionDto.getRepoIds().stream().map(e -> {
                QuRepoEntity entity = new QuRepoEntity();
                entity.setQuId(quId);
                entity.setRepoId(e);
                return entity;
            }).collect(Collectors.toList());
            quRepoService.saveBatch(quRepoList);
            // 4.存储选项
            List<OptionsEntity> answerList = questionDto.getAnswerList();
            answerList.forEach(e -> {
                e.setQuId(quId);
                // 存储图像
                if (e.getImage() != null && e.getImage().length() > 0) {
                    // todo: 存储题目图片逻辑
                    // e.setImage(fileHandler.upload(e.getImage()));
                }
            });
            optionsService.saveBatch(answerList);
        }catch (Exception e) {
            throw new RRException(e.getMessage(), e);
        }
    }

    /**
     * 根据repoId, 生成相关的随机题目
     * 选取当前没有被抽取过的题目, 并且优先选择历史抽过次数小的题目
     * 如果没有题目,抛出异常. 如果有,抽取,同时修改抽取的历史次数,和题目当前状态
     *
     * @param params
     * @return
     */
    @Override
    public QuestionDto randomQu(Map<String, Object> params) {
        String repoId = (String) params.get("repoId");
        if (StringUtils.isBlank(repoId)) {
            throw new RRException(ErrorCode.DATA_ERROR);
        }
        String level = (String) params.get("level");

        // 查询所有符合要求的题目
        List<QuestionDto> questionList = questionsDao.getQu(Long.valueOf(repoId));

        // 二分查找, 找到大于最小次数的值的下标
        int L = 0, R = questionList.size() - 1, index = -1, mid = -1, minDrawCnt = questionList.get(0).getDrawNum();
        while (L < R) {
            mid = (L + R) >> 1;
            if (questionList.get(mid).getDrawNum() > minDrawCnt) {
                index = mid;
                R = mid - 1;
            }else {
                L = mid + 1;
            }
        }
        // 随机抽取题目, 保证优先抽取过去抽取次数少的题目
        int num;
        if (index != -1) {
            num = RandomUtils.createInt(0, index + 1);
        }else {
            num = RandomUtils.createInt(0, questionList.size());
        }

        QuestionDto questionDto = questionList.get(num);

        // 设置status为true
        questionsDao.setStatus(questionDto.getId());

        return questionDto;
    }

    @Override
    public void updateQu(QuestionDto quFormDto) {
        try {
            // 1.获取题目
            QuestionsEntity questionsEntity = quFormDto.toEntity();
            // 存储图床
            if (questionsEntity.getImage() != null && questionsEntity.getImage().length() > 0) {
                // todo: image
                // questionsEntity.setImage(fileHandler.upload(questionsEntity.getImage()));
            }
            // 2.修改题目
            this.updateById(questionsEntity);
            // 3.1.获取题目id,提取选项列表
            Long quId = questionsEntity.getId();
            // 3.2.修改中间表(qu_repo)
            List<QuRepoEntity> quRepoList = quFormDto.getRepoIds().stream().map(e -> {
                QuRepoEntity entity = new QuRepoEntity();
                entity.setQuId(quId);
                entity.setRepoId(e);
                return entity;
            }).collect(Collectors.toList());
            quRepoService.updateBatchById(quRepoList);
            // 4.修改选项
            List<OptionsEntity> answerList = quFormDto.getAnswerList();
            answerList.forEach(e -> {
                e.setQuId(quId);
                // 存储图像
                if (e.getImage() != null && e.getImage().length() > 0) {
                    // e.setImage(fileHandler.upload(e.getImage()));
                    // todo: save image
                }
            });
            optionsService.updateBatchById(answerList);
        }catch (Exception e) {
            throw new RRException(e.getMessage(), e);
        }
    }

    @Override
    public void clearStatus() {
        questionsDao.clearStatus();
    }


    @Override
    public QuestionDto getQuById(String id) {
        // 数据校验
        if (org.apache.commons.lang3.StringUtils.isAnyBlank(id)) {
            throw new RRException(ErrorCode.DATA_ERROR);
        }
        // 查询题目
        QuestionsEntity question = this.getById(id);
        // 根据题目查询选项
        // 设置查询条件
        LambdaQueryWrapper<OptionsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OptionsEntity::getQuId, id);
        // 查询选项, 和题目关联的选项
        List<OptionsEntity> list = optionsService.list(queryWrapper);
        // 返回dto
        QuestionDto questionDto = question.toDto();
        questionDto.setAnswerList(list);

        return questionDto;
    }
}