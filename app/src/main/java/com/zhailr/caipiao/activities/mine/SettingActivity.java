package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.HomeActivity;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhailiangrong on 16/7/14.
 */
public class SettingActivity extends BaseActivity implements ISimpleDialogListener, ISimpleDialogCancelListener {

    @Bind(R.id.cache_tv)
    TextView cacheTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.setting);
    }


    @OnClick({R.id.rl_cache, R.id.rl_about, R.id.rl_address, R.id.rl_changepw, R.id.exit_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_cache:
                break;
            case R.id.rl_about:
                break;
            case R.id.rl_address:
                startActivity(new Intent(mContext, SiteListActivity.class));
                break;
            case R.id.rl_changepw:
                startActivity(new Intent(mContext, PWDManageActivity.class));
                break;
            case R.id.exit_confirm:
                showExitDialog();

                break;
        }
    }

    private void showExitDialog() {
        SimpleDialogFragment.createBuilder(SettingActivity.this, getSupportFragmentManager())
                .setTitle(R.string.exit_tip)
                .setMessage("是否确认退出登录？")
                .setPositiveButtonText("确认").setRequestCode(40)
                .setNegativeButtonText(R.string.cancel_button)
                .show();
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == 40) {
            PreferencesUtils.putString(getApplicationContext(), Constant.USER.USERID, "");
            startActivity(new Intent(this, HomeActivity.class));
            MyApplication.getInstance().finishAllExceptHome();
        }
    }

    @Override
    public void onCancelled(int requestCode) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_setting;
    }
}
