package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;

import butterknife.ButterKnife;

/**
 * Created by fengjiandong on 2016/9/23.
 */
public class AboutActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.about);
    }


    @Override
    public int getLayoutId() {
        return R.layout.ac_about;
    }
}
