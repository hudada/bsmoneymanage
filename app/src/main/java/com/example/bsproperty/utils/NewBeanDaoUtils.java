package com.example.bsproperty.utils;

import android.content.Context;

import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.DaoManager;
import com.example.bsproperty.bean.NewBean;

import java.util.List;

/**
 * Created by John on 2018/2/27.
 */

public class NewBeanDaoUtils {
    private static final String TAG = NewBeanDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public NewBeanDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    public boolean insert(NewBean newBean) {
        boolean flag = false;
        flag = mManager.getDaoSession().getNewBeanDao().insert(newBean) == -1 ? false : true;
        return flag;
    }

    public boolean updateTest(NewBean newBean) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getNewBeanDao().update(newBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteTest(NewBean newBean) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getNewBeanDao().delete(newBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<NewBean> queryAll() {
        return mManager.getDaoSession().getNewBeanDao().loadAll();
    }

    public NewBean queryTestById(long key) {
        return mManager.getDaoSession().load(NewBean.class, key);
    }

    public List<NewBean> queryTestByNativeSql(String sql, String[] conditions) {
        return mManager.getDaoSession().queryRaw(NewBean.class, sql, conditions);
    }
}
