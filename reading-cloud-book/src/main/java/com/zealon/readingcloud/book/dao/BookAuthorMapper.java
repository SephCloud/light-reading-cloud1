package com.zealon.readingcloud.book.dao;

import com.zealon.readingcloud.common.pojo.book.BookAuthor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图书作者
 * @author hasee
 */


@Repository
public interface BookAuthorMapper {

    List<BookAuthor> findPageWithResult(@Param("name") String name);

    Integer findPageWithCount(String name);

}
