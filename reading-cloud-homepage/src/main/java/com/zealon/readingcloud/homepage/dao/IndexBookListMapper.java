package com.zealon.readingcloud.homepage.dao;


import com.zealon.readingcloud.common.pojo.index.IndexBooklist;
import org.springframework.stereotype.Repository;

/**
 * 书单配置
 * @author hasee
 */
@Repository
public interface IndexBookListMapper {

    IndexBooklist selectById(Integer id);

}
