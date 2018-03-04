package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.TypeBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class TypeEditListActivity extends BaseActivity {

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
    private ArrayList<TypeBean> outtypes = new ArrayList<>();
    private ArrayList<TypeBean> intypes = new ArrayList<>();
    private MyAdapter adapter;
    private boolean flag;
    private ArrayList<TypeBean> mdata = new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("选择收支类型");
        btnRight.setText("添加分类");
        flag = true;
        rb01.setChecked(true);
        rb02.setChecked(false);
        MyApplication.getInstance().getTypeList();
        outtypes = MyApplication.getInstance().getOuttypes();
        intypes = MyApplication.getInstance().getIntypes();
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                slList.setRefreshing(false);
            }
        });
        mdata = outtypes;
        rvList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, R.layout.item_home_type, mdata);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(TypeEditListActivity.this, TypeEditActivity.class);
                intent.putExtra("id", mdata.get(position).getId());
                startActivityForResult(intent, 521);
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

    @OnClick({R.id.btn_back, R.id.rb_01, R.id.rb_02, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_right:
                Intent intent = new Intent(TypeEditListActivity.this, TypeEditActivity.class);
                intent.putExtra("id", -1l);
                startActivityForResult(intent, 521);
                break;
            case R.id.rb_01:
                flag = true;
                MyApplication.getInstance().getTypeList();
                outtypes = MyApplication.getOuttypes();
                mdata = outtypes;
                adapter.notifyDataSetChanged(mdata);
                break;
            case R.id.rb_02:
                flag = false;
                MyApplication.getInstance().getTypeList();
                intypes = MyApplication.getIntypes();
                mdata = intypes;
                mdata = intypes;
                adapter.notifyDataSetChanged(mdata);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<TypeBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<TypeBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, TypeBean type, int position) {
            holder.setText(R.id.tv_01, type.getType());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 521:
                    MyApplication.getInstance().getTypeList();
                    outtypes = MyApplication.getOuttypes();
                    intypes = MyApplication.getIntypes();
                    if (data.getBooleanExtra("flag", true)) {
                        flag = true;
                        rb01.setChecked(true);
                        rb02.setChecked(false);
                        mdata = outtypes;
                    } else {
                        flag = false;
                        rb01.setChecked(false);
                        rb02.setChecked(true);
                        mdata = intypes;
                    }
                    adapter.notifyDataSetChanged(mdata);
                    break;
                case 109:
                    break;
            }
        }
    }
}
