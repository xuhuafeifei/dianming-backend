package com.xhf.sign.controller;

import com.xhf.common.utils.R;
import com.xhf.model.sign.entity.TermScheduleEntity;
import com.xhf.sign.service.TermScheduleService;
import com.xhf.utils.common.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 学期课程表
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:27
 */
@RestController
@RequestMapping("sign/termschedule")
public class TermScheduleController {
    @Autowired
    private TermScheduleService termScheduleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = termScheduleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{semesterId}")
    public R info(@PathVariable("semesterId") Long semesterId){
		TermScheduleEntity termSchedule = termScheduleService.getById(semesterId);

        return R.ok().put("termSchedule", termSchedule);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TermScheduleEntity termSchedule){
		termScheduleService.saveOrUpdate(termSchedule);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] semesterIds){
		termScheduleService.removeByIds(Arrays.asList(semesterIds));

        return R.ok();
    }

}
