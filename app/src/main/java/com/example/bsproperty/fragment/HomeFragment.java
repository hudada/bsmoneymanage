package com.example.bsproperty.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.ui.AccSelectActivity;
import com.example.bsproperty.ui.TypeSelectActivity;
import com.example.bsproperty.utils.AccBeanDaoUtils;
import com.example.bsproperty.utils.NewBeanDaoUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
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
    private ArrayList<TypeBean> outtypes = new ArrayList<>();
    private ArrayList<TypeBean> intypes = new ArrayList<>();
    private ArrayList<AccBean> accs = new ArrayList<>();
    private TypeBean selectType;
    private AccBean selectAcc;
    boolean flag = true;


    @Override
    protected void loadData() {
        outtypes = MyApplication.getInstance().getOuttypes();
        intypes = MyApplication.getInstance().getIntypes();
        accs = MyApplication.getInstance().getAccs();
        String[] time = format.format(new Date()).split(" ");
        tvDate.setText(time[0]);
        tvTime.setText(time[1]);
        if (outtypes.size() > 0) {
            flag = true;
            tvType.setText("支出-" + outtypes.get(0).getType());
            //收入颜色 0xFFDE3E2C
            tvType.setTextColor(0xFF68CF6A);
            selectType = outtypes.get(0);
        } else {
            tvType.setText("暂无分类");
        }
        if (accs.size() > 0) {
            tvAccount.setText(accs.get(0).getAccount());
            selectAcc = accs.get(0);
        } else {
            tvAccount.setText("暂无账户");
        }
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
                        if (monthOfYear + 1 < 10 && dayOfMonth < 10) {
                            tvDate.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 >= 10 && dayOfMonth < 10) {
                            tvDate.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                        } else if (monthOfYear + 1 < 10 && dayOfMonth >= 10) {
                            tvDate.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } else {
                            tvDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
                        if (hourOfDay < 10 && minute < 10) {
                            tvTime.setText("0" + hourOfDay + ":0" + minute + ":00");
                        } else if (hourOfDay >= 10 && minute < 10) {
                            tvTime.setText(hourOfDay + ":0" + minute + ":00");
                        } else if (hourOfDay < 10 && minute >= 10) {
                            tvTime.setText("0" + hourOfDay + ":" + minute + ":00");
                        } else {
                            tvTime.setText(hourOfDay + ":" + minute + ":00");
                        }
                    }
                }, hour, minute, true);
                timePickerDialog.show();
                break;
            case R.id.btn_add:
                if (TextUtils.isEmpty(tvValue.getText())) {
                    Toast.makeText(mContext, "请输入正确金额！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tvAddress.getText())) {
                    Toast.makeText(mContext, "地址不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                NewBean newBean = new NewBean();
                newBean.setAccId(Long.parseLong(selectAcc.getId().toString()));
                newBean.setAddress(tvAddress.getText().toString());
                DecimalFormat fd=new DecimalFormat("00.00");
                newBean.setMoney(Double.parseDouble(fd.format(Double.parseDouble(tvValue.getText().toString()))));
                newBean.setTypeId(Long.parseLong(selectType.getId().toString()));
                String time = tvDate.getText().toString() + " " + tvTime.getText().toString();
                Log.e("test", time);
                Date d = new Date();
                try {
                    d = format.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newBean.setTime(d.getTime());
                NewBeanDaoUtils newDao = new NewBeanDaoUtils(mContext);
                AccBeanDaoUtils accDao = new AccBeanDaoUtils(mContext);
                boolean suc = newDao.insert(newBean);
                if (suc) {
                    AccBean acc = accDao.queryTestById(newBean.getAccId());
                    if (flag) {
                        acc.setMoney(acc.getMoney() - newBean.getMoney());
                    } else {
                        acc.setMoney(acc.getMoney() + newBean.getMoney());
                    }
                    boolean suc2 = accDao.updateTest(acc);
                    if (suc2) {
                        tvAddress.setText("");
                        tvValue.setText("");
                        String[] time2 = format.format(new Date()).split(" ");
                        tvDate.setText(time2[0]);
                        tvTime.setText(time2[1]);
                        Toast.makeText(mContext, "添加成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "添加失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_tozhi:
                //TODO 支付宝同步
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 521:
                    flag = data.getBooleanExtra("flag", false);
                    int pos = data.getIntExtra("pos", 0);
                    if (flag) {
                        tvType.setText("支出-" + outtypes.get(pos).getType());
                        tvType.setTextColor(0xFF68CF6A);
                        selectType = outtypes.get(pos);
                    } else {
                        tvType.setText("收入-" + intypes.get(pos).getType());
                        tvType.setTextColor(0xFFDE3E2C);
                        selectType = intypes.get(pos);
                    }
                    break;
                case 109:
                    int accpos = data.getIntExtra("pos", 0);
                    tvAccount.setText(accs.get(accpos).getAccount());
                    selectAcc = accs.get(accpos);
                    break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyApplication.getInstance().getTypeList();
    }
}
