package com.zhailr.caipiao.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class FCSDRecordResponse {

    /**
     * code : 200
     * data : {"historyFCSdList":[{"issue_num":"2016186","num_one":"6","num_three":"8","num_two":"1","open_time":"2016-07-11 20:32:00"},{"issue_num":"2016185","num_one":"3","num_three":"0","num_two":"8"},{"issue_num":"2016184","num_one":"7","num_three":"3","num_two":"0"},{"issue_num":"2016183","num_one":"6","num_three":"5","num_two":"9","open_time":"2016-07-08 20:32:00"},{"issue_num":"2016182","num_one":"4","num_three":"9","num_two":"8"},{"issue_num":"2016181","num_one":"8","num_three":"9","num_two":"6"},{"issue_num":"2016180","num_one":"6","num_three":"9","num_two":"5"},{"issue_num":"2016179","num_one":"3","num_three":"7","num_two":"8"},{"issue_num":"2016178","num_one":"2","num_three":"4","num_two":"4","open_time":"2016-07-03 20:32:00"},{"issue_num":"2016177","num_one":"1","num_three":"5","num_two":"9","open_time":"2016-07-02 20:32:00"},{"issue_num":"2016176","num_one":"7","num_three":"2","num_two":"1","open_time":"2016-07-01 20:32:00"},{"issue_num":"2016175","num_one":"6","num_three":"1","num_two":"4","open_time":"2016-06-30 20:32:00"},{"issue_num":"2016174","num_one":"0","num_three":"7","num_two":"0","open_time":"2016-06-29 20:34:00"},{"issue_num":"2016162","num_one":"6","num_three":"4","num_two":"3"}]}
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
         * issue_num : 2016186
         * num_one : 6
         * num_three : 8
         * num_two : 1
         * open_time : 2016-07-11 20:32:00
         */

        private List<HistoryFCSdListBean> historyFCSdList;

        public List<HistoryFCSdListBean> getHistoryFCSdList() {
            return historyFCSdList;
        }

        public void setHistoryFCSdList(List<HistoryFCSdListBean> historyFCSdList) {
            this.historyFCSdList = historyFCSdList;
        }

        public static class HistoryFCSdListBean {
            @SerializedName("issue_num")
            private String issueNum;
            @SerializedName("num_one")
            private String numOne;
            @SerializedName("num_three")
            private String numThree;
            @SerializedName("num_two")
            private String numTwo;
            @SerializedName("open_time")
            private String openTime;

            public String getIssueNum() {
                return issueNum;
            }

            public void setIssueNum(String issueNum) {
                this.issueNum = issueNum;
            }

            public String getNumOne() {
                return numOne;
            }

            public void setNumOne(String numOne) {
                this.numOne = numOne;
            }

            public String getNumThree() {
                return numThree;
            }

            public void setNumThree(String numThree) {
                this.numThree = numThree;
            }

            public String getNumTwo() {
                return numTwo;
            }

            public void setNumTwo(String numTwo) {
                this.numTwo = numTwo;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }
        }
    }
}
