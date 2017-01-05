package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.MSMCodeResponse;
import com.zhailr.caipiao.model.response.SmsCodeResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class RegisterTwoActivity extends BaseActivity {
    private static final String TAG = "RegisterTwoActivity";
    @Bind(R.id.register_verification_code_edit)
    EditText registerVerificationCodeEdit;
    @Bind(R.id.register_send_tel)
    TextView registerSendTel;
    @Bind(R.id.get_verification_code_bt)
    TextView getVerificationCodeBt;
    private String tel;
    private TimeCount time = new TimeCount(60000, 1000);//构造CountDownTimer对象;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.get_verification_code_title);
        tel = getIntent().getStringExtra("tel");
        tag = getIntent().getStringExtra("tag");
        registerSendTel.setText(this.getResources().getString(R.string.send_vertificode) + tel.substring(0, 3) + "****" + tel.substring(tel.length() - 4));
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_register_two;
    }

    @OnClick({R.id.get_verification_code_bt, R.id.register_two_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code_bt:
                getVertificaioncode();
                break;
            case R.id.register_two_next:
                if (StringUtils.isEmpty(registerVerificationCodeEdit.getText()
                        .toString().trim())) {
                    showToast(getString(R.string.vertificode_empty));
                    return;
                } else {
                    checkSMSCode();
                }
//                Intent intent = new Intent(this, RegisterThreeActivity.class);
//                intent.putExtra("identifying", registerVerificationCodeEdit
//                       .getText().toString().trim());
//                intent.putExtra("tel", tel);
//                if (null != tag && tag.equals(getString(R.string.forgot_pwd_title))) {
//                    intent.putExtra("tag", getString(R.string.forgot_pwd_title));
//                }
//                startActivity(intent);
                break;
        }
    }
/*
 *验证验证码是否正确
 */
    private void checkSMSCode() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("phone", tel);
        map.put("sms_code", registerVerificationCodeEdit.getText().toString().trim());
        if (null != tag && tag.equals(getString(R.string.forgot_pwd_title))) {
            map.put("sms_type", "2");
        } else {
            map.put("sms_type", "0");
        }
//        map.put("sms_type", "0");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.VALISMSCODE, map, TAG, new SpotsCallBack<SmsCodeResponse>(mContext) {

            @Override
            public void onSuccess(Response response, SmsCodeResponse SmsCodeResponse) {
                if (SmsCodeResponse.getCode().equals("200")) {//验证码正确跳转
//                    Intent intent = new Intent(RegisterTwoActivity.this, RegisterThreeActivity.class);
                    Intent intent = new Intent(RegisterTwoActivity.this, RegisterFourActivity.class);
                    intent.putExtra("identifying", registerVerificationCodeEdit
                            .getText().toString().trim());
                    intent.putExtra("tel", tel);
                    if (null != tag && tag.equals(getString(R.string.forgot_pwd_title))) {
                        intent.putExtra("tag", getString(R.string.forgot_pwd_title));
                    }
                    startActivity(intent);
                    finish();
                } else {
                    showToast(SmsCodeResponse.getMessage());//验证码错误
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }
/*
 *请求接口发送验证码
 */
    private void getVertificaioncode() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("phone_no", tel);
        // 0为注册，2：修改密码验证码；4为设置支付密码
        if (null != tag && tag.equals(getString(R.string.forgot_pwd_title))) {
            map.put("sms_type", "2");
        } else {
            map.put("sms_type", "0");
        }
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.GETSMSCODE, map, TAG, new SpotsCallBack<MSMCodeResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, MSMCodeResponse msmCodeResponse) {
                if (msmCodeResponse.getCode().equals("200")) {//验证码发送成功
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

/*
 *验证码倒计时
 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            getVerificationCodeBt.setText(getString(R.string.register_re_verification));
            getVerificationCodeBt.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            getVerificationCodeBt.setClickable(false);
            getVerificationCodeBt.setText(mContext.getResources().getString(R.string.left) + (int) millisUntilFinished / 1000 + getString(R.string.millions));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.cancelTag(TAG);
    }
}
