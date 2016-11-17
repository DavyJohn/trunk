package com.zhailr.caipiao.activities.LotteryHall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhailiangrong on 16/7/5.
 * 双色球
 */
public class DoubleColorBallChooseActivity extends BaseActivity {

    public static DoubleColorBallChooseActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.double_paly_choose);
        instance = this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_double_color_bal_choose;
    }

    @OnClick({R.id.normal_choose, R.id.dantuo_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normal_choose:
                startActivity(new Intent(DoubleColorBallChooseActivity.this, DoubleColorBallNormalActivity.class));
                break;
            case R.id.dantuo_choose:
                startActivity(new Intent(DoubleColorBallChooseActivity.this, DoubleColorDantuoActivity.class));
                break;
        }
    }
}
