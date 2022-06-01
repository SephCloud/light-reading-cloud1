package com.zealon.readingcloud.homepage.dao;

import com.zealon.readingcloud.common.pojo.index.IndexBannerItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Banner类
 * @author hasee
 */
@Repository
public interface IndexBannerItemMapper {

    IndexBannerItem selectById(Integer id);

    /**
     * 按Banner查询明细列表
     * @param bannerId
     * @return
     */
    List<IndexBannerItem> findPageWithResult(Integer bannerId);



}
