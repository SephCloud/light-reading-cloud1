package com.zealon.readingcloud.common.pojo.account;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author hasee
 */
@Data
public class UserBookshelf {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 图书id
     */
    private String bookId;

    /**
     * 图书最后章节id
     */
    private Integer lastChapterId;

    /**
     * 最后阅读时间
     */
    private Long lastReadTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
