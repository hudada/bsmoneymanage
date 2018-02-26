package com.example.bsproperty.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.AccountBean;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.ForumBean;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendForumActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_msg)
    EditText etMsg;

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_send_forum;
    }

    @Override
    protected void loadData() {
        tvTitle.setText("新帖子");
        btnRight.setText("发布");
    }


    @OnClick({R.id.btn_back, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right:
                String title = etTitle.getText().toString().trim();
                String msg = etMsg.getText().toString().trim();
                if (TextUtils.isEmpty(title)){showToast(SendForumActivity.this,"请输入标题");return;

                }
                if (TextUtils.isEmpty(msg)){showToast(SendForumActivity.this,"请输入内容");return;

                }
                ForumBean bean = new ForumBean();
                bean.setTitle(title);
                bean.setInfo(msg);
                bean.setNumber(MyApplication.getInstance().getUserBean().getNumber());
                OkHttpTools.postJson(SendForumActivity.this, ApiManager.POST_FORUM,
                        bean).build()
                        .execute(new BaseCallBack<BaseResponse>(SendForumActivity.this, BaseResponse.class) {
                            @Override
                            public void onResponse(BaseResponse baseResponse) {
                                showToast(SendForumActivity.this,baseResponse.getMessage());
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                break;
        }
    }
}
