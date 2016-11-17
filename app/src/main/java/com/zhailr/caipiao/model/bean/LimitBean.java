package com.zhailr.caipiao.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public class LimitBean implements Parcelable {

    /**
     * id :
     * robid :
     * goodsid :
     * goodsname :
     * discountprice :
     * count :
     * limitcount :
     * starttime :
     * endtime :
     * currentTimestamp : 0
     * waitTimestamp : 0
     * mainimage : picture/809d4736336e40beb015b029a2bb797f.jpg
     */

    private String id;
    private String robid;
    private String goodsid;
    private String goodsname;
    private String discountprice;
    private String count;
    private String limitcount;
    private String starttime;
    private String endtime;
    private int currentTimestamp;
    private int waitTimestamp;
    private String mainimage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRobid() {
        return robid;
    }

    public void setRobid(String robid) {
        this.robid = robid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getDiscountprice() {
        return discountprice;
    }

    public void setDiscountprice(String discountprice) {
        this.discountprice = discountprice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLimitcount() {
        return limitcount;
    }

    public void setLimitcount(String limitcount) {
        this.limitcount = limitcount;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(int currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public int getWaitTimestamp() {
        return waitTimestamp;
    }

    public void setWaitTimestamp(int waitTimestamp) {
        this.waitTimestamp = waitTimestamp;
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
        dest.writeString(this.id);
        dest.writeString(this.robid);
        dest.writeString(this.goodsid);
        dest.writeString(this.goodsname);
        dest.writeString(this.discountprice);
        dest.writeString(this.count);
        dest.writeString(this.limitcount);
        dest.writeString(this.starttime);
        dest.writeString(this.endtime);
        dest.writeInt(this.currentTimestamp);
        dest.writeInt(this.waitTimestamp);
        dest.writeString(this.mainimage);
    }

    public LimitBean() {
    }

    protected LimitBean(Parcel in) {
        this.id = in.readString();
        this.robid = in.readString();
        this.goodsid = in.readString();
        this.goodsname = in.readString();
        this.discountprice = in.readString();
        this.count = in.readString();
        this.limitcount = in.readString();
        this.starttime = in.readString();
        this.endtime = in.readString();
        this.currentTimestamp = in.readInt();
        this.waitTimestamp = in.readInt();
        this.mainimage = in.readString();
    }

    public static final Parcelable.Creator<LimitBean> CREATOR = new Parcelable.Creator<LimitBean>() {
        @Override
        public LimitBean createFromParcel(Parcel source) {
            return new LimitBean(source);
        }

        @Override
        public LimitBean[] newArray(int size) {
            return new LimitBean[size];
        }
    };
}
