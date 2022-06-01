package com.zealon.readingcloud.homepage.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zealon.readingcloud.account.feign.client.LikeSeeClient;
import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisHomepageKey;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.constant.CategoryConstant;
import com.zealon.readingcloud.common.enums.BookSerialStatusEnum;
import com.zealon.readingcloud.common.pojo.book.Book;
import com.zealon.readingcloud.common.pojo.index.IndexBooklist;
import com.zealon.readingcloud.common.pojo.index.IndexBooklistItem;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import com.zealon.readingcloud.common.utils.CommonUtil;
import com.zealon.readingcloud.homepage.dao.IndexBookListItemMapper;
import com.zealon.readingcloud.homepage.service.BookCenterService;
import com.zealon.readingcloud.homepage.service.IndexBannerService;
import com.zealon.readingcloud.homepage.service.IndexBookListItermService;
import com.zealon.readingcloud.homepage.service.IndexBookListService;
import com.zealon.readingcloud.homepage.vo.BookListBookVO;
import com.zealon.readingcloud.homepage.vo.IndexBannerVO;
import com.zealon.readingcloud.homepage.vo.IndexBookListVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 书单图书服务
 * @author hasee
 */
@Service
public class IndexBookListItemServiceImpl implements IndexBookListItermService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexBookListItemServiceImpl.class);

    @Autowired
    private IndexBookListService indexBookListService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BookCenterService bookCenterService;

    @Autowired
    private LikeSeeClient likeSeeClient;

    @Autowired
    private IndexBookListItemMapper indexBookListItemMapper;


    /**
     * 分页 - 书单更多接口
     * @param bookListId 书单ID
     * @param page 页数
     * @param limit 每页数量
     * @return
     */
    @Override
    public Result getBookListPagingBooks(Integer bookListId, Integer page, Integer limit) {

        //生成缓存key
        String key = RedisHomepageKey.getBooklistItemPagingKey(bookListId);

        //使用页码 + 数量作为Hash的key，防止不同数量的分页页码冲突
        String filed = page.toString() + limit;

        //从Redis缓存中查找key中field对应的hash值
        List<BookListBookVO> list = redisService.getHashListVal(key, filed, BookListBookVO.class);


        //如果缓存中没有对应值
        if(CommonUtil.isEmpty(list)){

            list = new ArrayList<>();
            PageHelper.startPage(page, limit);

            //从DB中查询
            Page<IndexBooklistItem> pageList = (Page<IndexBooklistItem>) indexBookListItemMapper.findPageWithResult(bookListId);

            //遍历bookListId符合的BookList
            for(int i = 0; i < pageList.size(); i++){

                String bookId = pageList.get(i).getBookId();

                //getBookVO函数，通过bookId得到BookListBook，并加入List
                BookListBookVO vo = getBookVO(bookId);
                if(vo != null){
                    list.add(vo);
                }

            }

            //如果List不为空，加入缓存
            if(list.size() > 0){

                redisService.setHashValExpire(key, filed, list, RedisExpire.HOUR_TWO);

            }
        }

        return ResultUtil.success(list);
    }

    /**
     * 书单换一换接口
     * @param bookListId 书单id
     * @param clientRandomNumber 客户端当前随机编号
     * @return
     */
    @Override
    public Result getBookListExchange(Integer bookListId, Integer clientRandomNumber) {

        IndexBooklist bookList = indexBookListService.getIndexBookListById(bookListId);

        if(bookList == null){

            return ResultUtil.notFound().buildMessage("找不到此书单！");

        }


        IndexBookListVO randomIndexBookListVO = indexBookListService.getRandomIndexBookListVO(bookList, clientRandomNumber);

        return ResultUtil.success(randomIndexBookListVO);
    }


    /**
     * 随机获取书单book
     * <p>
     *     1. 得到客户端随机编码的book
     *     2. 随机获取榜单图书，排除客户端当前book
     *     3. 随机图书列表包括不能重复图书
     * </p>
     * @param bookListId 书单ID
     * @param bookIds 图书IDs
     * @param showNumber 显示数量
     * @param clientRandomNumber 客户端当前随机编号
     * @param showLikeCount 是否显示喜欢数
     * @return
     */
    @Override
    public List<BookListBookVO> getBookListRandomBooks(Integer bookListId, String bookIds, Integer showNumber, Integer clientRandomNumber, Boolean showLikeCount) {

        //随机获取的书单
        Set<String> randomBooks = new HashSet<>();
        String[] bookIdArray = bookIds.split(",");

        //如果书单中的书数量 < 要展示的数量。直接返回顺序书单
        if(bookIdArray.length < showNumber){

            return getBookListOrderBooks(bookListId, bookIds, showNumber, showLikeCount);

        }

        //客户端书单
        Set<String> clientBookIds = getClientBookIds(bookListId, clientRandomNumber);
        Random random = new Random();


        while (randomBooks.size() < showNumber){
            //得到随机图书ID，并排重，排重客户端书单
            int randomIndex = random.nextInt(bookIdArray.length);

            //书单中随机的一个
            String bookId = bookIdArray[randomIndex];

            //客户端书单中不存在且书单中不存在
            if(!clientBookIds.contains(bookId) && !randomBooks.contains(bookId)){
                //加入set中去重
                randomBooks.add(bookId);
            }

        }

        String[] randomBookIdArray = {};
        randomBookIdArray = randomBooks.toArray(randomBookIdArray);


        return getBookListBookVOByBookIdArray(randomBookIdArray, showNumber, showLikeCount);
    }

    @Override
    public List<BookListBookVO> getBookListOrderBooks(Integer bookListId, String bookIds, Integer showNumber, Boolean showLikeCount) {

        String[] bookIdArray = bookIds.split(",");
        return getBookListBookVOByBookIdArray(bookIdArray, showNumber, showLikeCount);
    }


    /**
     * 获取书单VO
     * 将BookList中的BookIds（字符串数组）转换成 List<BookListBookVO>
     * @param bookIdArray
     * @param showNumber
     * @param showLikeCount
     * @return
     */
    private List<BookListBookVO> getBookListBookVOByBookIdArray(String[] bookIdArray, Integer showNumber, Boolean showLikeCount ){

        List<BookListBookVO> vos = new ArrayList<>();

        for(int i = 0; i < bookIdArray.length; i++){

            String bookId = bookIdArray[i];

            //getBookVO方法通过bookId返回相应的BookLikeBookVO
            BookListBookVO vo = getBookVO(bookId);

            if(vo != null){

                //如果需要展示喜欢数
                if(showLikeCount){
                    //获取喜欢数并存放在vo中
                    Integer likeCount = likeSeeClient.getBookLikesCount(bookId).getData();
                    vo.setLikeCount(likeCount);

                }
                vos.add(vo);
            }


            //VOS达到榜单所需数量，不用再获取了
            if(vos.size() == showNumber){

                break;
            }

        }

        return vos;
    }



    /**
     * 通过bookId获取榜单图书VO
     * @param bookId
     * @return
     */
    private BookListBookVO getBookVO(String bookId){

        Book book = bookCenterService.getBookById(bookId);

        if(book == null){

            LOGGER.warn("图书资源中心获取Book:{}失败！返回了空数据", bookId);
            return null;
        }

        BookListBookVO vo = new BookListBookVO();

        //将book的数据复制到BookListBookVO中
        BeanUtils.copyProperties(book, vo);
        vo.setLikeCount(0);

        //分类
        String categoryName = CategoryConstant.categorys.get(book.getDicCategory());
        vo.setCategoryName(categoryName);

        //连载状态
        String serialStatusName = BookSerialStatusEnum.values()[book.getDicSerialStatus() - 1].getName();
        vo.setCategoryName(serialStatusName);


        return vo;

    }


    /**
     * 获取客户端书单Id集合
     * @param bookListId
     * @param clientRandomNumber
     * @return
     */
    private Set<String> getClientBookIds(Integer bookListId, Integer clientRandomNumber){

        if(clientRandomNumber == null){
            return new HashSet<>();
        }

        //客户端当前显示书单
        String key = RedisHomepageKey.getBooklistRandomVoKey(bookListId);
        IndexBookListVO bookListVO;
        List<BookListBookVO> clientBooks = null;

        try {
            //尝试从缓存中获取
            bookListVO = redisService.getHashVal(key, clientRandomNumber.toString(), IndexBookListVO.class);

            if(bookListVO != null){
                clientBooks = bookListVO.getBooks();
            }

        }catch (Exception e){

            LOGGER.error("Redis获取客户端书单失败了！bookListId:{}, clientRandomNumber:{}",e, bookListId, clientRandomNumber);

        }

        if(clientBooks == null){
            clientBooks = new ArrayList<>();
        }

        return new HashSet(clientBooks);
    }
}
