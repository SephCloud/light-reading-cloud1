package com.zealon.readingcloud.account.feign.fallback;


import com.zealon.readingcloud.account.feign.client.LikeSeeClient;
import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 喜欢看客户端feign接口快速失败
 * @author hasee
 */
@Component
public class LikeSeeClientFallBack implements FallbackFactory<LikeSeeClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeSeeClientFallBack.class);

    @Override
    public LikeSeeClient create(Throwable throwable) {

        return new LikeSeeClient() {
            @Override
            public Result<Integer> getBookLikesCount(String bookId) {
                LOGGER.error("获取喜欢看[{}]数量失败：{}", bookId, throwable.getMessage());
                return ResultUtil.success(0);
            }
        };
    }
}
