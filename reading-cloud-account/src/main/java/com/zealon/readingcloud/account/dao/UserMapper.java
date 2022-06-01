package com.zealon.readingcloud.account.dao;

import com.zealon.readingcloud.common.pojo.account.User;
import org.springframework.stereotype.Repository;

/**
 * 用户
 * @author hasee
 */
@Repository
public interface UserMapper {

    int insert(User user);

    int updateByLoginName(User user);

    User selectByLoginName(String loginName);

}
