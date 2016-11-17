package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/28.
 */
public class CurrentNumResponse {


    /**
     * code : 200
     * data : {"issue_num":"2016095","system_date":"2016-08-16 14:53:24","time_draw":"2016-08-16 21:15:00.0","time_endsale":"2016-08-16 19:30:00.0","time_startsale":"2016-08-14 19:30:00.0"}
     * message : 成功
     */

    private String code;
    /**
     * issue_num : 2016095
     * system_date : 2016-08-16 14:53:24
     * time_draw : 2016-08-16 21:15:00.0
     * time_endsale : 2016-08-16 19:30:00.0
     * time_startsale : 2016-08-14 19:30:00.0
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
        private String issue_num;
        private String system_date;
        private String time_draw;
        private String time_endsale;
        private String time_startsale;

        public String getIssue_num() {
            return issue_num;
        }

        public void setIssue_num(String issue_num) {
            this.issue_num = issue_num;
        }

        public String getSystem_date() {
            return system_date;
        }

        public void setSystem_date(String system_date) {
            this.system_date = system_date;
        }

        public String getTime_draw() {
            return time_draw;
        }

        public void setTime_draw(String time_draw) {
            this.time_draw = time_draw;
        }

        public String getTime_endsale() {
            return time_endsale;
        }

        public void setTime_endsale(String time_endsale) {
            this.time_endsale = time_endsale;
        }

        public String getTime_startsale() {
            return time_startsale;
        }

        public void setTime_startsale(String time_startsale) {
            this.time_startsale = time_startsale;
        }
    }
}
