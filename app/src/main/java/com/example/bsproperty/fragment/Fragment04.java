package com.example.bsproperty.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.ui.AccEditListActivity;
import com.example.bsproperty.ui.TypeEditListActivity;
import com.example.bsproperty.ui.TypeSelectActivity;
import com.example.bsproperty.utils.AccBeanDaoUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yezi on 2018/1/27.
 */

public class Fragment04 extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @BindView(R.id.tv_now)
    TextView tvNow;
    @BindView(R.id.tv_moracc)
    TextView tvMoracc;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    @BindView(R.id.btn_moretype)
    Button btnMoretype;
    private AccBeanDaoUtils accDao;
    List<AccBean> accs = new ArrayList<>();
    private Fragment04.MyAdapter adapter;
    private boolean isInit=false;
    @Override
    protected void loadData() {
        accs = accDao.queryAll();
        getNowValue();
        setData();
        isInit=true;
    }

    private void setData() {
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                slList.setRefreshing(false);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new Fragment04.MyAdapter(mContext, R.layout.item_fr04_acc, (ArrayList<AccBean>) accs);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, final int position) {
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        accDao = new AccBeanDaoUtils(mContext);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_04;
    }


    @OnClick({ R.id.tv_moracc, R.id.rv_list, R.id.sl_list, R.id.btn_moretype})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_moracc:
                startActivityForResult(new Intent(mContext, AccEditListActivity.class), 521);
                break;
            case R.id.rv_list:
                break;
            case R.id.sl_list:
                break;
            case R.id.btn_moretype:
                startActivityForResult(new Intent(mContext, TypeEditListActivity.class), 109);
                break;
        }
    }

    public void getNowValue() {
        double allCount04=0.0;
        for (AccBean a:accs) {
            allCount04+=a.getMoney();
        }
        DecimalFormat df=new DecimalFormat("0.00");
        tvNow.setText(df.format(allCount04));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInit&&tvNow!=null){
            accs = accDao.queryAll();
            getNowValue();
            setData();
        }
    }

    private class MyAdapter extends BaseAdapter<AccBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<AccBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, AccBean type, int position) {
           holder.setText(R.id.tv_01,type.getAccount()).setText(R.id.tv_02,type.getMoney()+"  元");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 521:
                    accs = accDao.queryAll();
                    adapter.notifyDataSetChanged((ArrayList<AccBean>) accs);
                    getNowValue();
                    break;
                case 109:
                    break;
            }
        }
    }
}
