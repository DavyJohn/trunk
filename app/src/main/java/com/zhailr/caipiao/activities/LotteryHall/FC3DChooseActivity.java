package com.zhailr.caipiao.activities.LotteryHall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhailiangrong on 16/7/8.
 */
public class FC3DChooseActivity extends BaseActivity {

    @Bind(R.id.tv_normal)
    TextView tvNormal;
    @Bind(R.id.tv_dantuo)
    TextView tvDantuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.fc3d_paly_choose);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_fc3d_choose;
    }


    @OnClick({R.id.normal_choose, R.id.dantuo_choose, R.id.zxhz_choose, R.id.zshz_choose, R.id.zlhz_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.normal_choose:
                startActivity(new Intent(FC3DChooseActivity.this, FC3DNoramlActivity.class));
                break;
            case R.id.dantuo_choose:
                startActivity(new Intent(FC3DChooseActivity.this, FC3DGroupActivity.class));
                break;
            case R.id.zxhz_choose:
                startActivity(new Intent(FC3DChooseActivity.this, FC3DZXHZActivity.class));
                break;
            case R.id.zshz_choose:
                startActivity(new Intent(FC3DChooseActivity.this, FC3DZSHZActivity.class));
                break;
            case R.id.zlhz_choose:
                startActivity(new Intent(FC3DChooseActivity.this, FC3DZLHZActivity.class));
                break;
        }
    }
}
