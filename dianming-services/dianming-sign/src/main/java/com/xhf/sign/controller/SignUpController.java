package com.xhf.sign.controller;

import com.xhf.common.exception.ErrorCode;
import com.xhf.common.exception.RRException;
import com.xhf.common.utils.R;
import com.xhf.model.sign.dto.SignUpFormDto;
import com.xhf.model.sign.entity.SignUpEntity;
import com.xhf.sign.service.SignUpService;
import com.xhf.utils.common.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;



/**
 * 签到表
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 * @date 2023-08-17 17:14:26
 */
@RestController
@RequestMapping("sign/signup")
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    /**
     * 获取绫华的照片
     */
    @GetMapping("/get-linghua")
    public R getLinghua() {
        return signUpService.getLinghua();
    }

    /**
     * 开启签到, 获取签到二维码
     */
    @GetMapping("/sign-up-pict")
    public void signUpPict(Long id, Long semId, HttpServletResponse response) throws NoSuchAlgorithmException {
        signUpService.getImage(id, semId, response);
    }

    /**
     * 接收签到表单
     */
    @PostMapping("/sign-up-form")
    public R signUp(@RequestBody SignUpFormDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (StringUtils.isAnyBlank(
                dto.getRequestId(),
                dto.getStuSno(),
                String.valueOf(dto.getSemId())
                )
        ) {
            throw new RRException(ErrorCode.DATA_ERROR);
        }
        return signUpService.signUp(dto, response, request);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = signUpService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{signId}")
    public R info(@PathVariable("signId") Long signId){
		SignUpEntity signUp = signUpService.getById(signId);

        return R.ok().put("signUp", signUp);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SignUpEntity signUp){
        signUpService.saveOrUpdate(signUp);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] signIds){
		signUpService.removeByIds(Arrays.asList(signIds));

        return R.ok();
    }

}
