package com.example.bsproperty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.utils.AccBeanDaoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class AccEditListActivity extends BaseActivity {

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
    private ArrayList<AccBean> accs =new ArrayList<>();
    private MyAdapter adapter;
    private AccBeanDaoUtils accDao;

    @Override
    protected void initView(Bundle savedInstanceState) {
        btnRight.setText("添加账户");
        accDao=new AccBeanDaoUtils(this);
        tvTitle.setText("选择使用的账户");
        accs= (ArrayList<AccBean>) accDao.queryAll();
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
                Intent intent =new Intent(AccEditListActivity.this, AccEditActivity.class);
                intent.putExtra("id", accs.get(position).getId());
                startActivityForResult(intent, 521);
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

    @OnClick({R.id.btn_back,R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn_right:
                Intent intent =new Intent(AccEditListActivity.this, AccEditActivity.class);
                intent.putExtra("id",-1l);
                startActivityForResult(intent, 521);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter<AccBean>{

        public MyAdapter(Context context, int layoutId, ArrayList<AccBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, AccBean type, int position) {
            holder.setText(R.id.tv_01,type.getAccount());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 521:
                    accs= (ArrayList<AccBean>) accDao.queryAll();
                    adapter.notifyDataSetChanged(accs);
                    break;
                case 109:
                    break;
            }
        }
    }
}
