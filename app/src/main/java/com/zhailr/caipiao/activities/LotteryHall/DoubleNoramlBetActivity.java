package com.zhailr.caipiao.activities.LotteryHall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogCancelListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.mine.RegisterFourActivity;
import com.zhailr.caipiao.activities.mine.SiteListActivity;
import com.zhailr.caipiao.model.callBack.ZhuihaoBetRecordCallBack;
import com.zhailr.caipiao.model.response.UserInfoResponse;
import com.zhailr.caipiao.model.response.ZhuihaoResponse;
import com.zhailr.caipiao.model.response.ZhuihaodetailDataResponse;
import com.zhailr.caipiao.zoushitu.ZouShiTuActivity;
import com.zhailr.caipiao.activities.mine.LoginActivity;
import com.zhailr.caipiao.adapter.BetAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.model.callBack.BetRecordCallBack;
import com.zhailr.caipiao.model.response.BetRecordResponse;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/6.
 */
public class DoubleNoramlBetActivity extends BaseActivity implements ISimpleDialogListener, ISimpleDialogCancelListener {
    private static final String TAG = "DoubleNoramlBetActivity";
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
    @Bind(R.id.ok)
    TextView mTextOk;
    @Bind(R.id.ac_double_bet_display_zhandian_name)
    TextView mTextZhangDianName;
    @OnClick(R.id.ac_double_bet_display_zhandian_name) void name(){
        startActivity(new Intent(DoubleNoramlBetActivity.this, SiteListActivity.class));
    }
    private BetAdapter mAdapter;
    private ArrayList<BetBean> mList = new ArrayList<BetBean>();
    private BigInteger zs;
    private BigInteger price;
    public static DoubleNoramlBetActivity instance = null;
    private int MAX_NUM = 50;
    private List<ZhuihaodetailDataResponse> info = new ArrayList<>();
    private String mul ;
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
        instance = this;
        initView();
    }

    private void showExitDialog() {
        SimpleDialogFragment.createBuilder(DoubleNoramlBetActivity.this, getSupportFragmentManager())
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
//        mAdapter.setData(mList);

//        mAdapter.setOnItemClickListener(new BetAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(DoubleNoramlBetActivity.this, DoubleColorBallNormalActivity.class);
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
                String str = issue.getText().toString();
                if (StringUtils.isNotEmpty(str) && Integer.valueOf(str) > MAX_NUM) {
                    issue.setText(String.valueOf(MAX_NUM));
                    issue.setSelection(2);
                }
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
            if (Integer.parseInt(String.valueOf(price))>9999){
                Toast.makeText(mContext,"超出金额",Toast.LENGTH_SHORT).show();
                // TODO: 2017/1/17

                mTextOk.setEnabled(false);
            }else {
                mTextOk.setEnabled(true);
                tvPrice.setText("共 " + price + " 元");
                tvZhu.setText(zs + " 注 " + timesString + " 倍 " + issueString + " 期");
            }

        } else {
            tvPrice.setText("");
            tvZhu.setText("");
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_double_bet;
    }

    @OnClick({R.id.tv_choose, R.id.tv_auto_choose, R.id.tv_clear, R.id.ok})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_choose:
                //手动加入
                if (mList.size() < 20){
                    if (getIntent().getStringExtra("TAG").equals("DoubleColorBallNormal")){
                        intent = new Intent(DoubleNoramlBetActivity.this, DoubleColorBallNormalActivity.class);
                    }else if (getIntent().getStringExtra("TAG").equals("ZouShiTuActivity")){
                        intent = new Intent(DoubleNoramlBetActivity.this, ZouShiTuActivity.class);
                    }
                    intent.putExtra("List", mList);
                    startActivity(intent);
                }else{
                    showToast("投注数目不能超过20条");
                }
//                intent = new Intent(DoubleNoramlBetActivity.this, DoubleColorBallNormalActivity.class);
//                intent.putExtra("List", mList);
//                startActivity(intent);
                break;
            case R.id.tv_auto_choose:
                if (mList.size() < 20){
                    autoChooseOne(1);
                }else{
                    showToast("投注数目不能超过20条");
                }
//                autoChooseOne(1);
                changePriceAndZhu();
                break;
            case R.id.tv_clear:
                mList.clear();
                mAdapter.setData(mList);
                changePriceAndZhu();
                break;
            case R.id.ok:
                //先将所有追号显示出来
//                addZhuihao();
                //支付
                String timesString = times.getText().toString().equals("") ? "1" : times.getText().toString();
                if (TextUtils.isEmpty(PreferencesUtils.getString(mContext, Constant.USER.USERID))) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }else if (mList.size() == 0) {
                    showToast("请至少选择一注");
                }else if(Integer.valueOf(timesString.trim()).intValue() < 1) {
                    showToast("投注倍数不能为0");
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
//获取最新期号如果没有追号就调用此接口
    private void getIssueData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "SSQ");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                System.out.print(res);
                if (res.getCode().equals("200")) {
                    CurrentNumResponse.DataBean bean = res.getData();
                    if (getIntent().getStringExtra("currentNum").equals(bean.getIssue_num())
                            && bean.getSystem_date().compareTo(bean.getTime_draw()) < 0) {
                        requestData(bean.getIssue_num());//其好唯一 没有追号
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
        for (int i = 0; i < mList.size(); i++) {
            List<String> red = mList.get(i).getRedList();
            List<String> blue = mList.get(i).getBlueList();
            if (red.size() > 6 || blue.size() > 1) {
                sb.append("5002%");
            } else {
                sb.append("5001%");
            }
            for (int n = 0; n < red.size(); n++) {
                if (n != red.size() - 1) {
                    sb.append(red.get(n) + ",");
                } else {
                    sb.append(red.get(n) + "|");
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
        //判断是否追号
        if (TextUtils.isEmpty(issue.getText().toString())) {
            //不追号
            append = "1";
            isAppend = "0";
//            getIssueData();
        } else {
            //追号
            append = issue.getText().toString();
            isAppend = "1";
        }
        Log.e(TAG, "userId:" + PreferencesUtils.getString(mContext, Constant.USER.USERID)
                + "siteId:" + PreferencesUtils.getString(mContext, Constant.USER.SITEID)
                + "issue_num:" + num
                + "append:" + append
                + "is_append:" + isAppend
                + "multiple" + mul
                + "type_code:" + "SSQ"
                + "content:" + sb.toString());
        //下单接口 Siteid 要修改
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
                            Intent intent = new Intent(DoubleNoramlBetActivity.this, SelectPayTypeActivity.class);
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

    // 机选一注
    private void autoChooseOne(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList = new ArrayList<String>();
            ArrayList<String> blueList = new ArrayList<String>();
            String numString = null;
            // 1.在33个红球中生成6个不重复的随机数
            for (int i = 0; i < 6; i++) {
                int arcNum = random.nextInt(33);
                if (arcNum < 10) {
                    numString = "0" + arcNum;
                } else {
                    numString = "" + arcNum;
                }
                if (!redList.contains(numString) && arcNum != 0) {
                    redList.add(numString);
                } else {
                    i--;
                }
            }
            // 2.在16个蓝球中生成1个不重复的随机数
            int arcNum = random.nextInt(16);
            while (arcNum == 0) {
                arcNum = random.nextInt(16);
            }
            if (arcNum < 10) {
                numString = "0" + arcNum;
            } else {
                numString = "" + arcNum;
            }
            blueList.add(numString);
            // 得到注数和价钱(这里是生成一注)
//        getZhusuAccount(redList, blueList);
            // 3.封装成bean,添加到list中
            BetBean bet = new BetBean();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            // 对号码进行排序
            Collections.sort(redList, new Comparator<String>() {
                public int compare(String arg0, String arg1) {
                    return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
                }
            });
            for (int i = 0; i < redList.size(); i++) {
                sb1.append(redList.get(i) + "  ");
            }
            for (int i = 0; i < blueList.size(); i++) {
                sb2.append(blueList.get(i) + "  ");
            }
            bet.setRedNums(sb1.toString());
            bet.setBlueNums(sb2.toString());
            bet.setZhu("1");
            bet.setPrice("2");
            bet.setType("普通投注");
            bet.setBlueList(blueList);
            bet.setRedList(redList);
            mList.add(0, bet);
        }

        mAdapter.setData(mList);

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
       /*     if (mList.size() < 21){
                mAdapter.setData(mList);
            }else{
                showToast("投注数目不能超过20条");
            }*/
            mAdapter.setData(mList);
            Log.i(TAG, "mList.size:" + mList.size());
            changePriceAndZhu();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.getInstace().cancelTag(TAG);
        OkHttpUtils.getInstance().cancelTag(TAG);
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

    private void addZhuihao(){
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
                            info.addAll(response.getData().getChaseNumberInfo());
                            pinjieString(info);
                        }
                    }
                });
    }
    //拼接字符串"123,123,123,123,123"
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

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("=====","onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("=====","onRestart");
    }
}
