package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.ChargeResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/28.
 */
public class InputCrashActivity extends BaseActivity {
    private static final String TAG = "InputCrashActivity";
    @Bind(R.id.tips)
    TextView tips;
    @Bind(R.id.edit)
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("充值");
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_input_crash;
    }

    @OnClick(R.id.ok)
    public void onClick() {
        if (checkData()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
            map.put("recharge_fee", edit.getText().toString());
            mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.GETNETCHARGEINFO, map, TAG, new SpotsCallBack<ChargeResponse>(mContext, false) {

                @Override
                public void onSuccess(Response response, ChargeResponse res) {
                    if (res.getCode().equals("200")) {
                        Intent intent = new Intent(mContext, SelectChongzhiTypeActivity.class);
                        intent.putExtra("orderId", res.getBillNO());
                        intent.putExtra("billNo", res.getBillNO());
                        intent.putExtra("totalallprice", edit.getText().toString());
                        startActivity(intent);
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
    }

    private boolean checkData() {
        String crash = edit.getText().toString();
        if (StringUtils.isEmpty(crash) || !(Integer.valueOf(crash) > 0)) {
            showToast("请输入正确的金额！");
            return false;
        }
        return true;
    }
}
