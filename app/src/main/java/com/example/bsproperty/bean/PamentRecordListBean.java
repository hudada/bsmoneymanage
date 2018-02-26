package com.example.bsproperty.bean;

import com.example.bsproperty.ui.BaseActivity;

import java.util.ArrayList;

/**
 * Created by wdxc1 on 2018/1/29.
 */

public class PamentRecordListBean extends BaseResponse {
    private ArrayList<PamentRecordBean> data;

    public ArrayList<PamentRecordBean> getData() {
        return data;
    }

    public void setData(ArrayList<PamentRecordBean> data) {
        this.data = data;
    }
}
