package com.zealon.readingcloud.book.feign.fallback;

import com.zealon.readingcloud.book.feign.client.BookClient;
import com.zealon.readingcloud.common.pojo.book.Book;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 图书客户端feign接口快速失败
 * @author hasee
 */
@Component
public class BookClientFallBack implements FallbackFactory<BookClient> {


    private static final Logger LOGGER = LoggerFactory.getLogger(BookClientFallBack.class);

    @Override
    public BookClient create(Throwable throwable) {
        return new BookClient() {
            @Override
            public Result<Book> getBookById(String bookId) {
                LOGGER.error("获取图书[{}]：{}",bookId, throwable.getMessage());
                return ResultUtil.success(null);
            }
        };
    }
}
