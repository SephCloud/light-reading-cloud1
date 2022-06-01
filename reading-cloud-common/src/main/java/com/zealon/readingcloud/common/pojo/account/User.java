package com.zealon.readingcloud.common.pojo.account;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    //serialVersionUID 是用于在序列化和反序列化过程中进行核验的一个版本号。
    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 唯一id
     */
    private String uuid;

    /**
     * 密码
     */
    private String userPwd;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 中文名
     */
    private String nickName;

    /**
     * 联系电话
     */
    private String phoneNumber;

    private String headImgUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
