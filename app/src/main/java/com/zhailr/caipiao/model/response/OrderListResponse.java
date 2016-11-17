package com.zhailr.caipiao.model.response;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/18.
 */
public class OrderListResponse {

    /**
     * code : 200
     * data : {"lotteryorders":[{"bet_amount":"2","bet_time":"2016-07-18 15:33:04","content":"5201%2|9|2_1","is_append":"0","issue_num":"2016193","orderId":"20160718153304000985","status":"2","type_code":"福彩3D"},{"bet_amount":"0","bet_time":"2016-07-18 14:57:17","content":"5201%7|2|7_1","is_append":"0","issue_num":"2016193","orderId":"20160718145717000982","status":"2","type_code":"福彩3D"},{"bet_amount":"0","bet_time":"2016-07-18 14:53:01","content":"5201%7|5|8_1","is_append":"0","issue_num":"2016193","orderId":"20160718145301000981","status":"2","type_code":"福彩3D"},{"bet_amount":"0","bet_time":"2016-07-18 14:52:20","content":"5201%7|5|8_1","is_append":"0","issue_num":"2016193","orderId":"20160718145220000980","status":"0","type_code":"福彩3D"},{"bet_amount":"2","bet_time":"2016-07-18 14:46:57","content":"5201%0|1|4_1","is_append":"0","issue_num":"2016193","orderId":"20160718144657000979","status":"0","type_code":"福彩3D"},{"bet_amount":"2","bet_time":"2016-07-18 14:43:43","content":"56101%18_1","is_append":"0","issue_num":"160718036","orderId":"20160718144343000978","status":"0"},{"bet_amount":"2","bet_time":"2016-07-18 14:26:10","content":"5201%0|7|9_1","is_append":"0","issue_num":"2016193","orderId":"20160718142610000975","status":"0","type_code":"福彩3D"},{"bet_amount":"2","bet_time":"2016-07-18 13:59:40","content":"5211%0|1_1","is_append":"0","issue_num":"2016193","orderId":"20160718135940000974","status":"0","type_code":"福彩3D"},{"bet_amount":"2","bet_time":"2016-07-18 13:41:23","content":"5001%03,07,09,16,27,30|05_1","is_append":"0","issue_num":"2016083","orderId":"20160718134123000970","status":"0","type_code":"双色球"},{"bet_amount":"20","bet_time":"2016-07-18 13:38:38","content":"5001%05,19,21,22,23,24|13_1^5001%04,05,09,13,15,28|10_1^5001%11,14,15,17,24,28|01_1^5001%02,14,22,23,25,26|13_1^5001%07,15,18,22,25,30|11_1^5001%02,15,21,25,29,31|10_1^5001%01,02,03,11,18,26|02_1^5001%04,05,07,12,16,22|15_1^5001%05,08,10,19,29,30|07_1^5001%02,04,15,18,23,29|03_1","is_append":"0","issue_num":"2016083","orderId":"20160718133838000969","status":"0","type_code":"双色球"}]}
     * message : 成功
     */

    private String code;
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
        /**
         * bet_amount : 2
         * bet_time : 2016-07-18 15:33:04
         * content : 5201%2|9|2_1
         * is_append : 0
         * issue_num : 2016193
         * orderId : 20160718153304000985
         * status : 2
         * type_code : 福彩3D
         */

        private List<LotteryordersBean> lotteryorders;

        public List<LotteryordersBean> getLotteryorders() {
            return lotteryorders;
        }

        public void setLotteryorders(List<LotteryordersBean> lotteryorders) {
            this.lotteryorders = lotteryorders;
        }

        public static class LotteryordersBean {
            private String bet_amount;
            private String bet_time;
            private String content;
            private String is_append;
            private String issue_num;
            private String orderId;
            private String status;
            private String type_code;
            private String is_win;
            private String win_amount;

            public String getBet_amount() {
                return bet_amount;
            }

            public void setBet_amount(String bet_amount) {
                this.bet_amount = bet_amount;
            }

            public String getBet_time() {
                return bet_time;
            }

            public void setBet_time(String bet_time) {
                this.bet_time = bet_time;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIs_append() {
                return is_append;
            }

            public void setIs_append(String is_append) {
                this.is_append = is_append;
            }

            public String getIssue_num() {
                return issue_num;
            }

            public void setIssue_num(String issue_num) {
                this.issue_num = issue_num;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getType_code() {
                return type_code;
            }

            public void setType_code(String type_code) {
                this.type_code = type_code;
            }

            public String getIs_win() {
                return is_win;
            }

            public void setIs_win(String is_win) {
                this.is_win = is_win;
            }

            public String getWin_amount() {
                return win_amount;
            }

            public void setWin_amount(String win_amount) {
                this.win_amount = win_amount;
            }
        }
    }
}
