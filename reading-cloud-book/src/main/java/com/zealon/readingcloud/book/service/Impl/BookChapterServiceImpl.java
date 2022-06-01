package com.zealon.readingcloud.book.service.Impl;

import com.zealon.readingcloud.book.dao.BookChapterMapper;
import com.zealon.readingcloud.book.domain.BookPreviousAndNextChapterNode;
import com.zealon.readingcloud.book.service.BookChapterService;
import com.zealon.readingcloud.book.service.BookService;
import com.zealon.readingcloud.book.vo.BookChapterListVO;
import com.zealon.readingcloud.book.vo.BookChapterReadVO;
import com.zealon.readingcloud.book.vo.BookChapterVO;
import com.zealon.readingcloud.common.cache.RedisBookKey;
import com.zealon.readingcloud.common.cache.RedisExpire;
import com.zealon.readingcloud.common.cache.RedisService;
import com.zealon.readingcloud.common.pojo.book.Book;
import com.zealon.readingcloud.common.pojo.book.BookChapter;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author hasee
 */
@Service
public class BookChapterServiceImpl implements BookChapterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookChapterServiceImpl.class);

    @Autowired
    private BookChapterMapper bookChapterMapper;

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisService redisService;



    @Override
    public Result getBookChapterListByBookId(String bookId) {

        Book book = (Book) bookService.getBookById(bookId).getData();
        if(book == null){
            return ResultUtil.notFound().buildMessage("该书不存在于本系统");

        }

        String key = RedisBookKey.getBookChapterListKey(bookId);
        List<BookChapterListVO> chapterVOS = this.redisService.getCacheForList(key, BookChapter.class);

        if(null == chapterVOS || chapterVOS.size() == 0){

            List<BookChapter> chapters = bookChapterMapper.findPageWithResult(book.getId());

            if(chapters.size() > 0){

                for(int i = 0; i < chapters.size(); i++){

                    BookChapterListVO vo = new BookChapterListVO();
                    BeanUtils.copyProperties(chapters.get(i), vo);
                    chapterVOS.add(vo);

                }
                redisService.setExpireCache(key, chapterVOS, RedisExpire.HOUR);
            }

        }

        return ResultUtil.success(chapterVOS);
    }

    @Override
    public Result<BookChapter> getChapterById(String bookId, Integer chapterId) {

        BookChapter chapter;
        String key = RedisBookKey.getBookChapterKey(bookId);
        String field = chapterId.toString();
        chapter = this.redisService.getHashVal(key, field, BookChapter.class);

        if(chapter == null){
            chapter = bookChapterMapper.selectById(chapterId);

            if(chapter != null){

                this.redisService.setHashValExpire(key, field, chapter, RedisExpire.HOUR);
            }
        }

        return ResultUtil.success(chapter);
    }



    @Override
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId) {

        Book book  = (Book) bookService.getBookById(bookId).getData();

        if(book == null){

            return ResultUtil.notFound().buildMessage("该书不存在于系统中");

        }

        BookChapterReadVO result = new BookChapterReadVO();

        String field = chapterId.toString();

        if(chapterId == 0){

            field = "first";

        }else if(chapterId == -1){

            field = "last";
        }

        BookPreviousAndNextChapterNode chapterNode = this.getChapterNodeData(book.getId(), field);

        /**
         * 如果查询不到节点数据查询首章节
         */
        if(chapterNode == null){
            field = "first";
            chapterNode = this.getChapterNodeData(book.getId(), field);

            if(chapterNode == null){

                return ResultUtil.notFound().buildMessage("这本书还没有任何章节内容");
            }
        }

        String content = this.getChapterContent(bookId, chapterNode.getId());
        BookChapterVO current = new BookChapterVO(chapterNode.getId(), chapterNode.getName(), content);

        BookChapterVO pre = null;
        BookChapterVO next = null;

        if(chapterNode.getPre() != null){
            pre = new BookChapterVO(chapterNode.getPre().getId(), chapterNode.getPre().getName(), "");
        }
        if(chapterNode.getNext() != null)  {
            next = new BookChapterVO(chapterNode.getNext().getId(), chapterNode.getNext().getName(), "");
        }

        result.setCurrent(current);
        result.setPre(pre);
        result.setNext(next);

        return ResultUtil.success(result);
    }


    /**
     * 获取前后章节节点数据链表
     * @param bookId
     * @param field
     * @return
     */
    private BookPreviousAndNextChapterNode getChapterNodeData(final Integer bookId, final String field){
        //缓存获取
        String key = RedisBookKey.getBookChapterNodeKey(bookId);
        BookPreviousAndNextChapterNode chapterNode = redisService.getHashObject(key, field, BookPreviousAndNextChapterNode.class);

        /**
         * 缓存中存在节点就直接返回
         */
        if(chapterNode != null){
            return chapterNode;
        }

        /**
         * 获取该书中的章节
         */
        List<BookChapter> chapterList = bookChapterMapper.findPageWithResult(bookId);

        if(chapterList.size() == 0) {
            return null;
        }
        HashMap <String, BookPreviousAndNextChapterNode> map = new HashMap<>();

        /**
         * 上一章节点数据
         */
        BookPreviousAndNextChapterNode pre = null;

        try {

            for(int i = 1; i <= chapterList.size(); i++){

                BookChapter chapter = chapterList.get(i - 1);

                /**
                 * 若第i章已锁，则获取 i + 1 章内容，在list内即第 i 个元素
                 * 感觉此处的 if 应换成 while
                 */
                if(chapter.getLockStatus()){

                    if(i >= chapterList.size()){
                        break;
                    }
                    chapter = chapterList.get(i);
                }

                /**
                 * 得到当前节点数据
                 */
                BookPreviousAndNextChapterNode curr = new BookPreviousAndNextChapterNode(chapter.getId(), chapter.getName());

                if(pre != null){

                    curr.setPre(new BookPreviousAndNextChapterNode(pre));
                    pre.setNext(new BookPreviousAndNextChapterNode(curr));

                    map.put(pre.getId() + "",pre);
                }

                if(i == 2){

                    map.put("first", pre);
                }

                if(i == 1){

                    map.put("first", curr);
                }

                /**
                 * 存储节点数据
                 */
                map.put(curr.getId()+"", curr);
                pre = curr;

            }

            map.put("last", pre);
            redisService.setHashValsExpire(key, map, RedisExpire.HOUR_FOUR);

        }catch (Exception e){
            LOGGER.error("生成章节点数据异常:{}",e);

        }

        return map.get(field);

    }

    private String getChapterContent(String bookId, Integer chapterId){

        String content = "";
        BookChapter chapter = this.getChapterById(bookId, chapterId).getData();
        if(chapter != null){

            content = chapter.getContent();
        }

        return content;
    }
}
