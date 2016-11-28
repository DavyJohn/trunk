package com.zhailr.caipiao.zoushitu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.FCZxTuHundredFragment;
import com.zhailr.caipiao.fragments.FCZxTuTenFragment;
import com.zhailr.caipiao.fragments.FCZxTuUnitFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2016/11/28.
 */

public class FCZxTuActivity extends BaseActivity {

    private static final String TAG = FCZxTuActivity.class.getSimpleName();
    @Bind(R.id.fc_zx_tu_tab)
    SlidingTabLayout mTab;
    @Bind(R.id.fc_zx_tu_vp)
    NoScrollViewPager mVp;

    private String[] mTitles = {"百位","十位","个位"};
    private List<Fragment> listFragment = new ArrayList<>();
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("走势图");
        initView();
    }


    private void initView(){

        mVp.setNoScroll(true);

        Fragment hunder = new FCZxTuHundredFragment();
        Fragment ten = new FCZxTuTenFragment();
        Fragment unit = new FCZxTuUnitFragment();

        listFragment.add(hunder);
        listFragment.add(ten);
        listFragment.add(unit);
        mVp.setCurrentItem(0);

        adapter = new MyPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return listFragment.get(position);
            }

            @Override
            public int getCount() {
                return listFragment.size();
            }
        };
        mVp.setAdapter(adapter);
        mTab.setViewPager(mVp,mTitles);
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
        return R.layout.fc_zx_tu_layout;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }
    }

}
