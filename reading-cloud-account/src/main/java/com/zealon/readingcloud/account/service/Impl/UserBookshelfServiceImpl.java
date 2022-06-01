package com.zealon.readingcloud.account.service.Impl;

import com.zealon.readingcloud.account.bo.UserBookshelfBO;
import com.zealon.readingcloud.account.dao.UserBookShelfMapper;
import com.zealon.readingcloud.account.service.UserBookshelfService;
import com.zealon.readingcloud.account.service.task.UserBookShelfTask;
import com.zealon.readingcloud.account.vo.UserBookshelfVO;
import com.zealon.readingcloud.book.feign.client.BookClient;
import com.zealon.readingcloud.common.pojo.account.UserBookshelf;
import com.zealon.readingcloud.common.pojo.book.Book;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 书架服务
 * @author hasee
 */
@Service
public class UserBookshelfServiceImpl implements UserBookshelfService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserBookshelfServiceImpl.class);

    @Autowired
    private UserBookShelfMapper bookShelfMapper;

    /** 书架同步任务线程池 */
    @Autowired
    private ExecutorService userBookshelfQueueThreadPool;

    /** 图书资源中心feign接口 */
    @Autowired
    private BookClient bookClient;


    @Override
    public Result syncUserBookShelf(Integer userId, UserBookshelfBO bookshelfBO) {

        UserBookshelf bookshelf = new UserBookshelf();
        BeanUtils.copyProperties(bookshelfBO, bookshelf);

        //将该书的上一次阅读时间设置为现在
        bookshelf.setLastReadTime(System.currentTimeMillis());

        //异步处理同步任务
        UserBookShelfTask task = new UserBookShelfTask(bookshelfBO.getSyncType(), bookshelf, bookShelfMapper, userId);
        userBookshelfQueueThreadPool.execute(task);

        return ResultUtil.success();
    }

    /**
     * 查询用户书架
     * @param userId
     * @return
     */
    @Override
    public Result getUserBookshelf(Integer userId) {

        //在数据库中查询用户的书架
        List<UserBookshelf> pageWithResult = bookShelfMapper.findPageWithResult(userId);

        System.out.println("shelf的大小" + pageWithResult.size());

        //展示用的Bookshelf
        List<UserBookshelfVO> bookshelfs = new ArrayList<>();

        //遍历用户书架
        for(int i = 0; i < pageWithResult.size(); i++){

            //对书架中的书进行遍历
            UserBookshelf bookshelf = pageWithResult.get(i);

            //通过feign接口调用，获取到book的详细信息
            Book book = bookClient.getBookById(bookshelf.getBookId()).getData();

            if(book != null){
                //将book中的数据导入UserBookshelfVO中
                UserBookshelfVO vo = new UserBookshelfVO();
                BeanUtils.copyProperties(bookshelf, vo);
                vo.setBookName(book.getBookName());
                vo.setAuthorName(book.getAuthorName());
                vo.setImgUrl(book.getImgUrl());
                bookshelfs.add(vo);
            }

        }

        return ResultUtil.success(bookshelfs);
    }

    /**
     *查询用户的书架上是否存在该书
     * @param userId
     * @param bookId
     * @return
     */
    @Override
    public Result userBookExistBook(Integer userId, String bookId) {

        int result = 0;

        try {

            result = bookShelfMapper.selectCountByUserAndBookId(userId, bookId);

        } catch (Exception e){

            LOGGER.error("查询图书是否在用户书架中异常：{}", e);

        }

        return ResultUtil.success(result);
    }
}
