package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/20.
 */
public class NetPaymentResponse {

    /**
     * code : 200
     * data : {"amount":4,"billNo":"2016072015291400000014","status":"0","code":"200"}
     * message : 成功
     */
//    {"code":"200","data":{"amount":2,"code":"200","orderId":"20160801092922000352"},"message":"成功"}
    private String code;
    /**
     * amount : 4
     * billNo : 2016072015291400000014
     * status : 0
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
        private String billNo;
        private String status;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBillNo() {
            return billNo;
        }

        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
