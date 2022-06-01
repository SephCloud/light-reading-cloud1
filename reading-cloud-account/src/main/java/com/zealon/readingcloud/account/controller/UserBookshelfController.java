package com.zealon.readingcloud.account.controller;

import com.zealon.readingcloud.account.bo.UserBookshelfBO;
import com.zealon.readingcloud.account.service.UserBookshelfService;
import com.zealon.readingcloud.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 书架接口
 * @author hasee
 */
@Api(tags = "用户书架")
@RestController
@RequestMapping("account/bookshelf")
public class UserBookshelfController {

    @Autowired
    private UserBookshelfService bookshelfService;


    @ApiOperation(value = "同步书架图书接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户id", required = true, dataType = "int")
    })
    @PostMapping("/sync-book")
    public Result syncUserBookshelf(@RequestHeader("userId") Integer userId,
                                    @RequestBody UserBookshelfBO bookshelfBO){

        return bookshelfService.syncUserBookShelf(userId, bookshelfBO);
    }


    @ApiOperation(value = "获取用户的书架接口",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户id", required = true, dataType = "int")
    })
    @GetMapping("/get-books")
    public Result getUserBookshelf(@RequestHeader("userId") Integer userId){

        return bookshelfService.getUserBookshelf(userId);
    }


    @ApiOperation(value = "查询用户中是否存在该书籍",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户id", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "书籍id", required = true, dataType = "String")
    })
    @GetMapping("/exist-book")
    public Result<Integer> userBookshelfExistBook(@RequestHeader Integer userId, String bookId){

        return bookshelfService.userBookExistBook(userId, bookId);
    }




}
