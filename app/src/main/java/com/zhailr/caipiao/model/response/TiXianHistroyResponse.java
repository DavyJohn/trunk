package com.zhailr.caipiao.model.response;

import java.util.List;

/**
 * Created by zhailiangrong on 16/8/15.
 */
public class TiXianHistroyResponse {
    /**
     * code : 200
     * data : {"searchWithdrawRecords":[{"applyId":"73","bank_account":"15150557922@163.com","bank_type":"0","billNo":"2016081511055100000269","poundage":".01","status":"2","withdraw_fee":"1","withdraw_time":"2016-08-15 11:05:51"},{"applyId":"72","bank_account":"15150557922@163.com","bank_type":"0","billNo":"2016081316383300000268","poundage":".01","status":"2","withdraw_fee":"1","withdraw_time":"2016-08-13 16:38:33"}],"totalaccount":2}
     * message : 成功
     */

    private String code;
    /**
     * searchWithdrawRecords : [{"applyId":"73","bank_account":"15150557922@163.com","bank_type":"0","billNo":"2016081511055100000269","poundage":".01","status":"2","withdraw_fee":"1","withdraw_time":"2016-08-15 11:05:51"},{"applyId":"72","bank_account":"15150557922@163.com","bank_type":"0","billNo":"2016081316383300000268","poundage":".01","status":"2","withdraw_fee":"1","withdraw_time":"2016-08-13 16:38:33"}]
     * totalaccount : 2
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
        private int totalaccount;
        /**
         * applyId : 73
         * bank_account : 15150557922@163.com
         * bank_type : 0
         * billNo : 2016081511055100000269
         * poundage : .01
         * status : 2
         * withdraw_fee : 1
         * withdraw_time : 2016-08-15 11:05:51
         */
        private List<SearchWithdrawRecordsBean> searchWithdrawRecords;

        public int getTotalaccount() {
            return totalaccount;
        }

        public void setTotalaccount(int totalaccount) {
            this.totalaccount = totalaccount;
        }

        public List<SearchWithdrawRecordsBean> getSearchWithdrawRecords() {
            return searchWithdrawRecords;
        }

        public void setSearchWithdrawRecords(List<SearchWithdrawRecordsBean> searchWithdrawRecords) {
            this.searchWithdrawRecords = searchWithdrawRecords;
        }

        public static class SearchWithdrawRecordsBean {
            private String applyId;
            private String bank_account;
            private String bank_type;
            private String billNo;
            private String poundage;
            private String status;

            public String getApply_time() {
                return apply_time;
            }

            public void setApply_time(String apply_time) {
                this.apply_time = apply_time;
            }

            public String getAmount() {

                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            private String amount;
            private String apply_time;

            public String getApplyId() {
                return applyId;
            }

            public void setApplyId(String applyId) {
                this.applyId = applyId;
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

            public String getBillNo() {
                return billNo;
            }

            public void setBillNo(String billNo) {
                this.billNo = billNo;
            }

            public String getPoundage() {
                return poundage;
            }

            public void setPoundage(String poundage) {
                this.poundage = poundage;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

        }
    }
}
