package com.zealon.readingcloud.homepage.service;

import com.zealon.readingcloud.common.result.Result;

/**
 * 搜索服务
 * @author hasee
 */
public interface SearchService {

    /**
     * 获取热搜词
     * @param size
     * @return
     */
    Result getHotSearchWordList(Integer size);


    /**
     * 查询结果图书列表
     * @param keyword
     * @param from
     * @param size
     * @return
     */
    Result getSearchResultBook(String keyword, Integer from, Integer size);

}
