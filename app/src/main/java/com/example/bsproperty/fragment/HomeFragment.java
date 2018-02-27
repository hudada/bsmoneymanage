package com.example.bsproperty.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yezi on 2018/1/27.
 */

public class HomeFragment extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_value)
    EditText tvValue;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    EditText tvAddress;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.tv_tozhi)
    TextView tvTozhi;

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


    @OnClick({R.id.tv_type, R.id.account, R.id.tv_time, R.id.btn_add, R.id.tv_tozhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type:
                break;
            case R.id.account:
                break;
            case R.id.tv_time:
                break;
            case R.id.btn_add:
                break;
            case R.id.tv_tozhi:
                break;
        }
    }
}
