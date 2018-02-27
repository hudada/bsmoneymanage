package com.example.bsproperty.utils;

import android.content.Context;

import com.example.bsproperty.bean.DaoManager;
import com.example.bsproperty.bean.TypeBean;

import java.util.List;

/**
 * Created by John on 2018/2/27.
 */

public class TypeBeanDaoUtils {
    private static final String TAG = TypeBeanDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public TypeBeanDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    public boolean insert(TypeBean typeBean) {
        boolean flag = false;
        flag = mManager.getDaoSession().getTypeBeanDao().insert(typeBean) == -1 ? false : true;
        return flag;
    }

    public boolean updateTest(TypeBean typeBean) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getTypeBeanDao().update(typeBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteTest(TypeBean typeBean) {
        boolean flag = false;
        try {
            mManager.getDaoSession().getTypeBeanDao().delete(typeBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<TypeBean> queryAll() {
        return mManager.getDaoSession().getTypeBeanDao().loadAll();
    }

    public TypeBean queryTestById(long key) {
        return mManager.getDaoSession().load(TypeBean.class, key);
    }

    public List<TypeBean> queryTestByNativeSql(String sql, String[] conditions) {
        return mManager.getDaoSession().queryRaw(TypeBean.class, sql, conditions);
    }
}
