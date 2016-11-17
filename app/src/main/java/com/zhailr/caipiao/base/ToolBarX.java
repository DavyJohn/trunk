package com.zhailr.caipiao.base;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhailr.caipiao.R;

/**
 * Created by zhailiangrong on 16/6/9.
 */
public class ToolBarX {
    private Toolbar mToolBar;
    private AppCompatActivity mActivity;
    private ActionBar mActionBar;
    private RelativeLayout mRlCustom;
    private TextView mTitleView;


    public ToolBarX(Toolbar mToolBar, final AppCompatActivity mActivity) {
        this.mToolBar = mToolBar;
        this.mActivity = mActivity;
        this.mRlCustom = (RelativeLayout) mToolBar.findViewById(R.id.rlCustom);
        this.mActivity.setSupportActionBar(mToolBar);
        mActionBar = mActivity.getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("");
        mTitleView = new TextView(mActivity);
        //标题加粗---------------------------
        TextPaint tp = mTitleView.getPaint();
        tp.setFakeBoldText(true);
        //----------------------------------
        mTitleView.setTextSize(20);
        mTitleView.setTextColor(mActivity.getResources().getColor(R.color.toolbarTextColor));
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(-2, -2);
        params.gravity = Gravity.CENTER;
        mActionBar.setCustomView(mTitleView, params);
        mActionBar.setDisplayShowCustomEnabled(true);
        mToolBar.setNavigationIcon(R.drawable.back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.anim_in_left_right, R.anim.anim_out_left_right);
            }
        });

    }

    public ToolBarX setTitle(String title) {
        mTitleView.setText(title);
        return this;
    }

    public ToolBarX setTitle(int resId) {
        setTitle(mActivity.getString(resId));
        return this;
    }

    public ToolBarX setTitleColor(int color) {
        mTitleView.setTextColor(mActivity.getResources().getColor(color));
        return this;
    }


//    public ToolBarX setSubtitle(String subtitle) {
//        mActionBar.setSubtitle(subtitle);
//        return this;
//    }

//    public ToolBarX setSubtitle(int resId) {
//        mActionBar.setSubtitle(resId);
//        return this;
//    }

    /**
     * 是否可见
     * @param flag
     * @return
     */
    public ToolBarX setDisplayHomeAsUpEnabled(boolean flag) {
        mActionBar.setDisplayHomeAsUpEnabled(flag);
        return this;
    }


    public ToolBarX setNavigationIcon(int resId) {
        mToolBar.setNavigationIcon(resId);
        return this;
    }

    public ToolBarX setNavigationOnClickListener(View.OnClickListener listener) {
        mToolBar.setNavigationOnClickListener(listener);
        return this;
    }

    public ToolBarX setCustomView(View view) {
        mRlCustom.removeAllViews();
        mRlCustom.addView(view);
        return this;
    }


}
