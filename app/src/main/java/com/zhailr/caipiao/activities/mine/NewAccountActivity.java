package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.AccountInfoResponse;
import com.zhailr.caipiao.model.response.UserInfoResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;

import org.w3c.dom.Text;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2016/11/29.
 */

public class NewAccountActivity extends BaseActivity  {
    private static final String TAG = NewAccountActivity.class.getSimpleName();
    @Bind(R.id.new_account_money)
    TextView mText;
    @Bind(R.id.new_account_chongzhi)
    TextView mTextChongzhi;
    @Bind(R.id.new_account_tixian)
    TextView mTextTixian;
    @OnClick(R.id.account_ssq) void ssq(){
        Intent intent = new Intent(mContext,AccountActivity.class);
        intent.putExtra("type","SSQ");
        startActivity(intent);
    }
    @OnClick(R.id.account_fcsd) void  fcsd(){
        Intent intent = new Intent(mContext,AccountActivity.class);
        intent.putExtra("type","FCSD");
        startActivity(intent);
    }
    @OnClick(R.id.account_ks) void ks(){
        Intent intent = new Intent(mContext,AccountActivity.class);
        intent.putExtra("type","KS");
        startActivity(intent);
    }
    @OnClick(R.id.new_account_tixian) void tixian(){
        if (StringUtils.isEmpty(PreferencesUtils.getString(mContext, Constant.USER.REALNAME))) {
            startActivity(new Intent(mContext, PersonInfoActivity.class));
            Toast.makeText(mContext, "请先设置真实姓名和身份证信息", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(mContext, TiXianManagerActivity.class));
        }
    }
    @OnClick(R.id.new_account_chongzhi) void chongzhi(){
        startActivity(new Intent(mContext, InputCrashActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("账户明细");
        getUserData();
    }


    private void getUserData(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Log.e("====userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDUSERSACCOUNTINFO, map, TAG, new SpotsCallBack<AccountInfoResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, AccountInfoResponse res) {
                if (res.getCode().equals("200")) {
                    PreferencesUtils.putString(mContext, Constant.USER.BALANCE, res.getData().getCash_balance());
                    PreferencesUtils.putString(mContext, Constant.USER.GOLD, res.getData().getGold_balance());
                    PreferencesUtils.putString(mContext, Constant.USER.USABLE, res.getData().getAvailable_fee());
                    String balance = res.getData().getCash_balance();
                    if (StringUtils.isNotEmpty(balance)) {
                        mText.setText(balance);
                    }
                } else {
                    Toast.makeText(mContext, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.new_account_layout;
    }

}
