package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.OrderListLeftFargment;
import com.zhailr.caipiao.fragments.OrderListRightFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhailiangrong on 16/7/18.
 */
public class OrderListActivity extends BaseActivity  {

    private static final String TAG = "OrderListActivity";

    @Bind(R.id.ac_order_list_vp)
    ViewPager mVp;
    @Bind(R.id.ac_order_list_tab)
    SlidingTabLayout mTab;
    private String[] titles = {"普通记录","追号记录"};

    private List<Fragment> mFragments =  new ArrayList<>();
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("投注记录").setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().finishAllExceptHome();
            }
        });
        initView();
    }

    private void initView(){
        Fragment left = new OrderListLeftFargment();
        Fragment right = new OrderListRightFragment();
        mFragments.add(left);
        mFragments.add(right);

        mVp.setCurrentItem(0);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mVp.setAdapter(pagerAdapter);
        mTab.setViewPager(mVp,titles);
        mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mVp.setCurrentItem(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_order_list;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
