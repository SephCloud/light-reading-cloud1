package com.zealon.readingcloud.gateway.controller;

import com.zealon.readingcloud.common.result.Result;
import com.zealon.readingcloud.common.result.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 快速失败接口
 * @author hasee
 */
public class FallbackController {

    @GetMapping("/fallback")
    public Result fallback(){

        return ResultUtil.fail();

    }


}
