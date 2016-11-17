package com.zhailr.caipiao.model.response;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class SSQRecordResponse {

    /**
     * code : 200
     * data : {"historySsqList":[{"blue_num":"02","issue_num":"2016079","open_time":"2016-07-10(SUNDAY)","red_num1":"01","red_num2":"03","red_num3":"10","red_num4":"12","red_num5":"24","red_num6":"28","total_amount":"12.34"},{"blue_num":"02","issue_num":"2016078","open_time":"2016-07-07(THURSDAY)","red_num1":"02","red_num2":"04","red_num3":"08","red_num4":"23","red_num5":"26","red_num6":"29","total_amount":"0"},{"blue_num":"10","issue_num":"2016077","open_time":"2016-07-05(TUESDAY)","red_num1":"01","red_num2":"09","red_num3":"17","red_num4":"19","red_num5":"20","red_num6":"29","total_amount":"0"},{"blue_num":"10","issue_num":"2016077","open_time":"2016-07-05(TUESDAY)","red_num1":"01","red_num2":"09","red_num3":"17","red_num4":"19","red_num5":"20","red_num6":"29","total_amount":"0"},{"blue_num":"01","issue_num":"2016076","open_time":"2016-07-03(SUNDAY)","red_num1":"07","red_num2":"08","red_num3":"13","red_num4":"22","red_num5":"30","red_num6":"32","total_amount":"0"},{"blue_num":"07","issue_num":"2016075","open_time":"2016-06-30(THURSDAY)","red_num1":"01","red_num2":"03","red_num3":"06","red_num4":"16","red_num5":"29","red_num6":"32","total_amount":"0"},{"blue_num":"12","issue_num":"2016074","open_time":"2016-06-28(TUESDAY)","red_num1":"06","red_num2":"10","red_num3":"11","red_num4":"12","red_num5":"20","red_num6":"25","total_amount":"0"},{"blue_num":"13","issue_num":"2016073","open_time":"2016-06-26(SUNDAY)","red_num1":"09","red_num2":"11","red_num3":"12","red_num4":"15","red_num5":"16","red_num6":"20","total_amount":"0"},{"blue_num":"02","issue_num":"2016072","open_time":"2016-06-23(THURSDAY)","red_num1":"05","red_num2":"16","red_num3":"19","red_num4":"22","red_num5":"24","red_num6":"25","total_amount":"0"},{"blue_num":"05","issue_num":"2016066","open_time":"2016-06-09(THURSDAY)","red_num1":"03","red_num2":"07","red_num3":"13","red_num4":"18","red_num5":"19","red_num6":"20","total_amount":"0"}]}
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
         * blue_num : 02
         * issue_num : 2016079
         * open_time : 2016-07-10(SUNDAY)
         * red_num1 : 01
         * red_num2 : 03
         * red_num3 : 10
         * red_num4 : 12
         * red_num5 : 24
         * red_num6 : 28
         * total_amount : 12.34
         */

        private List<HistorySsqListBean> historySsqList;

        public List<HistorySsqListBean> getHistorySsqList() {
            return historySsqList;
        }

        public void setHistorySsqList(List<HistorySsqListBean> historySsqList) {
            this.historySsqList = historySsqList;
        }

        public static class HistorySsqListBean {
            private String blue_num;
            private String issue_num;
            private String open_time;
            private String red_num1;
            private String red_num2;
            private String red_num3;
            private String red_num4;
            private String red_num5;
            private String red_num6;
            private String total_amount;

            public String getBlue_num() {
                return blue_num;
            }

            public void setBlue_num(String blue_num) {
                this.blue_num = blue_num;
            }

            public String getIssue_num() {
                return issue_num;
            }

            public void setIssue_num(String issue_num) {
                this.issue_num = issue_num;
            }

            public String getOpen_time() {
                return open_time;
            }

            public void setOpen_time(String open_time) {
                this.open_time = open_time;
            }

            public String getRed_num1() {
                return red_num1;
            }

            public void setRed_num1(String red_num1) {
                this.red_num1 = red_num1;
            }

            public String getRed_num2() {
                return red_num2;
            }

            public void setRed_num2(String red_num2) {
                this.red_num2 = red_num2;
            }

            public String getRed_num3() {
                return red_num3;
            }

            public void setRed_num3(String red_num3) {
                this.red_num3 = red_num3;
            }

            public String getRed_num4() {
                return red_num4;
            }

            public void setRed_num4(String red_num4) {
                this.red_num4 = red_num4;
            }

            public String getRed_num5() {
                return red_num5;
            }

            public void setRed_num5(String red_num5) {
                this.red_num5 = red_num5;
            }

            public String getRed_num6() {
                return red_num6;
            }

            public void setRed_num6(String red_num6) {
                this.red_num6 = red_num6;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            @Override
            public String toString() {
                return "HistorySsqListBean{" +
                        "blue_num='" + blue_num + '\'' +
                        ", issue_num='" + issue_num + '\'' +
                        ", open_time='" + open_time + '\'' +
                        ", red_num1='" + red_num1 + '\'' +
                        ", red_num2='" + red_num2 + '\'' +
                        ", red_num3='" + red_num3 + '\'' +
                        ", red_num4='" + red_num4 + '\'' +
                        ", red_num5='" + red_num5 + '\'' +
                        ", red_num6='" + red_num6 + '\'' +
                        ", total_amount='" + total_amount + '\'' +
                        '}';
            }
        }

    }

}
