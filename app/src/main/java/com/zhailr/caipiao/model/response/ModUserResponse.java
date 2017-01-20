package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/29.
 */
public class ModUserResponse {
    /**
     * code : 200
     * data : {"code":"200"}
     * message : 成功
     */

    private String code;
    /**
     * code : 200
     */

    private String data;
    private String message;

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCode(String code) {
        this.code = code;
    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public static class DataBean {
//        private String code;
//
//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//    }
}
