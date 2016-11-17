package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/8/12.
 */
public class WithdrawAccountResponse {
    /**
     * code : 200
     * data : {"account_belong_ty":"ST","bank_account":"15150557922@163.com","bank_type":"0","real_name":"翟良荣"}
     * message : 成功
     */

    private String code;
    /**
     * account_belong_ty : ST
     * bank_account : 15150557922@163.com
     * bank_type : 0
     * real_name : 翟良荣
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
        private String account_belong_ty;
        private String bank_account;
        private String bank_type;
        private String real_name;

        public String getAccount_belong_ty() {
            return account_belong_ty;
        }

        public void setAccount_belong_ty(String account_belong_ty) {
            this.account_belong_ty = account_belong_ty;
        }

        public String getBank_account() {
            return bank_account;
        }

        public void setBank_account(String bank_account) {
            this.bank_account = bank_account;
        }

        public String getBank_type() {
            return bank_type;
        }

        public void setBank_type(String bank_type) {
            this.bank_type = bank_type;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }
    }
}
