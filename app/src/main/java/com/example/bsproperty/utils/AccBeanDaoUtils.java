package com.example.bsproperty.utils;

import android.content.Context;

import com.example.bsproperty.bean.AccBean;
import com.example.bsproperty.bean.DaoManager;
import com.example.bsproperty.bean.TypeBean;

import java.util.List;

/**
 * Created by John on 2018/2/27.
 */

public class AccBeanDaoUtils {
    private static final String TAG = AccBeanDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public AccBeanDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    public boolean insert(AccBean accBean) {
        boolean flag = false;
        flag = mManager.getDaoSession().getAccBeanDao().insert(accBean) == -1 ? false : true;
        return flag;
    }

    public boolean updateTest(AccBean accBean) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getAccBeanDao().update(accBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteTest(AccBean accBean) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getAccBeanDao().delete(accBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<AccBean> queryAll() {
        return mManager.getDaoSession().getAccBeanDao().loadAll();
    }

    public AccBean queryTestById(long key) {
        return mManager.getDaoSession().load(AccBean.class, key);
    }

    public List<AccBean> queryTestByNativeSql(String sql, String[] conditions) {
        return mManager.getDaoSession().queryRaw(AccBean.class, sql, conditions);
    }
}
