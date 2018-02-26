package com.example.bsproperty.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.BaseResponse;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.net.ApiManager;
import com.example.bsproperty.net.BaseCallBack;
import com.example.bsproperty.net.OkHttpTools;
import com.example.bsproperty.ui.BaseActivity;
import com.example.bsproperty.ui.EditPassActivity;
import com.example.bsproperty.ui.EditUserActivity;
import com.example.bsproperty.ui.LoginActivity;
import com.example.bsproperty.ui.MainActivity;
import com.example.bsproperty.ui.RegisterActivity;
import com.example.bsproperty.utils.SpUtils;
import com.example.bsproperty.view.ModifyItemDialog;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yezi on 2018/1/27.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.btn_btn)
    Button btnBtn;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.tv_men)
    TextView tvMen;
    @BindView(R.id.tv_edit_pass)
    TextView tvEditPass;


    private UserBean userBean;

    @Override
    protected void loadData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mContext != null) {
            Log.e("hdd1","show");
            userBean = MyApplication.getInstance().getUserBean();
            if (userBean == null) {
                btnBtn.setText("登      陆");
                btnEdit.setVisibility(View.GONE);
                tvAdd.setVisibility(View.GONE);
                tvEditPass.setVisibility(View.GONE);
            } else {
                OkHttpTools.sendGet(mContext, ApiManager.REGISTER + userBean.getNumber(),false)
                        .build().execute(new BaseCallBack<UserObjBean>(mContext, UserObjBean.class) {
                    @Override
                    public void onResponse(UserObjBean userObjBean) {
                        userBean = userObjBean.getData();
                        tvMoney.setText(userBean.getBalance() + "元");
                        tvNumber.setText(userBean.getNumber());
                        int sex= Integer.parseInt(userBean.getSex());
                        if (sex == 0) {
                            tvSex.setText("女");
                        } else if (sex == 1) {
                            tvSex.setText("男");
                        } else {
                            tvSex.setText("未填写");
                        }
                        String tel=userBean.getTel();
                        if (tel==null||tel.equals("")){
                            tvTel.setText("未填写");
                        }else{
                            tvTel.setText(tel);
                        }
                        String username=userBean.getName();
                        if (username==null||username.equals("")){
                            tvUsername.setText("未填写");
                        }else{
                            tvUsername.setText(username);
                        }
                        if (username==null||username.equals("")){
                            tvUsername.setText("未填写");
                        }else{
                            tvUsername.setText("你好，"+username);
                        }
                        String dong=userBean.getDong();
                        String dan=userBean.getDan();
                        String hao=userBean.getHao();
                        if(dong==null||dong.equals("")){
                            tvMen.setText("未填写");
                        }else{
                            tvMen.setText(dong+"栋"+dan+"单元"+hao+"号");
                        }
                        tvAdd.setVisibility(View.VISIBLE);
                        btnEdit.setVisibility(View.VISIBLE);
                        tvEditPass.setVisibility(View.VISIBLE);
                        btnBtn.setText("退      出");
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mContext != null) {
            setUserVisibleHint(true);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_mine;
    }

    @OnClick({R.id.btn_btn, R.id.tv_add,R.id.btn_edit,R.id.tv_edit_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_btn:
                if (btnBtn.getText().equals("登      陆")) {
                    startActivityForResult(new Intent(mContext, LoginActivity.class), 521);
                } else {
                    // 退出
                    SpUtils.cleanUserBean(mContext);
                    MyApplication.getInstance().setUserBean(null);
                    tvMoney.setText("");
                    tvNumber.setText("");
                    tvSex.setText("");
                    tvTel.setText("");
                    tvUsername.setText("");
                    tvMen.setText("");
                    btnBtn.setText("登      陆");
                    btnEdit.setVisibility(View.GONE);
                    tvAdd.setVisibility(View.GONE);
                    tvEditPass.setVisibility(View.GONE);
                }

                break;
            case R.id.tv_add:
                new ModifyItemDialog(mContext)
                        .setTitle("余额充值")
                        .setMessage("请输入需要充值的金额：")
                        .setCancelClick("取消", null)
                        .setOkClick("确认", new ModifyItemDialog.OnOkClickListener() {
                            @Override
                            public void onOkClick(final String etStr) {
                                if (!TextUtils.isEmpty(etStr)) {
                                    OkHttpTools.sendPut(mContext, ApiManager.RECORD_ADDMONEY + etStr + "/" + userBean.getNumber())
                                            .build().execute(new BaseCallBack<BaseResponse>(mContext, BaseResponse.class) {

                                        @Override
                                        public void onResponse(BaseResponse baseResponse) {
                                            DecimalFormat format = new DecimalFormat("#.00");
                                            userBean.setBalance(format.format(Double.parseDouble(userBean.getBalance()) + Double.parseDouble(etStr)) + "");
                                            ((BaseActivity)mContext).showToast(mContext,baseResponse.getMessage());
                                            tvMoney.setText(userBean.getBalance() + "元");
                                        }
                                    });

                                }

                            }
                        }).show();


                break;
            case R.id.btn_edit:
                Intent intent = new Intent(mContext, EditUserActivity.class);
                startActivityForResult(intent,109);
                break;
            case  R.id.tv_edit_pass:
                startActivity(new Intent(mContext, EditPassActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 521:
                    userBean = MyApplication.getInstance().getUserBean();
                    //更新UI
                    tvMoney.setText(userBean.getBalance() + "元");
                    tvNumber.setText(userBean.getNumber());
                    int sex = Integer.parseInt(userBean.getSex());
                    if (sex == 0) {
                        tvSex.setText("女");
                    } else if (sex == 1) {
                        tvSex.setText("男");
                    } else {
                        tvSex.setText("未填写");
                    }
                    String tel=userBean.getTel();
                    if (tel==null||tel.equals("")){
                        tvTel.setText("未填写");
                    }else{
                        tvTel.setText(tel);
                    }
                    String username=userBean.getName();
                    if (username==null||username.equals("")){
                        tvUsername.setText("未填写");
                    }else{
                        tvUsername.setText("你好，"+username);
                    }
                    String dong=userBean.getDong();
                    String dan=userBean.getDan();
                    String hao=userBean.getHao();
                    if(dong==null||dong.equals("")){
                        tvMen.setText("未填写");
                    }else{
                        tvMen.setText(dong+"栋"+dan+"单元"+hao+"号");
                    }
                    tvAdd.setVisibility(View.VISIBLE);
                    btnEdit.setVisibility(View.VISIBLE);
                    tvEditPass.setVisibility(View.VISIBLE);
                    btnBtn.setText("退      出");
                    break;
                case  109:
                    setUserVisibleHint(true);
                    break;
            }
        }
    }
}
