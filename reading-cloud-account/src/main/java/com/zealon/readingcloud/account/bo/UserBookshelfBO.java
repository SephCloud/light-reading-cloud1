package com.zealon.readingcloud.account.bo;

import lombok.Data;

/**
 * 用户书架上报数据 BO
 * @author hasee
 */
@Data
public class UserBookshelfBO {

    private Integer id;

    /**
     * 同步类型：
     * 1.新增  2.更新  3.删除
     */
    private Integer syncType;

    /**
     * 图书Id
     */
    private String bookId;

    /**
     * 最后章节Id
     */
    private Integer lastChapterId;



}
