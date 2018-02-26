package com.example.bsproperty.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPassActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.et_newpass)
    EditText etNewpass;
    @BindView(R.id.et_newpass2)
    EditText etNewpass2;
    private UserBean userBean;

    @Override
    protected void initView(Bundle savedInstanceState) {
        userBean = MyApplication.getInstance().getUserBean();
        tvTitle.setText("修改密码");
        btnRight.setText("保存");
        if (userBean != null) {
            tvNumber.setText(userBean.getNumber());
        }

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_edit_pass;
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.btn_back, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_right:
                if (etPass.getText().toString().equals("")) {
                    showToast(this, "密码不能为空！");
                    return;
                }
                if (etNewpass.getText().toString().equals("")) {
                    showToast(this, "新密码不能为空！");
                    return;
                }
                if (etNewpass2.getText().toString().equals("")) {
                    showToast(this, "请再次输入新密码！");
                    return;
                }
                if (!etNewpass.getText().toString().equals(etNewpass2.getText().toString())) {
                    showToast(this, "两次密码输入不一致！");
                    return;
                }
                OkHttpTools.sendPost(this, ApiManager.EDITPASS)
                        .addParams("number", userBean.getNumber())
                        .addParams("oldPass", etPass.getText().toString())
                        .addParams("newPass", etNewpass.getText().toString())
                        .build().execute(new BaseCallBack<BaseResponse>(this, BaseResponse.class) {
                        @Override
                        public void onResponse(BaseResponse baseResponse) {
                            showToast(EditPassActivity.this,baseResponse.getMessage());
                            EditPassActivity.this.finish();
                        }
                });
                break;
        }
    }
}
