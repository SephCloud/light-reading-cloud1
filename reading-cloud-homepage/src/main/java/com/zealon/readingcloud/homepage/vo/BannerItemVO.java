package com.zealon.readingcloud.homepage.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Banner 明细VO
 * @author hasee
 */
@Data
public class BannerItemVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Integer id;

    /**
     * 名称
     */
    private String name;


    /**
     * 图片连接
     */
    private String imgUrl;


    /**
     * 跳转链接
     */
    private String url;


}
