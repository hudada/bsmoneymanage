package com.example.bsproperty.bean;

import java.util.ArrayList;

/**
 * Created by wdxc1 on 2018/1/30.
 */

public class ReplayListBean extends BaseResponse {

    private ArrayList<ReplayBean> data;

    public ArrayList<ReplayBean> getData() {
        return data;
    }

    public void setData(ArrayList<ReplayBean> data) {
        this.data = data;
    }
}
