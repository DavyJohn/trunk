package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/29.
 */
public class ChargeResponse {
    /**
     * billNO : 2016072901350800000061
     * code : 200
     * message : 成功
     */

    private String billNO;
    private String code;
    private String message;

    public String getBillNO() {
        return billNO;
    }

    public void setBillNO(String billNO) {
        this.billNO = billNO;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
