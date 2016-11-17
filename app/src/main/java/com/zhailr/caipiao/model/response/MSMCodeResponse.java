package com.zhailr.caipiao.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class MSMCodeResponse implements Parcelable {

    /**
     * code : 200
     * data :
     * message : 成功
     */

    private String code;
    private String data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.data);
        dest.writeString(this.message);
    }

    public MSMCodeResponse() {
    }

    protected MSMCodeResponse(Parcel in) {
        this.code = in.readString();
        this.data = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<MSMCodeResponse> CREATOR = new Parcelable.Creator<MSMCodeResponse>() {
        @Override
        public MSMCodeResponse createFromParcel(Parcel source) {
            return new MSMCodeResponse(source);
        }

        @Override
        public MSMCodeResponse[] newArray(int size) {
            return new MSMCodeResponse[size];
        }
    };
}
