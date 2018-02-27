package com.example.bsproperty.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.ui.AccSelectActivity;
import com.example.bsproperty.ui.TypeSelectActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.app.TimePickerDialog.*;

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
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_address)
    EditText tvAddress;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.tv_tozhi)
    TextView tvTozhi;
    private ArrayList<String> outtypes = new ArrayList<>();
    private ArrayList<String> intypes = new ArrayList<>();
    private ArrayList<String> accs = new ArrayList<>();


    @Override
    protected void loadData() {
        outtypes = MyApplication.getInstance().getOuttypes();
        intypes = MyApplication.getInstance().getIntypes();
        accs = MyApplication.getInstance().getAccs();
        String[] time = format.format(new Date()).split(" ");
        tvDate.setText(time[0]);
        tvTime.setText(time[1]);
        tvType.setText("支出-" + outtypes.get(0));
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


    @OnClick({R.id.tv_type, R.id.account, R.id.tv_time, R.id.btn_add, R.id.tv_tozhi, R.id.tv_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type:
                startActivityForResult(new Intent(mContext, TypeSelectActivity.class), 521);
                break;
            case R.id.account:
                startActivityForResult(new Intent(mContext, AccSelectActivity.class), 109);
                break;
            case R.id.tv_date:
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int monthOfYear = c.get(Calendar.MONTH);
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear+1 < 10&& dayOfMonth<10) {
                            tvDate.setText(year + "-0" + (monthOfYear+1) + "-0" + dayOfMonth);
                        }else if (monthOfYear+1 >= 10&& dayOfMonth<10){
                            tvDate.setText(year + "-" + (monthOfYear+1) + "-0" + dayOfMonth);
                        }else if(monthOfYear+1 < 10 && dayOfMonth>=10){
                            tvDate.setText(year + "-0" + (monthOfYear+1) + "-" + dayOfMonth);
                        }else {
                            tvDate.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                        }
                    }
                }, year, monthOfYear, dayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.tv_time:
                final Calendar c2 = Calendar.getInstance();
                final int hour = c2.get(Calendar.HOUR_OF_DAY);
                int minute = c2.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay<10&&minute<10){
                        tvTime.setText("0"+hourOfDay + ":0" + minute + ":00");
                        }else if (hourOfDay>=10&&minute<10){
                            tvTime.setText(hourOfDay + ":0" + minute + ":00");
                        }else if (hourOfDay<10&&minute>=10){
                            tvTime.setText("0"+hourOfDay + ":" + minute + ":00");
                        }else{
                            tvTime.setText(hourOfDay + ":" + minute + ":00");
                        }
                    }
                }, hour, minute, true);
                timePickerDialog.show();
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
                    boolean flag = data.getBooleanExtra("flag", false);
                    int pos = data.getIntExtra("pos", 0);
                    if (flag) {
                        tvType.setText("支出-" + outtypes.get(pos));
                        tvType.setTextColor(0xFF68CF6A);
                    } else {
                        tvType.setText("收入-" + intypes.get(pos));
                        tvType.setTextColor(0xFFDE3E2C);
                    }
                    break;
                case 109:
                    int accpos = data.getIntExtra("pos", 0);
                    tvAccount.setText(accs.get(accpos));
                    break;
            }
        }
    }
}
