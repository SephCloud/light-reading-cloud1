package com.zealon.readingcloud.account.feign.client;

import com.zealon.readingcloud.account.feign.fallback.LikeSeeClientFallBack;
import com.zealon.readingcloud.common.result.Result;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 喜欢看客户端feign接口
 * @author hasee
 */


@FeignClient(contextId = "like", name = "light-reading-cloud-account",fallbackFactory = LikeSeeClientFallBack.class)
@Headers({"acceptEncoding: gzip","contentType: application/json"})
public interface LikeSeeClient {

    @GetMapping("/account/like-see/get-count")
    Result<Integer> getBookLikesCount(@RequestParam("bookId") String bookId);

}
