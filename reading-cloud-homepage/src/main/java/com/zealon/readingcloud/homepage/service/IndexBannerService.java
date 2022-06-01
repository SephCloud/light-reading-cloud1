package com.zealon.readingcloud.homepage.service;

import com.zealon.readingcloud.homepage.vo.IndexBannerVO;

/**
 * Banner服务
 * @author hasee
 */
public interface IndexBannerService {

    /**
     * 获取BannerVO
     * @param bannerId
     * @return
     */
    IndexBannerVO getBannerVO(Integer bannerId);

}
