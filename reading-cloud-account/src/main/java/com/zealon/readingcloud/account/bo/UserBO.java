package com.zealon.readingcloud.account.bo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户业务对象
 * @author hasee
 */
@Api(value = "用户注册信息")
@Data
public class UserBO {


    @ApiModelProperty(value = "登录名（至少4字符）", example = "apple")
    private String loginName;

    @ApiModelProperty(value = "密码", example = "123456")
    private String userPwd;

    @ApiModelProperty(value = "昵称", example = "苹果")
    private String nickName;

    @ApiModelProperty(value = "手机", example = "13800138000")
    private String phoneNumber;

}
