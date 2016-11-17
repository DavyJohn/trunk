package com.zhailr.caipiao.activities.LotteryHall;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.HomeActivity;
import com.zhailr.caipiao.activities.mine.OrderListActivity;
import com.zhailr.caipiao.activities.mine.PayPWDActivity;
import com.zhailr.caipiao.adapter.PayTypeListAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.PayType;
import com.zhailr.caipiao.model.response.AccountInfoResponse;
import com.zhailr.caipiao.model.response.BaseResponse;
import com.zhailr.caipiao.model.response.NetPaymentResponse;
import com.zhailr.caipiao.pay.PayOrderInfo;
import com.zhailr.caipiao.pay.PayResultInfo;
import com.zhailr.caipiao.pay.PayUtils;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.widget.MySecKeyboardView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/14.
 */
public class SelectPayTypeActivity extends BaseActivity {
    private static final String TAG = "SelectPayTypeActivity";
    @Bind(R.id.tv_pay_total)
    TextView tvPayTotal;
    @Bind(R.id.pay_type_list_view)
    ListView payTypeListView;
    @Bind(R.id.edit_pay_pwd)
    EditText editPayPwd;
    private List<PayType> datalist = new ArrayList<PayType>();
    private PayTypeListAdapter adapter;
    private String price;
    private String orderId;
    private KeyboardView mKeyboardView;
    private MySecKeyboardView mMySecKeykeardView;
    private String payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("支付方式");
        getAccountData();
        initUI();
    }

    private void initUI() {
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        price = getIntent().getStringExtra("totalallprice");
        String tvPrice = StringUtils.Double2String(Double.parseDouble(price));
        tvPayTotal.setText("￥" + tvPrice);
        orderId = getIntent().getStringExtra("orderId");
        getAccountData();
//        String balance = PreferencesUtils.getString(mContext, Constant.USER.BALANCE);
//        String gold = PreferencesUtils.getString(mContext, Constant.USER.GOLD);
//        datalist.add(new PayType("0", "支付宝支付", "支付宝安全支付", 1));
//        datalist.add(new PayType("0", "现金支付", "可用余额：" + balance, 1));
//        datalist.add(new PayType("0", "金币支付", "金币余额：" + gold, 1));
        adapter = new PayTypeListAdapter(this, datalist,
                R.layout.adapter_pay_type_choose_item);
        payTypeListView.setAdapter(adapter);
//        getAccountData();
        payTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    payFromZFB();

                } else if (position == 1) {
                    payType = "1";
                    payFromAccount();

                } else if (position == 2) {
                    payType = "2";
                    payFromAccount();

                }

            }


        });
//        if (StringUtils.isEmpty(balance) || StringUtils.isEmpty(gold)) {
//            getAccountData();
//        }
    }

    private void getAccountData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDUSERSACCOUNTINFO, map, TAG, new SpotsCallBack<AccountInfoResponse>(mContext, true) {

            @Override
            public void onSuccess(Response response, AccountInfoResponse res) {
                if (res.getCode().equals("200")) {
                    PreferencesUtils.putString(mContext, Constant.USER.BALANCE, res.getData().getCash_balance());
                    PreferencesUtils.putString(mContext, Constant.USER.GOLD, res.getData().getGold_balance());
                    PreferencesUtils.putString(mContext, Constant.USER.USABLE, res.getData().getAvailable_fee());
                    String balance = res.getData().getCash_balance();
                    String gold = res.getData().getGold_balance();
                    String keyong = res.getData().getAvailable_fee();
                    datalist.clear();
                    datalist.add(new PayType("0", "支付宝支付", "支付宝安全支付", 1));
                    datalist.add(new PayType("0", "现金支付", "可用余额：" + keyong, 1));
                    datalist.add(new PayType("0", "金币支付", "金币余额：" + gold, 1));
                    adapter.setData(datalist);

                } else {
                    Toast.makeText(mContext, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

    private void payFromAccount() {
        // 如果价格小于200，就直接支付
        if (Integer.valueOf(price) < 200) {
            payOrderFromAccount();
        } else {
            // 先判断有无开启支付
            String isPay = PreferencesUtils.getString(mContext, Constant.USER.ISPAY);
            if (!TextUtils.isEmpty(isPay) && isPay.equals("1")) {
                // 如果开启，则验证密码，显示输入框，强制弹出键盘
                editPayPwd.setVisibility(View.VISIBLE);
                editPayPwd.requestFocus();
                mMySecKeykeardView = new MySecKeyboardView(this, editPayPwd, mKeyboardView);
                mMySecKeykeardView.setOnFinishClickListener(new MySecKeyboardView.OnFinishedClickListener() {
                    @Override
                    public void onItemClick() {
                        showDialog();
                        vailUserAccountPWD();
                    }
                });
                mMySecKeykeardView.showKeyboard();

            } else {
                showToast("请先设置支付密码");
                startActivity(new Intent(mContext, PayPWDActivity.class));
            }
        }

    }

    private void vailUserAccountPWD() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("pay_pwd", editPayPwd.getText().toString());
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.VAILUSERACCOUTNPWD, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, final BaseResponse res) {
                if (res.getCode().equals("200")) {
                    payOrderFromAccount();

                } else {
                    dimissDialog();
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                dimissDialog();
                showToast(getString(R.string.request_error));
            }

        });
    }

    private void payOrderFromAccount() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("orderId", orderId);
        map.put("pay_way", payType);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.PAYORDER, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                dimissDialog();
                if (res.getCode().equals("200")) {
                    showToast("支付成功");
                    startActivity(new Intent(SelectPayTypeActivity.this, HomeActivity.class));
                    finish();
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                dimissDialog();
                showToast(getString(R.string.request_error));
            }
        });
    }

    private void payFromZFB() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("orderId", orderId);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.GETNETPAYMENT, map, TAG, new SpotsCallBack<NetPaymentResponse>(mContext) {

            @Override
            public void onSuccess(Response response, final NetPaymentResponse res) {
                if (res.getCode().equals("200")) {

                    PayOrderInfo payOrderInfo = new PayOrderInfo();
                    payOrderInfo.payType = PayOrderInfo.PayType.ALIPAY;
//                    payOrderInfo.orderPrice = "0.01";
                    payOrderInfo.orderPrice = res.getData().getAmount();
                    payOrderInfo.orderProductTitle = "腾翔彩票支付业务";
                    payOrderInfo.orderProductDescribe = "描述内容";
                    payOrderInfo.orderProductOrderNumber = res.getData().getBillNo();

                    PayUtils.toPay(SelectPayTypeActivity.this, payOrderInfo, new PayResultInfo() {
                        @Override
                        public void paySuccess() {
                            super.paySuccess();
                            getSuccessCallBack(res.getData().getAmount(), orderId, "3", res.getData().getBillNo(), "body", "seller_id", "subject", "out_trade_no");

                        }

                        @Override
                        public void payConfirm() {
                            super.payConfirm();
                            System.out.println("支付确认中");
                        }

                        @Override
                        public void payFailure() {
                            super.payFailure();
                            getFailCallBack(res.getData().getAmount(), orderId, "3", res.getData().getBillNo(), "body", "seller_id", "subject", "out_trade_no");
                            System.out.println("支付失败");
                            showToast("支付失败");
                        }
                    });
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
                showToast(getString(R.string.request_error));
            }

        });

    }

    private void getSuccessCallBack(String amount, String orderId, String payWay,String billNo,String desc,String sellerId,String subject,String tradeNo) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        System.out.println(PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("orderId", orderId);
        System.out.println(orderId);
        map.put("total_fee", amount);
        System.out.println(amount);
        map.put("pay_way", payWay);
        System.out.println(payWay);
        map.put("billNo", billNo);
        System.out.println(billNo);
        map.put("desc", desc);
        System.out.println(desc);
        map.put("seller_id", sellerId);
        System.out.println(sellerId);
        map.put("subject", subject);
        System.out.println(subject);
        map.put("trade_no", tradeNo);
        System.out.println(tradeNo);
        Log.e("map",map+"");
        // （支付方式（0：快捷支付，1：现金支付，2：金币支付，3：支付宝支付，4：微信支付））
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.WHOLSALEORDER, map, TAG, new SpotsCallBack<BaseResponse>(mContext) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                if (res.getCode().equals("200")) {
                    System.out.println("支付成功");
                    showToast("支付成功");
                    MyApplication.getInstance().finishAllExceptHome();
                    startActivity(new Intent(SelectPayTypeActivity.this, OrderListActivity.class));
                } else {
                    showToast("支付失败");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }
    private void getFailCallBack(String amount, String orderId, String payWay,String billNo,String desc,String sellerId,String subject,String tradeNo) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        System.out.println(PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("orderId", orderId);
        System.out.println(orderId);
        map.put("total_fee", amount);
        System.out.println(amount);
        map.put("pay_way", payWay);
        System.out.println(payWay);
        map.put("billNo", billNo);
        System.out.println(billNo);
        map.put("desc", desc);
        System.out.println(desc);
        map.put("seller_id", sellerId);
        System.out.println(sellerId);
        map.put("subject", subject);
        System.out.println(subject);
        map.put("trade_no", tradeNo);
        System.out.println(tradeNo);
        Log.e("map",map+"");
        // （支付方式（0：快捷支付，1：现金支付，2：金币支付，3：支付宝支付，4：微信支付））
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.ALIFAILED, map, TAG, new SpotsCallBack<BaseResponse>(mContext) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                if (res.getCode().equals("200")) {
                    System.out.println("支付失败");
                    showToast("支付失败");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_select_pay_type;
    }
}
