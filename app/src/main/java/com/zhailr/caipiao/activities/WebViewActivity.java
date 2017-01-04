package com.zhailr.caipiao.activities;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 腾翔信息 on 2016/11/15.
 */

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.web_view)
    WebView mWeb;
    String tag ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("玩法介绍");
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setDefaultTextEncodingName("utf-8");
        mWeb.setWebChromeClient(new WebChromeClient(){});
        tag = getIntent().getStringExtra("TAG");
        switch (tag){
            case "double_ball_normal_ways":
                mWeb.loadUrl("file:///android_asset/game_double.html");
//                mWeb.loadUrl("file:///android_asset/double_ball_ways.html");
                break;
            case "DANTUO":
                mWeb.loadUrl("file:///android_asset/double_ball_ban_tuo.html");
                break;
            case "FCSD":
                mWeb.loadUrl("file:///android_asset/game_3d.html");
                break;
            case "K3":
                mWeb.loadUrl("file:///android_asset/game_nearly.html");
                break;
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.webview_layout;
    }
}
