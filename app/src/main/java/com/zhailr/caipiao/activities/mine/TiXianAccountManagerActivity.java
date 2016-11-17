package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.WithdrawAccountResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/8/12.
 */
public class TiXianAccountManagerActivity extends BaseActivity {
    private static final String TAG = "TiXianAccountManagerActivity";
    @Bind(R.id.rel_name_tv)
    TextView relNameTv;
    @Bind(R.id.type_tv)
    TextView typeTv;
    @Bind(R.id.num_tv)
    TextView numTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.tixian_account_manager);
        FindAccount();
    }

    private void FindAccount() {
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


    @Override
    public int getLayoutId() {
        return R.layout.ac_tixian_account_manager;
    }

    @OnClick(R.id.change)
    public void onClick() {
        Intent intent = new Intent(mContext, AddTixianAccountActivity.class);
        startActivity(intent);
        finishAndTransition(true);
    }
}
