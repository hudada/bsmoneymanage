package com.example.bsproperty.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.PamentRecordBean;
import com.example.bsproperty.bean.PamentRecordListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;


public class PriceFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    private PriceFragment.MyAdapter adapter;
    private ArrayList<PamentRecordBean> mData;
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void loadData() {
        setUserVisibleHint(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (MyApplication.getInstance().getUserBean() != null) {
                OkHttpTools.sendGet(mContext, ApiManager.RECORD_LIST + MyApplication.getInstance().getUserBean().getNumber())
                        .build().execute(new BaseCallBack<PamentRecordListBean>(mContext, PamentRecordListBean.class) {
                    @Override
                    public void onResponse(PamentRecordListBean pamentRecordListBean) {
                        mData = pamentRecordListBean.getData();
                        adapter.notifyDataSetChanged(mData);
                    }
                });
            }
        }
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
        adapter = new PriceFragment.MyAdapter(mContext, R.layout.item_record, mData);
        rvList.setAdapter(adapter);
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_price;
    }

    private class MyAdapter extends BaseAdapter<PamentRecordBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<PamentRecordBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(final BaseViewHolder holder, final PamentRecordBean pamentRecordBean, final int position) {
            if (Integer.parseInt(pamentRecordBean.getType()) == 0) {
                holder.setText(R.id.tv_title, "水费" + "("
                        + ((Integer.parseInt(pamentRecordBean.getState()) == 0) ? "未缴清" : "已缴清") + ")");
            } else if (Integer.parseInt(pamentRecordBean.getType()) == 1) {
                holder.setText(R.id.tv_title, "电费" + "("
                        + ((Integer.parseInt(pamentRecordBean.getState()) == 0) ? "未缴清" : "已缴清") + ")");
            } else if (Integer.parseInt(pamentRecordBean.getType()) == 2) {
                holder.setText(R.id.tv_title, "燃气费" + "("
                        + ((Integer.parseInt(pamentRecordBean.getState()) == 0) ? "未缴清" : "已缴清") + ")");
            } else if (Integer.parseInt(pamentRecordBean.getType()) == 3) {
                holder.setText(R.id.tv_title, "停车费" + "("
                        + ((Integer.parseInt(pamentRecordBean.getState()) == 0) ? "未缴清" : "已缴清") + ")");
            } else if (Integer.parseInt(pamentRecordBean.getType()) == 4) {
                holder.setText(R.id.tv_title, "物管费" + "("
                        + ((Integer.parseInt(pamentRecordBean.getState()) == 0) ? "未缴清" : "已缴清") + ")");
            }
            holder.setText(R.id.tv_time,  format.format(new Date(Long.parseLong(pamentRecordBean.getDate()))));
            holder.setText(R.id.tv_content, pamentRecordBean.getAmount() + "元");
            final Button button = (Button) holder.getView(R.id.btn_commit);
            if (Integer.parseInt(pamentRecordBean.getState()) == 1) {
                button.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.VISIBLE);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpTools.sendPut(mContext, ApiManager.RECORD_COMMIT + pamentRecordBean.getId())
                            .build().execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {
                        @Override
                        public void onResponse(BaseResponse baseResponse) {
                            ((BaseActivity) mContext).showToast(mContext, baseResponse.getMessage());
                            mData.get(position).setState("1");
                            adapter.notifyItemChanged(position, "one");
                        }
                    });

                }
            });
        }

    }
}
