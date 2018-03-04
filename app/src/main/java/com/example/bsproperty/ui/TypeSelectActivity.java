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
import com.example.bsproperty.bean.TypeBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class TypeSelectActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    @BindView(R.id.rb_01)
    RadioButton rb01;
    @BindView(R.id.rb_02)
    RadioButton rb02;
    private ArrayList<TypeBean> outtypes=new ArrayList<>();
    private ArrayList<TypeBean> intypes=new ArrayList<>();
    private MyAdapter adapter;
    private boolean flag;

    @Override
    protected void initView(Bundle savedInstanceState) {
        MyApplication.getInstance().getTypeList();
        tvTitle.setText("选择收支类型");
        flag=true;
        rb01.setChecked(true);
        rb02.setChecked(false);
        outtypes = MyApplication.getInstance().getOuttypes();
        intypes= MyApplication.getInstance().getIntypes();
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                slList.setRefreshing(false);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, R.layout.item_home_type, outtypes);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent=new Intent();
                intent.putExtra("flag",flag);
                intent.putExtra("pos",position);
                setResult(RESULT_OK,intent);
                Log.e("test",flag+"+++"+position);
                finish();
            }
        });
        rvList.setAdapter(adapter);

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_home_type_selectm;
    }

    @Override
    protected void loadData() {


    }

    @OnClick({R.id.btn_back,R.id.rb_01,R.id.rb_02})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.rb_01:
                flag=true;
                adapter.notifyDataSetChanged(outtypes);
                break;
            case R.id.rb_02:
                flag=false;
                adapter.notifyDataSetChanged(intypes);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<TypeBean>{

        public MyAdapter(Context context, int layoutId, ArrayList<TypeBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, TypeBean type, int position) {
            holder.setText(R.id.tv_01,type.getType());
        }
    }
}
