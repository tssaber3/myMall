package com.tssaber.mmall;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tssaber.mmall.dao.CartMapper;
import com.tssaber.mmall.dao.GoodsMapper;
import com.tssaber.mmall.dao.OrderMapper;
import com.tssaber.mmall.dao.UserMapper;
import com.tssaber.mmall.entity.pojo.*;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.redis.CartKey;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.service.Impl.GoodsServiceImpl;
import com.tssaber.mmall.util.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.RegEx;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MmallApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private CartMapper cartMapper;

    @Test
    public void text(){
//        User user = new User();
//        Role role = new Role();
//        Authority authority = new Authority();
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//        role.setId(1);
//        role.setName("ROLE_ADMIN");
//        authority.setRole(role);
//        List<Authority> roles = new ArrayList<>();
//        roles.add(authority);
//        user.setRoles(roles);
//        user.setUsername("1274243927");
//        user.setPassword("123456");
//        user.setCreateTime(now);
//        user.setModifiedTime(now);
//        userMapper.insUser(user);
    }

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    public void text2(){
        Goods goods = new Goods();
        goods.setCategoryId(5);
        goods.setProductName("漫步者 TWS3 W2 W3耳机充电盒收纳包");
        goods.setSubtitle("适用于漫步者 TWS3 W2 W3耳机充电盒收纳包 TWS5便携收纳盒耳机包");
        goods.setMainImage("https://gd3.alicdn.com/imgextra/i3/808630189/O1CN016JbL3d1DGbLHFBjdY_!!808630189.jpg_400x400.jpg");
        goods.setSubImage("https://gd3.alicdn.com/imgextra/i3/808630189/O1CN016JbL3d1DGbLHFBjdY_!!808630189.jpg_400x400.jpg");
        goods.setDetail("品牌: 比博欧型号: 收纳盒质地: 其他笔记本包尺寸: 其他颜色分类: 登山扣款式【耳机包】用途: 多用途合一款式: 便携手包");
        goods.setPrice(new BigDecimal("16.00"));
        goods.setStock(100);
        goods.setStatus(1);
        goods.setCreateTime(new Timestamp(System.currentTimeMillis()));
        goods.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        goodsMapper.insGoods(goods);
    }


    @Test
    public void test3(){
        Goods goods1 = goodsMapper.selGoodsById(1);
        Cart cart = new Cart();
        cart.setUserId(1);
        cart.setGoodsId(1);
        cart.setGoodsImage(goods1.getMainImage());
        cart.setGoodsName(goods1.getProductName());
        cart.setGoodsPrice(goods1.getPrice());
        cart.setGoodsNum(1);
        cart.setCreateTime(new Timestamp(System.currentTimeMillis()));
        cart.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        cartMapper.insCart(cart);
    }

    @Test
    public void test4(){
        String json = "{\n" +
                "\t\"goodsId\": [{\n" +
                "\t\t\"goodsId\": 1\n" +
                "\t}, {\n" +
                "\t\t\"goodsId\": 1\n" +
                "\t}, {\n" +
                "\t\t\"goodsId\": 1\n" +
                "\t}, {\n" +
                "\t\t\"goodsId\": 1\n" +
                "\t}, {\n" +
                "\t\t\"goodsId\": 1\n" +
                "\t}]\n" +
                "}";
        List<Integer> goodsIdLists = new ArrayList<>();
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        JsonArray array = object.getAsJsonArray("goodsId");
        for (int i = 0;i < array.size();i++){
            JsonObject jsonObject = (JsonObject) array.get(i);
            goodsIdLists.add(jsonObject.get("goodsId").getAsInt());
        }
        System.out.println(goodsIdLists);
    }

    @Resource
    private GoodsServiceImpl goodsService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RedisUtils redisUtils;

    @Test
    public void test5(){
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setNickName("张俊");
        userDto.setAddress("重庆");
        userDto.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        userMapper.updUser(userDto);
    }

    @Test
    public void test6(){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Date d = new Date(now.getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String str = sdf.format(d);
        System.out.println(str);
    }

    @Test
    public void test7(){
        System.out.println(goodsMapper.selGoodsByCategory(5));
    }
}
