package com.pjb.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * 用户消息接收接口
 * @author pan_junbiao
 **/
public interface UserReceiver
{
    /**
     * 接收用户信息Json格式数据
     */
    public void receiverUserJson(Message message, Channel channel) throws Exception;

    /**
     * 延时接收用户信息Map格式数据
     */
    public void receiverDelayUserMap(Message message, Channel channel) throws Exception;
}
