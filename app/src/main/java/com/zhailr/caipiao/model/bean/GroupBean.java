package com.zhailr.caipiao.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public class GroupBean implements Parcelable {

    /**
     * goodsid :
     * id :
     * ordercount :
     * limitcount :
     * mainimage : picture/0b2a352dd1424be4989be400a8aa1c5e.png
     */

    private String goodsid;
    private String id;
    private String ordercount;
    private String limitcount;
    private String mainimage;

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(String ordercount) {
        this.ordercount = ordercount;
    }

    public String getLimitcount() {
        return limitcount;
    }

    public void setLimitcount(String limitcount) {
        this.limitcount = limitcount;
    }

    public String getMainimage() {
        return mainimage;
    }

    public void setMainimage(String mainimage) {
        this.mainimage = mainimage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goodsid);
        dest.writeString(this.id);
        dest.writeString(this.ordercount);
        dest.writeString(this.limitcount);
        dest.writeString(this.mainimage);
    }

    public GroupBean() {
    }

    protected GroupBean(Parcel in) {
        this.goodsid = in.readString();
        this.id = in.readString();
        this.ordercount = in.readString();
        this.limitcount = in.readString();
        this.mainimage = in.readString();
    }

    public static final Parcelable.Creator<GroupBean> CREATOR = new Parcelable.Creator<GroupBean>() {
        @Override
        public GroupBean createFromParcel(Parcel source) {
            return new GroupBean(source);
        }

        @Override
        public GroupBean[] newArray(int size) {
            return new GroupBean[size];
        }
    };
}
