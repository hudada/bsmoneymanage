package com.example.bsproperty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditUserActivity extends BaseActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    Button btnRight;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.rb_sex1)
    RadioButton rbSex1;
    @BindView(R.id.rb_sex2)
    RadioButton rbSex2;
    @BindView(R.id.et_dong)
    EditText etDong;
    @BindView(R.id.et_dan)
    EditText etDan;
    @BindView(R.id.et_hao)
    EditText etHao;
    UserBean userBean;
    UserBean newUser;

    @Override
    protected void initView(Bundle savedInstanceState) {
        userBean = MyApplication.getInstance().getUserBean();
        if (userBean != null) {
            tvNumber.setText(userBean.getNumber());
            tvTitle.setText("修改用户信息");
            btnRight.setText("保存");
            if (userBean.getTel() != null && !userBean.getTel().equals("")) {
                etTel.setText(userBean.getTel());
            }
            if (userBean.getName() != null && !userBean.getName().equals("")) {
                etUsername.setText(userBean.getName());
            }
            int sex = Integer.parseInt(userBean.getSex());
            if (sex == 0) {
                rbSex2.setChecked(true);
            } else {
                rbSex1.setChecked(true);
            }
            if (userBean.getDong() != null && !userBean.getDong().equals("")) {
                etDong.setText(userBean.getDong());
            }
            if (userBean.getDan() != null && !userBean.getDan().equals("")) {
                etDan.setText(userBean.getDan());
            }
            if (userBean.getHao() != null && !userBean.getHao().equals("")) {
                etHao.setText(userBean.getHao());
            }
        }

    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_edit_user;
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
                newUser = new UserBean();
                if (etUsername.getText() != null && !etUsername.getText().toString().equals("")) {
                    newUser.setName(etUsername.getText() + "");
                } else {
                    showToast(EditUserActivity.this, "用户名不能为空！");
                    return;
                }
                if (etTel.getText() != null && !etTel.getText().toString().equals("")) {
                    newUser.setTel(etTel.getText() + "");
                } else {
                    showToast(EditUserActivity.this, "手机号不能为空！");
                    return;
                }
                if (etDong.getText() != null && !etDong.getText().toString().equals("")) {
                    newUser.setDong(etDong.getText() + "");
                } else {
                    showToast(EditUserActivity.this, "栋数不能为空！");
                    return;
                }
                if (etDan.getText() != null && !etDan.getText().toString().equals("")) {
                    newUser.setDan(etDan.getText() + "");
                } else {
                    showToast(EditUserActivity.this, "单元号不能为空！");
                    return;
                }
                if (etHao.getText() != null && !etHao.getText().toString().equals("")) {
                    newUser.setHao(etHao.getText() + "");
                } else {
                    showToast(EditUserActivity.this, "门牌号不能为空！");
                    return;
                }
                if (rbSex1.isChecked()) {
                    newUser.setSex("" + 1);
                } else {
                    newUser.setSex("" + 0);
                }
                newUser.setNumber(userBean.getNumber());
                newUser.setBalance(userBean.getBalance());
                OkHttpTools.postJson(EditUserActivity.this, ApiManager.EDITUSER, newUser)
                        .build().execute(new BaseCallBack<UserObjBean>(EditUserActivity.this, UserObjBean.class) {
                    @Override
                    public void onResponse(UserObjBean userObjBean) {
                        // 更新全局User对象
                        userBean.setSex(userObjBean.getData().getSex());
                        userBean.setTel(userObjBean.getData().getTel());
                        userBean.setName(userObjBean.getData().getName());
                        userBean.setBalance(userObjBean.getData().getBalance());
                        userBean.setDong(userObjBean.getData().getDong());
                        userBean.setDan(userObjBean.getData().getDan());
                        userBean.setHao(userObjBean.getData().getHao());
                        showToast(EditUserActivity.this, "用户信息已修改！");
                        setResult(RESULT_OK);
                        EditUserActivity.this.finish();
                    }
                });
                break;
        }
    }
}
