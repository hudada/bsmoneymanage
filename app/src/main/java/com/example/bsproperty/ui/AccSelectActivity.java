package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ForumBean;
import com.example.bsproperty.bean.ReplayBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AccSelectActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    private ArrayList<String> accs =new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("选择使用的账户");
        accs= MyApplication.getInstance().getAccs();
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                slList.setRefreshing(false);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, R.layout.item_home_type, accs);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent=new Intent();
                intent.putExtra("pos",position);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        rvList.setAdapter(adapter);

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_acc_select;
    }

    @Override
    protected void loadData() {


    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                setResult(RESULT_CANCELED);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<String>{

        public MyAdapter(Context context, int layoutId, ArrayList<String> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, String type, int position) {
            holder.setText(R.id.tv_01,type);
        }
    }
}
