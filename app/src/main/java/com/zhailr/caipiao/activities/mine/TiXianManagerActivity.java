package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhailiangrong on 16/8/12.
 */
public class TiXianManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.tixian_manager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_tixian_manager;
    }

    @OnClick({R.id.account_choose, R.id.tixian_choose, R.id.search_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_choose:
                startActivity(new Intent(mContext, TiXianAccountManagerActivity.class));
                break;
            case R.id.tixian_choose:
                startActivity(new Intent(mContext, TiXianActivity.class));
                break;
            case R.id.search_choose:
                startActivity(new Intent(mContext, TiXianHistoryListActivity.class));
                break;
        }
    }
}
