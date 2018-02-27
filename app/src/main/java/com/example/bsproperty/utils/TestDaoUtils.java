package com.example.bsproperty.utils;

import android.content.Context;

import com.example.bsproperty.bean.DaoManager;
import com.example.bsproperty.bean.Test;
import com.example.bsproperty.bean.TestDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by John on 2018/2/27.
 */

public class TestDaoUtils {
    private static final String TAG = TestDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public TestDaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    public boolean insertTest(Test test){
        boolean flag = false;
        flag = mManager.getDaoSession().getTestDao().insert(test) == -1 ? false : true;
        return flag;
    }

    public boolean insertMultTest(final List<Test> testList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Test test : testList) {
                        mManager.getDaoSession().insertOrReplace(test);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean updateTest(Test test){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(test);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteTest(Test Test){
        boolean flag = false;
        try {
            mManager.getDaoSession().delete(Test);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteAll(){
        boolean flag = false;
        try {
            mManager.getDaoSession().deleteAll(Test.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    public List<Test> queryAllTest(){
        return mManager.getDaoSession().loadAll(Test.class);
    }

    public Test queryTestById(long key){
        return mManager.getDaoSession().load(Test.class, key);
    }

    public List<Test> queryTestByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(Test.class, sql, conditions);
    }

    public List<Test> queryTestByQueryBuilder(long id){
        QueryBuilder<Test> queryBuilder = mManager.getDaoSession().queryBuilder(Test.class);
        return queryBuilder.where(TestDao.Properties._id.eq(id)).list();
    }
}
