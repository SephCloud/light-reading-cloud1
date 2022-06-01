package com.zealon.readingcloud.account.controller;

import com.zealon.readingcloud.account.bo.UserBO;
import com.zealon.readingcloud.account.service.UserService;
import com.zealon.readingcloud.account.vo.AuthVO;
import com.zealon.readingcloud.common.pojo.account.User;
import com.zealon.readingcloud.common.request.RequestParams;
import com.zealon.readingcloud.common.result.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;

/**
 * 用户接口
 * 主要为用户注册和登录
 * @author hasee
 */
@Api(value = "用户服务接口")
@RestController
@RequestMapping("account/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "注册用户", httpMethod = "POST")
    @ApiResponses({@ApiResponse(code = 200, message = "",response = Result.class)})
    @PostMapping("/register")
    public Result register(UserBO userBO){

        return userService.register(userBO);

    }



    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "loginName", value = "登录名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "登录密码", required = true, dataType = "String")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "", response = AuthVO.class))
    @PostMapping("/login")
    public Result<AuthVO> login(@RequestBody RequestParams params){

        String loginName = params.getStringValue("loginName");
        String password = params.getStringValue("password");

        return userService.login(loginName, password);
    }



}
