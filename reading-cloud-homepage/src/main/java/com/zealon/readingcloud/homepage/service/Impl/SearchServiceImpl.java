package com.zealon.readingcloud.homepage.service.Impl;

import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import com.zealon.readingcloud.homepage.dao.HotSearchWordMapper;
import com.zealon.readingcloud.homepage.domain.RequestQuery;
import com.zealon.readingcloud.homepage.domain.SearchBookItem;
import com.zealon.readingcloud.homepage.domain.SearchBookResult;
import com.zealon.readingcloud.homepage.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图书查询服务
 * @author hasee
 */
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    /** ES Jest客户端对象 */
    @Autowired
    private JestClient jestClient;

    /** 索引别名 */
    @Value("${es.aliasName}")
    private String aliasName;


    /** 类型 */
    @Value("${es.indexType}")
    private String indexType;

    @Autowired
    private HotSearchWordMapper hotSearchWordMapper;

    /**
     * 得到搜索热词
     * @param size 个数
     * @return
     */
    @Override
    public Result getHotSearchWordList(Integer size) {

        List<String> hotSearchWordList = hotSearchWordMapper.getHotSearchWordList(size);

        return ResultUtil.success(hotSearchWordList);
    }

    /**
     *
     * @param keyword
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Result getSearchResultBook(String keyword, Integer page, Integer limit) {

        //查询条件
        Map query = new HashMap();

        //多字段匹配
        Map multiMatch = new HashMap();

        multiMatch.put("query", keyword);
        multiMatch.put("type", "most_fields");


        String[] fields = new String[]{"bookName^2", "bookName.pinyin", "author"};
        multiMatch.put("fields", fields);
        query.put("multi_match", multiMatch);

        int from = (page - 1) * limit;
        int size = from + limit;
        RequestQuery requestQuery = new RequestQuery(from, size, query);

        SearchBookResult searchBookResult = getSearchResult(requestQuery.toString());

        return ResultUtil.success(searchBookResult);
    }


    /**
     * ES 执行查询结果
     * @param query
     * @return
     */
    private SearchBookResult getSearchResult(String query){

        SearchBookResult result = new SearchBookResult();

        //封装查询对象
        Search search = new Search.Builder(query)
                .addIndex(aliasName)
                .addType(indexType).build();

        //执行查询
        try {

            SearchResult searchResult = jestClient.execute(search);
            List<SearchBookItem> bookList;

            if(searchResult.isSucceeded()){

                //查询成功，处理结果项
                List<SearchResult.Hit<SearchBookItem, Void>> hitsList = searchResult.getHits(SearchBookItem.class);
                bookList = new ArrayList<>(hitsList.size());

                for(SearchResult.Hit<SearchBookItem, Void> hit : hitsList){
                    bookList.add(hit.source);
                }

            }else {

                bookList = new ArrayList<>();
            }

            //赋值
            result.setTotal(searchResult.getTotal());
            result.setBookList(bookList);


        }catch (IOException e){

            LOGGER.error("查询图书异常, 查询语句：{}", query, e);

        }

        return result;

    }
}
