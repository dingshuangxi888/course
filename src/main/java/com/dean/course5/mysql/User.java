package com.dean.course5.mysql;

/**
 * Created by dean on 15/1/4.
 */
public class User {
    public long userId;

    public String userName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
