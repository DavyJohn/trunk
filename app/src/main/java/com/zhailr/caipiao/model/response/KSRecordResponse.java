package com.zhailr.caipiao.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class KSRecordResponse {

    /**
     * code : 200
     * data : {"hitoryQS":[{"issue_num":"20160713040","next_time":"2016-07-13 15:30:02.173","num_one":"1","num_three":"6","num_two":"1","open_time":"2016-07-13 15:10:39","win_number":"1,1,6"}]}
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
         * issue_num : 20160713040
         * next_time : 2016-07-13 15:30:02.173
         * num_one : 1
         * num_three : 6
         * num_two : 1
         * open_time : 2016-07-13 15:10:39
         * win_number : 1,1,6
         */

        private List<HitoryQSBean> hitoryQS;

        public List<HitoryQSBean> getHitoryQS() {
            return hitoryQS;
        }

        public void setHitoryQS(List<HitoryQSBean> hitoryQS) {
            this.hitoryQS = hitoryQS;
        }

        public static class HitoryQSBean {
            @SerializedName("issue_num")
            private String issueNum;
            @SerializedName("next_time")
            private String nextTime;
            @SerializedName("num_one")
            private String numOne;
            @SerializedName("num_three")
            private String numThree;
            @SerializedName("num_two")
            private String numTwo;
            @SerializedName("open_time")
            private String openTime;
            @SerializedName("win_number")
            private String winNumber;
            @SerializedName("lottery_num")
            private String lottery_num;

            public String getLottery_num() {
                return lottery_num;
            }

            public void setLottery_num(String lottery_num) {
                this.lottery_num = lottery_num;
            }

            public String getIssueNum() {
                return issueNum;
            }

            public void setIssueNum(String issueNum) {
                this.issueNum = issueNum;
            }

            public String getNextTime() {
                return nextTime;
            }

            public void setNextTime(String nextTime) {
                this.nextTime = nextTime;
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

            public String getWinNumber() {
                return winNumber;
            }

            public void setWinNumber(String winNumber) {
                this.winNumber = winNumber;
            }
        }
    }
}
