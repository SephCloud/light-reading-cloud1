package com.zealon.readingcloud.common.pojo.index;

import lombok.Data;

import java.util.Date;

/**
 * @author hasee
 * 书单配置明细表
 */
@Data
public class IndexBooklistItem {


    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 书单id
     */
    private Integer booklistId;

    /**
     * 图书id
     */
    private String bookId;

    /**
     * 排序
     */
    private Integer sortNumber;

    /**
     * 创建者
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新者
     */
    private String updater;

}
