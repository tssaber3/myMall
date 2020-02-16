package com.tssaber.mmall.controller;


import com.google.gson.Gson;
import com.tssaber.mmall.annotation.UserLoginToken;
import com.tssaber.mmall.common.ApiResponse;
import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.controller.vo.OrderVo;
import com.tssaber.mmall.entity.pojo.Order;
import com.tssaber.mmall.entity.pojo.OrderItem;
import com.tssaber.mmall.entity.pojo.dto.UserDto;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.service.Impl.OrderServiceImpl;
import com.tssaber.mmall.service.Impl.UserServiceImpl;
import com.tssaber.mmall.util.CommentUtil;
import com.tssaber.mmall.util.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zj
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private OrderServiceImpl orderService;

    private static final Gson GSON = CommentUtil.INSTANCE.getGson();

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @UserLoginToken
    @GetMapping("/selUser")
    public String selUser(HttpServletRequest request, HttpServletResponse response){
        UserDto userDto = userService.selUser(request);
        return GSON.toJson(ApiResponse.success(userDto));
    }

    @UserLoginToken
    @PostMapping("/changePassword")
    public String changePassword(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam("password") String password){
        if (password == null || password.length() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        boolean bok = userService.changePassword(password,request);
        if (bok){
            return GSON.toJson(ApiResponse.success("修改成功请,重新登录"));
        }else {
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
    }

    @UserLoginToken
    @PostMapping("/updateUserInfo")
    public String updateUserInfo(HttpServletRequest request,HttpServletResponse response,
                                 @RequestParam("userInfo") String userInfo){
        if (userInfo == null || userInfo.length() == 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        userService.updUserInfo(userInfo,request);
        UserDto userDto = userService.selUser(request);
        if (userDto != null){
            return GSON.toJson(ApiResponse.success(userDto));
        }
        throw new GlobalException(CExceptionEnums.BIND_ERROR);

    }

    @UserLoginToken
    @GetMapping("/getAllOrders")
    public String getAllOrders(HttpServletRequest request,HttpServletResponse response){
        UserDto userDto = userService.selUser(request);
        List<OrderVo> orders = orderService.selAllOrderByUserId(userDto.getId());
        if (orders != null){
            return GSON.toJson(ApiResponse.success(orders));
        }
        return GSON.toJson(ApiResponse.error(CExceptionEnums.BIND_ERROR));
    }

    /**
     * 获取订单的详细信息
     */
    @UserLoginToken
    @GetMapping("/{orderId}/getOrderDetail")
    public String getOrderDetail(HttpServletResponse response,HttpServletRequest request,
                                 @PathVariable("orderId") Integer orderId){
        if (orderId < 0){
            throw new GlobalException(CExceptionEnums.BIND_ERROR);
        }
        OrderVo orderItem = orderService.selOrderDetail(orderId,request);
        if (orderItem != null){
            return GSON.toJson(ApiResponse.success(orderItem));
        }
        return GSON.toJson(ApiResponse.error(CExceptionEnums.BIND_ERROR));
    }
}
