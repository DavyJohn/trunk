package com.zhailr.caipiao.activities.LotteryHall;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.WebViewActivity;
import com.zhailr.caipiao.activities.mine.LoginActivity;
import com.zhailr.caipiao.adapter.SimpleAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.widget.ShakeListener;
import com.zhailr.caipiao.zoushitu.ZouShiTuActivity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.shape.RectLightShape;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class DoubleColorBallNormalActivity extends BaseActivity {
    private static final String TAG = "DoubleColorBallNormal";
    @Bind(R.id.layout_ball)
    LinearLayout layoutBall;
    @Bind(R.id.auto_choose)
    TextView autoChoose;
    @Bind(R.id.tv_zhu)
    TextView tvZhu;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ac_double_floating_action_button)
    FloatingActionButton mFloatButton;
    @Bind(R.id.ok)
    TextView mTextOk;
    private HighLight mHightLight;

    private ArrayList<String> mRedList = new ArrayList<String>();
    private ArrayList<String> mBlueList = new ArrayList<String>();
    private ArrayList<TextView> mRedClickList = new ArrayList<TextView>();
    private ArrayList<TextView> mBlueClickList = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList = new ArrayList<TextView>();
    private ArrayList<TextView> mBlueAllList = new ArrayList<TextView>();
    private LinearLayout m_LinearLayout;
    private BigInteger zs;
    private TextView redBall;
    private BigInteger price;
    // 点确定后，需要传递的list
    private ArrayList<BetBean> mList = new ArrayList<BetBean>();
    ArrayList<BetBean> chooseList = new ArrayList<BetBean>();
    private int position = -1;
    private static final int START = 0;
    private String USERID;
    private String currentNum;
    private ShakeListener mShakeListener;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START:
                    autoChooseOne(1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        Constant.isClick = false;
        ButterKnife.bind(this);
        getToolBar().setTitle(R.string.double_normal_play);
        getCurrentNum();
        initView();
        initIntent();
        shake();

        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.CHOOSE_STYLE = 0;
                Constant.isClick = true;
                Intent intent = new Intent(DoubleColorBallNormalActivity.this,ShakeActivity.class);
                intent.putExtra("data",chooseList);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(mShakeListener!=null)
            mShakeListener.stop();
        super.onDestroy();

    }

    private void getCurrentNum() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "SSQ");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                if (res.getCode().equals("200")) {
                    currentNum = res.getData().getIssue_num();
                    tvTime.setText("第"+ currentNum +"期  " + "截止:"+ res.getData().getTime_draw());
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast(getString(R.string.request_error));
            }
        });
    }

    private void initView() {
//        showNextKnownTipView();

        mList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        int margin = 0;
        int diameter = 0;
        // 3:4:6:8
        if (160 < densityDpi && densityDpi <= 240) {
            diameter = 60;
            margin = 5;
        } else if (240 < densityDpi && densityDpi <= 320) {
            diameter = 80;
            margin = 6;
        } else if (320 < densityDpi && densityDpi <= 480) {
            diameter = 96;
            margin = 9;
        } else if (480 < densityDpi && densityDpi <= 640) {
            diameter = 128;
            margin = 12;
        }

        int redNum = 33;
        int blueNum = 16;
        initBallView(redNum, blueNum, margin, diameter);
    }

    private void initBallView(int redNum, int blueNum, int margin, int diameter) {
        int col = 0;
        // 计算一行有多少列 col = (w-(col+1)*m)/d;
        double colData = (double) (screenwidth - margin) / (diameter + margin);
        // 如果不为整数的话，那就重新计算margin的值
        // 舍掉小数取整
        col = (int) Math.floor(colData);
        int min = screenwidth - diameter * (col + 1);
        if (min > 5 * (col + 1)) {
            col = col + 1;
            margin = (new BigDecimal((double) (min / (col + 1))).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
        } else if (min < 0) {
            col = col - 1;
            margin = (new BigDecimal((double) (screenwidth - diameter * col) / (col + 1)).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
        } else {
            margin = (new BigDecimal((double) (screenwidth - diameter * col) / (col + 1)).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
        }
        // 计算总共有多少行
        int redRow = getRow(redNum, col);
        int blueRow = getRow(blueNum, col);

        // 画红球的view
        drawBallView(col, redRow, diameter, margin, redNum, "red");
        // 添加一条线
        drawLine();
        // 画蓝球的view
        drawBallView(col, blueRow, diameter, margin, blueNum, "blue");

    }

    private void drawLine() {
        View line = new View(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearParams.height = 3;// 控件的高强制设成4
        linearParams.width = screenwidth;// 控件的宽强制设成screenwidth
        linearParams.setMargins(0, 10, 0, 10);
        line.setLayoutParams(linearParams);
        line.setBackground(getResources().getDrawable(R.drawable.line));
        layoutBall.addView(line);
    }

    private void drawBallView(int col, int row, int diameter, int margin, int num, final String tag) {

        if (tag.equals("red")){
            TextView tips = new TextView(this);
            tips.setText("至少选择6个红球 1个蓝球");
            tips.setTextSize(16);
            tips.setPadding(8,8,8,8);
            layoutBall.addView(tips);
        }
        for (int i = 0; i < row; i++) {
            m_LinearLayout = new LinearLayout(this);//创建LinearLayout布局对象
            m_LinearLayout.setOrientation(LinearLayout.HORIZONTAL);//设置水平

            if (i != row - 1) {
                for (int j = i * col; j < i * col + col; j++) {
                    redBall = new TextView(this);
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearParams.height = diameter;// 控件的高强制设成diameter
                    linearParams.width = diameter;// 控件的宽强制设成diameter
                    if (j != i * col + col - 1) {
                        linearParams.setMargins(margin, 2, 0, 0);
                    } else {
                        linearParams.setMargins(margin, 2, screenwidth - col * margin, 0);
                    }

                    redBall.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                    if (j + 1 < 10) {
                        redBall.setText("0" + (j + 1));
                    } else {
                        redBall.setText("" + (j + 1));
                    }
                    redBall.setTextSize(18);
                    redBall.setGravity(Gravity.CENTER);
                    redBall.setPadding(0, 0, 0, 4);
                    if (tag.equals("red")) {
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList.add(redBall);
                    } else if (tag.equals("blue")) {
                        redBall.setTextColor(getResources().getColor(R.color.blue));
                        mBlueAllList.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.whiteball));

                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tag.equals("red")) {
                                clickRedBall((TextView) v);
                            } else if (tag.equals("blue")) {
                                clickBlueBall((TextView) v);
                            }
                        }
                    });
                    m_LinearLayout.addView(redBall);

                }
            } else {
                for (int j = i * col; j < num; j++) {
                    TextView redBall = new TextView(this);
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearParams.height = diameter;// 控件的高强制设成diameter
                    linearParams.width = diameter;// 控件的宽强制设成diameter
                    if (j != i * col + col - 1) {
                        linearParams.setMargins(margin, 2, 0, 0);
                    } else {
                        linearParams.setMargins(margin, 2, screenwidth - col * margin, 0);
                    }
                    redBall.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                    if (j + 1 < 10) {
                        redBall.setText("0" + (j + 1));
                    } else {
                        redBall.setText("" + (j + 1));
                    }
                    redBall.setTextSize(18);
                    redBall.setGravity(Gravity.CENTER);
                    redBall.setPadding(0, 0, 0, 4);
                    if (tag.equals("red")) {
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList.add(redBall);
                    } else if (tag.equals("blue")) {
                        redBall.setTextColor(getResources().getColor(R.color.blue));
                        mBlueAllList.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.whiteball));
                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tag.equals("red")) {
                                clickRedBall((TextView) v);
                            } else if (tag.equals("blue")) {
                                clickBlueBall((TextView) v);
                            }
                        }
                    });
                    m_LinearLayout.addView(redBall);
                }
            }
            layoutBall.addView(m_LinearLayout);

        }
    }
    private void shake(){
        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                mShakeListener.stop();
                clearClickBall();
                final Thread thread = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if (Constant.isClick  == false){
                            handler.obtainMessage(START).sendToTarget();
                        }else if (Constant.isClick == true){
                            try{
                                Thread.sleep(1500);
                                handler.obtainMessage(START).sendToTarget();
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                };
                thread.start();
//                autoChooseOne(1);
//                // finish and 继续 监听
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mShakeListener.start();
//                    }
//                }, 2000);
            }
        });
    }

    private int getRow(int num, int col) {
        return num % col != 0 ? num / col + 1 : num / col;
    }

    //点击蓝球
    private void clickBlueBall(TextView view) {
        if (view.getTag() == "0" || view.getTag() == null) {
            if (mBlueList.size() < 16) {
                view.setTag("1");
                view.setTextColor(getResources().getColor(R.color.white));
                view.setBackground(getResources().getDrawable(R.drawable.blueball));
                mBlueList.add(view.getText().toString());
                mBlueClickList.add(view);
            } else {
                showToast("选择的蓝球不能超过16个");
            }
        } else {
            view.setTag("0");
            view.setTextColor(getResources().getColor(R.color.blue));
            view.setBackground(getResources().getDrawable(R.drawable.whiteball));
            mBlueList.remove(view.getText().toString());
            mBlueClickList.remove(view);
        }
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 50}, -1);
        changeZhusuAccount();
        changeChooseBtn();
    }
    //点击红球
    private void clickRedBall(TextView view) {
        if ( view.getTag() == null || view.getTag() == "0" ) {
            if (mRedList.size() < 16) {
                // 选中
                view.setTag("1");
                view.setTextColor(getResources().getColor(R.color.white));
                view.setBackground(getResources().getDrawable(R.drawable.redball));
                mRedList.add(view.getText().toString());
                mRedClickList.add(view);
            } else {
                showToast("选择的红球不能超过16个");
            }
        } else {
            // 取消
            view.setTag("0");
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.whiteball));
            mRedList.remove(view.getText().toString());
            mRedClickList.remove(view);
        }

        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 50}, -1);
        changeZhusuAccount();
        changeChooseBtn();
    }

    private void changeChooseBtn() {
        if (mRedList.size() != 0 || mBlueList.size() != 0) {
            autoChoose.setText("清空");
            autoChoose.setTextColor(getResources().getColor(R.color.white));
        } else {
            autoChoose.setText("机选");
            autoChoose.setTextColor(getResources().getColor(R.color.yellow));
        }
    }
    //双色球算法
    private void changeZhusuAccount() {
        // 显示共多少注，共多少钱
        BigInteger m = BigInteger.valueOf(mBlueList.size());
        BigInteger n = BigInteger.valueOf(mRedList.size());
        //NSInteger zs = m*n*(n-1)*(n-2)*(n-3)*(n-4)*(n-5)/720;
        zs = ((((((m.multiply(n)).multiply(n.subtract(new BigInteger("1")))).multiply(n.subtract(new BigInteger("2")))).multiply(n.subtract(new BigInteger("3")))).multiply(n.subtract(new BigInteger("4")))).multiply(n.subtract(new BigInteger("5")))).divide(new BigInteger("720"));
        if (zs.compareTo(new BigInteger("0")) != 0) {
            price = zs.multiply(new BigInteger("2"));
            System.out.print(price);
            if (Integer.parseInt(String.valueOf(price))>9999){
                Toast.makeText(mContext,"超出金额",Toast.LENGTH_SHORT).show();
                // TODO: 2017/1/17
                mTextOk.setEnabled(false);
            }else {
                mTextOk.setEnabled(true);
                tvZhu.setText("共 " + zs + " 注");
                tvPrice.setText(" " + price + " 元");
            }

        } else {
            tvZhu.setText("");
            tvPrice.setText("");
        }
    }

    @OnClick({R.id.auto_choose, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_choose:
                if (autoChoose.getText().equals("机选")) {
                    DialogPlus dialog = DialogPlus.newDialog(this)
                            .setContentHolder(new GridHolder(3))
                            .setAdapter(new SimpleAdapter(DoubleColorBallNormalActivity.this, true))
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    USERID = TextUtils.isEmpty(PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID)) ? "" :PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID);
                                    switch (position) {
                                        case 0:
                                            if (USERID.equals("")){
                                                startActivity(new Intent(mContext, LoginActivity.class));
                                            }else {
                                                autoChooseOne(1);
                                            }

                                            break;
                                        case 1:
                                            if (USERID.equals("")){
                                                startActivity(new Intent(mContext, LoginActivity.class));
                                            }else {
                                                autoChooseOne(5);
                                            }
                                            break;
                                        case 2:
                                            if (USERID.equals("")){
                                                startActivity(new Intent(mContext, LoginActivity.class));
                                            }else {
                                                autoChooseOne(10);
                                            }
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setExpanded(false)
                            .create();
                    dialog.show();
                } else {
                    // 清空
                    clearClickBall();

                }
                break;
            case R.id.ok:
                USERID = TextUtils.isEmpty(PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID)) ? "" :PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID);
                if (USERID.equals("")){
                    startActivity(new Intent(mContext,LoginActivity.class));
                }else {
                    if (StringUtils.isEmpty(tvPrice.getText())) {
                        showToast("请至少选择6个红球，1个蓝球");
                    } else if (price.compareTo(new BigInteger("200000")) == 1) {
                        showToast("金额上限不能超过20万");
                    } else if(!TextUtils.isEmpty(currentNum)){
                        Intent intent = new Intent(this, DoubleNoramlBetActivity.class);
                        BetBean bet = new BetBean();
                        StringBuilder sb1 = new StringBuilder();
                        StringBuilder sb2 = new StringBuilder();
                        // 对号码进行排序
                        Collections.sort(mRedList, new Comparator<String>() {
                            public int compare(String arg0, String arg1) {
                                return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
                            }
                        });
                        Collections.sort(mBlueList, new Comparator<String>() {
                            public int compare(String arg0, String arg1) {
                                return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
                            }
                        });
                        for (int i = 0; i < mRedList.size(); i++) {
                            sb1.append(mRedList.get(i) + "  ");
                        }
                        for (int i = 0; i < mBlueList.size(); i++) {
                            sb2.append(mBlueList.get(i) + "  ");
                        }
                        bet.setRedNums(sb1.toString());
                        bet.setBlueNums(sb2.toString());
                        bet.setType("普通投注");
                        bet.setPrice(price + "");
                        bet.setZhu(zs + "");
                        bet.setBlueList(mBlueList);
                        bet.setRedList(mRedList);
                        if (chooseList.size() != 0 && position != -1) {
                            chooseList.set(position, bet);
                        } else {
                            chooseList.add(0, bet);
                        }
                        intent.putExtra("list", chooseList);
                        intent.putExtra("TAG",TAG);
                        intent.putExtra("currentNum", currentNum);
                        startActivity(intent);
                        finish();
                    }else if (TextUtils.isEmpty(currentNum)){
                        Toast.makeText(mContext,"当前网络不稳定，请稍等一会！！！",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void clearClickBall() {
        for (int i = 0; i < mBlueClickList.size(); i++) {
            TextView textView = mBlueClickList.get(i);
            textView.setTag(1);
            textView.setTextColor(getResources().getColor(R.color.blue));
            textView.setBackground(getResources().getDrawable(R.drawable.whiteball));
        }
        for (int i = 0; i < mRedClickList.size(); i++) {
            TextView textView = mRedClickList.get(i);
            textView.setTag(1);
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setBackground(getResources().getDrawable(R.drawable.whiteball));
        }
        tvZhu.setText("");
        tvPrice.setText("");
        mRedList.clear();
        mBlueList.clear();
        mRedClickList.clear();
        mBlueClickList.clear();
        changeChooseBtn();
    }

    private void initIntent() {
        clearClickBall();
        if (null != getIntent().getSerializableExtra("bean")) {// 点击进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("position", -1);
            mRedList = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList();
            mBlueList = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getBlueList();
            for (int i = 0; i < mBlueList.size(); i++) {
                TextView textView = mBlueAllList.get(Integer.valueOf(mBlueList.get(i)) - 1);
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.blueball));
            }
            for (int i = 0; i < mRedList.size(); i++) {
                TextView textView = mRedAllList.get(Integer.valueOf(mRedList.get(i)) - 1);
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.redball));
            }
            changeZhusuAccount();
            changeChooseBtn();
        } else if (null != getIntent().getSerializableExtra("List")) {// 从自选进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        }
    }

    // 机选一注 num 参数为选几注
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
            chooseList.add(0, bet);
        }
        if (!TextUtils.isEmpty(currentNum)){
            Intent intent = new Intent(this, DoubleNoramlBetActivity.class);
            intent.putExtra("list", chooseList);
            intent.putExtra("currentNum", currentNum);
            intent.putExtra("TAG",TAG);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(mContext,"当前网络不稳定，请稍等一会！！！",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_double_normaol;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        PopupMenu popupMenu = new PopupMenu(mContext,menu);

        getMenuInflater().inflate(R.menu.zoushi,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.zoushi:
                Intent intent = new Intent(mContext,ZouShiTuActivity.class);
                intent.putExtra("List",mList);
                startActivity(intent);
                break;
            case R.id.zhidao:
                showNextKnownTipView();
                break;
            case R.id.introduce:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("TAG","double_ball_normal_ways");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNextKnownTipView(){
        mHightLight = new HighLight(DoubleColorBallNormalActivity.this)
                .anchor(findViewById(R.id.ac_double_rootview))
                .addHighLight(R.id.layout_ball,R.layout.info_gravity_left_down,new OnBottomPosCallback(60),new RectLightShape())
                .addHighLight(R.id.ac_double_floating_action_button,R.layout.info_gravity_left_down,new OnLeftPosCallback(45),new RectLightShape())
                .autoRemove(false)
                .enableNext()
                .setClickCallback(new HighLight.OnClickCallback() {
                    @Override
                    public void onClick() {
                        mHightLight.next();
                    }
                });
        mHightLight.show();
    }
    public void clickKnown(View view)
    {
        if(mHightLight.isShowing() && mHightLight.isNext())//如果开启next模式
        {
            mHightLight.next();
        }else
        {
            remove(null);
        }
    }
    public void remove(View view)
    {
        mHightLight.remove();
    }
}
