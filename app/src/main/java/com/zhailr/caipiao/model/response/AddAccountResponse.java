package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/8/16.
 */
public class AddAccountResponse {
    /**
     * code : 200
     * data : {"message":"提现账户存在未完成的提现申请"}
     * message : 成功
     */

    private String code;
    /**
     * message : 提现账户存在未完成的提现申请
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
}
