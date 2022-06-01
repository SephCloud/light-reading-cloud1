package com.zealon.readingcloud.book.service.Impl;

import com.zealon.readingcloud.book.dao.DataDictionaryMapper;
import com.zealon.readingcloud.book.service.DataDictionaryService;
import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.pojo.book.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 字典服务实现类
 * @author hasee
 */
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private DataDictionaryMapper dataDictionaryMapper;

    @Override
    public Map<String, DataDictionary> getDictionaries(String dicType, String field) {

        String key = "dictionary" + dicType;

        HashMap<String, DataDictionary> map = new HashMap<>();

        List<DataDictionary> dictionaries = redisService.getHashListVal(key, field, DataDictionary.class);

        if(dictionaries.size() == 0){
            dictionaries = dataDictionaryMapper.findPageWithResult(dicType);
            if(dictionaries.size() > 0){
                map = this.getMap(dictionaries);
                redisService.setHashValsExpire(key, map, RedisExpire.DAY);

            }else{


                map = this.getMap(dictionaries);
            }
        }

        return map;
    }


    private  HashMap<String, DataDictionary> getMap(List<DataDictionary> dictionaries){

        HashMap<String, DataDictionary> map = new HashMap<>();

        for(int i = 0; i < dictionaries.size(); i++){
            DataDictionary dictionary = dictionaries.get(i);
            map.put(dictionary.getCode().toString(), dictionary);

        }
        return map;
    }
}
