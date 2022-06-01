package com.zealon.readingcloud.homepage.service.Impl;

import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisHomepageKey;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.enums.BooklistMoreTypeEnum;
import com.zealon.readingcloud.common.pojo.index.IndexBooklist;
import com.zealon.readingcloud.homepage.common.Const;
import com.zealon.readingcloud.homepage.dao.IndexBookListMapper;
import com.zealon.readingcloud.homepage.service.IndexBookListItermService;
import com.zealon.readingcloud.homepage.service.IndexBookListService;
import com.zealon.readingcloud.homepage.vo.BookListBookVO;
import com.zealon.readingcloud.homepage.vo.IndexBookListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 书单服务
 * @author hasee
 */
@Service
public class IndexBookListServiceImpl implements IndexBookListService {

    @Autowired
    private IndexBookListMapper indexBookListMapper;

    @Autowired
    private IndexBookListItermService indexBookListItermService;

    @Autowired
    private RedisService redisService;



    /**
     *获取书单VO
     * @param bookListId
     * @param clientRandomNumber
     * @return
     */
    @Override
    public IndexBookListVO getIndexBookListVO(Integer bookListId, Integer clientRandomNumber) {

        IndexBooklist bookList = this.getIndexBookListById(bookListId);

        //是否随机获取
        Boolean randomFlag = bookList.getMoreType() == BooklistMoreTypeEnum.EXCHANGE.getValue();
        IndexBookListVO bookListVO;

        //是随机获取
        if(randomFlag){
            bookListVO = getRandomIndexBookListVO(bookList, clientRandomNumber);
        }else {

            String key = RedisHomepageKey.getBooklistVoKey(bookListId);
            bookListVO = redisService.getCache(key, IndexBookListVO.class);

            //缓存中没有该值
            if(bookListVO == null){
                //从DB中顺序获取list
                List<BookListBookVO> books = indexBookListItermService.getBookListOrderBooks(bookList.getId(), bookList.getBookIds(), bookList.getShowNumber(), bookList.getShowLikeCount());

                if(books.size() > 0){
                    bookListVO = new IndexBookListVO();
                    BeanUtils.copyProperties(bookList, bookListVO);
                    bookListVO.setBooks(books);
                    //换一换随机编码
                    bookListVO.setRandomNumber(1);
                    redisService.setExpireCache(key, bookListVO, RedisExpire.HOUR_TWO);
                }

            }

        }

        return bookListVO;
    }

    /**
     * 通过bookListId 查询IndexBookList对象
     * @param bookListId
     * @return
     */
    @Override
    public IndexBooklist getIndexBookListById(Integer bookListId) {
        String key = RedisHomepageKey.getBooklistDbKey(bookListId);
        IndexBooklist bookList = redisService.getCache(key, IndexBooklist.class);

        if(bookList == null){
            //缓存中没有，在db中查询
            bookList = indexBookListMapper.selectById(bookListId);

            if(bookList != null){
                redisService.setExpireCache(key, bookList, RedisExpire.DAY);
            }
        }

        return bookList;
    }

    /**
     *生成随机书单
     * @param bookList
     * @param clientRandomNumber
     * @return
     */
    @Override
    public IndexBookListVO getRandomIndexBookListVO(IndexBooklist bookList, Integer clientRandomNumber) {

        Random random = new Random();

        //生成一个随机数，介于0-n之间（不包括n）
        Integer randomNumber = random.nextInt(Const.BOOOKLIST_RANDOM_COUNT);

        //客户端随机数存在
        if(clientRandomNumber != null){
            //直到客户端随机数不等于randomValue
            while (randomNumber.intValue() == clientRandomNumber){
                randomNumber = random.nextInt(Const.BOOOKLIST_RANDOM_COUNT);
            }
        }

        //生成bookList的缓存key
        String key = RedisHomepageKey.getBooklistRandomVoKey(bookList.getId());

        //缓存中是否存在key为BookListId，field为randomNumber的值
        IndexBookListVO bookListVO = redisService.getHashVal(key, randomNumber.toString(), IndexBookListVO.class);

        //缓存中不存在，则从DB中获取
        if(bookListVO == null){
            List<BookListBookVO> books = indexBookListItermService.getBookListRandomBooks(bookList.getId(), bookList.getBookIds(), bookList.getShowNumber(), clientRandomNumber, bookList.getShowLikeCount());

            if(books.size() > 0){

                bookListVO = new IndexBookListVO();
                BeanUtils.copyProperties(bookList, bookListVO);
                bookListVO.setBooks(books);
                bookListVO.setRandomNumber(randomNumber);

                //hash缓存
                redisService.setHashValExpire(key, randomNumber.toString(), bookListVO, RedisExpire.HOUR_TWO);
            }

        }

        return bookListVO;
    }
}
