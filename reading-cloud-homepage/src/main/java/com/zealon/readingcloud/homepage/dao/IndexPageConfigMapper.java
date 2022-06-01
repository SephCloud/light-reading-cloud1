package com.zealon.readingcloud.homepage.dao;

import com.zealon.readingcloud.common.pojo.index.IndexPageConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主页配置
 * @author hasee
 */
@Repository
public interface IndexPageConfigMapper {

    /**
     * 主键查询实体
     * @param id
     * @return
     */
    IndexPageConfig selectById(Integer id);


    /**
     * 查询主页配置
     * @param pageType
     * @return
     */
    List<IndexPageConfig> findPageWithResult(Integer pageType);

}
