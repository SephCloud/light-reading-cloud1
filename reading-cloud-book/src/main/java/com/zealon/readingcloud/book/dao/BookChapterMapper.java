package com.zealon.readingcloud.book.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zealon.readingcloud.common.pojo.book.BookChapter;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图书章节
 * @author hasee
 */


@Repository
public interface BookChapterMapper {

    BookChapter selectById(@Param("id") Integer id);

    List<BookChapter> findPageWithResult(@Param("bookId") Integer bookId);

    int findPageWithCount(@Param("bookId") Integer bookId);

    /**
     * 查询上一章节ID
     * @param bookId
     * @param currentSortNumber
     * @return
     */
    @Select("select id from book_chapter  " +
            " where book_id=#{bookId} " +
            " and sort_number < #{currentSortNumber} " +
            " order by sort_number desc limit 1")
    Integer selectPreChapterId(@Param("bookId") Integer bookId,
                               @Param("currentSortNumber") Integer currentSortNumber);

    /**
     * 查询下一章节ID
     * @param bookId
     * @param currentSortNumber
     * @return
     */
    @Select("select id from book_chapter  " +
            " where book_id=#{bookId} " +
            " and sort_number > #{currentSortNumber} " +
            " order by sort_number asc limit 1")
    Integer selectNextChapterId(@Param("bookId") Integer bookId,
                                @Param("currentSortNumber") Integer currentSortNumber);

}
