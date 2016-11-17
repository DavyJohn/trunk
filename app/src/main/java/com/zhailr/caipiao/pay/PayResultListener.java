package com.zhailr.caipiao.pay;

/**
 * Created by come on 2016/1/21.
 */
public interface PayResultListener {
    void paySuccess();
    void payFailure();
    void payCancel();
    void payConfirm();
}
