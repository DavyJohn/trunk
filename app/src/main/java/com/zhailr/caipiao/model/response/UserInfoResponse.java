package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/20.
 */
public class UserInfoResponse {

    /**
     * code : 200
     * email :
     * id_card : 321281199112197633
     * is_pay : 0
     * message : 成功
     * phone_no : 18913616146
     * real_name : 翟良荣
     * userName : 美国队长
     */

    private String code;
    private String email;
    private String id_card;
    private String is_pay;
    private String message;
    private String phone_no;
    private String real_name;
    private String userName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
