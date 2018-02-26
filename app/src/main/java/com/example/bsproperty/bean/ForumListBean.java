package com.example.bsproperty.bean;

import java.util.ArrayList;

/**
 * Created by yezi on 2018/1/27.
 */

public class ForumListBean extends BaseResponse {
    private ArrayList<ForumBean> data;

    public ArrayList<ForumBean> getData() {
        return data;
    }

    public void setData(ArrayList<ForumBean> data) {
        this.data = data;
    }
}
