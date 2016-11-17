package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/20.
 */
public class BaseResponse {
    /**
     * code : 200
     * data :
     * message : 成功
     */

    private String code;
    private String data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
