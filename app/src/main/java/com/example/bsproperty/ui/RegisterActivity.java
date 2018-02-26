package com.example.bsproperty.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsproperty.R;
import com.example.bsproperty.bean.AccountBean;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.et_repass)
    EditText etRepass;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvTitle.setText("用户注册");
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.btn_back, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_register:
                String number = etNumber.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                String repass = etRepass.getText().toString().trim();

                if (TextUtils.isEmpty(number)) {
                    showToast(RegisterActivity.this, "账号不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    showToast(RegisterActivity.this, "密码不能为空！");
                    return;
                }
                if (!pass.equals(repass)) {
                    showToast(RegisterActivity.this, "两次密码输入不一致！");
                    return;
                }
                OkHttpTools.postJson(RegisterActivity.this, ApiManager.REGISTER, new AccountBean(number, pass)).build()
                        .execute(new BaseCallBack<UserObjBean>(RegisterActivity.this, UserObjBean.class) {
                            @Override
                            public void onResponse(UserObjBean userObjBean) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                                        .setTitle("注册成功")
                                        .setMessage("注册账号为：" + userObjBean.getData().getNumber())
                                        .setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                RegisterActivity.this.finish();
                                            }
                                        });
                                builder.show();
                            }
                        });
                break;
        }
    }
}
