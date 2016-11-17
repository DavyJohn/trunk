package com.zhailr.caipiao.model.callBack;

import android.util.Log;

import com.google.gson.Gson;
import com.zhailr.caipiao.model.response.BetRecordResponse;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/15.
 */
public abstract class BetRecordCallBack extends Callback<BetRecordResponse> {

    @Override
    public BetRecordResponse parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Log.i("zhlr", string.toString());
        BetRecordResponse gson = new Gson().fromJson(string, BetRecordResponse.class);
        return gson;
    }
}
