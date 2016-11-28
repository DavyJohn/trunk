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

            @SerializedName("hundreds_digit0")
            private String hundredsdigit0;
            @SerializedName("hundreds_digit1")
            private String hundredsdigit1;
            @SerializedName("hundreds_digit2")
            private String hundredsdigit2;
            @SerializedName("hundreds_digit3")
            private String hundredsdigit3;
            @SerializedName("hundreds_digit4")
            private String hundredsdigit4;
            @SerializedName("hundreds_digit5")
            private String hundredsdigit5;
            @SerializedName("hundreds_digit6")
            private String hundredsdigit6;
            @SerializedName("hundreds_digit7")
            private String hundredsdigit7;
            @SerializedName("hundreds_digit8")
            private String hundredsdigit8;
            @SerializedName("hundreds_digit9")
            private String hundredsdigit9;

            @SerializedName("single_digit0")
            private String singledigit0;
            @SerializedName("single_digit1")
            private String singledigit1;
            @SerializedName("single_digit2")
            private String singledigit2;
            @SerializedName("single_digit3")
            private String singledigit3;
            @SerializedName("single_digit4")
            private String singledigit4;
            @SerializedName("single_digit5")
            private String singledigit5;
            @SerializedName("single_digit6")
            private String singledigit6;
            @SerializedName("single_digit7")
            private String singledigit7;
            @SerializedName("single_digit8")
            private String singledigit8;
            @SerializedName("single_digit9")
            private String singledigit9;

            @SerializedName("ten_digit0")
            private String tendigit0;
            @SerializedName("ten_digit1")
            private String tendigit1;
            @SerializedName("ten_digit2")
            private String tendigit2;
            @SerializedName("ten_digit3")
            private String tendigit3;
            @SerializedName("ten_digit4")
            private String tendigit4;
            @SerializedName("ten_digit5")
            private String tendigit5;
            @SerializedName("ten_digit6")
            private String tendigit6;
            @SerializedName("ten_digit7")
            private String tendigit7;
            @SerializedName("ten_digit8")
            private String tendigit8;
            @SerializedName("ten_digit9")
            private String tendigit9;

            public String getHundredsdigit0() {
                return hundredsdigit0;
            }

            public void setHundredsdigit0(String hundredsdigit0) {
                this.hundredsdigit0 = hundredsdigit0;
            }

            public String getHundredsdigit1() {
                return hundredsdigit1;
            }

            public void setHundredsdigit1(String hundredsdigit1) {
                this.hundredsdigit1 = hundredsdigit1;
            }

            public String getHundredsdigit2() {
                return hundredsdigit2;
            }

            public void setHundredsdigit2(String hundredsdigit2) {
                this.hundredsdigit2 = hundredsdigit2;
            }

            public String getHundredsdigit3() {
                return hundredsdigit3;
            }

            public void setHundredsdigit3(String hundredsdigit3) {
                this.hundredsdigit3 = hundredsdigit3;
            }

            public String getHundredsdigit4() {
                return hundredsdigit4;
            }

            public void setHundredsdigit4(String hundredsdigit4) {
                this.hundredsdigit4 = hundredsdigit4;
            }

            public String getHundredsdigit5() {
                return hundredsdigit5;
            }

            public void setHundredsdigit5(String hundredsdigit5) {
                this.hundredsdigit5 = hundredsdigit5;
            }

            public String getHundredsdigit6() {
                return hundredsdigit6;
            }

            public void setHundredsdigit6(String hundredsdigit6) {
                this.hundredsdigit6 = hundredsdigit6;
            }

            public String getHundredsdigit7() {
                return hundredsdigit7;
            }

            public void setHundredsdigit7(String hundredsdigit7) {
                this.hundredsdigit7 = hundredsdigit7;
            }

            public String getHundredsdigit8() {
                return hundredsdigit8;
            }

            public void setHundredsdigit8(String hundredsdigit8) {
                this.hundredsdigit8 = hundredsdigit8;
            }

            public String getHundredsdigit9() {
                return hundredsdigit9;
            }

            public void setHundredsdigit9(String hundredsdigit9) {
                this.hundredsdigit9 = hundredsdigit9;
            }

            public String getSingledigit0() {
                return singledigit0;
            }

            public void setSingledigit0(String singledigit0) {
                this.singledigit0 = singledigit0;
            }

            public String getSingledigit1() {
                return singledigit1;
            }

            public void setSingledigit1(String singledigit1) {
                this.singledigit1 = singledigit1;
            }

            public String getSingledigit2() {
                return singledigit2;
            }

            public void setSingledigit2(String singledigit2) {
                this.singledigit2 = singledigit2;
            }

            public String getSingledigit3() {
                return singledigit3;
            }

            public void setSingledigit3(String singledigit3) {
                this.singledigit3 = singledigit3;
            }

            public String getSingledigit4() {
                return singledigit4;
            }

            public void setSingledigit4(String singledigit4) {
                this.singledigit4 = singledigit4;
            }

            public String getSingledigit5() {
                return singledigit5;
            }

            public void setSingledigit5(String singledigit5) {
                this.singledigit5 = singledigit5;
            }

            public String getSingledigit6() {
                return singledigit6;
            }

            public void setSingledigit6(String singledigit6) {
                this.singledigit6 = singledigit6;
            }

            public String getSingledigit7() {
                return singledigit7;
            }

            public void setSingledigit7(String singledigit7) {
                this.singledigit7 = singledigit7;
            }

            public String getSingledigit8() {
                return singledigit8;
            }

            public void setSingledigit8(String singledigit8) {
                this.singledigit8 = singledigit8;
            }

            public String getSingledigit9() {
                return singledigit9;
            }

            public void setSingledigit9(String singledigit9) {
                this.singledigit9 = singledigit9;
            }

            public String getTendigit0() {
                return tendigit0;
            }

            public void setTendigit0(String tendigit0) {
                this.tendigit0 = tendigit0;
            }

            public String getTendigit1() {
                return tendigit1;
            }

            public void setTendigit1(String tendigit1) {
                this.tendigit1 = tendigit1;
            }

            public String getTendigit2() {
                return tendigit2;
            }

            public void setTendigit2(String tendigit2) {
                this.tendigit2 = tendigit2;
            }

            public String getTendigit3() {
                return tendigit3;
            }

            public void setTendigit3(String tendigit3) {
                this.tendigit3 = tendigit3;
            }

            public String getTendigit4() {
                return tendigit4;
            }

            public void setTendigit4(String tendigit4) {
                this.tendigit4 = tendigit4;
            }

            public String getTendigit5() {
                return tendigit5;
            }

            public void setTendigit5(String tendigit5) {
                this.tendigit5 = tendigit5;
            }

            public String getTendigit6() {
                return tendigit6;
            }

            public void setTendigit6(String tendigit6) {
                this.tendigit6 = tendigit6;
            }

            public String getTendigit7() {
                return tendigit7;
            }

            public void setTendigit7(String tendigit7) {
                this.tendigit7 = tendigit7;
            }

            public String getTendigit8() {
                return tendigit8;
            }

            public void setTendigit8(String tendigit8) {
                this.tendigit8 = tendigit8;
            }

            public String getTendigit9() {
                return tendigit9;
            }

            public void setTendigit9(String tendigit9) {
                this.tendigit9 = tendigit9;
            }

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
