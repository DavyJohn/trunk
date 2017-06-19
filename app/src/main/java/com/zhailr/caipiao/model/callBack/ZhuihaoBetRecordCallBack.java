package com.zhailr.caipiao.model.callBack;

import android.util.Log;

import com.google.gson.Gson;
import com.zhailr.caipiao.model.response.ZhuihaoResponse;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2016/11/30.
 */

public abstract class ZhuihaoBetRecordCallBack extends Callback<ZhuihaoResponse> {
    @Override
    public ZhuihaoResponse parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Log.i("zhlr", string.toString());
        ZhuihaoResponse gson = new Gson().fromJson(string, ZhuihaoResponse.class);
        return gson;
    }

}
