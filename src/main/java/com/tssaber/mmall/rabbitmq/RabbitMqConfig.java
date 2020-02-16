package com.tssaber.mmall.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Year;

/**
 * @Author:tssaber rabbitmq配置
 * @Date: 2020/2/2 17:08
 * @Version 1.0
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 进行数据库的扣款落库 和订单的落库 在一个队列
     */
    public static final String ORDER_QUEUE = "order_queue";

    /**
     * 购物车 多个商品时使用的队列
     */
    public static final String ORDER_CART = "order_cart";

    public static final String TOPIC_EXCHANGE = "topic_exchange";

    /**
     * json格式转化
     */
    private static final MessageConverter JSON_MESSAGE_CONVERTER = new Jackson2JsonMessageConverter();


    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE);
    }

    @Bean
    public Queue orderCartQueue(){
        return new Queue(ORDER_CART);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue,TopicExchange topicExchange){
        return BindingBuilder.bind(orderQueue).to(topicExchange).with("order.goods");
    }

    @Bean
    public Binding orderCartBing(Queue orderCartQueue,TopicExchange topicExchange){
        return BindingBuilder.bind(orderCartQueue).to(topicExchange).with("order.cart");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(JSON_MESSAGE_CONVERTER);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(JSON_MESSAGE_CONVERTER);
        return factory;
    }
}
