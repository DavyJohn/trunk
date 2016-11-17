package com.zhailr.caipiao.model.response;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/27.
 */
public class SiteListResponse {

    /**
     * code : 200
     * data : {"siteLists":[{"site_id":"1","site_name":"自营","site_no":"001","status":"0","type":"3"}]}
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
         * site_id : 1
         * site_name : 自营
         * site_no : 001
         * status : 0
         * type : 3
         */

        private List<SiteListsBean> siteLists;

        public List<SiteListsBean> getSiteLists() {
            return siteLists;
        }

        public void setSiteLists(List<SiteListsBean> siteLists) {
            this.siteLists = siteLists;
        }

        public static class SiteListsBean {
            private String site_id;
            private String site_name;
            private String site_no;
            private String status;
            private String type;

            public String getSite_id() {
                return site_id;
            }

            public void setSite_id(String site_id) {
                this.site_id = site_id;
            }

            public String getSite_name() {
                return site_name;
            }

            public void setSite_name(String site_name) {
                this.site_name = site_name;
            }

            public String getSite_no() { return site_no; }

            public void setSite_no(String site_no) {
                this.site_no = site_no;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
