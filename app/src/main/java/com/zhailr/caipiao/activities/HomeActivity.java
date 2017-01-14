package com.zhailr.caipiao.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.CartFragment;
import com.zhailr.caipiao.fragments.HomeFragment;
import com.zhailr.caipiao.fragments.MineFragment;
import com.zhailr.caipiao.fragments.SortFragment;
import com.zhailr.caipiao.utils.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhailiangrong on 16/6/10.
 */
public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    @Bind(R.id.flContainer)
    FrameLayout flContainer;
    @Bind(R.id.tabHost)
    FragmentTabHost tabHost;
    @Bind(R.id.rbHome)
    RadioButton rbHome;
    @Bind(R.id.rbSort)
    RadioButton rbSort;
    @Bind(R.id.rbCart)
    RadioButton rbCart;
    @Bind(R.id.rbMine)
    RadioButton rbMine;
    @Bind(R.id.rgTab)
    RadioGroup rgTab;

    private Class[] fragments = {HomeFragment.class, SortFragment.class, CartFragment.class, MineFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        init();
    }

    private void init() {
        getToolBar().setTitle(R.string.home).setDisplayHomeAsUpEnabled(false);
        tabHost.setup(this, getSupportFragmentManager(), R.id.flContainer);
        for (int i = 0; i < fragments.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(i)).setIndicator(String.valueOf(i));
            tabHost.addTab(tabSpec, fragments[i], null);
        }
        tabHost.setCurrentTab(0);
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbHome:
                        hasToolBar(true);
                        tabHost.setCurrentTab(0);
                        getToolBar().setTitle(R.string.home).setDisplayHomeAsUpEnabled(false);
                        break;
                    case R.id.rbSort:
                        hasToolBar(true);
                        tabHost.setCurrentTab(1);
                        getToolBar().setTitle(R.string.oneyuan).setDisplayHomeAsUpEnabled(false);
//                        Toast.makeText(HomeActivity.this, "敬请期待！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbCart:
                        hasToolBar(true);
                        tabHost.setCurrentTab(2);
                        getToolBar().setTitle(R.string.result).setDisplayHomeAsUpEnabled(false);
                        break;
                    case R.id.rbMine:
                        tabHost.setCurrentTab(3);
                        hasToolBar(true);
                        getToolBar().setTitle(R.string.mine).setDisplayHomeAsUpEnabled(false);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            CommonUtil.exitBy2Click(this);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_homepage;

    }
}
