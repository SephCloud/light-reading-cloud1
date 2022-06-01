package com.zealon.readingcloud.account.service.task;

import com.zealon.readingcloud.common.cache.RedisAccountKey;
import com.zealon.readingcloud.common.cache.RedisService;

/**
 * 喜欢看点击任务
 * @author hasee
 */
public class LikeSeeClickTask implements Runnable{

    private RedisService redisService;
    private String bookId;
    private Integer value;


    @Override
    public void run() {

        //缓存中是否存在该书的喜欢数
        if(this.redisService.hashHasKey(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, this.bookId)){

            int val = 1;

            //value为0时取消喜欢，1时增加喜欢
            if(value <= 0){
                val = -1;
            }
            this.redisService.hashIncrement(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, this.bookId, value);
        }

    }

    public LikeSeeClickTask(RedisService redisService, String bookId, Integer value){

        this.redisService = redisService;
        this.bookId = bookId;
        this.value = value;
    }
}
