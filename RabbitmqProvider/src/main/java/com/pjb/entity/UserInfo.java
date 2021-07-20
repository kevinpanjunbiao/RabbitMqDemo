package com.pjb.entity;

/**
 * 用户信息实体类
 * @author pan_junbiao
 **/
public class UserInfo
{
    private int userId; //用户编号
    private String userName; //用户姓名
    private String blogUrl; //博客地址
    private String blogRemark; //博客信息

    //省略getter与setter方法...

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getBlogUrl()
    {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl)
    {
        this.blogUrl = blogUrl;
    }

    public String getBlogRemark()
    {
        return blogRemark;
    }

    public void setBlogRemark(String blogRemark)
    {
        this.blogRemark = blogRemark;
    }
}