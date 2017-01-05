package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.BaseResponse;
import com.zhailr.caipiao.model.response.RegisterResponse;
import com.zhailr.caipiao.utils.CommonUtil;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.StringUtils;

import java.net.URLEncoder;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class RegisterThreeActivity extends BaseActivity {
    private static final String TAG = "RegisterThreeActivity";
    @Bind(R.id.user_login_password)
    EditText userLoginPassword;
    @Bind(R.id.appearpwd)
    ImageView appearpwd;
//    @Bind(R.id.register_confirm)
//    Button registerConfirm;
    private String identifying;
    private String tel;
    private String tag;
    private String siteId;
    private boolean isapppwd = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("设置密码");
        identifying = getIntent().getStringExtra("identifying");
        tel = getIntent().getStringExtra("tel");
        tag = getIntent().getStringExtra("tag");
        siteId = getIntent().getStringExtra("siteId");
        userLoginPassword.setKeyListener(new DigitsKeyListener() {
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
        userLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userLoginPassword.getText().length() == 0) {
                    appearpwd.setVisibility(View.GONE);
                } else {
                    appearpwd.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_register_three;
    }

    @OnClick({R.id.appearpwd, R.id.register_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appearpwd:
                isAppearPwd();
                break;
            case R.id.register_confirm:
                if (checkEmpty()) {
                    String password = userLoginPassword.getText().toString();
                    if (password.contains(" ")){
                        showToast("输入密码中不能含有空格");
                    }else {
                        if (null != tag && tag.equals(getString(R.string.forgot_pwd_title))) {
                            // 第二步:验证找回密码的手机号和验证码
                                checkSMSCode();
                        } else {
//                            Intent intent = new Intent(RegisterThreeActivity.this, RegisterFourActivity.class);
//                            intent.putExtra("identifying",identifying);
//                            intent.putExtra("tel",tel);
//                            intent.putExtra("tag",tag);
//                            startActivity(intent);
//                            finish();
                            registerUser();
                        }
                    }
                }
                break;
        }
    }

    private void checkSMSCode() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String param = userLoginPassword.getText().toString().trim();
        try {
            param = URLEncoder.encode(param, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        map.put("phone_no", tel);
        map.put("sms_code", identifying);
        map.put("sms_type", "2");
        map.put("new_pwd", param);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SETPWD, map, TAG, new SpotsCallBack<BaseResponse>(mContext) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                if (res.getCode().equals("200")) {
                        showToast("密码修改成功！");
                        startActivity(new Intent(RegisterThreeActivity.this, LoginActivity.class));
                        finish();
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    private void registerUser() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String param = userLoginPassword.getText().toString().trim();
        try {
            param = URLEncoder.encode(param, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        map.put("phone_no", tel);
        map.put("sms_code", identifying);
        map.put("password", param);
        map.put("siteId",siteId);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.USERREGISTER, map, TAG, new SpotsCallBack<RegisterResponse>(mContext) {

            @Override
            public void onSuccess(Response response, RegisterResponse registerResponse) {
                if (registerResponse.getCode().equals("200")) {
                    showToast("账号注册成功！");
                    startActivity(new Intent(RegisterThreeActivity.this, LoginActivity.class));
                    finish();
                } else {
                    showToast(registerResponse.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }
        });
    }

    private void isAppearPwd() {
        if (isapppwd) {
            // 设置为密文显示
            userLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            CommonUtil.moveCursor2End(userLoginPassword);
            appearpwd.setImageResource(R.drawable.hidepwd);
            isapppwd = false;
        } else {
            // 设置为明文显示
            userLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            CommonUtil.moveCursor2End(userLoginPassword);
            appearpwd.setImageResource(R.drawable.appearpwd);
            isapppwd = true;
        }
    }

    private boolean checkEmpty() {
        boolean checkresult = true;
        String userPassword = userLoginPassword.getText().toString().trim();
        if (StringUtils.isEmpty(userLoginPassword.getText().toString().trim())) {
            showToast(getString(R.string.please_input_login_pwd));
            checkresult = false;
        }else if(userPassword.length() <6||userPassword.length()>16){
            showToast("请输入6-16位的登录密码");
            checkresult = false;
        }
        return checkresult;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.cancelTag(TAG);
    }
    public String getStringData(int id) {
        return getResources().getString(id);
    }
}
