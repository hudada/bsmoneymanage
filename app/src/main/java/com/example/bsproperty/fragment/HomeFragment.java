package com.example.bsproperty.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.ui.TypeSelectActivity;

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

public class HomeFragment extends BaseFragment {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_value)
    EditText tvValue;
    @BindView(R.id.account)
    TextView tvAccount;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    EditText tvAddress;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.tv_tozhi)
    TextView tvTozhi;
    private ArrayList<String> outtypes=new ArrayList<>();
    private ArrayList<String> intypes=new ArrayList<>();
    private ArrayList<String> accs =new ArrayList<>();



    @Override
    protected void loadData() {
        outtypes = MyApplication.getInstance().getOuttypes();
        intypes= MyApplication.getInstance().getIntypes();
        accs= MyApplication.getInstance().getAccs();
        tvTime.setText(format.format(new Date()));
        tvType.setText("支出-"+outtypes.get(0));
        //收入颜色 0xFFDE3E2C
        tvType.setTextColor(0xFF68CF6A);
        tvAccount.setText(accs.get(0));
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_home;
    }


    @OnClick({R.id.tv_type, R.id.account, R.id.tv_time, R.id.btn_add, R.id.tv_tozhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type:
                startActivityForResult(new Intent(mContext, TypeSelectActivity.class), 521);
                break;
            case R.id.account:
                break;
            case R.id.tv_time:
                break;
            case R.id.btn_add:
                break;
            case R.id.tv_tozhi:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 521:
                    boolean flag=data.getBooleanExtra("flag",false);
                    int pos=data.getIntExtra("pos",0);
                    if (flag){
                        tvType.setText("支出-"+outtypes.get(pos));
                        tvType.setTextColor(0xFF68CF6A);
                    }else{
                        tvType.setText("收入-"+intypes.get(pos));
                        tvType.setTextColor(0xFFDE3E2C);
                    }
                    break;
                case  109:
                    break;
            }
        }
    }
}
