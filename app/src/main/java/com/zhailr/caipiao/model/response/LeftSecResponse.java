package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/28.
 */
public class LeftSecResponse {

    /**
     * code : 200
     * endTime : 201
     * message : 成功
     */

    private String code;
    private int endTime;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
