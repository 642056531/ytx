package com.ytx.erp.bean;

import java.io.Serializable;

/**
 * @author KePeiKun
 * @time 2022/7/22
 * @describe 登录
 */
public class LoginBean implements Serializable {
    String username;
    String password;
    String code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
