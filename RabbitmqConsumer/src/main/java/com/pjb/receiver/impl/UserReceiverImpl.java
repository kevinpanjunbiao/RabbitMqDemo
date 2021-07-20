package com.pjb.receiver.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjb.entity.UserInfo;
import com.pjb.receiver.UserReceiver;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户消息接收类
 * @author pan_junbiao
 **/
@Service
public class UserReceiverImpl implements UserReceiver
{
    /**
     * 接收用户信息Json格式数据
     */
    @Override
    public void receiverUserJson(Message message, Channel channel) throws Exception
    {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try
        {
            //将JSON格式数据转换为实体对象
            ObjectMapper mapper = new ObjectMapper();
            UserInfo userInfo = mapper.readValue(message.getBody(), UserInfo.class);

            System.out.println("接收者收到JSON格式消息：");
            System.out.println("用户编号：" + userInfo.getUserId());
            System.out.println("用户名称：" + userInfo.getUserName());
            System.out.println("博客地址：" + userInfo.getBlogUrl());
            System.out.println("博客信息：" + userInfo.getBlogRemark());

            /**
             * 确认消息，参数说明：
             * long deliveryTag：唯一标识 ID。
             * boolean multiple：是否批处理，当该参数为 true 时，
             * 则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
             */
            channel.basicAck(deliveryTag, true);

            /**
             * 否定消息，参数说明：
             * long deliveryTag：唯一标识 ID。
             * boolean multiple：是否批处理，当该参数为 true 时，
             * 则可以一次性确认 deliveryTag 小于等于传入值的所有消息。
             * boolean requeue：如果 requeue 参数设置为 true，
             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
             * 而不会把它发送给新的消费者。
             */
            //channel.basicNack(deliveryTag, true, false);
        }
        catch (Exception e)
        {
            /**
             * 拒绝消息，参数说明：
             * long deliveryTag：唯一标识 ID。
             * boolean requeue：如果 requeue 参数设置为 true，
             * 则 RabbitMQ 会重新将这条消息存入队列，以便发送给下一个订阅的消费者；
             * 如果 requeue 参数设置为 false，则 RabbitMQ 立即会还把消息从队列中移除，
             * 而不会把它发送给新的消费者。
             */
            channel.basicReject(deliveryTag, false);

            e.printStackTrace();
        }
    }

    /**
     * 延时接收用户信息Map格式数据
     */
    @Override
    public void receiverDelayUserMap(Message message, Channel channel) throws Exception
    {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try
        {
            //将JSON格式数据转换为Map对象
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
            Map<String, Object> resultMap = mapper.readValue(message.getBody(),javaType);

            System.out.println("接收者收到Map格式消息：");
            System.out.println("用户编号：" + resultMap.get("userId"));
            System.out.println("用户名称：" + resultMap.get("userName"));
            System.out.println("博客地址：" + resultMap.get("blogUrl"));
            System.out.println("博客信息：" + resultMap.get("userRemark"));

            //确认消息
            channel.basicAck(deliveryTag, true);

            //否定消息
            //channel.basicNack(deliveryTag, true, false);
        }
        catch (Exception e)
        {
            //拒绝消息
            channel.basicReject(deliveryTag, false);

            e.printStackTrace();
        }
    }
}
