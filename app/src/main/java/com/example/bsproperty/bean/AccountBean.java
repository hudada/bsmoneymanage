package com.example.bsproperty.bean;

/**
 * Created by wdxc1 on 2018/1/28.
 */

public class AccountBean {
    private String number;
    private String pwd;

    public AccountBean(String number, String pwd) {
        this.number = number;
        this.pwd = pwd;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNumber() {
        return number;
    }

    public String getPwd() {
        return pwd;
    }
}
