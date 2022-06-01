package com.zealon.readingcloud.homepage.service;

import com.zealon.readingcloud.common.pojo.index.IndexBooklist;
import com.zealon.readingcloud.homepage.vo.IndexBookListVO;

/**
 * 书单服务
 * @author hasee
 */
public interface IndexBookListService {

    /**
     * 获取书单VO
     * @param bookListId
     * @param clientRandomNumber
     * @return
     */
    IndexBookListVO getIndexBookListVO(Integer bookListId, Integer clientRandomNumber);


    /**
     * 查询书单信息
     * @param bookListId
     * @return
     */
    IndexBooklist getIndexBookListById(Integer bookListId);


    /**
     * 获取随机榜单VO
     * @param bookList
     * @param clientRandomNumber
     * @return
     */
    IndexBookListVO getRandomIndexBookListVO(IndexBooklist bookList, Integer clientRandomNumber);


}
