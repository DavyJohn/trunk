package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.BaseResponse;
import com.zhailr.caipiao.model.response.MSMCodeResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/8/12.
 */
public class AddTixianAccountActivity extends BaseActivity {
    private static final String TAG = "AddTixianAccountActivity";
    @Bind(R.id.edit_code)
    EditText editCode;
    @Bind(R.id.get_verification_code)
    TextView getVerificationCode;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.account)
    EditText account;
    private TimeCount time = new TimeCount(60000, 1000);//构造CountDownTimer对象;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.tixian_account);
        String tel = PreferencesUtils.getString(mContext, Constant.USER.PHONENUM);
        tvPhone.setText("此账号绑定的手机号是" + tel.substring(0, 3) + "****" + tel.substring(tel.length() - 4));
    }

    private void getVertificaioncode() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userid", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("phone_no", PreferencesUtils.getString(mContext, Constant.USER.PHONENUM));
        map.put("sms_type", "3");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.GETSMSPAYCODE, map, TAG, new SpotsCallBack<MSMCodeResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, MSMCodeResponse msmCodeResponse) {
                if (msmCodeResponse.getCode().equals("200")) {
                    showToast("验证码已发送");
                    time.start();//开始计时
                } else {
                    showToast(msmCodeResponse.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void submit() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("sms_code", editCode.getText().toString());
        map.put("phone", PreferencesUtils.getString(mContext, Constant.USER.PHONENUM));
        map.put("sms_type", "3");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.VALISMSCODE, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                if (res.getCode().equals("200")) {
                    if (null != getIntent().getStringExtra("type") && getIntent().getStringExtra("type").equals("0")) {
                        // 新增账户
                        addAccount();
                    }
                    // 修改账户
                    else {
                        changeAccount();
                    }
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast(mContext.getResources().getString(R.string.request_error));
            }
        });

    }

    private void changeAccount() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String param = account.getText().toString().trim();
        try {
            param = URLEncoder.encode(param, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("real_name", PreferencesUtils.getString(mContext, Constant.USER.REALNAME));
        map.put("bank_type", "0");
        map.put("bank_account", param);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.MODWITHDRAWACCOUNT, map, TAG, new SpotsCallBack<String>(mContext, false) {

            @Override
            public void onSuccess(Response response, String s) {
                Log.e("tag",s+"response"+response.toString());
                String code;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        showToast(jsonObject.getString("message"));
                        sendBroadcast(new Intent(Constant.ACCOUNTRECEIVER));
                        finish();
                    } else {
                        showToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast(mContext.getResources().getString(R.string.request_error));
            }
        });
//        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.MODWITHDRAWACCOUNT, map, TAG, new SpotsCallBack<AddAccountResponse>(mContext, false) {
//
//            @Override
//            public void onSuccess(Response response, AddAccountResponse res) {
//                if (res.getCode().equals("200")) {
//                    showToast(res.getData().getMessage());
//                    finish();
//                } else {
//                    showToast(res.getMessage());
//                }
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//                showToast(mContext.getResources().getString(R.string.request_error));
//            }
//        });
    }

    private void addAccount() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String param = account.getText().toString().trim();
        try {
            param = URLEncoder.encode(param, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("real_name", PreferencesUtils.getString(mContext, Constant.USER.REALNAME));
        map.put("acccount_type", "ST");
        map.put("bank_type", "0");
        map.put("bank_account", param);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.ADDWITHDRAWACCOUNT, map, TAG, new SpotsCallBack<String>(mContext, false) {

            @Override
            public void onSuccess(Response response, String s) {
                Log.e("tag",s+"response"+response.toString());
                String code;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        showToast(jsonObject.getString("message"));
                        sendBroadcast(new Intent(Constant.ACCOUNTRECEIVER));
                        finish();
                    } else {
                        showToast(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast(mContext.getResources().getString(R.string.request_error));
            }
        });
//        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.ADDWITHDRAWACCOUNT, map, TAG, new SpotsCallBack<AddAccountResponse>(mContext, false) {
//
//            @Override
//            public void onSuccess(Response response, AddAccountResponse res) {
//                if (res.getCode().equals("200")) {
//                    showToast(res.getData().getMessage());
//                    finish();
//                } else {
//                    showToast(res.getMessage());
//                }
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//                showToast(mContext.getResources().getString(R.string.request_error));
//            }
//        });
    }


    @OnClick({R.id.get_verification_code, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                getVertificaioncode();
                break;
            case R.id.save:
                if (checkData()) {
                    submit();
                }
                break;
        }
    }
    private boolean checkData(){
        String zhanghu = account.getText().toString().trim();
        String yanzhengma = editCode.getText().toString().trim();
        if(TextUtils.isEmpty(zhanghu.trim())){
            showToast("请输入支付宝账户");
            return false;
        }
        if(TextUtils.isEmpty(yanzhengma.trim())){
            showToast("请输入验证码");
            return false;
        }
        return true;
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            getVerificationCode.setText(getString(R.string.register_re_verification));
            getVerificationCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            getVerificationCode.setClickable(false);
            getVerificationCode.setText(mContext.getResources().getString(R.string.left) + (int) millisUntilFinished / 1000 + getString(R.string.millions));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_add_tixian_account;
    }
}
