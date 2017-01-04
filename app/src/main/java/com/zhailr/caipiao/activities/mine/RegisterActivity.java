package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.BaseResponse;
import com.zhailr.caipiao.utils.CommonUtil;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/5.
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    @Bind(R.id.register_username)
    EditText registerUsername;
    private String mobileNo = "";
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        tag = getIntent().getStringExtra("tag");
        if (null != tag && tag.equals(getString(R.string.forgot_pwd_title))) {
            getToolBar().setTitle(R.string.forgot_pwd_title);
        } else {
            getToolBar().setTitle(R.string.register_title);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_register;
    }

    @OnClick(R.id.register_one_next)
    public void onClick() {
        if (checkMobileNo()) {
            if (null != tag && tag.equals(getString(R.string.forgot_pwd_title))) {
                Intent intent = new Intent(this, RegisterTwoActivity.class);
                intent.putExtra("tag", getString(R.string.forgot_pwd_title));
                intent.putExtra("tel", mobileNo);
                startActivity(intent);
                finish();
            } else {
                // 注册时，验证手机号是否被注册
                checkPhone();
            }

        }

    }

    private void checkPhone() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("phone_no", mobileNo);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.CHECKPHONE, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                if (res.getCode().equals("200")) {//手机号码没有被注册
                    Intent intent = new Intent(mContext, RegisterTwoActivity.class);
                    intent.putExtra("tel", mobileNo);
                    startActivity(intent);
                } else {
                    showToast(res.getMessage());//手机号码已被注册
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private boolean checkMobileNo() {
        boolean checkresult = true;
        mobileNo = registerUsername.getText().toString().trim();
        if (StringUtils.isEmpty(mobileNo)) {//手机号码为空
            showToast(getString(R.string.register_input_your_tel));
            checkresult = false;
        } else if (!CommonUtil.isMobileNO(mobileNo)) {//手机号码格式有误
            showToast(getString(R.string.tel_error));
            checkresult = false;
        }
        return checkresult;
    }

}
