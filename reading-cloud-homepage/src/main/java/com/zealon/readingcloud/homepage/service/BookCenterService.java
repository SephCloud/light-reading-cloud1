package com.zealon.readingcloud.homepage.service;

import com.zealon.readingcloud.common.pojo.book.Book;

/**
 * 图书服务中心
 * @author hasee
 */
public interface BookCenterService {

    /**
     * 获取图书详情
     * @param bookId
     * @return
     */
    Book getBookById(String bookId);

}
