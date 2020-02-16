package com.tssaber.mmall.rabbitmq;

import com.tssaber.mmall.entity.pojo.dto.OrderDto;
import com.tssaber.mmall.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author:tssaber 生产者
 * @Date: 2020/2/2 19:21
 * @Version 1.0
 */
@Component
public class TopicSender {

    private static final Logger log = LoggerFactory.getLogger(TopicSender.class);

    @Resource
    private AmqpTemplate rabbitTemplate;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 扣库存 同时下订单
     * @param orderDto:订单详情
     */
    public void sendOrder(OrderDto orderDto){
        log.info("发送扣库存到消费者");
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE,"order.goods",redisUtils.beanToString(orderDto));
    }

    /**
     * 购物车的情况下有多个商品
     */
    public void sendCartOrder(OrderDto orderDto){
        log.info("购物车的订单消费");
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE,"order.cart",redisUtils.beanToString(orderDto));
    }
}
