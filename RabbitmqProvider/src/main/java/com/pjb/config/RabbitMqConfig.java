package com.pjb.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 * @author pan_junbiao
 **/
@Configuration
public class RabbitMqConfig
{
    public static final String DIRECT_QUEUE = "direct_queue"; //Direct队列名称
    public static final String DIRECT_EXCHANGE = "direct_exchange"; //交换器名称
    public static final String DIRECT_ROUTING_KEY = "direct_routing_key"; //路由键

    public static final String DELAY_QUEUE = "delay_queue"; //延时队列名称
    public static final String DELAY_EXCHANGE = "delay_exchange"; //交换器名称
    public static final String DELAY_ROUTING_KEY = "delay_routing_key"; //路由键

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        //设置Json转换器
        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback()
        {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause)
            {
                System.out.println("\n确认消息送到交换机(Exchange)结果：");
                System.out.println("相关数据：" + correlationData);
                System.out.println("是否成功：" + ack);
                System.out.println("错误原因：" + cause);
                System.out.println("\n\n");
            }
        });

        //确认消息送到队列(Queue)回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback()
        {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage)
            {
                System.out.println("\n确认消息送到队列(Queue)结果：");
                System.out.println("发生消息：" + returnedMessage.getMessage());
                System.out.println("回应码：" + returnedMessage.getReplyCode());
                System.out.println("回应信息：" + returnedMessage.getReplyText());
                System.out.println("交换机：" + returnedMessage.getExchange());
                System.out.println("路由键：" + returnedMessage.getRoutingKey());
            }
        });
        return rabbitTemplate;
    }

    /**
     * Json转换器
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Direct交换器
     */
    @Bean
    public DirectExchange directExchange()
    {
        /**
         * 创建交换器，参数说明：
         * String name：交换器名称
         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
         */
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    /**
     * 队列
     */
    @Bean
    public Queue directQueue()
    {
        /**
         * 创建队列，参数说明：
         * String name：队列名称。
         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
         * 持久化的队列会存盘，在服务器重启的时候不会丢失相关信息。
         * boolean exclusive：设置是否排他，默认也是 false。为 true 则设置队列为排他。
         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
         * 当没有生产者或者消费者使用此队列，该队列会自动删除。
         * Map<String, Object> arguments：设置队列的其他一些参数。
         */
        return new Queue(DIRECT_QUEUE, true, false, false, null);
    }

    /**
     * 绑定
     */
    @Bean
    Binding directBinding(DirectExchange directExchange, Queue directQueue)
    {
        //将队列和交换机绑定, 并设置用于匹配键：routingKey路由键
        return BindingBuilder.bind(directQueue).to(directExchange).with(DIRECT_ROUTING_KEY);
    }

    /******************************延时队列******************************/

    @Bean
    public CustomExchange delayExchange()
    {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue delayQueue()
    {
        Queue queue = new Queue(DELAY_QUEUE, true);
        return queue;
    }

    @Bean
    public Binding delaybinding(Queue delayQueue, CustomExchange delayExchange)
    {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_ROUTING_KEY).noargs();
    }
}
