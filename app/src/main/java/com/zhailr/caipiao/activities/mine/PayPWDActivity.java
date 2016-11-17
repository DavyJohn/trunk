package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.MSMCodeResponse;
import com.zhailr.caipiao.utils.CommonUtil;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;

import java.net.URLEncoder;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/21.
 */
public class PayPWDActivity extends BaseActivity {
    private static final String TAG = "PayPWDActivity";
    @Bind(R.id.edit_pay_pwd)
    EditText editPayPwd;
    @Bind(R.id.edit_code)
    EditText editCode;
    @Bind(R.id.get_verification_code)
    TextView getVerificationCode;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.appearpwd)
    ImageView appearpwd;
//    @Bind(R.id.submit)
//    Button submit;
    private boolean isapppwd = false;
    private TimeCount time = new TimeCount(60000, 1000);//构造CountDownTimer对象;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("支付密码设置");
        String tel = PreferencesUtils.getString(mContext, Constant.USER.PHONENUM);
        tvPhone.setText("此账号绑定的手机号是" + tel.substring(0, 3) + "****" + tel.substring(tel.length() - 4));
        editPayPwd.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] data = getStringData(R.string.login_only_can_input).toCharArray();
                return data;

            }

        });
    }

    private void isAppearPwd() {
        if (isapppwd) {
            // 设置为密文显示
            editPayPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            CommonUtil.moveCursor2End(editPayPwd);
            appearpwd.setImageResource(R.drawable.hidepwd);
            isapppwd = false;
        } else {
            // 设置为明文显示
            editPayPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            CommonUtil.moveCursor2End(editPayPwd);
            appearpwd.setImageResource(R.drawable.appearpwd);
            isapppwd = true;
        }
    }

    @OnClick({R.id.appearpwd, R.id.get_verification_code, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appearpwd:
                isAppearPwd();
                break;
            case R.id.get_verification_code:
                getVertificaioncode();
                break;
            case R.id.submit:
                if (checkData()) {
                    submit();
                }
                break;
        }
    }

    private boolean checkData() {
        String pwd = editPayPwd.getText().toString().trim();
        try {
            pwd = URLEncoder.encode(pwd, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        if (TextUtils.isEmpty(pwd.trim())) {
            showToast("请输入支付密码");
            return false;
        } else if (TextUtils.isEmpty(editCode.getText().toString().trim())) {
            showToast("请输入验证码");
            return false;
        }
        if (pwd.length() < 6||pwd.length() > 16) {
            showToast("请输入6-16位的支付密码");
            return false;
        }
//            *各种字符的unicode编码的范围：
//            * 数字：[0x30,0x39]（或十进制[48, 57]）
//            *小写字母：[0x61,0x7a]（或十进制[97, 122]）
//            * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (!pwd.matches(regex)) {
            showToast("请输入包含数字与字母的支付密码");
            return false;
        }
        return true;
    }

    private void submit() {
        // 设置支付密码后，默认开启支付
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String pwd = editPayPwd.getText().toString().trim();
        try {
            pwd = URLEncoder.encode(pwd, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("is_pay", "1");
        map.put("paypwd", pwd);
        map.put("sms_code", editCode.getText().toString().trim());
        // 1：修改支付密码验证码；2：修改密码验证码；3：设置支付密码验证码；4找回支付密码验证码
        String isPay = PreferencesUtils.getString(mContext, Constant.USER.ISPAY);
        if (!isPay.equals("1")) {
            map.put("sms_type", "3");
        } else {
            map.put("sms_type", "1");
        }
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.USERACCOUNTPWD, map, TAG, new SpotsCallBack<MSMCodeResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, MSMCodeResponse msmCodeResponse) {
                if (msmCodeResponse.getCode().equals("200")) {
                    PreferencesUtils.putString(mContext, Constant.USER.ISPAY, "1");
                    showToast("支付密码设置成功");
                    finish();
                } else {
                    showToast(msmCodeResponse.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    private void getVertificaioncode() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userid", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("phone_no", PreferencesUtils.getString(mContext, Constant.USER.PHONENUM));
        // 1：修改支付密码验证码；2：修改密码验证码；3：设置支付密码验证码；4找回支付密码验证码
        String isPay = PreferencesUtils.getString(mContext, Constant.USER.ISPAY);
        if (!isPay.equals("1")) {
            map.put("sms_type", "3");
        } else {
            map.put("sms_type", "1");
        }
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
    protected void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.cancelTag(TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_pay_pwd;
    }
    public String getStringData(int id) {
        return getResources().getString(id);
    }
}
