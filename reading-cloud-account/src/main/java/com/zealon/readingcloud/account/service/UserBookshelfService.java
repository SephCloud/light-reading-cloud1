package com.zealon.readingcloud.account.service;

import com.zealon.readingcloud.account.bo.UserBookshelfBO;
import com.zealon.readingcloud.common.result.Result;

/**
 * 暑假服务
 * @author hasee
 */
public interface UserBookshelfService {

    /**
     * 同步书架
     * @param userId
     * @param bookshelf
     * @return
     */
    Result syncUserBookShelf(Integer userId, UserBookshelfBO bookshelf);


    /**
     * 获取用户书架数据
     * @param userId
     * @return
     */
    Result getUserBookshelf(Integer userId);


    /**
     * 用户书架是否存在该图书
     * @param userId
     * @param bookId
     * @return
     */
    Result userBookExistBook(Integer userId, String bookId);


}
