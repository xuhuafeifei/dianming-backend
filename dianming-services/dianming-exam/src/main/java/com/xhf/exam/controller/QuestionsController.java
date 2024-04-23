package com.xhf.exam.controller;

import com.xhf.common.utils.R;
import com.xhf.exam.service.QuestionsService;
import com.xhf.model.exam.dto.QuestionDto;
import com.xhf.utils.common.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@RestController
@RequestMapping("exam/questions")
@Slf4j
public class QuestionsController {
    @Autowired
    private QuestionsService questionsService;

    /**
     * 展示题目
     */
    @GetMapping("listQu")
    public R listQ(@RequestParam Map<String, Object> params) {
        PageUtils page = questionsService.queryPage(params);

        return R.ok().put("data", page);
    }

    /**
     * 保存题目
     */
    @PostMapping("saveQu")
    @Transactional
    public R saveQu(@RequestBody QuestionDto quFormDto) {
        quFormDto.setStatus(true);
        quFormDto.setDrawNum(0);
        quFormDto.setRightNum(0);

        if (quFormDto.getId() == null) {
            questionsService.saveQu(quFormDto);
        } else {
            questionsService.updateQu(quFormDto);
        }
        return R.ok();
    }

    /**
     * 随机抽取题目
     */
    @GetMapping("randomQu")
    @Transactional
    public R randomQu(@RequestParam Map<String, Object> params) {
        QuestionDto questionDto = questionsService.randomQu(params);
        return R.ok().put("data", questionDto);
    }

    /**
     * 根据id查询题目(包含选项)
     */
    @GetMapping("/info")
    public R getQuOpById(@RequestParam String id) {
        QuestionDto questionDto = questionsService.getQuById(id);

        return R.ok().put("data", questionDto);
    }

    /**
     * 清空单日题目抽奖状态
     */
    @GetMapping("clearStatus")
    public R clearStatus() {
        questionsService.clearStatus();

        return R.ok();
    }
}
