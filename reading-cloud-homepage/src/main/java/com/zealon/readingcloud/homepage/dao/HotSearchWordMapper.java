package com.zealon.readingcloud.homepage.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 热搜词
 * @author hasee
 */
@Repository
public interface HotSearchWordMapper {

    /**
     * 获取热搜词
     * @param size
     * @return
     */
    @Select("SELECT name FROM hot_search_word order by frequency desc limit #{size}")
    List<String> getHotSearchWordList(int size);

}
