package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/22.
 */
public class AccountInfoResponse {

    /**
     * code : 200
     * data : {"cash_balance":"100000","gold_balance":"99978"}
     * message : 成功
     */

    private String code;
    /**
     * cash_balance : 100000
     * gold_balance : 99978
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
        private String cash_balance;
        private String gold_balance;
        private String available_fee;
        public String getCash_balance() {
            return cash_balance;
        }

        public void setCash_balance(String cash_balance) {
            this.cash_balance = cash_balance;
        }

        public String getGold_balance() {
            return gold_balance;
        }

        public void setGold_balance(String gold_balance) {
            this.gold_balance = gold_balance;
        }

        public String getAvailable_fee() {
            return available_fee;
        }

        public void setAvailable_fee(String available_fee) {
            this.available_fee = available_fee;
        }
    }
}
