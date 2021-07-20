package com.pjb.sender;

import com.pjb.entity.UserInfo;

import java.util.Map;

/**
 * 用户消息发送服务接口
 * @author pan_junbiao
 **/
public interface UserSender
{
    /**
     * 发送用户信息Json格式数据
     * @param userInfo 用户信息实体类
     */
    public void sendUserJson(UserInfo userInfo);

    /**
     * 延时发送用户信息Map格式数据
     * @param userMap 用户信息Map
     */
    public void sendDelayUserMap(Map userMap);
}
