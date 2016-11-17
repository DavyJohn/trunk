package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.WithdrawAccountResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;


/**
 * Created by zhailiangrong on 16/8/13.
 */
public class TiXianActivity extends BaseActivity {
    private static final String TAG = "TiXianActivity";
    @Bind(R.id.rel_name_tv)
    TextView relNameTv;
    @Bind(R.id.type_tv)
    TextView typeTv;
    @Bind(R.id.num_tv)
    TextView numTv;
    @Bind(R.id.money_tv)
    TextView moneyTv;
    @Bind(R.id.money_et)
    EditText moneyEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.tixian_application);
        moneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
//                    moneyEt.setText("0.00");
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        moneyEt.setText(s);
                        moneyEt.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    moneyEt.setText(s);
                    moneyEt.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        moneyEt.setText(s.subSequence(0, 1));
                        moneyEt.setSelection(1);
                        return;
                    }
                }

        }

        @Override
        public void afterTextChanged (Editable s){

        }
    }
        );

    findAccount();

}

    private void findAccount() {
        // 查询是否有提现账号
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("acccount_type", "ST");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDWITHDRAWACCOUNT, map, TAG, new SpotsCallBack<WithdrawAccountResponse>(mContext, true) {

            @Override
            public void onSuccess(Response response, WithdrawAccountResponse res) {
                WithdrawAccountResponse.DataBean bean = res.getData();
                if (null != bean && bean.getAccount_belong_ty() != null) {
                    relNameTv.setText(bean.getReal_name());
                    numTv.setText(bean.getBank_account());
                    if (bean.getAccount_belong_ty().equals("ST")) {
                        typeTv.setText("支付宝账户");
                    }
                    moneyTv.setText(PreferencesUtils.getString(mContext, Constant.USER.USABLE));
                } else {
                    Intent intent = new Intent(mContext, AddTixianAccountActivity.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast(mContext.getResources().getString(R.string.request_error));
            }
        });
    }


    private boolean checkData() {
        String str = moneyEt.getText().toString();
        if (TextUtils.isEmpty(str)) {
            showToast("请输入金额");
            return false;
        }
        if (!StringUtils.isMoneyNum(str)) {
            showToast("请输入正确的数字");
            return false;
        }
        String oweMoney = PreferencesUtils.getString(mContext, Constant.USER.USABLE);
        if (Double.valueOf(str) > Double.valueOf(oweMoney)) {
            showToast("金额数不能超过可提现金额上限");
            return false;
        }
        return true;
    }

    private void saveData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("amount", moneyEt.getText().toString());
        mOkHttpHelper.post(mContext,Constant.COMMONURL + Constant.APPLYWITHDRAW, map, TAG, new SpotsCallBack<String>(mContext){
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
                Log.e("error",e.toString()+"code"+code+"response"+response.toString());
                showToast(mContext.getResources().getString(R.string.request_error));
            }
        });
     /*   mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.APPLYWITHDRAW, map, TAG, new SpotsCallBack<ApplyResponse>(mContext) {

            @Override
            public void onSuccess(Response response, ApplyResponse res) {
                Log.e("tag",res.toString()+"respones"+response.toString());
                System.out.println(res.toString());
                if (res.getCode() != null) {
                    System.out.println(res.toString());
                    if (res.getCode().equals("200")) {
                        showToast(res.getMessage());
                        sendBroadcast(new Intent(Constant.ACCOUNTRECEIVER));
                        finish();
                    } else {
                        showToast(res.getMessage());
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.e("error",e.toString()+"code"+code+"respones"+response.toString());
                Log.i(TAG, response.toString());
            }

        });*/
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_tixian;
    }

    @OnClick({R.id.clear1, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear1:
                moneyEt.setText("");
                break;
            case R.id.save:
                if (checkData()) {
                    saveData();
                }
                break;
        }
    }
}
