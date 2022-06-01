package com.zealon.readingcloud.homepage.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.homepage.vo.BookListBookVO;

import java.util.List;

/**
 * 书单图书服务
 * @author hasee
 */
public interface IndexBookListItermService {

    /**
     * 书单更多分类接口
     * @param bookListId 书单ID
     * @param page 页数
     * @param limit 每页数量
     * @return
     */
    Result getBookListPagingBooks(Integer bookListId, Integer page, Integer limit);


    /**
     * 书单换一换接口
     * @param bookListId 书单id
     * @param clientRandomNumber 客户端当前随机编号
     * @return
     */
    Result getBookListExchange(Integer bookListId, Integer clientRandomNumber);


    /**
     * 随机获取书单图书信息
     * @param bookListId 书单ID
     * @param bookIds 图书IDs
     * @param showNumber 显示数量
     * @param clientRandomNumber 客户端当前随机编号
     * @param showLikeCount
     * @return
     */
    List<BookListBookVO> getBookListRandomBooks(Integer bookListId, String bookIds, Integer showNumber, Integer clientRandomNumber, Boolean showLikeCount);


    /**
     * 顺序获取书单图书
     * @param bookListId 书单id
     * @param bookIds 图书ids
     * @param showNumber 显示数量
     * @param showLikeCount 显示喜欢数
     * @return
     */
    List<BookListBookVO> getBookListOrderBooks(Integer bookListId, String bookIds, Integer showNumber, Boolean showLikeCount);

}
