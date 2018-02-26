package com.example.bsproperty.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yezi on 2018/1/27.
 */

public class NoticeListBean extends BaseResponse {
    private ArrayList<NoticeBean> data;

    public ArrayList<NoticeBean> getData() {
        return data;
    }

    public void setData(ArrayList<NoticeBean> data) {
        this.data = data;
    }
}
