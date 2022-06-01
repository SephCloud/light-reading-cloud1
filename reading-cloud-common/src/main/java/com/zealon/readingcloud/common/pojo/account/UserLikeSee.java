package com.zealon.readingcloud.common.pojo.account;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

// Serializable 可被序列化
@Data
public class UserLikeSee implements Serializable {

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
     * 创建时间
     */
    private Date createTime;


}
