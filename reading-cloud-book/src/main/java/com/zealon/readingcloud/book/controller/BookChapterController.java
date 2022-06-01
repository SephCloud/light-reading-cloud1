package com.zealon.readingcloud.book.controller;


import com.zealon.readingcloud.book.service.BookChapterService;
import com.zealon.readingcloud.book.vo.BookChapterReadVO;
import com.zealon.readingcloud.common.pojo.book.BookChapter;
import com.zealon.readingcloud.common.result.Result;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import java.sql.ResultSet;

@Api(value = "book/chapter", tags = "章节查询接口")
@RestController
@RequestMapping("book/chapter")
public class BookChapterController {

    @Autowired
    private BookChapterService bookChapterService;

    @ApiOperation(value = "查询图书章节基本信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "chapterId", value = "章节ID", dataType = "Integer")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "bookId", response = BookChapter.class)})
    @RequestMapping("/getChapter")
    public Result getChapter(String bookId, Integer chapterId){

        return bookChapterService.getChapterById(bookId, chapterId);
    }


    @ApiOperation(value = "查询图书章节列表信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书Id", dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = BookChapter.class)})
    @RequestMapping("/getChapterList")
    public Result getBookChapterList(String bookId){

        return bookChapterService.getBookChapterListByBookId(bookId);
    }


    @ApiOperation(value = "阅读内容", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "chapterId", value = "章节ID", dataType = "Integer")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = BookChapterReadVO.class)})
    @RequestMapping("/readChapter")
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId){

        return bookChapterService.readChapter(bookId, chapterId);

    }

}
