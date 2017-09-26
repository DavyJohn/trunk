package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.HomeActivity;
import com.zhailr.caipiao.adapter.PayTypeListAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.PayType;
import com.zhailr.caipiao.model.response.BaseResponse;
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
public class SelectChongzhiTypeActivity extends BaseActivity {
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
    private String billNo;
    private KeyboardView mKeyboardView;
    private MySecKeyboardView mMySecKeykeardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("充值方式");

        initUI();
    }

    private void initUI() {
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);

        price = StringUtils.Double2String(Double.parseDouble(getIntent()
                .getStringExtra("totalallprice")));
        tvPayTotal.setText(price);
        orderId = getIntent().getStringExtra("orderId");
        billNo = getIntent().getStringExtra("billNo");
        datalist.add(new PayType("0", "支付宝支付", "支付宝安全支付", 1));
//        datalist.add(new PayType("0", "余额支付", "账号余额安全支付", 1));
        adapter = new PayTypeListAdapter(this, datalist,
                R.layout.adapter_pay_type_choose_item);
        payTypeListView.setAdapter(adapter);
        payTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    PayOrderInfo payOrderInfo = new PayOrderInfo();
                    payOrderInfo.payType = PayOrderInfo.PayType.ALIPAY;
//                    payOrderInfo.orderPrice = "0.01";
                    payOrderInfo.orderPrice = price;
                    payOrderInfo.orderProductTitle = "腾翔彩票充值业务";
                    payOrderInfo.orderProductDescribe = "描述内容";
                    payOrderInfo.orderProductOrderNumber = orderId;

                    PayUtils.toPay(SelectChongzhiTypeActivity.this, payOrderInfo, new PayResultInfo() {
                        @Override
                        public void paySuccess() {
                            super.paySuccess();
                            getSuccessCallBack(price, orderId, "3", "body", "seller_id", "subject", "out_trade_no");

                        }

                        @Override
                        public void payConfirm() {
                            super.payConfirm();
                            System.out.println("支付确认中");
                        }

                        @Override
                        public void payFailure() {
                            super.payFailure();
                            getFailCallBack(price, orderId, "3", "body", "seller_id", "subject", "out_trade_no");
//                            System.out.println("支付失败");
//                            showToast("支付失败");
                        }
                    });
                } else if (position == 1) {
                }

            }


        });
    }


    private void getSuccessCallBack(String amount,String billNo,String payWay,String desc,String sellerId,String subject,String tradeNo) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        System.out.println(PreferencesUtils.getString(mContext, Constant.USER.USERID));
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
        // （支付方式（0：快捷支付，1：现金支付，2：金币支付，3：支付宝支付，4：微信支付））
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.WHOLSALERECHARGE, map, TAG, new SpotsCallBack<BaseResponse>(mContext) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                showToast("充值成功");
                MyApplication.getInstance().finishAllExceptHome();
                startActivity(new Intent(SelectChongzhiTypeActivity.this, HomeActivity.class));
                sendBroadcast(new Intent(Constant.ACCOUNTRECEIVER));
//                if (res.getCode().equals("200")) {
//
//                }
//                else {
//                    showToast("充值失败");
//                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }
    private void getFailCallBack(String amount,String billNo,String payWay,String desc,String sellerId,String subject,String tradeNo) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        System.out.println(PreferencesUtils.getString(mContext, Constant.USER.USERID));
//        map.put("orderId", orderId);
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
        // （支付方式（0：快捷支付，1：现金支付，2：金币支付，3：支付宝支付，4：微信支付））
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.ALIFAILED, map, TAG, new SpotsCallBack<BaseResponse>(mContext) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                System.out.println("支付失败");
                showToast("支付失败");
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
