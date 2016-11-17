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
 * Created by zhailiangrong on 16/7/20.
 */
public class PWDManageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.pwd_manage);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_pwd_manage;
    }

    @OnClick({R.id.rl_pay_pwd, R.id.rl_login_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_pay_pwd:
                startActivity(new Intent(mContext, PayPWDActivity.class));
                break;
            case R.id.rl_login_pwd:
                startActivity(new Intent(mContext, ChangePWDActivity.class));
                break;
        }
    }
}
