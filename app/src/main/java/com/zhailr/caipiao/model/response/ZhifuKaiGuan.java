package com.zhailr.caipiao.model.response;

/**
 * Created by 腾翔信息 on 2017/1/5.
 */

public class ZhifuKaiGuan {

    private String payGoldConfig;

    public String getPayGoldConfig() {
        return payGoldConfig;
    }

    public void setPayGoldConfig(String payGoldConfig) {
        this.payGoldConfig = payGoldConfig;
    }

    @Override
    public String toString() {
        return "ZhifuKaiGuan{" +
                "payGoldConfig='" + payGoldConfig + '\'' +
                '}';
    }
}
