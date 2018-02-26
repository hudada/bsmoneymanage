package com.example.bsproperty.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.adapter.BaseAdapter;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.ForumBean;
import com.example.bsproperty.bean.ReplayBean;
import com.example.bsproperty.bean.ReplayListBean;
import com.example.bsproperty.fragment.HomeFragment;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForumDetailActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.sl_list)
    SwipeRefreshLayout slList;
    @BindView(R.id.et_reply)
    EditText etReply;

    private ArrayList<ReplayBean> mData;
    private MyAdapter adapter;
    private ForumBean info;
    private int replyCount = 0;
    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        rvList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_forum_detail;
    }

    @Override
    protected void loadData() {

        info = (ForumBean) getIntent().getSerializableExtra("data");
        if (info != null){
            tvTitle.setText(info.getTitle());
            OkHttpTools.sendGet(this, ApiManager.FORUM_DETAIL_LIST + info.getId()).build()
                    .execute(new BaseCallBack<ReplayListBean>(this,ReplayListBean.class) {
                        @Override
                        public void onResponse(ReplayListBean replayListBean) {
                            mData = replayListBean.getData();
                            adapter = new MyAdapter(ForumDetailActivity.this, R.layout.item_forum_detail, mData);
                            adapter.setmHeadView(LayoutInflater.from(ForumDetailActivity.this).inflate(R.layout.head_forum_detail, null, false),
                                    new BaseAdapter.OnInitHead() {
                                        @Override
                                        public void onInitHeadData(View headView, Object o) {
                                            ((TextView)headView.findViewById(R.id.tv_name)).setText("业主"+info.getNumber()+"发布");
                                            ((TextView)headView.findViewById(R.id.tv_time)).setText(
                                                    format.format(new Date(Long.parseLong(info.getDate()))));
                                            ((TextView)headView.findViewById(R.id.tv_content)).setText(info.getInfo());
                                        }
                                    });
                            rvList.setAdapter(adapter);
                        }
                    });
        }

    }

    @OnClick({R.id.btn_back,R.id.btn_reply})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_reply:
                String msg = etReply.getText().toString().trim();
                if (TextUtils.isEmpty(msg)){
                    showToast(ForumDetailActivity.this,"请输入回复内容");
                    return;
                }
                final ReplayBean replayBean = new ReplayBean();
                replayBean.setNumber(MyApplication.getInstance().getUserBean().getNumber());
                replayBean.setInfo(msg);
                replayBean.setForumId(info.getId());
                replayBean.setDate(System.currentTimeMillis()+"");
                OkHttpTools.postJson(ForumDetailActivity.this,ApiManager.POST_REPLY,
                        replayBean).build().execute(new BaseCallBack<BaseResponse>(ForumDetailActivity.this,BaseResponse.class) {
                    @Override
                    public void onResponse(BaseResponse baseResponse) {
                        showToast(ForumDetailActivity.this,baseResponse.getMessage());
                        mData.add(replayBean);
                        adapter.notifyDataSetChanged(mData);
                        etReply.setText("");
                        replyCount++;
                    }
                });
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("count",replyCount);
        setResult(RESULT_OK,intent);
        super.finish();
    }

    private class MyAdapter extends BaseAdapter<ReplayBean>{

        public MyAdapter(Context context, int layoutId, ArrayList<ReplayBean> data) {
            super(context, layoutId, data);
        }

        @Override
        public void initItemView(BaseViewHolder holder, ReplayBean replayBean, int position) {
            holder.setText(R.id.tv_name,"业主"+replayBean.getNumber()+"回复");
            holder.setText(R.id.tv_time,(
                    format.format(new Date(Long.parseLong(replayBean.getDate())))));
            holder.setText(R.id.tv_content,replayBean.getInfo());
        }
    }
}
