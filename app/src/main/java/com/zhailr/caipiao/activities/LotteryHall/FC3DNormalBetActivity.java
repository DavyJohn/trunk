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
import com.zhailr.caipiao.model.response.ZhuihaoResponse;
import com.zhailr.caipiao.model.response.ZhuihaodetailDataResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.zoushitu.FCZxTuActivity;
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
 * Created by zhailiangrong on 16/7/8.
 */
public class FC3DNormalBetActivity extends BaseActivity implements ISimpleDialogListener, ISimpleDialogCancelListener {
    private static final String TAG = "FC3DNormalBetActivity";
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
    @Bind(R.id.ac_double_bet_display_zhandian_name)
    TextView mTextZhangDianName;
    @OnClick(R.id.ac_double_bet_display_zhandian_name) void name(){
        startActivity(new Intent(FC3DNormalBetActivity.this, SiteListActivity.class));
    }

    private BetAdapter mAdapter;
    private ArrayList<BetBean> mList = new ArrayList<BetBean>();
    private BigInteger zs;
    private BigInteger price;
    private String tag;
    private int MAX_NUM = 50;
    private List<ZhuihaodetailDataResponse> info = new ArrayList<>();
    private String mul ;
    private String issue_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle(R.string.fc3d_bet)
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
        SimpleDialogFragment.createBuilder(FC3DNormalBetActivity.this, getSupportFragmentManager())
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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        mAdapter = new BetAdapter(this);
        mAdapter.setData(mList);
//        mAdapter.setOnItemClickListener(new BetAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = null;
//                if (tag.equals("FC3DNoramlActivity")) {
//                    intent = new Intent(FC3DNormalBetActivity.this, FC3DNoramlActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("FC3DGroupActivity")) {
//                    intent = new Intent(FC3DNormalBetActivity.this, FC3DGroupActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("FC3DZXHZActivity")) {
//                    intent = new Intent(FC3DNormalBetActivity.this, FC3DZXHZActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("FC3DZSHZActivity")) {
//                    intent = new Intent(FC3DNormalBetActivity.this, FC3DZSHZReselectActivity.class);
//                    intent.putExtra("bean", mList.get(position));
//                    intent.putExtra("position", position);
//                    intent.putExtra("list", mList);
//                    startActivity(intent);
//                } else if (tag.equals("FC3DZLHZActivity")) {
//                    intent = new Intent(FC3DNormalBetActivity.this, FC3DZLHZActivity.class);
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
                if (mList.size() < 20) {
                    if (tag.equals("FC3DNoramlActivity")) {
                        Intent intent = new Intent(FC3DNormalBetActivity.this, FC3DNoramlActivity.class);
                        intent.putExtra("List", mList);
                        startActivity(intent);
                    } else if (tag.equals("FC3DGroupActivity")) {
                        Intent intent = new Intent(FC3DNormalBetActivity.this, FC3DGroupActivity.class);
                        intent.putExtra("List", mList);
                        startActivity(intent);
                    } else if (tag.equals("FC3DZXHZActivity")) {
                        Intent intent = new Intent(FC3DNormalBetActivity.this, FC3DZXHZActivity.class);
                        intent.putExtra("List", mList);
                        startActivity(intent);
                    } else if (tag.equals("FC3DZSHZActivity")) {
                        Intent intent = new Intent(FC3DNormalBetActivity.this, FC3DZSHZActivity.class);
                        intent.putExtra("List", mList);
                        startActivity(intent);
                    } else if (tag.equals("FC3DZLHZActivity")) {
                        Intent intent = new Intent(FC3DNormalBetActivity.this, FC3DZLHZActivity.class);
                        intent.putExtra("List", mList);
                        startActivity(intent);
                    }else if (tag.equals("FCZxTuActivity")){
                        Intent intent = new Intent(FC3DNormalBetActivity.this, FCZxTuActivity.class);
                        intent.putExtra("List", mList);
                        startActivity(intent);
                    }
                }else{
                    showToast("投注数目不能超过20条");
                }
                break;
            case R.id.tv_auto_choose:
                if (mList.size() < 20) {
                    if (tag.equals("FC3DNoramlActivity")) {
                        autoChooseOne(1);
                    } else if (tag.equals("FC3DGroupActivity")) {
                        autoChooseOne2(1);
                    } else if (tag.equals("FC3DZXHZActivity")) {
                        autoChooseOne3(1);
                    } else if (tag.equals("FC3DZSHZActivity")) {
                        autoChooseOne4(1);
                    } else if (tag.equals("FC3DZLHZActivity")) {
                        autoChooseOne5(1);
                    }else if (tag.equals("FCZxTuActivity")){// 还要细分这事哪个的走势图
                        autoChooseOne(1);
                    }
                }else{
                    showToast("投注数目不能超过20条");
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
        map.put("type_code", "FCSD");
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
        StringBuffer sb = new StringBuffer();
        pingjie(Integer.parseInt(TextUtils.isEmpty(issue.getText().toString()) ? "1" : issue.getText().toString()),TextUtils.isEmpty(times.getText().toString()) ? "1" : times.getText().toString());
        sb = getSbFromTag(sb);
        String append;
        String isAppend;
        if (TextUtils.isEmpty(issue.getText().toString())) {
            append = "1";
            isAppend = "0";//不追号
        } else {
            append = issue.getText().toString();
            isAppend = "1";//追号
        }
        Log.e(TAG, "userId:" + PreferencesUtils.getString(mContext, Constant.USER.USERID)
                + "siteId:" + PreferencesUtils.getString(mContext, Constant.USER.SITEID)
                + "issue_num:" + num
                + "append:" + append
                + "is_append:" + isAppend
                + "multiple:" + mul
                + "type_code:" + "FCSD"
                + "channel:" + "ANDROID"
                + "content:" + sb.toString());
        OkHttpUtils.get().url(Constant.COMMONURL + Constant.FC3DRECORDREQUEST)
                .addParams(Constant.SSQOrderRequest.USERID, PreferencesUtils.getString(mContext, Constant.USER.USERID))
                .addParams(Constant.SSQOrderRequest.SITEID, PreferencesUtils.getString(mContext, Constant.USER.SITEID))
                .addParams(Constant.SSQOrderRequest.ISSUENUM, num)
                .addParams(Constant.SSQOrderRequest.APPEND, append)
                .addParams(Constant.SSQOrderRequest.ISAPPEND, isAppend)
                .addParams(Constant.SSQOrderRequest.MULTIPLE, mul)
                .addParams(Constant.SSQOrderRequest.TYPECODE, "FCSD")
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
                            Intent intent = new Intent(FC3DNormalBetActivity.this, SelectPayTypeActivity.class);
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
            List<String> red3 = mList.get(i).getRedList3();
            // 3D直选
            if (tag.equals("FC3DNoramlActivity") || tag.equals("FCZxTuActivity")) {
                if (red.size() == 1 && red2.size() == 1 && red3.size() == 1) {
                    sb.append("5201%");
                    sb.append(red.get(0) + "|" + red2.get(0) + "|" + red3.get(0) + "_1");
                } else {
                    sb.append("5202%");
                    for (int n = 0; n < red.size(); n++) {
                        if (n != red.size() - 1) {
                            sb.append(red.get(n) + ",");
                        } else {
                            sb.append(red.get(n) + "|");
                        }
                    }
                    for (int n = 0; n < red2.size(); n++) {
                        if (n != red2.size() - 1) {
                            sb.append(red2.get(n) + ",");
                        } else {
                            sb.append(red2.get(n) + "|");
                        }
                    }
                    for (int n = 0; n < red3.size(); n++) {
                        if (n != red3.size() - 1) {
                            sb.append(red3.get(n) + ",");
                        } else {
                            sb.append(red3.get(n) + "_1");
                        }
                    }
                }
            }
            // 组三
            else if (tag.equals("FC3DGroupActivity")) {
                if (red.size() == 1 && red2.size() == 1) {
                    sb.append("5211%");
                    sb.append(red.get(0) + "|" + red2.get(0) + "_1");
                } else {
                    sb.append("5212%");
                    for (int n = 0; n < red.size(); n++) {
                        if (n != red.size() - 1) {
                            sb.append(red.get(n) + ",");
                        } else {
                            sb.append(red.get(n) + "|");
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
            }
            // 直选和值
            else if (tag.equals("FC3DZXHZActivity")) {
                sb.append("5213%");
                for (int n = 0; n < red.size(); n++) {
                    if (n != red.size() - 1) {
                        sb.append(red.get(n) + ",");
                    } else {
                        sb.append(red.get(n) + "_1");
                    }
                }
            }
            // 组三和值
            else if (tag.equals("FC3DZSHZActivity")) {
                sb.append("5205%");
                for (int n = 0; n < red.size(); n++) {
                    if (n != red.size() - 1) {
                        sb.append(red.get(n) + ",");
                    } else {
                        sb.append(red.get(n) + "_1");
                    }
                }
            }
            // 组六和值
            else if (tag.equals("FC3DZLHZActivity")) {
                sb.append("5209%");
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
            ArrayList<String> redList2 = new ArrayList<String>();
            ArrayList<String> redList3 = new ArrayList<String>();
            // 1.在0-9个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(10);
            String numString1 = "" + arcNum;
            redList1.add(numString1);
            arcNum = random.nextInt(10);
            String numString2 = "" + arcNum;
            redList2.add(numString2);
            arcNum = random.nextInt(10);
            String numString3 = "" + arcNum;
            redList3.add(numString3);
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(numString1 + "  |  " + numString2 + "  |  " + numString3);
            bet.setBlueNums("");
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setType("直选单式");
            bet.setRedList(redList1);
            bet.setRedList2(redList2);
            bet.setRedList3(redList3);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);

    }

    // 机选一注
    private void autoChooseOne2(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            ArrayList<String> redList2 = new ArrayList<String>();
            ArrayList<String> redList3 = new ArrayList<String>();
            // 1.在0-9个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(10);
            String numString1 = "" + arcNum;
            redList1.add(numString1);
            String numString2;
            do {
                arcNum = random.nextInt(10);
                numString2 = "" + arcNum;
            }while (numString2.equals(numString1));
            redList2.add(numString2);
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(numString1 + "  |  " + numString2);
            bet.setBlueNums("");
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setType("直选单式");
            bet.setRedList(redList1);
            bet.setRedList2(redList2);
            bet.setRedList3(redList2);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
    }

    // 机选一注
    private void autoChooseOne3(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            // 1.在0-27个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(27);
            String numString1 = "" + arcNum;
            redList1.add(numString1);
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(numString1);
            bet.setBlueNums("");
            int zs = changeZhusuAccount(numString1);
            bet.setZhu(zs + "");
            bet.setPrice(zs * 2 + "");
            bet.setType("直选和值");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);

    }

    // 算直选组三的注数
    private int changeZhusuAccount(String strr) {
        int zs = 0;

        if (strr.equals("0") || strr.equals("27")) {
            zs += 1;
        }
        if (strr.equals("1") || strr.equals("26")) {
            zs += 3;
        }
        if (strr.equals("2") || strr.equals("25")) {
            zs += 6;
        }
        if (strr.equals("3") || strr.equals("24")) {
            zs += 10;
        }
        if (strr.equals("4") || strr.equals("23")) {
            zs += 15;
        }
        if (strr.equals("5") || strr.equals("22")) {
            zs += 21;
        }
        if (strr.equals("6") || strr.equals("21")) {
            zs += 28;
        }
        if (strr.equals("7") || strr.equals("20")) {
            zs += 36;
        }
        if (strr.equals("8") || strr.equals("19")) {
            zs += 45;
        }
        if (strr.equals("9") || strr.equals("18")) {
            zs += 55;
        }
        if (strr.equals("10") || strr.equals("17")) {
            zs += 63;
        }
        if (strr.equals("11") || strr.equals("16")) {
            zs += 69;
        }
        if (strr.equals("12") || strr.equals("15")) {
            zs += 73;
        }
        if (strr.equals("13") || strr.equals("14")) {
            zs += 75;
        }
        return zs;
    }

    // 机选一注
    private void autoChooseOne4(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            // 1.在0-27个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(25) + 1;
            String numString1 = "" + arcNum;
            redList1.add(numString1);
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(numString1);
            bet.setBlueNums("");
            int zs = changeZhusuAccountForZS(numString1);
            bet.setZhu(zs + "");
            bet.setPrice(zs * 2 + "");
            bet.setType("组三和值");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
    }

    // 算组三和值的注数
    private int changeZhusuAccountForZS(String strr) {
        int zs = 0;
        if (strr.equals("1") || strr.equals("26")) {
            zs += 1;
        }
        if (strr.equals("2") || strr.equals("25")) {
            zs += 2;
        }
        if (strr.equals("3") || strr.equals("24")) {
            zs += 1;
        }
        if (strr.equals("4") || strr.equals("23")) {
            zs += 3;
        }
        if (strr.equals("5") || strr.equals("22")) {
            zs += 3;
        }
        if (strr.equals("6") || strr.equals("21")) {
            zs += 3;
        }
        if (strr.equals("7") || strr.equals("20")) {
            zs += 4;
        }
        if (strr.equals("8") || strr.equals("19")) {
            zs += 5;
        }
        if (strr.equals("9") || strr.equals("18")) {
            zs += 4;
        }
        if (strr.equals("10") || strr.equals("17")) {
            zs += 5;
        }
        if (strr.equals("11") || strr.equals("16")) {
            zs += 5;
        }
        if (strr.equals("12") || strr.equals("15")) {
            zs += 4;
        }
        if (strr.equals("13") || strr.equals("14")) {
            zs += 5;
        }
        return zs;
    }

    // 机选一注
    private void autoChooseOne5(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            // 1.在0-27个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(22) + 3;
            String numString1 = "" + arcNum;
            redList1.add(numString1);
            // 2.封装成bean,添加到list中
            BetBean bet = new BetBean();
            bet.setRedNums(numString1);
            bet.setBlueNums("");
            int zs = changeZhusuAccountForZL(numString1);
            bet.setZhu(zs + "");
            bet.setPrice(zs * 2 + "");
            bet.setType("组六和值");
            bet.setRedList(redList1);
            mList.add(0, bet);
        }
        mAdapter.setData(mList);
    }

    private int changeZhusuAccountForZL(String strr) {
        int zs = 0;
        if (strr.equals("3") || strr.equals("4") || strr.equals("23") || strr.equals("24")) {
            zs += 1;
        }
        if (strr.equals("5") || strr.equals("22")) {
            zs += 2;
        }
        if (strr.equals("6") || strr.equals("21")) {
            zs += 3;
        }
        if (strr.equals("7") || strr.equals("20")) {
            zs += 4;
        }
        if (strr.equals("8") || strr.equals("19")) {
            zs += 5;
        }
        if (strr.equals("9") || strr.equals("18")) {
            zs += 7;
        }
        if (strr.equals("10") || strr.equals("17")) {
            zs += 8;
        }
        if (strr.equals("11") || strr.equals("16")) {
            zs += 9;
        }
        if (strr.equals("12") || strr.equals("13") || strr.equals("14") || strr.equals("15")) {
            zs += 10;
        }
        return zs;
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
            tag = getIntent().getStringExtra("tag");
            mList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
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
    //追号
    private void addZhuihao(){
        String issue_data = issue.getText().toString();
        String times_data = times.getText().toString();
        String price_data = tvPrice.getText().toString();
        OkHttpUtils.get().url(Constant.COMMONURL+Constant.ZHUIHAO)
                .addParams(Constant.USER.ZHUIHAO_TIMES,issue.getText().toString())
                .addParams("type_code","FCSD")
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

    @Override
    protected void onStart() {
        super.onStart();
        String name = PreferencesUtils.getString(getApplicationContext(),Constant.SiteName);
        if (name.isEmpty()){
            mTextZhangDianName.setText("请选择当前站点");
        }else {
            mTextZhangDianName.setText("当前站点为："+PreferencesUtils.getString(mContext,Constant.SiteName));
        }
    }
}
