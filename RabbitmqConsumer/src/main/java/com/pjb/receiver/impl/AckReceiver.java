package com.pjb.receiver.impl;

import com.pjb.config.RabbitMqConfig;
import com.pjb.receiver.UserReceiver;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息接收确认处理类
 * 所有的消息，都由该类接收
 * @author pan_junbiao
 **/
@Service
public class AckReceiver implements ChannelAwareMessageListener
{
    /**
     * 用户消息接收类
     */
    @Autowired
    private UserReceiver userReceiver;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception
    {
        //时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("消息接收成功，接收时间：" + dateFormat.format(new Date()) + "\n");

        //获取队列名称
        String queueName = message.getMessageProperties().getConsumerQueue();

        //接收用户信息Json格式数据
        if (queueName.equals(RabbitMqConfig.DIRECT_QUEUE))
        {
            userReceiver.receiverUserJson(message, channel);
        }

        //延时接收用户信息Map格式数据
        if (queueName.equals(RabbitMqConfig.DELAY_QUEUE))
        {
            userReceiver.receiverDelayUserMap(message, channel);
        }

        //多个队列的处理，则如上述代码，继续添加方法....
    }
}
