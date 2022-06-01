package com.zealon.readingcloud.account.dao;

import com.zealon.readingcloud.common.pojo.account.UserLikeSee;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户爱看
 * @author hasee
 */
@Repository
public interface UserLikeSeeMapper {

    int deleteByUserIdAndBookId(@Param("userId") Integer userId,
                                @Param("bookId") String bookId);

    int insert(UserLikeSee userLikeSee);

    int selectCountByUserAndBookId(@Param("userId") Integer userId,
                                   @Param("bookId") String bookId);

    List<UserLikeSee> findPageWithResult(@Param("userId") Integer userId);

    Integer findPageWithCount(@Param("bookId") String bookId);

}
