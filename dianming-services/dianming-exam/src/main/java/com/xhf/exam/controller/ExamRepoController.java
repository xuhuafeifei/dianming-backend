package com.xhf.exam.controller;

import com.xhf.common.utils.R;
import com.xhf.exam.service.ExamRepoService;
import com.xhf.model.exam.entity.ExamRepoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 考试题库
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-03-22 12:47:17
 */
@RestController
@RequestMapping("exam/examrepo")
@Slf4j
public class ExamRepoController {
    @Autowired
    private ExamRepoService examRepoService;

    /**
     * 展示所有的题库
     */
    @GetMapping("/listAll")
    public R listAll() {
        log.info("start to list all...");
        // 设置查询条件
        List<ExamRepoEntity> list = examRepoService.list();
        return R.ok().put("data", list);
    }
}
