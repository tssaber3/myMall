package com.tssaber.mmall.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tssaber.mmall.dao.UserMapper;
import com.tssaber.mmall.entity.pojo.User;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/1/29 17:12
 * @Version 1.0
 */
@RestController
@RequestMapping("/text")
public class TextController {

    @Resource
    private UserMapper userMapper;

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    private static final Logger log = LoggerFactory.getLogger(TextController.class);

    @RequestMapping("/textAjax")
    public String textAjax(HttpServletRequest request, HttpServletResponse response){
        User user = userMapper.selUserById(1);
        System.out.println("ssss");
        return GSON.toJson(user);
    }

    @RequestMapping("/testJson")
    public String testJson(@RequestParam("json")String json){
        System.out.println(json);
        return GSON.toJson("成功");
    }
}
