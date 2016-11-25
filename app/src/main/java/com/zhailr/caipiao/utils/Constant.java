package com.zhailr.caipiao.utils;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public class Constant {
    public static final String WANFENURL ="http://wanfen.iask.in/lottery-view-gateway/";
    // 通用地址

//   public static final String COMMONURL = "http://122.194.107.210:8880/gateway/";//测试环境
    //正式环境
    public static final String COMMONURL = "http://192.168.2.4:8807/gateway/";
//    public static final String COMMONURL = "http://121.40.100.15:8807/gateway/";
    public static final String UPDATEURL = "http://122.194.107.210:8880/update/update.xml";
    // 登录成功刷新数据广播
    public static final String LOGINRECEIVER = "com.zhailr.caipiao.loginreceiver";
    // 订单列表刷新广播
    public static final String ORDERLISTRECEIVER = "com.zhailr.caipiao.orderlistreceiver";
    // 用户信息刷新广播
    public static final String USERINFORECEIVER = "com.zhailr.caipiao.userinforeceiver";
    // 账户信息刷新广播
    public static final String ACCOUNTRECEIVER = "com.zhailr.caipiao.accountreceiver";
    // 用户登录地址
                        public static final String LOGINURL = "appUser/UserLoginRequest";
    // 验证手机号唯一性 appUser/CheckPhoneRequest
    public static final String CHECKPHONE = "appUser/CheckPhoneRequest";
    // 用户注册获取验证码
    public static final String GETSMSCODE = "appUser/GetSMSCodeRequest";
    // 用户注册
    public static final String USERREGISTER = "appUser/UserRegisterRequest";
    // 找回密码-验证手机号和验证码
    public static final String SETPWD = "appUser/setpwdRequest";
    // 首页信息
    public static final String HOME = "appLotteryNew/FindNewAwardRecordRequest";
    // 开奖信息-双色球
    public static final String SSQRECORD = "appLotteryHistory/SearchSSQHistoryRecordsRequest";
    // 福彩3D
    public static final String FCSDRECORD = "appLotteryHistory/SearchFCSDHistoryRecordsRequest";
    // K3
    public static final String KSRECORD = "appLotteryHistory/SearchQSHistoryRecordsRequest";

    public static final String SSQRECORDREQUEST = "appRetLotteryOrder/RetSSQOrderRequest";

    public static final String KSRECORDREQUEST = "appRetLotteryOrder/RetKSOrderRequest";

    public static final String FC3DRECORDREQUEST = "appRetLotteryOrder/RetFCSDOrderRequest";
    // 获取投注信息
    public static final String USERLOTTERYLIST = "appUser/SearchUserLotteryListRequest";
    // 订单详情   iosUser/OrderDetailRequest
    public static final String ORDERDETAIL = "appUser/OrderDetailRequest";
    // 站点列表
    public static final String FINDSITELIST = "appUser/FindSiteListRequest";
    // 修改用户信息 appUser/ModUserInfoRequest
    public static final String MODUSERINFO = "appUser/ModUserInfoRequest";
    // 身份信息 appUser/ModUserInformationRequest
    public static final String MODUSERINFOFORMATION = "appUser/ModUserInformationRequest";
    // 获取身份证、是否开启余额支付等 appUser/FindUserSettingInfoRequest
    public static final String FINDUSERSETTINGINFO = "appUser/FindUserSettingInfoRequest";
    // 账号信息 appUser/FindUserAccountInfoRequest
    public static final String FINDUSERSACCOUNTINFO = "appUserAccount/FindUserAccountInfoRequest";
    // 设置支付密码 appUser/SetUserAccountPwdRequest
    public static final String MODUSERLOGINPWD = "appUser/ModUserLoginPwdRequest";
    // 设置支付密码 appUser/SetUserAccountPwdRequest
    public static final String USERACCOUNTPWD = "appUser/SetUserAccountPwdRequest";
    // 支付宝支付 appRetLotteryPay/GetNetPaymentInfoRequest
    public static final String GETNETPAYMENT = "appRetLotteryPay/GetNetPaymentInfoRequest";
    // 取消订单 appUser/CancelingOrderRequest
    public static final String CANCELORDER = "appUser/CancelingOrderRequest";
    // 支付成功回调 appRetLotteryPay/WholsaleOrderNetPayRequest
    public static final String WHOLSALEORDER = "appRetLotteryPay/WholsaleOrderNetPayRequest";
    // 支付失败回调 appRetLotteryPay/AliFailedRequest
    public static final String ALIFAILED = "appRetLotteryPay/AliFailedRequest";
    //充值成功回调 appRetLotteryPay/WholsaleRechargeNetPayRequest
    public static final String WHOLSALERECHARGE = "appRetLotteryPay/WholsaleRechargeNetPayRequest";
    // 账号余额支付 appRetLotteryPay/PayOrderRequest
    public static final String PAYORDER = "appRetLotteryPay/PayOrderRequest";
    // 验证用户账户支付密码 appUserAccount/ValiUserAccountPwdRequest
    public static final String VAILUSERACCOUTNPWD = "appUserAccount/ValiUserAccountPwdRequest";
    // 验证验证码
    public static final String VALISMSCODE = "appUser/IosCheckPhoneRequest";
    // 用户支付获取验证码
    public static final String GETSMSPAYCODE = "appUser/GetSMSCodePayRequest";
    // appUserAccount/SearchUserAccountDetailRequest
    public static final String USERACCOUNTDETAIL = "appUserAccount/SearchUserAccountDetailRequest";
    // 充值
    public static final String GETNETCHARGEINFO = "appRetLotteryPay/GetNetRechargeInfoRequest";
    // 快三倒计时 appLotteryNew/FindNowTime
    public static final String FINDNOWTIME = "appLotteryNew/FindNowTime";
    // 获取当前最新的期号
    public static final String FINDNEWAWARD = "appLotteryNew/FindNewAwardRequest";
    // 获取开奖号码
    public static final String SEARCHHISTORY = "appLotteryHistory/SearchHistoryRequest";
    // 查询提现账户
    public static final String FINDWITHDRAWACCOUNT = "appRetLotteryPay/FindWithdrawAccountInfoRequest";
    // 新增提现账户
    public static final String ADDWITHDRAWACCOUNT = "appRetLotteryPay/AddWithdrawAccountInfoRequest";
    // 修改提现账户
    public static final String MODWITHDRAWACCOUNT = "appRetLotteryPay/ModWithdrawAccountInfoRequest";
    // 提现申请
    public static final String APPLYWITHDRAW = "appRetLotteryPay/ApplyWithDrawRequest";
    // 提现记录
    public static final String SEARCHWITHDRAWRECORDS = "appRetLotteryPay/SearchWithdrawRecordsRequest";



    public class USER {
        public static final String USERNAME = "userName";
        public static final String REALNAME = "real_name";
        public static final String USERID = "userId";
        public static final String SITEID = "siteId";// 站点ID
        public static final String ISPAY = "isPay";// 是否开启支付
        public static final String BALANCE = "balance";// 现金
        public static final String GOLD = "gold";// 金币
        public static final String USABLE = "usable";//可用
        public static final String PHONENUM = "phoneNum";
    }

    public class SSQOrderRequest {
        /**
         * userId :
         * type_code : 彩种编码
         * issue_num : 期号
         * append : 追号期数
         * is_append : 是否追号
         * content : 彩票内容
         * siteId : 站点id
         * multiple : 总倍数
         */
        public static final String USERID = "userId";
        public static final String TYPECODE = "type_code";
        public static final String CHANNEL = "channel";
        public static final String ISSUENUM = "issue_num";
        public static final String APPEND = "append";
        public static final String ISAPPEND = "is_append";
        public static final String CONTENT = "content";
        public static final String SITEID = "siteId";
        public static final String MULTIPLE = "multiple";
    }

    public static  int CHOOSE_STYLE = -1;//0 双色球 1 福彩 2 快三...自己看去吧 写不动了
    public static boolean isClick = false;//写不下去了


    public static int v_line ;//多少行

    public static int isRED = 0; //当 ==0 时展示 红球走势 当==1 时 展示蓝球走势
}
