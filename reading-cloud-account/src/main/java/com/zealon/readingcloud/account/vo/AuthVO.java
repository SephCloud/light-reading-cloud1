package com.zealon.readingcloud.account.vo;

import lombok.Data;

/**
 * 认证VO
 * @author hasee
 */
@Data
public class AuthVO {

    private String token;
    private UserVO user;

}
