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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.mine.LoginActivity;
import com.zhailr.caipiao.activities.mine.SiteListActivity;
import com.zhailr.caipiao.adapter.BetAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.model.callBack.BetRecordCallBack;
import com.zhailr.caipiao.model.callBack.ZhuihaoBetRecordCallBack;
import com.zhailr.caipiao.model.response.BetRecordResponse;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.model.response.LeftSecResponse;
import com.zhailr.caipiao.model.response.UserInfoResponse;
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
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/12.
 */
public class K3PlayBetActivity extends BaseActivity implements ISimpleDialogListener, ISimpleDialogCancelListener {
    private static final String TAG = "K3PlayBetActivity";
    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.issue)
    EditText issue;
    @Bind(R.id.isstop_check)
    CheckBox mBox;
    @Bind(R.id.ac_double_bet_display_zhandian_name)
    TextView mTextZhangDianName;
    @OnClick(R.id.ac_double_bet_display_zhandian_name) void name(){
        startActivity(new Intent(K3PlayBetActivity.this, SiteListActivity.class));
    }
    @Bind(R.id.times)
    EditText times;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_zhu)
    TextView tvZhu;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.ok)
    TextView ok;
    private BetAdapter mAdapter;
    private ArrayList<BetBean> mList = new ArrayList<BetBean>();
    private BigInteger zs;
    private BigInteger price;
    private String tag;
    private long currentNum;
    private long currentSec;
    // 倒计时
    private TimeCount time;
    private int MAX_NUM = 100;
    private List<ZhuihaodetailDataResponse> info = new ArrayList<>();
    private String mul ;
    private String issue_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        Constant.isClick = false;
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
        mBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // TODO: 2017/6/26 判断是否追号停止
                    Constant.isStop = "1";
                }else {
                    Constant.isStop = "0";
                }
            }
        });
        initView();
    }

    private void showExitDialog() {
        SimpleDialogFragment.createBuilder(K3PlayBetActivity.this, getSupportFragmentManager())
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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        mAdapter = new BetAdapter(this);
        mAdapter.setData(mList);
//        mAdapter.setOnItemClickListener(new BetAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = null;
//                if (tag.equals("K3HeZhiActivity")) {
//                    intent = new Intent(K3PlayBetActivity.this, K3HeZhiActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("K33TongActivity")) {
//                    intent = new Intent(K3PlayBetActivity.this, K33TongActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("K32TongActivity")) {
//                    intent = new Intent(K3PlayBetActivity.this, K32TongActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("K33BuTongActivity")) {
//                    intent = new Intent(K3PlayBetActivity.this, K33BuTongActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("K32BuTongActivity")) {
//                    intent = new Intent(K3PlayBetActivity.this, K32BuTongActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                }
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

                String str = issue.getText().toString();
                if (StringUtils.isNotEmpty(str) && Integer.valueOf(str) > MAX_NUM) {
                    issue.setText(String.valueOf(MAX_NUM));
                    issue.setSelection(2);
                }changePriceAndZhu();
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
                if (StringUtils.isNotEmpty(str) && Integer.valueOf(str) > 50) {
                    times.setText(String.valueOf(50));
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
            if (Integer.parseInt(String.valueOf(price))>9999){
                ok.setEnabled(false);
                Toast.makeText(mContext,"超出金额",Toast.LENGTH_LONG).show();
            }else {
                ok.setEnabled(true);
                tvPrice.setText("共 " + price + " 元");
                tvZhu.setText(zs + " 注 " + timesString + " 倍 " + issueString + " 期");
            }

        } else {
            tvPrice.setText("");
            tvZhu.setText("");
        }

    }

    @OnClick({R.id.tv_choose, R.id.tv_auto_choose, R.id.tv_clear, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_choose:
                if (tag.equals("K3HeZhiActivity")) {
                    Intent intent = new Intent(K3PlayBetActivity.this, K3HeZhiActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                } else if (tag.equals("K33TongActivity")) {
                    Intent intent = new Intent(K3PlayBetActivity.this, K33TongActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                } else if (tag.equals("K32TongActivity")) {
                    Intent intent = new Intent(K3PlayBetActivity.this, K32TongActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                } else if (tag.equals("K33BuTongActivity")) {
                    Intent intent = new Intent(K3PlayBetActivity.this, K33BuTongActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                } else if (tag.equals("K32BuTongActivity")) {
                    Intent intent = new Intent(K3PlayBetActivity.this, K32BuTongActivity.class);
                    intent.putExtra("List", mList);
                    startActivity(intent);
                }

                break;
            case R.id.tv_auto_choose:
                if (tag.equals("K3HeZhiActivity")) {
                    autoChooseOne(1);
                } else if (tag.equals("K33TongActivity")) {
                    autoChooseOne2(1);
                } else if (tag.equals("K32TongActivity")) {
                    autoChooseOne3(1);
                } else if (tag.equals("K33BuTongActivity")) {
                    autoChooseOne4(1);
                } else if (tag.equals("K32BuTongActivity")) {
                    autoChooseOne5(1);
                }

                changePriceAndZhu();
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
                    } else if (Integer.valueOf(timesString.trim()).intValue() < 1) {
                        showToast("投注倍数不能为0");
                    } else if (mList.size() == 0) {
                        showToast("请至少选择一注");
                    } else {
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
        map.put("type_code", "KS");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                if (res.getCode().equals("200")) {
                    CurrentNumResponse.DataBean bean = res.getData();
                    if (getIntent().getStringExtra("currentNum").equals(bean.getIssue_num())
                            && bean.getSystem_date().compareTo(bean.getTime_draw()) < 0) {
//                    if (bean.getSystem_date().compareTo(bean.getTime_draw()) < 0) {
                        currentNum = Long.valueOf(bean.getIssue_num());
                        requestData(bean.getIssue_num());
                    } else {
                        dimissDialog();
                        //TODO BUG
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
        StringBuffer sb = new StringBuffer();
        pingjie(Integer.parseInt(TextUtils.isEmpty(issue.getText().toString()) ? "1" : issue.getText().toString()),TextUtils.isEmpty(times.getText().toString()) ? "1" : times.getText().toString());
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

        OkHttpUtils.get().url(Constant.COMMONURL + Constant.KSRECORDREQUEST)
                .addParams(Constant.SSQOrderRequest.USERID, PreferencesUtils.getString(mContext, Constant.USER.USERID))
                .addParams(Constant.SSQOrderRequest.SITEID, PreferencesUtils.getString(mContext, Constant.USER.SITEID))
                .addParams(Constant.SSQOrderRequest.ISSUENUM, num)
                .addParams(Constant.SSQOrderRequest.APPEND, append)
                .addParams(Constant.SSQOrderRequest.ISAPPEND, isAppend)
                .addParams(Constant.SSQOrderRequest.MULTIPLE, mul)
                .addParams(Constant.SSQOrderRequest.TYPECODE, "KS")
                .addParams(Constant.SSQOrderRequest.CHANNEL, "ANDROID")
                .addParams(Constant.SSQOrderRequest.CONTENT, sb.toString())
                .addParams("isStop", Constant.isStop)
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
                            Intent intent = new Intent(K3PlayBetActivity.this, SelectPayTypeActivity.class);
                            intent.putExtra("orderId", response.getData().getOrderId());
                            intent.putExtra("totalallprice", response.getData().getAmount());
                            startActivity(intent);
                            finish();
                            Constant.isStop = "0";
                        } else {
                            showToast(response.getMessage());
                        }
                    }
                });
    }


    private void addZhuihao(){
        OkHttpUtils.get().url(Constant.COMMONURL+Constant.ZHUIHAO)
                .addParams(Constant.USER.ZHUIHAO_TIMES,issue.getText().toString())
                .addParams("type_code","KS")
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
        requestData(issue_num);
    }
    private StringBuffer getSbFromTag(StringBuffer sb) {
        for (int i = 0; i < mList.size(); i++) {
            List<String> red = mList.get(i).getRedList();
            List<String> red2 = mList.get(i).getRedList2();
            List<String> red3 = mList.get(i).getRedList3();
            // 和值
            if (tag.equals("K3HeZhiActivity")) {
                if (red.size() > 1) {
                    sb.append("56102%");
                } else {
                    sb.append("56101%");
                }

                for (int n = 0; n < red.size(); n++) {
                    if (n != red.size() - 1) {
                        sb.append(red.get(n) + ",");
                    } else {
                        sb.append(red.get(n) + "_1");
                    }
                }
            }
            // 三同号
            else if (tag.equals("K33TongActivity")) {
                if (mList.get(i).getRedList().get(0).equals("三同号通选")) {
                    sb.append("56103%777_1");
                } else {
                    if (red.size() > 1) {
                        sb.append("56105%");
                    } else {
                        sb.append("56104%");
                    }
                    for (int n = 0; n < red.size(); n++) {
                        if (n != red.size() - 1) {
                            sb.append(red.get(n) + ",");
                        } else {
                            sb.append(red.get(n) + "_1");
                        }
                    }
                }
            }
            // 二同号
            else if (tag.equals("K32TongActivity")) {
                String fuDan = "56106%";
                String fuFU = "56107%";
                String danDan = "56108%";
                String danFu = "56109%";
                if (red3 == null || red3.size() == 0) {
                    if (red.size() == 1 && red2.size() == 1) {
                        sb.append(danDan);
                        sb.append(red.get(0) + red.get(0) + "|" + red2.get(0) + "_1");
                    } else {
                        sb.append(danFu);
                        for (int n = 0; n < red.size(); n++) {
                            if (n != red.size() - 1) {
                                sb.append(red.get(n) + red.get(n) + ",");
                            } else {
                                sb.append(red.get(n) + red.get(n) + "|");
                            }
                        }
                        for (int n = 0; n < red2.size(); n++) {
                            if (n != red2.size() - 1) {
                                sb.append(red2.get(n) + ",");
                            } else {
                                sb.append(red2.get(n) + "_1");
                            }
                        }
                    }
                } else {
                    if (red3.size() == 1) {
                        sb.append(fuDan);
                        sb.append(red3.get(0) + red3.get(0) + "_1");
                    } else {
                        sb.append(fuFU);
                        for (int n = 0; n < red3.size(); n++) {
                            if (n != red3.size() - 1) {
                                sb.append(red3.get(n) + red3.get(n) + ",");
                            } else {
                                sb.append(red3.get(n) + red3.get(n) + "_1");
                            }
                        }
                    }
                }

            }
            // 三不同号
            else if (tag.equals("K33BuTongActivity")) {
                String threeSame = "56116%789_1";
                String fu = "56111%";
                String dan = "56110%";
                if (mList.get(i).getRedList().get(0).equals("三连号通选")) {
                    sb.append(threeSame);
                } else {
                    if (red.size() > 3) {
                        sb.append(fu);
                    } else {
                        sb.append(dan);
                    }
                    for (int n = 0; n < red.size(); n++) {
                        if (n != red.size() - 1) {
                            sb.append(red.get(n) + ",");
                        } else {
                            sb.append(red.get(n) + "_1");
                        }
                    }
                }
            }
            // 二不同号
            else if (tag.equals("K32BuTongActivity")) {
                String fu = "56114%";
                String dan = "56113%";
                if (red.size() > 2) {
                    sb.append(fu);
                } else {
                    sb.append(dan);
                }
                for (int n = 0; n < red.size(); n++) {
                    if (n != red.size() - 1) {
                        sb.append(red.get(n) + ",");
                    } else {
                        sb.append(red.get(n) + "_1");
                    }
                }
            }

            if (i != mList.size() - 1) {
                sb.append("^");
            }
        }
        return sb;
    }

    // 机选一注
    private void autoChooseOne(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            // 1.在0-9个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(16);
            String numString1 = "";
            if (arcNum + 3 < 10) {
                numString1 = "0" + (arcNum + 3);
            } else {
                numString1 = "" + (arcNum + 3);
            }
            redList1.add(numString1);
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(numString1 + "[和值]");
            bet.setBlueNums("");
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setType("和值");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
    }

    // 机选一注
    private void autoChooseOne2(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            // 1.在0-5个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(6);
            int m = arcNum + 1;
            String numString1 = m * 100 + m * 10 + m + "";
            redList1.add(numString1);
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(numString1 + "[三同号单选]");
            bet.setBlueNums("");
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setType("三同号单选");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
    }

    // 机选一注
    private void autoChooseOne3(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            ArrayList<String> redList2 = new ArrayList<String>();
            ArrayList<String> redList3 = new ArrayList<String>();
            int type = random.nextInt(2);
            BetBean bet = new BetBean();
            StringBuffer sb = new StringBuffer();
            if (type == 0) {
                int arcNum = random.nextInt(6);
                arcNum = arcNum + 1;
                redList3.add(arcNum + "");
                bet.setRedNums(arcNum * 10 + arcNum + "*" + "[二同号复选]");
                bet.setType("二同号复选");
            } else {
                // 选两个不同的数
                for (int i = 0; i < 2; i++) {
                    int arcNum = random.nextInt(5);
                    arcNum = arcNum + 1;
                    if (!redList1.contains(arcNum + "") && arcNum != 0) {
                        if (i == 0) {
                            redList1.add(arcNum + "");
                        } else {
                            redList2.add(arcNum + "");
                        }

                    } else {
                        i--;
                    }
                }
                for (int i = 0; i < redList1.size(); i++) {
                    sb.append(redList1.get(i) + redList1.get(i));
                }
                sb.append("#");
                for (int i = 0; i < redList2.size(); i++) {
                    sb.append(redList2.get(i));
                }
                bet.setRedNums(sb.toString() + "[二同号单选]");
                bet.setType("二同号单选");
            }
            bet.setBlueNums("");
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
    }

    // 机选一注
    private void autoChooseOne4(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 3; i++) {
                int arcNum = random.nextInt(6);
                arcNum = arcNum + 1;
                if (!redList1.contains(arcNum + "") && arcNum != 0) {
                    redList1.add(arcNum + "");
                } else {
                    i--;
                }
            }
            for (int i = 0; i < redList1.size(); i++) {
                if (i != redList1.size() - 1) {
                    sb.append(redList1.get(i) + "  ");
                } else {
                    sb.append(redList1.get(i) + "[三不同号]");
                }
            }
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(sb.toString());
            bet.setBlueNums("");
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setType("三不同号");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
    }

    // 机选一注
    private void autoChooseOne5(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 2; i++) {
                int arcNum = random.nextInt(6);
                arcNum = arcNum + 1;
                if (!redList1.contains(arcNum + "") && arcNum != 0) {
                    redList1.add(arcNum + "");
                } else {
                    i--;
                }
            }
            for (int i = 0; i < redList1.size(); i++) {
                if (i != redList1.size() - 1) {
                    sb.append(redList1.get(i) + "  ");
                } else {
                    sb.append(redList1.get(i) + "[二不同号]");
                }
            }
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(sb.toString());
            bet.setBlueNums("");
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setType("二不同号");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
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
            if (millisUntilFinished / 1000 == 0) {
                showMessageDialog(getString(R.string.last_min_tips), mContext);
            }
//            if (millisUntilFinished / 1000 == 600) {
//                showDialogMessage(getString(R.string.first_min_tips));
//            }
            if (millisUntilFinished / 1000 > 0) {
                ok.setClickable(true);
            } else {
                ok.setClickable(false);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();
            time = null;
        }
        mOkHttpHelper.cancelTag(TAG);
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
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
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
    @Override
    public int getLayoutId() {
        return R.layout.ac_double_bet;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取站点 判断UserID
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", TextUtils.isEmpty(PreferencesUtils.getString(mContext, Constant.USER.USERID))? "" : PreferencesUtils.getString(mContext, Constant.USER.USERID) );
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDUSERSETTINGINFO, map, TAG, new SpotsCallBack<UserInfoResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, UserInfoResponse res) {
                if (res.getCode().equals("200")) {
                    mTextZhangDianName.setText("当前站点为："+res.getSiteName());
                    PreferencesUtils.putString(getApplicationContext(),Constant.USER.SITEID,res.getSiteId());
                }else{
                    finish();
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }
        });
    }
}
