package com.zhailr.caipiao.activities.LotteryHall;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.adapter.SimpleAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.model.response.KSRecordResponse;
import com.zhailr.caipiao.model.response.LeftSecResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.widget.ShakeListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/12.
 */
public class K33BuTongActivity extends BaseActivity {
    private static final String TAG = "K33BuTongActivity";
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.layout_san_bu_tong)
    LinearLayout layoutSanBuTong;
    @Bind(R.id.layout_tong_xuan)
    LinearLayout layoutTongXuan;
    @Bind(R.id.tv_auto_choose)
    TextView tvAutoChoose;
    @Bind(R.id.tv_zhu)
    TextView tvZhu;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.ac_k3_san_bu_floating_action_button)
    FloatingActionButton mFloatButton;

    // 代码创建的layout
    private LinearLayout m_LinearLayout;
    private ArrayList<String> mRedList1 = new ArrayList<String>();
    private ArrayList<TextView> mRedClickList1 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList1 = new ArrayList<TextView>();
    // 注数
    private int zs;
    private int price;
    private int position = -1;
    private static final int START = 0;
    // 点确定后，需要传递的list
    ArrayList<BetBean> chooseList = new ArrayList<BetBean>();
    private int col = 0;
    private boolean hasTongXuan;
    // 倒计时
    private TimeCount time;
    private long currentNum;
    private boolean flag;
    private long currentSec;
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
        Constant.isClick = false;
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("快3 三不同号");
        initUI();
        initIntent();
        getKSData();
        getLeftSec();
        getCurrentNum();
        shake();
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.CHOOSE_STYLE = 9;
                Constant.isClick = true;
                startActivity(new Intent(K33BuTongActivity.this,ShakeActivity.class));
            }
        });
    }

    private void initUI() {
        int margin = 0;
        int diameter = 0;
        // 3:4:6:8
        if (160 < densityDpi && densityDpi <= 240) {
            diameter = 90;
        } else if (240 < densityDpi && densityDpi <= 320) {
            diameter = 100;
        } else if (320 < densityDpi && densityDpi <= 480) {
            diameter = 140;
        } else if (480 < densityDpi && densityDpi <= 640) {
            diameter = 180;
        }
        int redNum = 6;
        col = 6;
        int min = screenwidth - diameter * col;
        if (min > 20 * (col + 1)) {
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
        // 画同号的view
        drawBallView(col, redRow, diameter, margin, redNum, "1", layoutSanBuTong);
    }

    private void drawBallView(int col, int row, int diameter, int margin, int num, final String tag, LinearLayout linearLayout) {
        for (int i = 0; i < row; i++) {
            m_LinearLayout = new LinearLayout(this);//创建LinearLayout布局对象
            m_LinearLayout.setOrientation(LinearLayout.HORIZONTAL);//设置水平
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 0);
            m_LinearLayout.setLayoutParams(params);
            if (i != row - 1) {
                for (int j = i * col + 1; j < i * col + col + 1; j++) {
                    TextView redBall = new TextView(this);
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearParams.height = diameter;// 控件的高强制设成diameter
                    linearParams.width = diameter;// 控件的宽强制设成diameter
                    if (j != i * col + col) {
                        linearParams.setMargins(margin, 2, 0, 0);
                    } else {
                        linearParams.setMargins(margin, 2, screenwidth - col * margin, 0);
                    }
                    redBall.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                    redBall.setTextSize(18);
                    redBall.setGravity(Gravity.CENTER);
                    redBall.setPadding(0, 0, 0, 4);
                    redBall.setText(j + "");
                    redBall.setTextColor(getResources().getColor(R.color.red));
                    mRedAllList1.add(redBall);
                    redBall.setBackground(getResources().getDrawable(R.drawable.graybutton));

                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickRedBall((TextView) v);
                        }
                    });
                    m_LinearLayout.addView(redBall);


                }
            } else {
                for (int j = i * col + 1; j < num + 1; j++) {
                    TextView redBall = new TextView(this);
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearParams.height = diameter;// 控件的高强制设成diameter
                    linearParams.width = diameter;// 控件的宽强制设成diameter
                    if (j != i * col + col) {
                        linearParams.setMargins(margin, 2, 0, 0);
                    } else {
                        linearParams.setMargins(margin, 2, screenwidth - col * margin, 0);
                    }
                    redBall.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                    redBall.setId(j);
                    redBall.setTextSize(18);
                    redBall.setGravity(Gravity.CENTER);
                    redBall.setPadding(0, 0, 0, 4);
                    redBall.setText(j + "");
                    redBall.setTextColor(getResources().getColor(R.color.red));
                    mRedAllList1.add(redBall);
                    redBall.setBackground(getResources().getDrawable(R.drawable.graybutton));
                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickRedBall((TextView) v);
                        }
                    });
                    m_LinearLayout.addView(redBall);
                }
            }
            linearLayout.addView(m_LinearLayout);

        }
    }

    private void shake(){
        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                mShakeListener.stop();
                clearClickBall();
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if (Constant.isClick == true){
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if (Constant.isClick == false){
//
                        }
                        handler.obtainMessage(START).sendToTarget();


                    }
                };thread.start();
//                // finish and 继续监听
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mShakeListener.start();
//                    }
//                }, 2000);
            }
        });
    }

    private void clickRedBall(TextView view) {
        if (view.getTag() == null || view.getTag() == "0") {
            // 选中
            view.setTag("1");
            view.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
            view.setTextColor(getResources().getColor(R.color.white));
            mRedList1.add(view.getText().toString());
            mRedClickList1.add(view);
        } else {
            // 取消
            view.setTag("0");
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.graybutton));
            mRedList1.remove(view.getText().toString());
            mRedClickList1.remove(view);
        }
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 50}, -1);
        changeZhusuAccount();
        changeChooseBtn();
    }

    private void changeChooseBtn() {
        if (mRedList1.size() != 0 || layoutTongXuan.getTag() == null) {
            tvAutoChoose.setText("清空");
            tvAutoChoose.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvAutoChoose.setText("机选");
            tvAutoChoose.setTextColor(getResources().getColor(R.color.yellow));
        }
    }

    // 显示共多少注，共多少钱
    private void changeZhusuAccount() {
     //   if (mRedList1.size() != 0) {
        if (mRedList1.size() > 3) {
            int m = mRedList1.size();
            // zs = m!/3!*(m-3)!三不同选的算法
            int a = 1;
            int b = 1;
            for (int i = 1; i < m + 1; i++) {
                a = a * i;
            }
            for (int i = 1; i < m - 3 + 1; i++) {
                b = b * i;
            }
            zs = a / (b * 6);
            if (layoutTongXuan.getTag() == null) {
                zs = zs + 1;
            }
        }else if(mRedList1.size() == 3){
            zs = 1;
            if (layoutTongXuan.getTag() == null) {
                zs = zs + 1;
            }
        }else{
            if (layoutTongXuan.getTag() == null) {
                zs = 1;
            } else {
                zs = 0;
            }
        }
        if (zs != 0) {
            price = zs * 2;
            tvZhu.setText("共 " + zs + " 注");
            tvPrice.setText(" " + price + " 元");
        } else {
            tvZhu.setText("");
            tvPrice.setText("");
        }
    }

    private int getRow(int num, int col) {
        return num % col != 0 ? num / col + 1 : num / col;
    }

    private void clearClickBall() {
        for (int i = 0; i < mRedClickList1.size(); i++) {
            TextView view = mRedClickList1.get(i);
            view.setTag("1");
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.graybutton));
        }
        layoutTongXuan.setTag("1");
        layoutTongXuan.setBackground(getResources().getDrawable(R.drawable.graybutton));
        TextView tv = (TextView) layoutTongXuan.getChildAt(0);
        tv.setTextColor(getResources().getColor(R.color.red));
        tvZhu.setText("");
        tvPrice.setText("");
        mRedList1.clear();
        mRedClickList1.clear();
        changeChooseBtn();
    }

    // 机选一注
    private void autoChooseOne(int num) {
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
            chooseList.add(0, bet);
        }
        Intent intent = new Intent(this, K3PlayBetActivity.class);
        intent.putExtra("list", chooseList);
        intent.putExtra("tag", TAG);
        startActivity(intent);
        finish();
    }

    private void initIntent() {
        clearClickBall();
        if (null != getIntent().getSerializableExtra("bean")) {// 点击进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("position", -1);
            mRedList1 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList();
            for (int i = 0; i < mRedList1.size(); i++) {
                if (mRedList1.get(i).equals("三连号通选")) {
                    mRedList1.remove("三连号通选");
                    hasTongXuan = true;
                    layoutTongXuan.setTag("0");
                    layoutTongXuan.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
                    TextView tv = (TextView) layoutTongXuan.getChildAt(0);
                    tv.setTextColor(getResources().getColor(R.color.white));
                } else {
                    TextView textView = mRedAllList1.get(Integer.valueOf(mRedList1.get(i)) - 1);
                    textView.setTag("0");
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
                }

            }
            changeZhusuAccount();
            changeChooseBtn();
        } else if (null != getIntent().getSerializableExtra("List")) {// 从自选进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        }
    }

    @OnClick({R.id.tv_auto_choose, R.id.ok, R.id.layout_tong_xuan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_tong_xuan:
                if (layoutTongXuan.getTag() != null) {
                    layoutTongXuan.setTag("0");
                    layoutTongXuan.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
                    TextView tv = (TextView) layoutTongXuan.getChildAt(0);
                    tv.setTextColor(getResources().getColor(R.color.white));
                } else {
                    layoutTongXuan.setTag("1");
                    layoutTongXuan.setBackground(getResources().getDrawable(R.drawable.graybutton));
                    TextView tv = (TextView) layoutTongXuan.getChildAt(0);
                    tv.setTextColor(getResources().getColor(R.color.red));
                }
                Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{0, 50}, -1);
                changeZhusuAccount();
                changeChooseBtn();
                break;
            case R.id.tv_auto_choose:
                if (tvAutoChoose.getText().equals("机选")) {
                    DialogPlus dialog = DialogPlus.newDialog(this)
                            .setContentHolder(new GridHolder(3))
                            .setAdapter(new SimpleAdapter(K33BuTongActivity.this, true))
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    switch (position) {
                                        case 0:
                                            autoChooseOne(1);
                                            break;
                                        case 1:
                                            autoChooseOne(5);
                                            break;
                                        case 2:
                                            autoChooseOne(10);
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
                if(mRedList1.size() != 0&&mRedList1.size() < 3){
                    tvAutoChoose.setClickable(false);
                    ok.setClickable(false);
                    showToast("三不同选请至少下一注或者不下注");
                }else {
                    if (zs != 0) {
                        // 跳转
                        Intent intent = new Intent(this, K3PlayBetActivity.class);
                        BetBean bet = new BetBean();
                        // 点击进来的
                        if (chooseList.size() != 0 && position != -1) {
                            if (hasTongXuan) {
                                // 其他的另外添加
                                if (mRedList1.size() != 0) {
                                    bet = changeDanXuan(bet);
                                    chooseList.add(0, bet);
                                }
                            }
                            // 从单选点进来的，先改单选，再改通选
                            else {
                                if (mRedList1.size() != 0) {
                                    bet = changeDanXuan(bet);
                                    chooseList.set(position, bet);
                                }
                                if (layoutTongXuan.getTag() == null) {
                                    bet = changeTongXuan(bet);
                                    chooseList.add(0, bet);
                                }
                            }

                        }
                        // 新增的
                        else {
                            if (layoutTongXuan.getTag() == null) {
                                bet = changeTongXuan(bet);
                                chooseList.add(0, bet);
                            }
                            if (mRedList1.size() != 0) {
                                bet = changeDanXuan(bet);
                                chooseList.add(0, bet);
                            }

                        }
                        intent.putExtra("list", chooseList);
                        intent.putExtra("tag", TAG);
                        intent.putExtra("currentNum", currentNum);
                        intent.putExtra("currentSec", currentSec);
                        startActivity(intent);
                        finish();

                    } else {
                        showToast("请至少选择1注");
                    }
                }
                break;
        }
    }

    private BetBean changeDanXuan(BetBean bet) {
        bet = new BetBean();
        StringBuffer sb1 = new StringBuffer();
        Collections.sort(mRedList1, new Comparator<String>() {
            public int compare(String arg0, String arg1) {
                return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
            }
        });
        for (int i = 0; i < mRedList1.size(); i++) {
            if (i != mRedList1.size() - 1) {
                sb1.append(mRedList1.get(i) + "  ");
            } else {
                sb1.append(mRedList1.get(i) + "[三不同号]");
            }
        }
        bet.setRedNums(sb1.toString());
        bet.setBlueNums("");
        bet.setType("三不同号");
        if (layoutTongXuan.getTag() == null) {
            zs = zs - 1;
            price = zs * 2;
            bet.setPrice(price + "");
        }else {
            price = zs * 2;
            bet.setPrice(price + "");
        }
        bet.setZhu(zs + "");
        bet.setRedList(mRedList1);
        return bet;
    }

    private BetBean changeTongXuan(BetBean bet) {
        bet = new BetBean();
        ArrayList<String> redList1 = new ArrayList<String>();
        String numString1 = "三连号通选";
        redList1.add(numString1);
        // 2.封装成bean,添加到list中
        bet = new BetBean();
        bet.setRedNums(numString1 + "[三连号通选]");
        bet.setBlueNums("");
        bet.setZhu("1");
        bet.setPrice("2");
        bet.setType("三连号通选");
        bet.setRedList(redList1);
        return bet;
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
                    if (res.getEndTime() < 0 && !flag) {
                        tvTime.setText(getString(R.string.stop_bet));
                        flag = true;
//                    } else if (res.getEndTime() < 180 && res.getEndTime() != 0 && !flag) {
//                        showMessageDialog(getString(R.string.last_min_tips), mContext);
//                        flag = true;
//                    } else if (res.getEndTime() != 0 && !flag){
//                        showMessageDialog(getString(R.string.last_min_tips), mContext);
//                        flag = true;
                    }
//                    else if ( res.getEndTime() > 570 && !flag) {
//                        showDialogMessage(getString(R.string.first_min_tips));
//                        flag = true;
//                    }
//                    if (res.getEndTime() > 0) {
//                        time = new TimeCount((long) res.getEndTime() * 1000, 1000);
//                        time.start();
//                    }
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

    private void getKSData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("lottery_no_number", "1");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.KSRECORD, map, TAG, new SpotsCallBack<KSRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, KSRecordResponse data) {
                if (null != data.getData().getHitoryQS() && data.getData().getHitoryQS().size() != 0) {
                    KSRecordResponse.DataBean.HitoryQSBean bean = data.getData().getHitoryQS().get(0);
                    tvNum.setText("上期开奖:" + bean.getLottery_num());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast("服务器异常，请稍后再试");
            }

        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tvTime.setText(getString(R.string.being_updat));
            tvNum.setText(getString(R.string.waiting_for_lottery));
            tvAutoChoose.setClickable(false);
            ok.setClickable(false);
            getLeftSec();
            getCurrentNum();
            dismissMessageDialog();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            currentSec = millisUntilFinished;
            if (currentNum != 0)
                tvTime.setText("第" + currentNum + "期  截止" + StringUtils.getMillionsToMin((int) millisUntilFinished / 1000));
            if (millisUntilFinished / 1000 == 540)
                getKSData();
            if (millisUntilFinished / 1000 == 0) {
                showMessageDialog(getString(R.string.last_min_tips), mContext);
            }
//            if (millisUntilFinished / 1000 == 600) {
//                showDialogMessage(getString(R.string.first_min_tips));
//            }
            if (millisUntilFinished / 1000 > 0) {
                tvAutoChoose.setClickable(true);
                ok.setClickable(true);
                mFloatButton.setClickable(true);
            } else {
                tvAutoChoose.setClickable(false);
                ok.setClickable(false);
                mFloatButton.setClickable(false);
            }

        }
    }


    @Override
    protected void onDestroy() {
        if(mShakeListener!=null)
            mShakeListener.stop();
        super.onDestroy();
        if (time != null) {
            time.cancel();
            time = null;
        }
        mOkHttpHelper.cancelTag(TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_k3_san_bu_tong;
    }
}
