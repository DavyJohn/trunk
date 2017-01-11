package com.zhailr.caipiao.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.mine.AboutActivity;
import com.zhailr.caipiao.activities.mine.AccountActivity;
import com.zhailr.caipiao.activities.mine.InputCrashActivity;
import com.zhailr.caipiao.activities.mine.LoginActivity;
import com.zhailr.caipiao.activities.mine.NewAccountActivity;
import com.zhailr.caipiao.activities.mine.OrderListActivity;
import com.zhailr.caipiao.activities.mine.PersonInfoActivity;
import com.zhailr.caipiao.activities.mine.RegisterActivity;
import com.zhailr.caipiao.activities.mine.SettingActivity;
import com.zhailr.caipiao.activities.mine.TiXianManagerActivity;
import com.zhailr.caipiao.base.BaseFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.AccountInfoResponse;
import com.zhailr.caipiao.model.response.UserInfoResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.widget.TZCPPullRefresh;
import com.zhailr.caipiao.widget.pullableview.PullToRefreshLayout;
import com.zhailr.caipiao.widget.pullableview.PullableScrollView;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "MineFragment";
    @Bind(R.id.no_login)
    ViewStub noLogin;
    @Bind(R.id.user_head)
    ImageView userHead;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.account_yu_e)
    TextView accountYue;
    @Bind(R.id.scrollView)
    PullableScrollView scrollView;
    @Bind(R.id.refresh_view)
    TZCPPullRefresh refreshView;
    @Bind(R.id.account_jin_bi)
    TextView accountJinBi;
    @Bind(R.id.account_ke_yong)
    TextView accountKeyong;
    @Bind(R.id.content_layout)
    LinearLayout contentLayout;
    private View mNoLoginView;
    private View rootView;//缓存Fragment view
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what == 0){
//                accountYue.setText("账户余额:" + (msg.arg1+"") + "元");
//            }else if (msg.what == 1){
//                accountJinBi.setText("金币余额:" + (msg.arg1+"") + "元");
//            }else if (msg.what == 2){
//                accountKeyong.setText("可用余额:" + (msg.arg1+"") + "元");
//            }
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fm_mine, null);
        ButterKnife.bind(this, rootView);
        mNoLoginView = noLogin.inflate();
        registerReceiver();
        return rootView;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.USERINFORECEIVER);
        filter.addAction(Constant.ACCOUNTRECEIVER);
        mContext.registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.USERINFORECEIVER)) {
                getUserData();
            } else if (intent.getAction().equals(Constant.ACCOUNTRECEIVER)) {
                getAccountData();
            }
        };
    };

    private void changeView() {
        // 判断是否登录
        if (StringUtils.isEmpty(PreferencesUtils.getString(mContext, Constant.USER.USERID))) {
            showNoLoginView();
            TextView rightLogin = (TextView) mNoLoginView.findViewById(R.id.right_register);
            rightLogin.setOnClickListener(this);
            Button login = (Button) mNoLoginView.findViewById(R.id.login_btn);
            login.setOnClickListener(this);
        } else {
            showLoginView();
            String username = PreferencesUtils.getString(mContext, Constant.USER.USERNAME);
            if (StringUtils.isNotEmpty(username)) {
                userName.setText(username);
                PreferencesUtils.putString(mContext.getApplicationContext(),Constant.USER.USERNAME,username);
            }
            String balance = PreferencesUtils.getString(mContext, Constant.USER.BALANCE);
            if (StringUtils.isNotEmpty(balance)) {
                accountYue.setText("账户余额:" + balance + "元");
            }
            String jinbi = PreferencesUtils.getString(mContext, Constant.USER.GOLD);
            if (StringUtils.isNotEmpty(jinbi)) {
                accountJinBi.setText("金币余额:" + jinbi + "元");
            }
            String keyong = PreferencesUtils.getString(mContext, Constant.USER.USABLE);
            if (StringUtils.isNotEmpty(keyong)) {
                accountKeyong.setText("可用余额:" + keyong + "元");
            }
            scrollView.setCanPullDown(true);
            scrollView.setCanPullUp(false);
            refreshView.setTAG(TAG);
            refreshView.setOnRefreshListener(this);
            getUserData();
            getAccountData();
        }
    }

    public void showNoLoginView() {
        contentLayout.setVisibility(View.GONE);
        mNoLoginView.setVisibility(View.VISIBLE);

    }

    public void showLoginView() {
        if (mNoLoginView != null) {
            mNoLoginView.setVisibility(View.GONE);
        }
        contentLayout.setVisibility(View.VISIBLE);
    }

    private void getUserData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDUSERSETTINGINFO, map, TAG, new SpotsCallBack<UserInfoResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, UserInfoResponse res) {
                if (res.getCode().equals("200")) {
                    PreferencesUtils.putString(mContext, Constant.USER.ISPAY, res.getIs_pay());
                    PreferencesUtils.putString(mContext, Constant.USER.USERNAME, res.getUserName());
                } else {
                    Toast.makeText(mContext, res.getMessage(), Toast.LENGTH_SHORT).show();
                    PreferencesUtils.putString(mContext, Constant.USER.USERID, "");
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

    private void getAccountData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
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
//                        Message msg = handler.obtainMessage();
//                        msg.what = 0;
//                        msg.arg1 = Integer.parseInt(balance);
//                        handler.sendMessage(msg);
                        accountYue.setText("账户余额:" + balance + "元");
                    }
                    String jinbi = res.getData().getGold_balance();
                    if (StringUtils.isNotEmpty(jinbi)) {
//                        Message msg = handler.obtainMessage();
//                        msg.what = 1;
//                        msg.arg1 = Integer.parseInt(jinbi);
//                        handler.sendMessage(msg);
                        accountJinBi.setText("金币余额:" + jinbi + "元");
                    }
                    String keyong = res.getData().getAvailable_fee();
                    if (StringUtils.isNotEmpty(keyong)) {
//                        Message msg = handler.obtainMessage();
//                        msg.what = 2;
//                        msg.arg1 = Integer.parseInt(keyong);
//                        handler.sendMessage(msg);
                        accountKeyong.setText("可用余额:" + keyong + "元");
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
    public void onResume() {
        super.onResume();
        changeView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.cancelTag(TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.layout_user, R.id.layout_chong_zhi, R.id.layout_ti_xian, R.id.layout_order, R.id.layout_accout, R.id.layout_setting,R.id.layout_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            case R.id.login_btn:
                startActivity(new Intent(mContext, LoginActivity.class));
                break;
            case R.id.layout_user:
                startActivity(new Intent(mContext, PersonInfoActivity.class));
                break;
            case R.id.layout_chong_zhi:
                startActivity(new Intent(mContext, InputCrashActivity.class));
                break;
            case R.id.layout_ti_xian:
                if (StringUtils.isEmpty(PreferencesUtils.getString(mContext, Constant.USER.REALNAME))) {
                    startActivity(new Intent(mContext, PersonInfoActivity.class));
                    Toast.makeText(mContext, "请先设置真实姓名和身份证信息", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(mContext, TiXianManagerActivity.class));
                }
                break;
            case R.id.layout_order:
                startActivity(new Intent(mContext, OrderListActivity.class));
                break;
            case R.id.layout_accout:
//                startActivity(new Intent(mContext, AccountActivity.class));

                startActivity(new Intent(mContext, NewAccountActivity.class));

                break;
            case R.id.layout_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.layout_about:
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
        getUserData();
        getAccountData();
    }


    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
