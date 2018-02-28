package com.example.bsproperty.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by wdxc1 on 2018/2/28.
 */
@Entity
public class NewBean {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private Long  typeId;
    @NotNull
    private Long  accId;
    @NotNull
    private Long time;
    @NotNull
    private double money;
    @NotNull
    private String  address;
    @Generated(hash = 2019284792)
    public NewBean(Long id, @NotNull Long typeId, @NotNull Long accId,
            @NotNull Long time, double money, @NotNull String address) {
        this.id = id;
        this.typeId = typeId;
        this.accId = accId;
        this.time = time;
        this.money = money;
        this.address = address;
    }
    @Generated(hash = 1213378749)
    public NewBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTypeId() {
        return this.typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    public Long getAccId() {
        return this.accId;
    }
    public void setAccId(Long accId) {
        this.accId = accId;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public double getMoney() {
        return this.money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
