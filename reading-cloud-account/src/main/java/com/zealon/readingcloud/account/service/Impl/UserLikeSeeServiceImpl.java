package com.zealon.readingcloud.account.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zealon.readingcloud.account.dao.UserLikeSeeMapper;
import com.zealon.readingcloud.account.service.UserLikeSeeService;
import com.zealon.readingcloud.account.service.task.LikeSeeClickTask;
import com.zealon.readingcloud.book.feign.client.BookClient;
import com.zealon.readingcloud.common.cache.RedisAccountKey;
import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.pojo.account.UserLikeSee;
import com.zealon.readingcloud.common.pojo.book.Book;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import com.zealon.readingcloud.common.vo.SimpleBookVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 用户喜欢服务
 * @author hasee
 */
@Service
public class UserLikeSeeServiceImpl implements UserLikeSeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLikeSeeServiceImpl.class);

    @Autowired
    private UserLikeSeeMapper likeSeeMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookClient bookClient;

    @Autowired
    private ExecutorService commonQueueThreadPool;


    @Override
    public Result likeSeeClick(Integer userId, String bookId, Integer value) {

        try {
            //0取消喜欢 1增加喜欢
            if(0 == value){
                //用户对每篇文章的喜欢单成一条记录，所以只需要删去该记录
                likeSeeMapper.deleteByUserIdAndBookId(userId, bookId);

            }else{
                //在UserLikeSee表中新增一条记录
                UserLikeSee like = new UserLikeSee();
                like.setUserId(userId);
                like.setBookId(bookId);
                likeSeeMapper.insert(like);
            }

            //更新缓存
            LikeSeeClickTask task = new LikeSeeClickTask(redisService, bookId, value);
            commonQueueThreadPool.execute(task);

        }catch (Exception e){

            LOGGER.error("用户喜欢点击操作异常：{}", e);
            return ResultUtil.fail();

        }

        return ResultUtil.success();
    }

    @Override
    public Result<Integer> getBookLikeCount(String bookId) {


        //从缓存中获取喜欢数
        Integer likeCount = redisService.getHashVal(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, bookId, Integer.class);

        if(likeCount == null){

            //若缓存中没有，则通过LikeSeeMapper从数据库中获取
            likeCount = likeSeeMapper.findPageWithCount(bookId);

            //将获取的值再以键值对存入缓存中
            redisService.setHashValExpire(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, bookId, likeCount, RedisExpire.HOUR);

        }

        return ResultUtil.success(likeCount);
    }


    /**
     * 得到用户喜欢列表
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Result getUserLikeBookList(Integer userId, Integer page, Integer limit) {

        try{

            //调用分页
            PageHelper.startPage(page, limit);

            //通过userId 在 UserLikeSee表中查找用户喜欢的书籍，得到书籍列表
            Page<UserLikeSee> pageWithResult = (Page<UserLikeSee>) likeSeeMapper.findPageWithResult(userId);
            List<SimpleBookVO> books = new ArrayList<>();

            for(int i = 0; i < pageWithResult.size(); i++){

                SimpleBookVO vo = new SimpleBookVO();

                //遍历喜欢的书籍记录
                UserLikeSee likeSee = pageWithResult.get(i);

                //通过likeSee中得到的BookId，得到书籍的详细信息
                Book book = bookClient.getBookById(likeSee.getBookId()).getData();

                if(book != null){

                    BeanUtils.copyProperties(book, vo);
                    books.add(vo);

                }

            }

            return ResultUtil.success(books);

        }catch (Exception e){
            LOGGER.error("获取用户[{}]喜欢书单异常:{}", userId, e);
            return ResultUtil.fail();
        }

    }


    /**
     * 查询用户是否喜欢此书
     * @param userId
     * @param bookId
     * @return 0 为不喜欢 1 为喜欢
     */
    @Override
    public Result userLikeThisBook(Integer userId, String bookId) {
        int result = 0;

        try {
            result = likeSeeMapper.selectCountByUserAndBookId(userId, bookId);

        }catch (Exception e){
            LOGGER.error("查询喜欢此书异常：{}", e);
        }

        return ResultUtil.success(result);
    }
}
