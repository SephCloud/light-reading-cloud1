package com.zealon.readingcloud.book.service;

import com.zealon.readingcloud.book.vo.BookChapterReadVO;
import com.zealon.readingcloud.book.vo.BookChapterVO;
import com.zealon.readingcloud.common.pojo.book.BookChapter;
import com.zealon.readingcloud.common.result.Result;

/**
 * 图书章节服务
 * @author hasee
 */
public interface BookChapterService {

    /**
     * 获取章节目录
     * @param bookId
     * @return
     */
    Result getBookChapterListByBookId(String bookId);

    /**
     * 获取章节内容
     * @param bookId
     * @param chapterId
     * @return
     */
    Result<BookChapter> getChapterById(String bookId, Integer chapterId);

    /**
     * 阅读章节
     * @param bookId
     * @param chapterId 章节Id（0获取首节， -1获取末章节）
     * @return
     */
    Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId);

}
