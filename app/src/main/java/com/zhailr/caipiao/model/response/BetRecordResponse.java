package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/15.
 */
public class BetRecordResponse {

    /**
     * code : 200
     * data : {"amount":2,"code":"200","orderId":"20160715114011000921"}
     * message : 成功
     */

    private String code;
    /**
     * amount : 2
     * code : 200
     * orderId : 20160715114011000921
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
        private String amount;
        private String code;
        private String orderId;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
