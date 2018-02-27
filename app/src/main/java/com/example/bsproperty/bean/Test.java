package com.example.bsproperty.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by John on 2018/2/27.
 */

@Entity
public class Test {
    @Id(autoincrement = true)
    private Long _id;
    private String source;
    @NotNull
    private String url;
    @Generated(hash = 1283263923)
    public Test(Long _id, String source, @NotNull String url) {
        this._id = _id;
        this.source = source;
        this.url = url;
    }
    @Generated(hash = 372557997)
    public Test() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
