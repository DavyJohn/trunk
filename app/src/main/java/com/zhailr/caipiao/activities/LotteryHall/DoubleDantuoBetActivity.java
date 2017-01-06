package com.zhailr.caipiao.activities.LotteryHall;

import android.content.Intent;
import android.os.Bundle;
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
import com.zhailr.caipiao.model.callBack.ZhuihaoBetRecordCallBack;
import com.zhailr.caipiao.model.response.BetRecordResponse;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.model.response.ZhuihaoResponse;
import com.zhailr.caipiao.model.response.ZhuihaodetailDataResponse;
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
 * Created by zhailiangrong on 16/7/6.
 */
public class DoubleDantuoBetActivity extends BaseActivity implements ISimpleDialogListener, ISimpleDialogCancelListener {
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
    private BetAdapter mAdapter;
    private ArrayList<BetBean> mList = new ArrayList<BetBean>();
    private BigInteger zs;
    private BigInteger price;
    private int MAX_NUM = 50;
    private String mul ;
    private List<ZhuihaodetailDataResponse> info = new ArrayList<>();
    private String issue_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle(R.string.double_bet)
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

        initView();
    }

    private void showExitDialog() {
        SimpleDialogFragment.createBuilder(DoubleDantuoBetActivity.this, getSupportFragmentManager())
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
        tvAutoChoose.setVisibility(View.GONE);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        mAdapter = new BetAdapter(this);
        mAdapter.setData(mList);
//        mAdapter.setOnItemClickListener(new BetAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(DoubleDantuoBetActivity.this, DoubleColorDantuoActivity.class);
//                intent.putExtra("bean", mList.get(position));
//                intent.putExtra("position", position);
//                intent.putExtra("list", mList);
//                startActivity(intent);
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
                if(mList.size() < 20) {
                    Intent intent = new Intent(DoubleDantuoBetActivity.this, DoubleColorDantuoActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                }else{
                    showToast("投注数目不能超过20条");
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
                    finish();
                }
                else if (mList.size() == 0) {
                    showToast("请至少选择一注");
                }else if(Integer.valueOf(timesString.trim()).intValue() < 1) {
                    showToast("投注倍数不能为0");
                }else {
                    // 先调用订单接口，然后跳转支付方式
                    showDialog();
                    if (issue.getText().toString().equals("")){
                        getIssueData();
                    }else {
                        addZhuihao();
                    }

                }
                break;
        }
    }

    private void getIssueData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "SSQ");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                if (res.getCode().equals("200")) {
                    CurrentNumResponse.DataBean bean = res.getData();
                    if (getIntent().getStringExtra("currentNum").equals(bean.getIssue_num()) && bean.getSystem_date().compareTo(bean.getTime_draw()) < 0) {
                        requestData(bean.getIssue_num());
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

    private void requestData(String num) {
        pingjie(Integer.parseInt(issue.getText().toString()),times.getText().toString());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mList.size(); i++) {
            List<String> red = mList.get(i).getRedList();
            List<String> red2 = mList.get(i).getRedList2();
            List<String> blue = mList.get(i).getBlueList();
            sb.append("5003%");
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
                    sb.append(red2.get(n) + "|");
                }
            }
            for (int m = 0; m < blue.size(); m++) {
                if (m != blue.size() - 1) {
                    sb.append(blue.get(m) + ",");
                } else {
                    sb.append(blue.get(m) + "_1");
                }
            }
            if (i != mList.size() - 1) {
                sb.append("^");
            }
        }
        String append;
        String isAppend;
        if (TextUtils.isEmpty(issue.getText().toString())) {
            append = "1";
            isAppend = "0";
        } else {
            append = issue.getText().toString();
            isAppend = "1";
        }
        Log.i(TAG, sb.toString());
        OkHttpUtils.get().url(Constant.COMMONURL + Constant.SSQRECORDREQUEST)
                .addParams(Constant.SSQOrderRequest.USERID, PreferencesUtils.getString(mContext, Constant.USER.USERID))
                .addParams(Constant.SSQOrderRequest.SITEID, PreferencesUtils.getString(mContext, Constant.USER.SITEID))
                .addParams(Constant.SSQOrderRequest.ISSUENUM, num)
                .addParams(Constant.SSQOrderRequest.APPEND, append)
                .addParams(Constant.SSQOrderRequest.ISAPPEND, isAppend)
                .addParams(Constant.SSQOrderRequest.MULTIPLE, mul)
                .addParams(Constant.SSQOrderRequest.TYPECODE, "SSQ")
                .addParams(Constant.SSQOrderRequest.CHANNEL, "ANDROID")
                .addParams(Constant.SSQOrderRequest.CONTENT, sb.toString())
                .build()
                .execute(new BetRecordCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dimissDialog();
                    }

                    @Override
                    public void onResponse(BetRecordResponse response, int id) {
                        dimissDialog();
                        if (null != response.getCode() && response.getCode().equals("200")) {
                            Intent intent = new Intent(DoubleDantuoBetActivity.this, SelectPayTypeActivity.class);
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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//因为启动模式为singleTask,必须保留旧数据
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != getIntent().getSerializableExtra("list")) {
            mList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            mAdapter.setData(mList);
            Log.i(TAG, "mList.size:" + mList.size());
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
    //追号
    private void addZhuihao(){
        String issue_data = issue.getText().toString();
        String times_data = times.getText().toString();
        String price_data = tvPrice.getText().toString();
        OkHttpUtils.get().url(Constant.COMMONURL+Constant.ZHUIHAO)
                .addParams(Constant.USER.ZHUIHAO_TIMES,issue.getText().toString())
                .addParams("type_code","SSQ")
                .addParams("multiple",TextUtils.isEmpty(times.getText().toString()) ? "1" : times.getText().toString())
                .addParams("amount",tvPrice.getText().toString())
                .build()
                .execute(new ZhuihaoBetRecordCallBack() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dimissDialog();
                    }

                    @Override
                    public void onResponse(ZhuihaoResponse response, int id) {
                        dimissDialog();
                        info.clear();
                        if (response.getCode().equals("200") && response.getCode() != null ){
                            showToast("success");
                            info.addAll(response.getData().getChaseNumberInfo());
                            pinjieString(info);
                        }
                    }
                });
    }

    private void pinjieString(List<ZhuihaodetailDataResponse> issum){
        issue_num = "";
        for (int i=0;i<issum.size();i++){
            if (i <issum.size()-1){
                issue_num = issue_num+""+issum.get(i).getIssue_num()+","+"";
            }else if (i == issum.size()-1){
                issue_num = issue_num+issum.get(issum.size()-1).getIssue_num();
            }
        }
        Log.e("==============issue_num",issue_num);
        requestData(issue_num);
    }
    private void pingjie(int number,String s){
        mul = "";
        for (int i=0;i<number;i++){
            if (i<number-1){
                mul = mul+""+s+","+"";
            }else if (i == number-1){
                mul = mul+s;
            }
        }
    }
}
