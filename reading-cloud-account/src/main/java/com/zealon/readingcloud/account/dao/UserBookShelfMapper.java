package com.zealon.readingcloud.account.dao;

import com.zealon.readingcloud.common.pojo.account.UserBookshelf;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户书架
 * @author hasee
 */
@Repository
public interface UserBookShelfMapper {

    int deleteById(Integer id);

    int insert(UserBookshelf userBookshelf);

    int updateByUserIdAndBookId(UserBookshelf userBookshelf);

    int selectCountByUserAndBookId(@Param("userId") Integer userId,
                                   @Param("bookId") String bookId);

    UserBookshelf selectById(Integer id);

    List<UserBookshelf> findPageWithResult(@Param("userId") Integer userId);

}
