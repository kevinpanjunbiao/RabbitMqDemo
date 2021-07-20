package com.pjb.sender;

import com.pjb.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户消息发送测试类
 * @author pan_junbiao
 **/
@SpringBootTest
public class UserSenderTest
{


    /**
     * 发送用户信息Json格式数据
     */
    @Test
    public void sendUserJson() throws Exception
    {
        //创建用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1);
        userInfo.setUserName("pan_junbiao的博客");
        userInfo.setBlogUrl("https://blog.csdn.net/pan_junbiao");
        userInfo.setBlogRemark("您好，欢迎访问 pan_junbiao的博客");

        //执行发送
        userSender.sendUserJson(userInfo);

        //由于这里使用的是测试方法，当测试方法结束，RabbitMQ相关的资源也就关闭了，
        //会导致消息确认的回调出现问题，所有加段延时，该延时与业务无关
        Thread.sleep(2000);
    }

    /**
     * 用户消息发送类
     */
    @Autowired
    private UserSender userSender;

    /**
     * 延时发送用户信息Map格式数据
     */
    @Test
    public void sendDelayUserMap() throws Exception
    {
        //创建用户信息Map
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", "1");
        userMap.put("userName", "pan_junbiao的博客");
        userMap.put("blogUrl", "https://blog.csdn.net/pan_junbiao");
        userMap.put("userRemark", "您好，欢迎访问 pan_junbiao的博客");

        //执行发送
        userSender.sendDelayUserMap(userMap);

        //由于这里使用的是测试方法，当测试方法结束，RabbitMQ相关的资源也就关闭了，
        //会导致消息确认的回调出现问题，所有加段延时，该延时与业务无关
        Thread.sleep(2000);
    }
}
