package com.xhf.user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.xhf.common.utils.R;
import com.xhf.common.utils.Res;
import com.xhf.model.user.entity.UserEntity;
import com.xhf.user.service.UserService;
import com.xhf.utils.common.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:45:57
 */
@RestController
@RequestMapping("user/user/")
@Slf4j
public class UserController {
    public static void main(String[] args) {
        String salt = "tyut_123456";
        String password = "admin123";
        System.out.println(DigestUtils.md5DigestAsHex((salt + password).getBytes()));
    }
    @Autowired
    private UserService userService;

    @GetMapping("personal/password")
    public R changePassword(@RequestParam String id, String usedPassword, String newPassword, String secNewPassword) {
        boolean flag = userService.update(id, usedPassword, newPassword, secNewPassword);
        return flag ? R.ok() : R.error();
    }

    @GetMapping("login")
    public Res login(@RequestParam String username, String password) {
        return Res.ok().put("data", userService.login(username, password));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/info")
    public R getInfo(@RequestParam Integer userId) throws JsonProcessingException {
        return userService.getInfo(userId);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{uname}")
    public R info(@PathVariable("uname") String uname){
		UserEntity user = userService.getById(uname);
        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user){
		userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user){
		userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] unames){
		userService.removeByIds(Arrays.asList(unames));

        return R.ok();
    }
}
