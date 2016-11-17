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
 * Created by zhailiangrong on 16/7/11.
 */
public class K3ChooseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle(R.string.k3_play_choose);
        initUI();
    }

    private void initUI() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_k3_choose;
    }

    @OnClick({R.id.layout_he_zhi, R.id.layout_san_tong, R.id.layout_er_tong, R.id.layout_san_bu_tong, R.id.layout_er_bu_tong, R.id.layout_san_bu_tong_dan, R.id.layout_er_bu_tong_dan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_he_zhi:
                startActivity(new Intent(K3ChooseActivity.this, K3HeZhiActivity.class));
                break;
            case R.id.layout_san_tong:
                startActivity(new Intent(K3ChooseActivity.this, K33TongActivity.class));
                break;
            case R.id.layout_er_tong:
                startActivity(new Intent(K3ChooseActivity.this, K32TongActivity.class));
                break;
            case R.id.layout_san_bu_tong:
                startActivity(new Intent(K3ChooseActivity.this, K33BuTongActivity.class));
                break;
            case R.id.layout_er_bu_tong:
                startActivity(new Intent(K3ChooseActivity.this, K32BuTongActivity.class));
                break;
            case R.id.layout_san_bu_tong_dan:
                startActivity(new Intent(K3ChooseActivity.this, K33BuTongDanActivity.class));
                break;
            case R.id.layout_er_bu_tong_dan:
                startActivity(new Intent(K3ChooseActivity.this, K32BuTongDanActivity.class));
                break;
        }
    }
}
