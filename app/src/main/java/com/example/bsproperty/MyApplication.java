package com.example.bsproperty;

import android.app.Application;
import android.util.Log;

import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.TypeBean;
import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.bean.UserBean;
import com.example.bsproperty.utils.AccBeanDaoUtils;
import com.example.bsproperty.utils.SpUtils;
import com.example.bsproperty.utils.TypeBeanDaoUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by yezi on 2018/1/27.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private UserBean userBean;
    private static ArrayList<TypeBean> outtypes = new ArrayList<>();
    private static ArrayList<TypeBean> intypes = new ArrayList<>();
    private static ArrayList<AccBean> accs = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
        getTypeList();
        getAccList();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("hdd"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        OkHttpUtils.initClient(okHttpClient);


    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();

        }
        return instance;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public static ArrayList<TypeBean> getOuttypes() {
        return outtypes;
    }

    public static ArrayList<TypeBean> getIntypes() {
        return intypes;
    }

    public ArrayList<AccBean> getAccs() {
        return accs;
    }

    public void initData() {
        if (SpUtils.getIsFirst(this)) {
            TypeBeanDaoUtils typeDao = new TypeBeanDaoUtils(this);
            TypeBean t1 = new TypeBean();
            t1.setFlag(0);
            t1.setType("吃饭");
            typeDao.insert(t1);
            TypeBean t2 = new TypeBean();
            t2.setFlag(0);
            t2.setType("看电影");
            typeDao.insert(t2);
            TypeBean t3 = new TypeBean();
            t3.setFlag(0);
            t3.setType("买东西");
            typeDao.insert(t3);
            TypeBean t4 = new TypeBean();
            t4.setFlag(1);
            t4.setType("零花钱");
            typeDao.insert(t4);
            TypeBean t5 = new TypeBean();
            t5.setFlag(1);
            t5.setType("红包");
            typeDao.insert(t5);
            TypeBean t6 = new TypeBean();
            t6.setFlag(1);
            t6.setType("兼职");
            typeDao.insert(t6);

            AccBeanDaoUtils accDao = new AccBeanDaoUtils(this);
            AccBean a1 = new AccBean();
            a1.setAccount("现金");
            a1.setMoney(0);
            accDao.insert(a1);
            AccBean a2 = new AccBean();
            a2.setAccount("支付宝");
            a2.setMoney(0);
            accDao.insert(a2);
            AccBean a3 = new AccBean();
            a3.setAccount("银行卡");
            a3.setMoney(0);
            accDao.insert(a3);
            SpUtils.setIsFirst(this, false);
        }
    }

    public void getTypeList() {
        TypeBeanDaoUtils typeDao = new TypeBeanDaoUtils(this);
        List<TypeBean> typeAll = typeDao.queryAll();
        for (TypeBean t : typeAll) {
            if (t.getFlag() == 0) {
                outtypes.add(t);
            } else {
                intypes.add(t);
            }
        }
    }

    public void getAccList() {
        AccBeanDaoUtils accDao = new AccBeanDaoUtils(this);
        List<AccBean> typeAll = accDao.queryAll();
        for (AccBean t : typeAll) {
            accs.add(t);

        }
    }
}

