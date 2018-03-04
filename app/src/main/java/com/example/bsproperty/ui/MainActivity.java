package com.example.bsproperty.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.NewBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.broadcastReceiver.SMSBroadcastReceiver;
import com.example.bsproperty.fragment.Fragment02;
import com.example.bsproperty.fragment.Fragment03;
import com.example.bsproperty.fragment.Fragment04;
import com.example.bsproperty.fragment.HomeFragment;
import com.example.bsproperty.utils.AccBeanDaoUtils;
import com.example.bsproperty.utils.NewBeanDaoUtils;
import com.example.bsproperty.utils.SpUtils;
import com.example.bsproperty.utils.TypeBeanDaoUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends BaseActivity {


    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.tb_bottom)
    TabLayout tbBottom;

    private long backTime;
    private HomeFragment homeFragment;
    private Fragment02 fragment02;
    private Fragment03 fragment03;
    private Fragment04 fragment04;
    private ArrayList<Fragment> fragments;
    private MyFragmentPagerAdapter adapter;
    private String[] tabs = new String[]{
            "首  页", "预  算", "明细查询", "设  置"
    };
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private NewBeanDaoUtils newDao;
    private AccBeanDaoUtils accBeanDaoUtils;
    private TypeBeanDaoUtils typeBeanDaoUtils;

    @Override
    protected void initView(Bundle savedInstanceState) {
        newDao = new NewBeanDaoUtils(this);
        accBeanDaoUtils = new AccBeanDaoUtils(this);
        typeBeanDaoUtils = new TypeBeanDaoUtils(this);
        //授权
        PermissionGen.with(this)
                .addRequestCode(521)
                .permissions(Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS
                ).request();


        homeFragment = new HomeFragment();
        fragment02 = new Fragment02();
        fragment03 = new Fragment03();
        fragment04 = new Fragment04();
        fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(fragment02);
        fragments.add(fragment03);
        fragments.add(fragment04);

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);

        tbBottom.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                tbBottom.addTab(tbBottom.newTab().setText(tabs[i]), true);
            } else {
                tbBottom.addTab(tbBottom.newTab().setText(tabs[i]), false);
            }
        }
        tbBottom.setupWithViewPager(vpContent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 521)
    private void smsSucceed() {
        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(1000);
        //注册广播
        registerReceiver(mSMSBroadcastReceiver, intentFilter);
        readSms();
    }

    private void readSms() {

        List<NewBean> beans = newDao.queryTestByNativeSql("order by time desc limit 0,1", new String[]{});
        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = new String[]{"_id", "address", "body", "date"};
        Cursor cur;
        if (beans.size() > 0) {
            String[] key = new String[MyApplication.getInstance().getNumList().size() + 1];
            StringBuffer val = new StringBuffer();
            for (int i = 0; i < MyApplication.getInstance().getNumList().size(); i++) {
                key[i] = MyApplication.getInstance().getNumList().get(i);
                if (i == MyApplication.getInstance().getNumList().size() - 1){
                    val.append(" address=? ) and date>?");
                }else if (i == 0){
                    val.append("( address=? ");
                    val.append("or ");
                }else{
                    val.append(" address=? ");
                    val.append("or ");
                }
            }
            key[key.length - 1] = beans.get(0).getTime() + "";
            cur = getContentResolver().query(uri, projection,
                    val.toString(),
                    key,
                    "date desc");
        } else {
            String[] key = new String[MyApplication.getInstance().getNumList().size()];
            StringBuffer val = new StringBuffer();
            for (int i = 0; i < MyApplication.getInstance().getNumList().size(); i++) {
                key[i] = MyApplication.getInstance().getNumList().get(i);
                if (i == MyApplication.getInstance().getNumList().size() - 1){
                    val.append(" address=? ");
                }else{
                    val.append(" address=? ");
                    val.append("or ");
                }
            }
            cur = getContentResolver().query(uri, projection,
                    val.toString(),
                    key,
                    "date desc");
        }


        if (cur.moveToFirst()) {
            int index_Address = cur.getColumnIndex("address");
            int index_Body = cur.getColumnIndex("body");
            int index_Date = cur.getColumnIndex("date");

            do {
                String strAddress = cur.getString(index_Address);
                String strbody = cur.getString(index_Body);
                long longDate = cur.getLong(index_Date);

                List<AccBean> beans1 = accBeanDaoUtils.queryTestByNativeSql("where account = ?", new String[]{"支付宝"});
                AccBean zfb;
                if (beans1.size() > 0) {
                    zfb = beans1.get(0);
                } else {
                    AccBean a2 = new AccBean();
                    a2.setAccount("支付宝");
                    a2.setMoney(0);
                    accBeanDaoUtils.insert(a2);
                    List<AccBean> beans2 = accBeanDaoUtils.queryTestByNativeSql("where account = ?", new String[]{"支付宝"});
                    zfb = beans2.get(0);
                }

                NewBean newBean = new NewBean();
                newBean.setAccId(zfb.getId());
                newBean.setAddress("支付宝");
                DecimalFormat fd = new DecimalFormat("00.00");

                try {
                    String[] daifu = strbody.split("代付");
                    String money;
                    if (daifu.length > 1) {
                        money = daifu[1].split("元")[0];
                    } else {
                        String[] fukuan = strbody.split("付款");
                        money = fukuan[1].split("元")[0];
                    }

                    newBean.setMoney(Double.parseDouble(fd.format(Double.parseDouble(money))));
                    List<TypeBean> typeBeans = typeBeanDaoUtils.queryTestByNativeSql("where type = ? and flag = 0", new String[]{"支付宝"});
                    TypeBean typeBean;
                    if (typeBeans.size() > 0){
                        typeBean = typeBeans.get(0);
                    }else{
                        TypeBean t7 = new TypeBean();
                        t7.setFlag(0);
                        t7.setType("支付宝");
                        typeBeanDaoUtils.insert(t7);
                        List<TypeBean> typeBeans1 = typeBeanDaoUtils.queryTestByNativeSql("where type = ? and flag = 0", new String[]{"支付宝"});
                        typeBean = typeBeans1.get(0);
                    }
                    newBean.setTypeId(typeBean.getId());
                    newBean.setTime(longDate);
                    boolean suc = newDao.insert(newBean);

                    if (suc) {
                        AccBean acc = accBeanDaoUtils.queryTestById(newBean.getAccId());
                        acc.setMoney(acc.getMoney() - newBean.getMoney());
                        boolean suc2 = accBeanDaoUtils.updateTest(acc);
                    }
                } catch (Exception e) {

                }

            } while (cur.moveToNext());

            if (!cur.isClosed()) {
                cur.close();
                cur = null;
            }
        }
    }

    @PermissionFail(requestCode = 521)
    private void smsError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-权限 中开启短信相关权限，才能自动记录短信消费");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSMSBroadcastReceiver != null) {
            unregisterReceiver(mSMSBroadcastReceiver);
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backTime < 2000) {
            super.onBackPressed();
        } else {
            showToast(this, "再按一次，退出程序");
            backTime = System.currentTimeMillis();
        }
        backTime = System.currentTimeMillis();
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
