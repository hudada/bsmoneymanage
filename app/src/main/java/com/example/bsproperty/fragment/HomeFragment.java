package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.NoticeBean;
import com.example.bsproperty.bean.NoticeListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by yezi on 2018/1/27.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    private MyAdapter adapter;
    private ArrayList<NoticeBean> mData;
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void loadData() {
        OkHttpTools.sendGet(mContext, ApiManager.HOME_LIST).build().execute(new BaseCallBack<NoticeListBean>(mContext, NoticeListBean.class) {
            @Override
            public void onResponse(NoticeListBean noticeListBean) {
                mData = noticeListBean.getData();
                adapter.notifyDataSetChanged(mData);
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mData = new ArrayList<>();
        slList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                slList.setRefreshing(false);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MyAdapter(mContext, R.layout.item_notice, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mData.get(position).getTitle())
                        .setMessage(mData.get(position).getInfo())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_home;
    }

    private class MyAdapter extends BaseAdapter<NoticeBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<NoticeBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, NoticeBean noticeBean, int position) {
            holder.setText(R.id.tv_title, noticeBean.getTitle());
            holder.setText(R.id.tv_time, format.format(new Date(Long.parseLong(noticeBean.getDate()))));
            holder.setText(R.id.tv_content, noticeBean.getInfo());
        }
    }
}
