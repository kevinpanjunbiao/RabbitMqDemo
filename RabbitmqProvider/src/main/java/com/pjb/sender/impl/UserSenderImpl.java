package com.pjb.sender.impl;

import com.pjb.config.RabbitMqConfig;
import com.pjb.entity.UserInfo;
import com.pjb.sender.UserSender;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 用户消息发送服务类
 * @author pan_junbiao
 **/
@Service
public class UserSenderImpl implements UserSender
{
    @Autowired
    RabbitTemplate rabbitTemplate;

    //时间格式
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 发送用户信息Json格式数据
     * @param userInfo 用户信息实体类
     */
    @Override
    public void sendUserJson(UserInfo userInfo)
    {
        /**
         * 发送消息，参数说明：
         * String exchange：交换器名称。
         * String routingKey：路由键。
         * Object object：发送内容。
         */
        rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE, RabbitMqConfig.DIRECT_ROUTING_KEY, userInfo);

        System.out.println("\nJson格式数据消息发送成功，发送时间：" + dateFormat.format(new Date()));
    }

    /**
     * 延时发送用户信息Map格式数据
     * @param userMap 用户信息Map
     */
    @Override
    public void sendDelayUserMap(Map userMap)
    {
        rabbitTemplate.convertAndSend(RabbitMqConfig.DELAY_EXCHANGE, RabbitMqConfig.DELAY_ROUTING_KEY, userMap, new MessagePostProcessor()
        {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException
            {
                //消息延迟5秒
                message.getMessageProperties().setHeader("x-delay", 5000);
                return message;
            }
        });

        System.out.println("Map格式数据消息发送成功，发送时间：" + dateFormat.format(new Date()));
    }
}
