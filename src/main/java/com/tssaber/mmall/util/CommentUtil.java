package com.tssaber.mmall.util;

import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 常用工具类
 * @author: tssaber
 * @time: 2019/11/25 0025 18:50
 */
public enum  CommentUtil {
    /**
     *
     */
    INSTANCE;


    private Gson gson;
    private RestTemplate restTemplate;

    CommentUtil(){
        this.gson = new Gson();
        this.restTemplate = new RestTemplate();
    }

    public Gson getGson() {
        return gson;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
