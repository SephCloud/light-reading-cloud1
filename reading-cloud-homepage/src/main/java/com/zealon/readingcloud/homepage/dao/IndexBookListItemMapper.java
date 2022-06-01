package com.zealon.readingcloud.homepage.dao;


import com.zealon.readingcloud.common.pojo.index.IndexBooklistItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 书单配置 book
 * @author hasee
 */
@Repository
public interface IndexBookListItemMapper {

    /**
     * 查询榜单全部图书
     * @param bookListId
     * @return
     */
    List<IndexBooklistItem> findPageWithResult(Integer bookListId);

}
