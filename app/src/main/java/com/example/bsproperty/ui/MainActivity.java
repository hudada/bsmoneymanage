package com.example.bsproperty.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.bsproperty.MyApplication;
import com.example.bsproperty.R;
import com.example.bsproperty.fragment.Fragment02;
import com.example.bsproperty.fragment.Fragment03;
import com.example.bsproperty.fragment.Fragment04;
import com.example.bsproperty.fragment.HomeFragment;
import com.example.bsproperty.utils.SpUtils;

import java.util.ArrayList;

import butterknife.BindView;

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

    @Override
    protected void initView(Bundle savedInstanceState) {

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
    protected int getRootViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void loadData() {
        MyApplication.getInstance().setUserBean(SpUtils.getUserBean(this));
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
