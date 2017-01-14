package com.zhailr.caipiao.activities.LotteryHall;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.mine.LoginActivity;
import com.zhailr.caipiao.adapter.BetAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.model.callBack.BetRecordCallBack;
import com.zhailr.caipiao.model.response.BetRecordResponse;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.model.response.LeftSecResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class K3DanTuoBetActivity extends BaseActivity implements ISimpleDialogListener, ISimpleDialogCancelListener {
    private static final String TAG = "DoubleDantuoBetActivity";
    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.issue)
    EditText issue;
    @Bind(R.id.times)
    EditText times;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_zhu)
    TextView tvZhu;
    @Bind(R.id.tv_auto_choose)
    TextView tvAutoChoose;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.tv_num)
    TextView tvNum;
    private BetAdapter mAdapter;
    private ArrayList<BetBean> mList = new ArrayList<BetBean>();
    private BigInteger zs;
    private BigInteger price;
    private String tag;
    private long currentNum;
    private long currentSec;
    // 倒计时
    private TimeCount time;
    private int MAX_NUM = 50;
    private String USERID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle(R.string.k3_bet)
                .setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 加个判断对话框 TODO
                        if (mList.size() != 0) {
                            showExitDialog();
                        } else {
                            finishAndTransition(true);
                        }
                    }
                });
        USERID = TextUtils.isEmpty(PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID)) ? "" : PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID);
        initView();
    }

    private void showExitDialog() {
        SimpleDialogFragment.createBuilder(K3DanTuoBetActivity.this, getSupportFragmentManager())
                .setTitle(R.string.exit_tip)
                .setMessage(R.string.message_clear)
                .setPositiveButtonText(R.string.clear_button).setRequestCode(40)
                .setNegativeButtonText(R.string.cancel_button)
                .show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void initView() {
        tvNum.setVisibility(View.VISIBLE);
        time = new TimeCount(currentSec, 1000);
        time.start();
        tvAutoChoose.setVisibility(View.GONE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        mAdapter = new BetAdapter(this);
        mAdapter.setData(mList);
//        mAdapter.setOnItemClickListener(new BetAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                if (tag.equals("K33BuTongDanActivity")) {
//                    Intent intent = new Intent(K3DanTuoBetActivity.this, K33BuTongDanActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("K32BuTongDanActivity")) {
//                    Intent intent = new Intent(K3DanTuoBetActivity.this, K32BuTongDanActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                }
//
//            }
//        });
        mAdapter.setOnCloseClickListener(new BetAdapter.OnCloseClickListener() {
            @Override
            public void onCloseClick(View view, int position) {
                mList.remove(mList.get(position));
                mAdapter.setData(mList);
                changePriceAndZhu();
            }
        });
        recycleView.setAdapter(mAdapter);
        issue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    issue.setSelection(issue.getText().length());
                    issue.getSelectionStart();
                }
            }
        });
        issue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changePriceAndZhu();
            }
        });
        times.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    times.setSelection(times.getText().length());
                    times.getSelectionStart();
                }
            }
        });
        times.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = times.getText().toString();
                if (StringUtils.isNotEmpty(str) && Integer.valueOf(str) > MAX_NUM) {
                    times.setText(String.valueOf(MAX_NUM));
                    times.setSelection(2);
                }
                changePriceAndZhu();
            }
        });

    }

    private void changePriceAndZhu() {
        zs = new BigInteger("0");
        price = new BigInteger("0");
        for (int i = 0; i < mList.size(); i++) {
            String zsString = mList.get(i).getZhu();
            String priceString = mList.get(i).getPrice();
            zs = zs.add(new BigInteger(zsString));
            price = price.add(new BigInteger(priceString));
        }
        String issueString = issue.getText().toString().equals("") ? "1" : issue.getText().toString();
        String timesString = times.getText().toString().equals("") ? "1" : times.getText().toString();
        price = (price.multiply(new BigInteger(issueString))).multiply(new BigInteger(timesString));
        if (price.compareTo(new BigInteger("0")) != 0) {
            tvPrice.setText("共 " + price + " 元");
            tvZhu.setText(zs + " 注 " + timesString + " 倍 " + issueString + " 期");
        } else {
            tvPrice.setText("");
            tvZhu.setText("");
        }

    }


    @OnClick({R.id.tv_choose, R.id.tv_auto_choose, R.id.tv_clear, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_choose:
                if (tag.equals("K33BuTongDanActivity")) {
                    Intent intent = new Intent(K3DanTuoBetActivity.this, K33BuTongDanActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                } else if (tag.equals("K32BuTongDanActivity")) {
                    Intent intent = new Intent(K3DanTuoBetActivity.this, K32BuTongDanActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                }

                break;
            case R.id.tv_clear:
                mList.clear();
                mAdapter.setData(mList);
                changePriceAndZhu();
                break;
            case R.id.ok:
                String timesString = times.getText().toString().equals("") ? "1" : times.getText().toString();
                if (TextUtils.isEmpty(PreferencesUtils.getString(mContext, Constant.USER.USERID))) {
                    startActivity(new Intent(this, LoginActivity.class));
                }else if(Integer.valueOf(timesString.trim()).intValue() < 1) {
                    showToast("投注倍数不能为0");
                }else if (mList.size() == 0) {
                    showToast("请至少选择一注");
                } else {
                    // 先调用订单接口，然后跳转支付方式
                    showDialog();
                    getIssueData();
                }
                break;
        }
    }

    private void getCurrentNum() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "KS");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                if (res.getCode().equals("200")) {
                    currentNum = Long.valueOf(res.getData().getIssue_num());
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast("服务器异常，请稍后再试");
            }
        });
    }

    private void getLeftSec() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "KS");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNOWTIME, map, TAG, new SpotsCallBack<LeftSecResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, LeftSecResponse res) {
                if (res.getCode().equals("200")) {
                    time = new TimeCount((long) res.getEndTime() * 1000, 1000);
                    time.start();

                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast("服务器异常，请稍后再试");
            }
        });
    }

    private void getIssueData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "KS");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                if (res.getCode().equals("200")) {
                    CurrentNumResponse.DataBean bean = res.getData();
//                    if (String.valueOf(currentNum).equals(bean.getIssue_num())
//                            && bean.getSystem_date().compareTo(bean.getTime_draw()) < 0) {
                    if (bean.getSystem_date().compareTo(bean.getTime_draw()) < 0) {
                        currentNum = Long.valueOf(bean.getIssue_num());
                        requestData();
                    } else {
                        dimissDialog();
                        showToast(getString(R.string.bet_error));
                    }
                } else {
                    dimissDialog();
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                dimissDialog();
                showToast(getString(R.string.request_error));
            }
        });
    }

    private void requestData() {
        StringBuffer sb = new StringBuffer();
        sb = getSbFromTag(sb);
        String append;
        String isAppend;
        if (TextUtils.isEmpty(issue.getText().toString())) {
            append = "1";
            isAppend = "0";
        } else {
            append = issue.getText().toString();
            isAppend = "1";
        }
        Log.i(TAG, "userId:" + PreferencesUtils.getString(mContext, Constant.USER.USERID)
                + "siteId:" + PreferencesUtils.getString(mContext, Constant.USER.SITEID)
                + "issue_num:" + currentNum
                + "append:" + append
                + "is_append:" + isAppend
                + "multiple:" + "1"
                + "type_code:" + "KS"
                + "channel:" + "ANDROID"
                + "content:" + sb.toString());
        OkHttpUtils.get().url(Constant.COMMONURL + Constant.KSRECORDREQUEST)
                .addParams(Constant.SSQOrderRequest.USERID, PreferencesUtils.getString(mContext, Constant.USER.USERID))
                .addParams(Constant.SSQOrderRequest.SITEID, PreferencesUtils.getString(mContext, Constant.USER.SITEID))
                .addParams(Constant.SSQOrderRequest.ISSUENUM, String.valueOf(currentNum))
                .addParams(Constant.SSQOrderRequest.APPEND, append)
                .addParams(Constant.SSQOrderRequest.ISAPPEND, isAppend)
                .addParams(Constant.SSQOrderRequest.MULTIPLE, TextUtils.isEmpty(times.getText().toString()) ? "1" : times.getText().toString())
                .addParams(Constant.SSQOrderRequest.TYPECODE, "KS")
                .addParams(Constant.SSQOrderRequest.CHANNEL, "ANDROID")
                .addParams(Constant.SSQOrderRequest.CONTENT, sb.toString())
                .build()
                .execute(new BetRecordCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(BetRecordResponse response, int id) {
                        dimissDialog();
                        if (null != response.getCode() && response.getCode().equals("200")) {
                            Intent intent = new Intent(K3DanTuoBetActivity.this, SelectPayTypeActivity.class);
                            intent.putExtra("orderId", response.getData().getOrderId());
                            intent.putExtra("totalallprice", response.getData().getAmount());
                            startActivity(intent);
                            finish();
                        } else {
                            showToast(response.getMessage());
                        }
                    }
                });
    }

    private StringBuffer getSbFromTag(StringBuffer sb) {
        for (int i = 0; i < mList.size(); i++) {
            List<String> red = mList.get(i).getRedList();
            List<String> red2 = mList.get(i).getRedList2();
            String dan2 = "56112%";
            String dan3 = "56115%";
            // 胆拖三不同号
            if (tag.equals("K33BuTongDanActivity")) {
                sb.append(dan2);
            }
            // 胆拖二不同号
            else if (tag.equals("K32BuTongDanActivity")) {
                sb.append(dan3);
            }
            for (int n = 0; n < red.size(); n++) {
                if (n != red.size() - 1) {
                    sb.append(red.get(n) + ",");
                } else {
                    sb.append(red.get(n) + "#");
                }
            }
            for (int n = 0; n < red2.size(); n++) {
                if (n != red2.size() - 1) {
                    sb.append(red2.get(n) + ",");
                } else {
                    sb.append(red2.get(n) + "_1");
                }
            }
            if (i != mList.size() - 1) {
                sb.append("^");
            }
        }
        return sb;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//因为启动模式为singleTask,必须保留旧数据
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tvNum.setText(getString(R.string.being_updat));
            ok.setClickable(false);
            getLeftSec();
            getCurrentNum();
            dismissMessageDialog();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if (currentNum != 0)
                tvNum.setText("第" + currentNum + "期  截止" + StringUtils.getMillionsToMin((int) millisUntilFinished / 1000));
            if (millisUntilFinished / 1000 == 180) {
                showMessageDialog(getString(R.string.last_min_tips), mContext);
            }
//            if (millisUntilFinished / 1000 == 600) {
//                showDialogMessage(getString(R.string.first_min_tips));
//            }
            if (millisUntilFinished / 1000 > 180) {
                ok.setClickable(true);
            } else {
                ok.setClickable(false);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != getIntent().getSerializableExtra("list")) {
            tag = getIntent().getStringExtra("tag");
            mList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            currentNum = getIntent().getLongExtra("currentNum", 0);
            currentSec = getIntent().getLongExtra("currentSec", 0);
            mAdapter.setData(mList);
            changePriceAndZhu();
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == 40) {
            finishAndTransition(true);
        }
    }

    @Override
    public void onCancelled(int requestCode) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_double_bet;
    }


}
