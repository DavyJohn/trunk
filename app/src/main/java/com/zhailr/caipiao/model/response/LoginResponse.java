package com.zhailr.caipiao.model.response;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class LoginResponse {

    /**
     * code : 200
     * message : 成功
     * data : {"balance":"0","noPay_num":0,"site_id":"1","code":"200","userInfo":{"phoneNum":"17768601406","email":null,"userId":"52","userName":"user_17768601406","realName":"韩欣","type":"P","password":"4QrcOUm6Wau+VuBX8g+IPg=="}}
     */

    private String code;
    private String message;
    /**
     * balance : 0
     * noPay_num : 0
     * site_id : 1
     * code : 200
     * userInfo : {"phoneNum":"17768601406","email":null,"userId":"52","userName":"user_17768601406","realName":"韩欣","type":"P","password":"4QrcOUm6Wau+VuBX8g+IPg=="}
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String balance;
        private int noPay_num;
        private String site_id;
        private String code;
        /**
         * phoneNum : 17768601406
         * email : null
         * userId : 52
         * userName : user_17768601406
         * realName : 韩欣
         * type : P
         * password : 4QrcOUm6Wau+VuBX8g+IPg==
         */

        private UserInfoBean userInfo;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public int getNoPay_num() {
            return noPay_num;
        }

        public void setNoPay_num(int noPay_num) {
            this.noPay_num = noPay_num;
        }

        public String getSite_id() {
            return site_id;
        }

        public void setSite_id(String site_id) {
            this.site_id = site_id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            private String phoneNum;
            private String email;
            private String userId;
            private String userName;
            private String realName;
            private String type;
            private String password;

            public String getPhoneNum() {
                return phoneNum;
            }

            public void setPhoneNum(String phoneNum) {
                this.phoneNum = phoneNum;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }
    }
}
