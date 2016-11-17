package com.zhailr.caipiao.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhailiangrong on 16/7/6.
 */
public class BetBean implements Serializable {

    /**
     * redNums :
     * blueNums :
     * type :
     * price :
     */

    private String redNums;
    private String blueNums;
    private String type;
    private String price;
    private String zhu;
    private ArrayList<String> redList;
    private ArrayList<String> redList2;
    private ArrayList<String> redList3;
    private ArrayList<String> blueList;

    public String getRedNums() {
        return redNums;
    }

    public void setRedNums(String redNums) {
        this.redNums = redNums;
    }

    public String getBlueNums() {
        return blueNums;
    }

    public void setBlueNums(String blueNums) {
        this.blueNums = blueNums;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<String> getRedList() {
        return redList;
    }

    public void setRedList(ArrayList<String> mRedList) {
        this.redList = mRedList;
    }

    public ArrayList<String> getBlueList() {
        return blueList;
    }

    public void setBlueList(ArrayList<String> mBlueList) {
        this.blueList = mBlueList;
    }

    public String getZhu() {
        return zhu;
    }

    public void setZhu(String zhu) {
        this.zhu = zhu;
    }

    public ArrayList<String> getRedList2() {
        return redList2;
    }

    public void setRedList2(ArrayList<String> redList2) {
        this.redList2 = redList2;
    }

    public ArrayList<String> getRedList3() {
        return redList3;
    }

    public void setRedList3(ArrayList<String> redList3) {
        this.redList3 = redList3;
    }
}
