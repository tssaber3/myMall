package com.tssaber.mmall.controller;

import com.google.gson.Gson;
import com.tssaber.mmall.annotation.UserLoginToken;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.controller.vo.CartVo;
import com.tssaber.mmall.controller.vo.GoodsVo;
import com.tssaber.mmall.entity.pojo.Cart;
import com.tssaber.mmall.entity.pojo.Goods;
import com.tssaber.mmall.entity.pojo.dto.CartBo;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.service.Impl.CartServiceImpl;
import com.tssaber.mmall.service.Impl.GoodsServiceImpl;
import com.tssaber.mmall.service.Impl.UserServiceImpl;
import com.tssaber.mmall.util.CommentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tssaber
 * @Date: 2020/2/7 14:13
 * @Version 1.0
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartServiceImpl cartService;

    @Resource
    private GoodsServiceImpl goodsService;

    @Resource
    private UserServiceImpl userService;

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    /**
     * 加入购物车
     * @param request
     * @param response
     * @param cartInfo
     * @return
     */
    @UserLoginToken
    @RequestMapping("/insCart")
    public String insCart(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("cartInfo") String cartInfo){
        if (cartInfo == null || cartInfo.length() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        boolean bok = cartService.addCart(cartInfo,request);
        if (bok){
            return GSON.toJson(ApiResponse.success("添加成功"));
        }
        throw new GlobalException(CExceptionEnums.BIND_ERROR);
    }

    @UserLoginToken
    @RequestMapping("/selCartAll")
    public String selCartAll(HttpServletRequest request,HttpServletResponse response){
        List<Cart> carts = cartService.selCartAll(request);
        List<CartVo> result = new ArrayList<>();
        if (carts != null && !carts.isEmpty()){
            for (Cart cart:carts){
                result.add(new CartVo(cart));
            }
            return GSON.toJson(ApiResponse.success(result));
        }
        return GSON.toJson(ApiResponse.error(CExceptionEnums.CART_NULL));
    }

    /**
     * 用布隆过滤器来改进一下
     * @param request
     * @param response
     * @param cartId
     * @return
     */
    @UserLoginToken
    @GetMapping("/{cartId}/delCartByGoodsId")
    public String delCartByGoodsId(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable("cartId") Integer cartId){
        if (cartId < 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        boolean bok = cartService.delCartById(request,cartId);
        if (bok){
            return GSON.toJson(ApiResponse.success("成功"));
        }
        return GSON.toJson(ApiResponse.error(CExceptionEnums.BIND_ERROR));
    }

    /**
     * 这是用于购物车中 一次多个商品进行购买
     * @param request
     * @param response
     * @param orderInfo
     * @return
     */
    @UserLoginToken
    @PostMapping("/getUrl")
    public String getUrl(HttpServletRequest request,HttpServletResponse response,
                         @RequestParam("orderInfo") String orderInfo){
        if (orderInfo == null || orderInfo.length() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        CartBo cartBo = cartService.exportUrl(orderInfo);
        List<Goods> goods = goodsService.selAllGoodsById(cartBo.getGoodsIdList());
        UserDto userDto = userService.selUser(request);
        List<GoodsVo> goodsVoList = new ArrayList<>();
        for (Goods goods1:goods){
            goodsVoList.add(new GoodsVo(goods1));
        }
        cartBo.setGoodsVos(goodsVoList);
        cartBo.setAddress(userDto.getAddress());
        return GSON.toJson(ApiResponse.success(cartBo));
    }
}
