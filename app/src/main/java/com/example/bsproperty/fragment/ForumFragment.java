package com.example.bsproperty.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.ForumBean;
import com.example.bsproperty.bean.ForumListBean;
import com.example.bsproperty.bean.NoticeBean;
import com.example.bsproperty.bean.NoticeListBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.ForumDetailActivity;
import com.example.bsproperty.ui.SendForumActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yezi on 2018/1/27.
 */

public class ForumFragment extends BaseFragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    private MyAdapter adapter;
    private ArrayList<ForumBean> mData;
    private int curPosition = -1;
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void loadData() {
        OkHttpTools.sendGet(mContext, ApiManager.FORUM_LIST).build()
                .execute(new BaseCallBack<ForumListBean>(mContext, ForumListBean.class) {
                    @Override
                    public void onResponse(ForumListBean forumListBean) {
                        mData = forumListBean.getData();
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
        adapter = new MyAdapter(mContext, R.layout.item_forum, mData);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                curPosition = position;
                Intent intent = new Intent(mContext, ForumDetailActivity.class);
                intent.putExtra("data",mData.get(position));
                startActivityForResult(intent,5521);
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 5521:
                    int count = data.getIntExtra("count",0);
                    if (count != 0 && curPosition != -1){
                        int curCount = Integer.parseInt(mData.get(curPosition).getCount());
                        int total = curCount + count;
                        mData.get(curPosition).setCount(total+"");
                        adapter.notifyItemChanged(curPosition);
                        curPosition = -1;
                    }
                    break;
                case 521:
                    loadData();
                    break;
            }
        }
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_forum;
    }

    private class MyAdapter extends BaseAdapter<ForumBean> {

        public MyAdapter(Context context, int layoutId, ArrayList<ForumBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ForumBean forumBean, int position) {
            holder.setText(R.id.tv_title, forumBean.getTitle());
            holder.setText(R.id.tv_name, "(业主"+forumBean.getNumber()+"发布)");
            holder.setText(R.id.tv_time,(
                    format.format(new Date(Long.parseLong(forumBean.getDate())))));
            holder.setText(R.id.tv_content, forumBean.getInfo());
            holder.setText(R.id.tv_count, forumBean.getCount() + "回复");
        }
    }


    @OnClick({R.id.tv_send})
    public void onViewClicked() {
        startActivityForResult(new Intent(mContext, SendForumActivity.class),521);
    }

}
