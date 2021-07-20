package com.pjb.config;

import com.pjb.receiver.impl.AckReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 * @author pan_junbiao
 **/
@Configuration
public class RabbitMqConfig
{
    public static final String DIRECT_QUEUE = "direct_queue"; //Direct队列名称
    public static final String DELAY_QUEUE = "delay_queue"; //延时队列名称

    /**
     * 消息接收确认处理类
     */
    @Autowired
    private AckReceiver ackReceiver;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     * 客户端配置
     * 配置手动确认消息、消息接收确认
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer()
    {
        //消费者数量，默认10
        int DEFAULT_CONCURRENT = 10;

        //每个消费者获取最大投递数量 默认50
        int DEFAULT_PREFETCH_COUNT = 50;

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(DEFAULT_CONCURRENT);
        container.setMaxConcurrentConsumers(DEFAULT_PREFETCH_COUNT);

        // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        //添加队列，可添加多个队列
        container.addQueues(new Queue(DIRECT_QUEUE,true));
        container.addQueues(new Queue(DELAY_QUEUE,true));

        //设置消息处理类
        container.setMessageListener(ackReceiver);

        return container;
    }
}
