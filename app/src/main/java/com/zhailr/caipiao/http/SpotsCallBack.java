package com.zhailr.caipiao.http;

import android.content.Context;

import com.zhailr.caipiao.widget.WaitProgressDialog;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public abstract class SpotsCallBack<T> extends BaseCallBack<T>{

    private WaitProgressDialog mWaitProgressDialog;
    private boolean isShow;

    public SpotsCallBack(Context context) {
        this(context,true);
    }

    public SpotsCallBack(Context context, boolean flag) {
        mWaitProgressDialog = new WaitProgressDialog(context, "正在加载中...");
        isShow = flag;
    }

    public SpotsCallBack(Context context, boolean flag, String title) {
        mWaitProgressDialog = new WaitProgressDialog(context, title);
        isShow = flag;
    }

    private void showDialog() {
        if (isShow) {
            mWaitProgressDialog.show();
        }
    }

    private void dimissDialog() {
        if (mWaitProgressDialog != null) {
            mWaitProgressDialog.dismiss();
        }
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dimissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dimissDialog();
    }
}
