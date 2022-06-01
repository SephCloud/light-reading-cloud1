package com.zealon.readingcloud.book.dao;
import com.zealon.readingcloud.common.pojo.book.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookMapper{


    Book selectById(Integer id);

//    @Select("select * from book where book_id = #{bookId}")
    Book selectByBookId(String bookId);

    List<Book> findPageWithResult(@Param("dicCategory") Integer dicCategory,
                                  @Param("dicChannel") Integer dicChannel,
                                  @Param("dicSerialStatus") Integer dicSerialStatus,
                                  @Param("onlineStatus") Integer onlineStatus,
                                  @Param("authorId") Integer authorId,
                                  @Param("bookId") String bookId,
                                  @Param("bookName") String bookName);

    Integer findPageWithCount(@Param("dicCategory") Integer dicCategory,
                              @Param("dicChannel") Integer dicChannel,
                              @Param("dicSerialStatus") Integer dicSerialStatus,
                              @Param("onlineStatus") Integer onlineStatus,
                              @Param("authorId") Integer authorId,
                              @Param("bookId") String bookId,
                              @Param("bookName") String bookName);



}
