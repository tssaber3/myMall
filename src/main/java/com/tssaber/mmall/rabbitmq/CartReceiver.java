package com.tssaber.mmall.rabbitmq;

import com.tssaber.mmall.entity.pojo.dto.OrderDto;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author:tssaber 购物车使用的消费者
 * @Date: 2020/2/9 17:52
 * @Version 1.0
 */
@Component
@RabbitListener(queues = RabbitMqConfig.ORDER_CART,containerFactory = "rabbitListenerContainerFactory")
public class CartReceiver {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private OrderService orderService;

    public static final Logger log = LoggerFactory.getLogger(CartReceiver.class);

    /**
     * 购物车 处理订单的落库
     * @param str
     */
    @RabbitHandler
    public void process(String str){
        log.info("购物车 订单的落库 {}",str);
        OrderDto orderDto = redisUtils.stringToBean(str,OrderDto.class);
        orderService.processOrderCart(orderDto);
    }
}
