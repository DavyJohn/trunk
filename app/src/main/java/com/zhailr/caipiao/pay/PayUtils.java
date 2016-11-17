package com.zhailr.caipiao.pay;

import android.app.Activity;

import com.zhailr.caipiao.pay.alipay.Alipay;


/**
 * Created by come on 2016/1/21.
 */
public class PayUtils {

    public static void toPay(Activity activity,PayOrderInfo payOrderInfo ,PayResultListener payResultListener){
        BasePay basePay = null;

        if(payOrderInfo.payType == PayOrderInfo.PayType.ALIPAY)
            basePay = new Alipay(activity, payOrderInfo);


        if(basePay != null)
            basePay.pay(payResultListener);
    }

}
