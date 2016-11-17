package com.zhailr.caipiao.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public class HotBean implements Parcelable {
    /**
     * goodsId : 42
     * daySell : 8
     * picUrl : picture/1285fd8d69464e94a037f44702dfcf27.jpg
     */

    private int goodsId;
    private String daySell;
    private String picUrl;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getDaySell() {
        return daySell;
    }

    public void setDaySell(String daySell) {
        this.daySell = daySell;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.goodsId);
        dest.writeString(this.daySell);
        dest.writeString(this.picUrl);
    }

    public HotBean() {
    }

    protected HotBean(Parcel in) {
        this.goodsId = in.readInt();
        this.daySell = in.readString();
        this.picUrl = in.readString();
    }

    public static final Parcelable.Creator<HotBean> CREATOR = new Parcelable.Creator<HotBean>() {
        @Override
        public HotBean createFromParcel(Parcel source) {
            return new HotBean(source);
        }

        @Override
        public HotBean[] newArray(int size) {
            return new HotBean[size];
        }
    };
}
