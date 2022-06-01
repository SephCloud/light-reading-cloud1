package com.zealon.readingcloud.homepage.service.Impl;

import com.zealon.readingcloud.book.feign.client.BookClient;
import com.zealon.readingcloud.common.cache.RedisBookKey;
import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.pojo.book.Book;
import com.zealon.readingcloud.homepage.service.BookCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 图书中心服务
 * @author hasee
 */
@Service
public class BookCenterServiceImpl implements BookCenterService {

    @Autowired
    private BookClient bookClient;

    @Autowired
    private RedisService redisService;

    @Override
    public Book getBookById(String bookId) {
        //通过BookId返回Book对象


        //生成图书中心的缓存名称key
        String key = RedisBookKey.BookCenter.getFeignClientBookKey(bookId);

        //从缓存中通过key获取的所需的book值
        Book book = redisService.getCache(key, Book.class);

        //如果缓存中存在book，直接返回
        if(book != null){

            return  book;
        }

        //如果不存在，则通过图书中心服务获取
        book = bookClient.getBookById(bookId).getData();


        if(book != null){
            //如果book存在存入缓存中,设置过期时间
            redisService.setExpireCache(key, book, RedisExpire.HOUR);
        }


        return book;
    }
}
