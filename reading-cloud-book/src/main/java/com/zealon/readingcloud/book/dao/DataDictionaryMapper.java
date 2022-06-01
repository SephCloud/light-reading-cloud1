package com.zealon.readingcloud.book.dao;
import com.zealon.readingcloud.common.pojo.book.DataDictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据字典
 * @author hasee
 */


@Repository
public interface DataDictionaryMapper {

    DataDictionary selectById(Integer id);

    DataDictionary selectByDicTypeAndCode(@Param("dicType") String dicType, @Param("code") Integer code);

    List<DataDictionary> findPageWithResult(@Param("dicType") String dicType);

    Integer findPageWithCount(@Param("dicType") String dicType);
}
