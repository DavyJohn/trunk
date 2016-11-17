package com.zhailr.caipiao.model.response;

import java.util.List;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public class HomeResponse{


    /**
     * code : 200
     * issue_num : 2016079
     * message : 成功
     * time_draw : 2016-07-10 21:15:00.0
     * type : 双色球
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String code;
        private String issue_num;
        private String message;
        private String time_draw;
        private String type;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getIssue_num() {
            return issue_num;
        }

        public void setIssue_num(String issue_num) {
            this.issue_num = issue_num;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTime_draw() {
            return time_draw;
        }

        public void setTime_draw(String time_draw) {
            this.time_draw = time_draw;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
