package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/8/13.
 */
public class ApplyResponse {
    /**
     * code : 200
     * data : {"message":"账户提现申请成功"}
     * message : 成功
     */

    private String code;
    /**
     * message : 账户提现申请成功
     */

    private DataBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @Override
    public String toString() {
        return "ApplyResponse{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
