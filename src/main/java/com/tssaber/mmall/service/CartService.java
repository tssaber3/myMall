package com.tssaber.mmall.service;

import com.tssaber.mmall.entity.pojo.Cart;
import com.tssaber.mmall.entity.pojo.dto.CartBo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/7 14:21
 * @Version 1.0
 */
@Service
public interface CartService {

    /**
     * 添加购物车
     * @param cartInfo
     * @param request
     * @return
     */
    boolean addCart(String cartInfo, HttpServletRequest request);

    /**
     * 根据用户名获取全部购物车中内容
     * @param request
     * @return
     */
    List<Cart> selCartAll(HttpServletRequest request);

    /**
     * 进行购物车的删减
     * @param request
     * @param cartId
     * @return
     */
    boolean delCartById(HttpServletRequest request,Integer cartId);

    /**
     * 解析orderInfo 判断是否还有货 并且返回加密之后的url
     * @param orderInfo
     * @return
     */
    CartBo exportUrl(String orderInfo);

    /**
     * 订单处理完毕后
     * 删除用户的购物车
     * @param userId
     */
    void delCartAll(Integer userId);

}
