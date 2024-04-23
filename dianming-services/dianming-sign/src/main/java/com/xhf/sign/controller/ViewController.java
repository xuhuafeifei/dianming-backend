package com.xhf.sign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 返回静态页面
 *
 * @author xuhuafei
 * @email 2508020102@qq.com
 */
@Controller
@RequestMapping("sign/view")
public class ViewController {
    /**
     * 签到静态网页
     */
    @GetMapping("/sign-up")
    @CrossOrigin
    public String getSignFormHtml(HttpServletRequest request, HttpServletResponse response, String requestId) throws IOException {

        return "form";
    }

    @GetMapping("/page")
    @CrossOrigin
    public String page() {
        return "page";
    }
}
