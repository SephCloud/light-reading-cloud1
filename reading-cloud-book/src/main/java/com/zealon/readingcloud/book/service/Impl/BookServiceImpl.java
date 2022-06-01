package com.zealon.readingcloud.book.service.Impl;


import com.zealon.readingcloud.book.dao.BookMapper;
import com.zealon.readingcloud.book.service.BookService;
import com.zealon.readingcloud.book.vo.BookVO;
import com.zealon.readingcloud.common.cache.RedisBookKey;
import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.constant.CategoryConstant;
import com.zealon.readingcloud.common.enums.BookSerialStatusEnum;
import com.zealon.readingcloud.common.pojo.book.Book;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 图书服务
 * @author hasee
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public Result getBookById(String bookId) {

        /**
         * key = "book-center:detail-bookId"
         * 通过bookId生成该book的key名
         */
        String key = RedisBookKey.getBookKey(bookId);


        /**
         * 从缓存中获取book的数据
         */
        Book book = this.redisService.getCache(key, Book.class);


        if(book == null){

            book = this.bookMapper.selectByBookId(bookId);

            if(null != book){

                this.redisService.setExpireCache(key, book, RedisExpire.HOUR);

            }else{

                return ResultUtil.notFound().buildMessage("找不到"+ bookId +"这本书！");

            }

        }

        return ResultUtil.success(book);
    }

    @Override
    public Result<BookVO> getBookDetails(String bookId) {

        Book book = (Book) this.getBookById(bookId).getData();

        if(book == null){

            return ResultUtil.notFound().buildMessage("找不到"+ bookId +"这本书！");
        }

        BookVO bookVO = new BookVO();
        BeanUtils.copyProperties(book, bookVO);

        //分类
        String categoryName = CategoryConstant.categorys.get(book.getDicCategory());
        bookVO.setCategoryName(categoryName);

        //连载状态
        String serialStatusName = BookSerialStatusEnum.values()[book.getDicSerialStatus() - 1].getName();
        bookVO.setSerialStatusName(serialStatusName);

        return ResultUtil.success(bookVO);
    }
}
