package com.study.redis.apply.pojo;

import java.io.Serializable;

/**
 * @author Hash
 * @since 2020/12/12
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userName;
    private String userId;

    public User(String userId, String userName) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "User [userName=" + userName + ", userId=" + userId + "]";
    }
}
