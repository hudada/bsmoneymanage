package com.example.bsproperty.fragment;

import android.os.Bundle;

import com.example.bsproperty.R;

import java.text.SimpleDateFormat;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment04 extends BaseFragment {
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void loadData() {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_home;
    }

}
