package com.zealon.readingcloud.homepage.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

/**
 * ES 请求查询体
 * @author hasee
 */
@Data
public class RequestQuery {
    private int from;
    private int size;
    private Map query;

    public RequestQuery(int from, int size, Map query){
        this.from = from;
        this.size = size;
        this.query = query;
    }


    @Override
    public String toString(){

        return JSONObject.toJSONString(this);
    }


}
