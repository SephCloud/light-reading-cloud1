package com.zealon.readingcloud.book.service;

import com.zealon.readingcloud.book.vo.BookVO;
import com.zealon.readingcloud.common.result.Result;

/**
 * 图书服务
 * @author hasee
 */
public interface BookService {


    /**
     * 查询图书基本信息
     * @param bookId
     * @return
     */
    Result getBookById(String bookId);


    /**
     * 查询图书详情
     * @param bookId
     * @return
     */
    Result<BookVO> getBookDetails(String bookId);

}
