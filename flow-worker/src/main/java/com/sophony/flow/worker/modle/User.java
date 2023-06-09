package com.sophony.flow.worker.modle;

/**
 * User
 *
 * @author yzm
 * @version 1.5.0
 * @description 用户
 * @date 2023/3/9 10:06
 */
public class User<T> {

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 拓展数据，用userinfo做key插入expand中
     */
    private T data;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
