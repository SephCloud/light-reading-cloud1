package com.zealon.readingcloud.homepage.controller;

import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.homepage.service.Impl.IndexBookListItemServiceImpl;
import com.zealon.readingcloud.homepage.vo.BookVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 书单图书接口
 * @author hasee
 */
@Api(tags = "精品页书单图书接口")
@RestController
public class IndexBookListItemController {

    @Autowired
    private IndexBookListItemServiceImpl indexBookListItemService;



    @ApiOperation(value = "书单更多分页接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookListId", value = "书单Id", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页数量", required = true, dataType = "Integer")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = BookVO.class)})
    @GetMapping("index/getBookListPagingBooks")
    public Result getBookListPagingBooks(Integer bookListId, Integer page, Integer limit){

        return indexBookListItemService.getBookListPagingBooks(bookListId, page, limit);
    }


    @ApiOperation(value = "书单换一换接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookListId", value = "书单Id", required = true),
            @ApiImplicitParam(paramType = "query", name = "clientRandomNumber", value = "客户端当前书单编号", required = true, dataType = "Integer")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = BookVO.class)})
    @GetMapping("index/getBookListExchange")
    public Result getBookListExchange(Integer bookListId, Integer clientRandomNumber){

        return indexBookListItemService.getBookListExchange(bookListId, clientRandomNumber);
    }

}
