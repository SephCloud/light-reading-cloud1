package com.zealon.readingcloud.account.controller;

import com.zealon.readingcloud.account.service.UserLikeSeeService;
import com.zealon.readingcloud.common.pojo.account.UserLikeSee;
import com.zealon.readingcloud.common.request.RequestParams;
import com.zealon.readingcloud.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 喜欢看接口
 * @author hasee
 */
@Api(tags = "喜欢看")
@RestController
@RequestMapping("account/like-see")
public class LikeSeeController {

    @Autowired
    private UserLikeSeeService userLikeSeeService;


    @ApiOperation(value = "用户喜欢点击接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书Id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "value", value = "值：0取消喜欢，1喜欢", required = true, dataType = "int")
    })
    @PostMapping("/click")
    public Result likeSeeClick(@RequestHeader("userId") Integer userId,
                               @RequestBody RequestParams params){

        String bookId = params.getStringValue("bookId");
        Integer value = params.getIntValue("value");


        return userLikeSeeService.likeSeeClick(userId, bookId, value);

    }

    @ApiOperation(value = "获取图书喜欢总数", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", required = true, dataType = "String")
    })
    @GetMapping("/get-count")
    public Result<Integer> getBookListCount(String bookId){

        return userLikeSeeService.getBookLikeCount(bookId);

    }


    @ApiOperation(value = "获取用户喜欢的图书列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户id", required = true,dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页数量", required = true, dataType = "int")
    })
    @GetMapping("/get-books")
    public Result getUserLikeBookList(@RequestHeader("userId") Integer userId, Integer page, Integer limit){

        return userLikeSeeService.getUserLikeBookList(userId, page, limit);
    }




    @ApiOperation(value = "用户是否喜欢此书", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户Id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "书籍id", required = true, dataType = "String")
    })
    @GetMapping("/exist-book")
    public Result<Integer> userLikeThisBook(@RequestHeader("userId") Integer userId,
                                            String bookId){

        return userLikeSeeService.userLikeThisBook(userId, bookId);
    }



}
