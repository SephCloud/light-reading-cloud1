package com.zealon.readingcloud.homepage.dao;

import com.zealon.readingcloud.common.pojo.index.IndexBanner;
import org.springframework.stereotype.Repository;

/**
 * Banner
 * @author hasee
 */
@Repository
public interface IndexBannerMapper {

    IndexBanner selectById(Integer id);

}
