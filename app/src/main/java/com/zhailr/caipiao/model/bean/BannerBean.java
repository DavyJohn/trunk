package com.zhailr.caipiao.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public class BannerBean implements Parcelable {


    /**
     * id :
     * type :
     * name :
     * path : picture/19015cb31c80417098129a06b094d2f1.jpg
     * linktype :
     * goodsid : 38
     * goodsname :
     * shelfflag :
     * sort :
     * delflg :
     */

    private String id;
    private String type;
    private String name;
    private String path;
    private String linktype;
    private String goodsid;
    private String goodsname;
    private String shelfflag;
    private String sort;
    private String delflg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLinktype() {
        return linktype;
    }

    public void setLinktype(String linktype) {
        this.linktype = linktype;
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

    public String getShelfflag() {
        return shelfflag;
    }

    public void setShelfflag(String shelfflag) {
        this.shelfflag = shelfflag;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDelflg() {
        return delflg;
    }

    public void setDelflg(String delflg) {
        this.delflg = delflg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.linktype);
        dest.writeString(this.goodsid);
        dest.writeString(this.goodsname);
        dest.writeString(this.shelfflag);
        dest.writeString(this.sort);
        dest.writeString(this.delflg);
    }

    public BannerBean() {
    }

    protected BannerBean(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.name = in.readString();
        this.path = in.readString();
        this.linktype = in.readString();
        this.goodsid = in.readString();
        this.goodsname = in.readString();
        this.shelfflag = in.readString();
        this.sort = in.readString();
        this.delflg = in.readString();
    }

    public static final Parcelable.Creator<BannerBean> CREATOR = new Parcelable.Creator<BannerBean>() {
        @Override
        public BannerBean createFromParcel(Parcel source) {
            return new BannerBean(source);
        }

        @Override
        public BannerBean[] newArray(int size) {
            return new BannerBean[size];
        }
    };
}
