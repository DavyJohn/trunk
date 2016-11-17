package com.zhailr.caipiao.pay;

/**
 * Created by come on 2016/1/21.
 */
public class PayOrderInfo {
    public static enum PayType{
        ALIPAY,
        UNION,
        WEIXIN
    }
    public String orderProductTitle;
    public String orderProductDescribe;
    public String orderProductOrderNumber;
    public String orderRedirctUrl;
    public String orderPrice;
    public PayType payType;
}
