package com.zhailr.caipiao.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class RegisterResponse implements Parcelable {

    /**
     * code : 404002
     * message : 验证码超时
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        dest.writeString(this.message);
    }

    public RegisterResponse() {
    }

    protected RegisterResponse(Parcel in) {
        this.code = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<RegisterResponse> CREATOR = new Parcelable.Creator<RegisterResponse>() {
        @Override
        public RegisterResponse createFromParcel(Parcel source) {
            return new RegisterResponse(source);
        }

        @Override
        public RegisterResponse[] newArray(int size) {
            return new RegisterResponse[size];
        }
    };
}
