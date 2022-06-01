package com.zealon.readingcloud.homepage.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 精品页VO对象
 * @author hasee
 */
@Data
public class IndexPageVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 配置项类型：1.banner 2.榜单
     */
    private Integer itemType;


    /**
     * 榜单/banner Id
     */
    private Integer itemId;

    /**
     * Banner VO对象
     */
    private IndexBannerVO banner;

    /**
     * 榜单VO对象
     */
    private IndexBookListVO bookList;


}
