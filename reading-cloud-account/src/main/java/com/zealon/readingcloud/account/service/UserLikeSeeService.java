package com.zealon.readingcloud.account.service;

import com.zealon.readingcloud.common.result.Result;

/**
 * 喜欢看服务
 * @author hasee
 */
public interface UserLikeSeeService {

    /**
     * 点击喜欢处理
     * @param userId
     * @param bookId
     * @param value 0：取消喜欢。1：喜欢
     * @return
     */
    Result likeSeeClick(Integer userId, String bookId, Integer value);


    /**
     * 获取图书喜欢数量
     * @param bookId
     * @return
     */
    Result<Integer> getBookLikeCount(String bookId);


    /**
     * 获取用户喜欢列表
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    Result getUserLikeBookList(Integer userId, Integer page, Integer limit);


    /**
     * 用户是否喜欢此书
     * @param userId
     * @param bookId
     * @return
     */
    Result userLikeThisBook(Integer userId, String bookId);


}
