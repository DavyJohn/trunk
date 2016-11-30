package com.zhailr.caipiao.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 腾翔信息 on 2016/11/29.
 */

public class ZhuihaoResponse {

    private String code;
    private DataBean data;

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

    public static class DataBean {
        private List<ZhuihaodetailDataResponse> chaseNumberInfo;

        public List<ZhuihaodetailDataResponse> getChaseNumberInfo() {
            return chaseNumberInfo;
        }

        public void setChaseNumberInfo(List<ZhuihaodetailDataResponse> chaseNumberInfo) {
            this.chaseNumberInfo = chaseNumberInfo;
        }
    }
}
