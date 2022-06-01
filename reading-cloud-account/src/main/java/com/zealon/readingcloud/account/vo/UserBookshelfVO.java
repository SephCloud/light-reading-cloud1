package com.zealon.readingcloud.account.vo;

import lombok.Data;

/**
 * 书架VO
 * @author hasee
 */
@Data
public class UserBookshelfVO {

    private Integer id;

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 图书Id
     */
    private String bookId;

    private String bookName;

    private String authorName;

    private String imgUrl;

    /**
     * 图书最后章节Id
     */
    private Integer lastChapterId;

    /**
     * 最后阅读时间
     */
    private Long lastReadTime;

}
