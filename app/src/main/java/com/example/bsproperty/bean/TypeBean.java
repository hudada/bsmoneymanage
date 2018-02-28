package com.example.bsproperty.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wdxc1 on 2018/2/28.
 */
@Entity
public class TypeBean {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private int flag;
    @Generated(hash = 435186254)
    public TypeBean(Long id, @NotNull String type, int flag) {
        this.id = id;
        this.type = type;
        this.flag = flag;
    }
    @Generated(hash = 119682038)
    public TypeBean() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getFlag() {
        return this.flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
}
