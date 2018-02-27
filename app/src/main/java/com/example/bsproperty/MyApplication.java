package com.example.bsproperty;

import android.app.Application;

import com.example.bsproperty.bean.UserObjBean;
import com.example.bsproperty.bean.UserBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by yezi on 2018/1/27.
 */

public class MyApplication extends Application {

    private static MyApplication instance;
    private UserBean userBean;
    private static ArrayList<String> outtypes=new ArrayList<>();
    private static ArrayList<String> intypes=new ArrayList<>();
    private static ArrayList<String> accs =new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        outtypes.add(0,"吃饭");
        outtypes.add(1,"看电影");
        outtypes.add(2,"买东西");
        intypes.add(0,"零花钱");
        intypes.add(1,"兼职");
        intypes.add(2,"红包");
        accs.add(0,"现金");
        accs.add(1,"银行卡");
        accs.add(2,"支付宝");
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

    public ArrayList<String> getOuttypes() {
        return outtypes;
    }

    public ArrayList<String> getIntypes() {
        return intypes;
    }

    public ArrayList<String> getAccs() {
        return accs;
    }
}
