package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.BaseResponse;
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
 * Created by zhailiangrong on 16/7/27.
 */
public class ChangePWDActivity extends BaseActivity {
    private static final String TAG = "ChangePWDActivity";
    @Bind(R.id.edit_pay_pwd1)
    EditText editPayPwd1;
    @Bind(R.id.edit_pay_pwd2)
    EditText editPayPwd2;
    @Bind(R.id.edit_pay_pwd3)
    EditText editPayPwd3;
    @Bind(R.id.appearpwd1)
    ImageView appearpwd1;
    @Bind(R.id.appearpwd2)
    ImageView appearpwd2;
    @Bind(R.id.appearpwd3)
    ImageView appearpwd3;
    @Bind(R.id.submit)
    Button submit;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("登录密码修改");
        key();
    }


    @OnClick({R.id.appearpwd1, R.id.appearpwd2, R.id.appearpwd3, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appearpwd1:
                flag1 = isAppearPwd(editPayPwd1, appearpwd1, flag1);
                break;
            case R.id.appearpwd2:
                flag2 = isAppearPwd(editPayPwd2, appearpwd2, flag2);
                break;
            case R.id.appearpwd3:
                flag3 = isAppearPwd(editPayPwd3, appearpwd3, flag3);
                break;
            case R.id.submit:
                if (checkData()) {
                    submit();
                }
                break;
        }
    }

    private void submit() {
        // 设置支付密码后，默认开启支付
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String param1 = editPayPwd1.getText().toString().trim();
        String param2 = editPayPwd3.getText().toString().trim();
        try {
            param1 = URLEncoder.encode(param1, "utf-8").replaceAll("%","%");
            param2 = URLEncoder.encode(param2, "utf-8").replaceAll("%","%");
        }catch (Exception e){

        }
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("old_pwd", param1);
        map.put("new_pwd", param2);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.MODUSERLOGINPWD, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, BaseResponse msmCodeResponse) {
                if (msmCodeResponse.getCode().equals("200")) {
                    showToast("登录密码修改成功");
                    finish();
                } else {
                    showToast(msmCodeResponse.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast(mContext.getResources().getString(R.string.request_error));
            }
        });
    }

    private boolean checkData() {
        String pwd1 = editPayPwd1.getText().toString().trim();
        String pwd2 = editPayPwd2.getText().toString().trim();
        String pwd3 = editPayPwd3.getText().toString().trim();
        if (TextUtils.isEmpty(pwd1.trim())) {
            showToast("请在第一个输入框输入旧密码");
            return false;
        }
        if (TextUtils.isEmpty(pwd2.trim())) {
            showToast("请在第二个输入框输入新密码");
            return false;
        }
        if(pwd2.length() < 6||pwd2.length() >16){
            showToast("请输入6-16位的新密码");
            return false;
        }
        if (TextUtils.isEmpty(pwd1.trim())) {
            showToast("请在第三个输入框再次输入新密码");
            return false;
        }
        if (!pwd2.equals(pwd3)) {
            showToast("两次新密码输入不一致");
            return false;
        }
        return true;
    }

    private boolean isAppearPwd(EditText editPayPwd, ImageView appearpwd, boolean isapppwd) {
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
        return isapppwd;
    }
    private void key(){
        editPayPwd1.setKeyListener(new DigitsKeyListener() {
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
        editPayPwd2.setKeyListener(new DigitsKeyListener() {
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
        editPayPwd3.setKeyListener(new DigitsKeyListener() {
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
    public String getStringData(int id) {
        return getResources().getString(id);
    }
    @Override
    public int getLayoutId() {
        return R.layout.ac_change_pwd;
    }
}
