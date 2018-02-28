package com.example.bsproperty.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by wdxc1 on 2018/2/28.
 */
@Entity
public class AccBean {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String account;
    @NotNull
    private double money;
    @Generated(hash = 1066011718)
    public AccBean(Long id, @NotNull String account, double money) {
        this.id = id;
        this.account = account;
        this.money = money;
    }
    @Generated(hash = 107958732)
    public AccBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public double getMoney() {
        return this.money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
}
