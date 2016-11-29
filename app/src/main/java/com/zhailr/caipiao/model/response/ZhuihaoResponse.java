package com.zhailr.caipiao.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 腾翔信息 on 2016/11/29.
 */

public class ZhuihaoResponse {

    private String appenId;
    private String issue_num;
    private String multiple;
    private String amount;
    private String open_time;

    public String getAppenId() {
        return appenId;
    }

    public void setAppenId(String appenId) {
        this.appenId = appenId;
    }

    public String getIssue_num() {
        return issue_num;
    }

    public void setIssue_num(String issue_num) {
        this.issue_num = issue_num;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }
}
