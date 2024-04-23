package com.xhf.model.user.vo;

import lombok.Data;

import java.util.List;

@Data
public class LoginVo {
    private String username;
    private String accessToken;
    private String refreshToken;
    private Long id;
    private List<String> roles;
}
