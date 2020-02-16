package com.tssaber.mmall.rabbitmq;

import com.tssaber.mmall.entity.pojo.dto.OrderDto;
import com.tssaber.mmall.redis.RedisUtils;
import com.tssaber.mmall.service.Impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author:tssaber 消费者
 * @Date: 2020/2/2 19:26
 * @Version 1.0
 */
@Component
@RabbitListener(queues = RabbitMqConfig.ORDER_QUEUE,containerFactory = "rabbitListenerContainerFactory")
public class TopicReceiver {

    private static final Logger log = LoggerFactory.getLogger(TopicReceiver.class);

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private OrderServiceImpl orderService;

    /**
     * 1.订单的生成
     * 2.进行扣库存的落库
     * @param str:OrderDto的json
     */
    @RabbitHandler
    public void process(String str){
        log.info("消费者消费到订单的请求: {}",str);
        OrderDto orderDto = redisUtils.stringToBean(str,OrderDto.class);
        orderService.processOrder(orderDto);
    }
}
