package com.zhailr.caipiao.pay;

import android.content.Context;

/**
 * Created by come on 2016/1/21.
 */
public abstract class BasePay {

    protected PayOrderInfo payOrderInfo;
    protected PayResultListener payResultListener;
    protected Context           mContext;

    public BasePay(Context context, PayOrderInfo payOrderInfo){
        mContext = context;
        this.payOrderInfo = payOrderInfo;
    }

    protected void pay(PayResultListener payResult){
        this.payResultListener = payResult;
    }
}
