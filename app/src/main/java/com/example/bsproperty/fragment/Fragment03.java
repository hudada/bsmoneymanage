package com.example.bsproperty.fragment;

import android.os.Bundle;

import com.example.bsproperty.R;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.utils.NewBeanDaoUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment03 extends BaseFragment {
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void loadData() {
        NewBeanDaoUtils utils = new NewBeanDaoUtils(getActivity());
        List<NewBean> data =  utils.queryTestByNativeSql("where _id = ?",
                new String[]{"1"});
        int x= 1;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_03;
    }

}
