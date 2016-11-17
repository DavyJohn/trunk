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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setDefaultTextEncodingName("utf-8");
        mWeb.setWebChromeClient(new WebChromeClient(){});
        mWeb.loadUrl("file:///android_asset/kuaisan.html");
    }

    @Override
    public int getLayoutId() {
        return R.layout.webview_layout;
    }
}
