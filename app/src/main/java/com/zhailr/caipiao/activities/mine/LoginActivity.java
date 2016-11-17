package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.HomeActivity;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.LoginResponse;
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
 * Created by zhailiangrong on 16/7/5.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    @Bind(R.id.login_username)
    EditText loginUsername;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.login);
        loginPassword.setKeyListener(new DigitsKeyListener() {
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

    @Override
    public int getLayoutId() {
        return R.layout.ac_login;
    }

    @OnClick({R.id.login_btn, R.id.user_register, R.id.user_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                checkLogin();
                break;
            case R.id.user_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.user_forget_password:
                // 页面复用
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("tag", getString(R.string.forgot_pwd_title));
                startActivity(intent);
                break;
        }
    }
//检验登录的用户名密码是否正确
    private void login(String username, String password) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("phone_no", username);
        map.put("password", password);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.LOGINURL, map, TAG, new SpotsCallBack<LoginResponse>(mContext) {

            @Override
            public void onSuccess(Response response, LoginResponse loginResponse) {
                if (null != loginResponse.getCode() && loginResponse.getCode().equals("200")) {
                    LoginResponse.DataBean.UserInfoBean user = loginResponse.getData().getUserInfo();
                    PreferencesUtils.putString(getApplicationContext(), Constant.USER.USERID, user.getUserId());// 用于判断用户登录状态
                    PreferencesUtils.putString(getApplicationContext(), Constant.USER.SITEID, loginResponse.getData().getSite_id());
                    PreferencesUtils.putString(getApplicationContext(), Constant.USER.USERNAME, loginResponse.getData().getUserInfo().getUserName());
                    PreferencesUtils.putString(getApplicationContext(), Constant.USER.BALANCE, loginResponse.getData().getBalance());
                    PreferencesUtils.putString(getApplicationContext(), Constant.USER.PHONENUM, loginResponse.getData().getUserInfo().getPhoneNum());
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(loginResponse.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

    private void checkLogin() {
        CommonUtil.hideInputMethod(this, loginUsername.getWindowToken());
        CommonUtil.hideInputMethod(this, loginPassword.getWindowToken());
        String username = loginUsername.getText().toString().trim();
        try {
            username = URLEncoder.encode(username, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        if (TextUtils.isEmpty(username)) {
            showToast(getString(R.string.register_input_your_tel));
            loginUsername.requestFocus();
            return;
        } else if (!CommonUtil.isMobileNO(username)) {
            showToast(getString(R.string.tel_error));
            return;
        }
        String password = loginPassword.getText().toString().trim();
        try {
            password = URLEncoder.encode(password, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        if (TextUtils.isEmpty(password)) {
            showToast(getString(R.string.please_input_login_pwd));
            loginPassword.requestFocus();
            return;
        }
        if(loginPassword.getText().toString().contains(" ")){
            showToast("密码错误");
            return;
        }else {
            login(username, password);
        }
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
