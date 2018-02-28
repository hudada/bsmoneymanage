package com.example.bsproperty.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.bsproperty.bean.UserBean;
import com.google.gson.Gson;

/**
 * Created by yezi on 2018/1/29.
 */

public class SpUtils {

    private static final String SP_NAME = "sp_name";
//    private static final String ABOUT_USER = "about_user";
    private static final String IS_FIRST = "isFirst";
    private static final String BUDGET="budget";

    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

//    public static void setUserBean(Context context, UserBean userBean) {
//        if (userBean != null) {
//            getSp(context).edit().putString(ABOUT_USER, new Gson().toJson(userBean)).apply();
//        }
//    }

    public static void setIsFirst(Context context, Boolean first) {
            getSp(context).edit().putBoolean(IS_FIRST, first).apply();
    }

    public static void setBudget(Context context, double budget) {
        getSp(context).edit().putString(BUDGET, budget+"").apply();
    }

//    public static UserBean getUserBean(Context context) {
//        String user = getSp(context).getString(ABOUT_USER, "");
//        if (!TextUtils.isEmpty(user)) {
//            return new Gson().fromJson(user, UserBean.class);
//        } else {
//            return null;
//        }
//    }

    public static Boolean getIsFirst(Context context) {
        Boolean first = getSp(context).getBoolean(IS_FIRST, true);
        return first;
    }
    public static double getBudget(Context context) {
        String first = getSp(context).getString(BUDGET,"0");
        return Double.parseDouble(first);
    }

//    public static void cleanUserBean(Context context) {
//        getSp(context).edit().putString(ABOUT_USER, "").apply();
//    }
}
