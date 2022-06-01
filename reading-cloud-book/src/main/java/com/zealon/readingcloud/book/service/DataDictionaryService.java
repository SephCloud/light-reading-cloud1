package com.zealon.readingcloud.book.service;

import com.zealon.readingcloud.common.pojo.book.DataDictionary;

import java.util.Map;

/**
 * 字典服务
 * @author hasee
 */
public interface DataDictionaryService {

    /**
     * 字典服务
     * @param dicType
     * @param field
     * @return
     */
    Map<String, DataDictionary> getDictionaries(String dicType, String field);

}
