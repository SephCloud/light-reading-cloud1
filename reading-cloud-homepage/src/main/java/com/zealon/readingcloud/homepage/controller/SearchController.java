package com.zealon.readingcloud.homepage.controller;

import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.homepage.service.SearchService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;

/**
 * 图书查询接口
 * @author hasee
 */
@Api(tags = "图书查询接口")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;



    @ApiOperation(value = "图书查询接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "关键字", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页数量", required = true, dataType = "int")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class)})
    @GetMapping("index/searchBooks")
    public Result getSearchResultBooks(String keyword, Integer page, Integer limit){

        return searchService.getSearchResultBook(keyword, page, limit);
    }


    @ApiOperation(value = "获取热搜词接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "size", value = "返回数量", required = true, dataType = "int")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class)})
    @GetMapping("index/getHotSearchWords")
    public Result getHotSearchWordList(Integer size){

        return  searchService.getHotSearchWordList(size);

    }

}
