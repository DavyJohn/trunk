package com.zhailr.caipiao.activities.LotteryHall;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.zhailr.caipiao.model.response.KSRecordResponse;
import com.zhailr.caipiao.model.response.LeftSecResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
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
import zhy.com.highlight.HighLight;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.position.OnTopPosCallback;
import zhy.com.highlight.shape.RectLightShape;

/**
 * Created by zhailiangrong on 16/7/11.
 */
public class K3HeZhiActivity extends BaseActivity {
    private static final String TAG = "K3HeZhiActivity";
    @Bind(R.id.layout_he_zhi)
    LinearLayout layoutHeZhi;
    @Bind(R.id.tv_zhu)
    TextView tvZhu;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_da)
    TextView tvDa;
    @Bind(R.id.tv_xiao)
    TextView tvXiao;
    @Bind(R.id.tv_dan)
    TextView tvDan;
    @Bind(R.id.tv_shuang)
    TextView tvShuang;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_auto_choose)
    TextView tvAutoChoose;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.ac_k3_he_floating_action_button)
    FloatingActionButton mFloatButton;
    // 代码创建的layout
    private LinearLayout m_LinearLayout;
    private RelativeLayout.LayoutParams mParams;
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
    // 倒计时
    private TimeCount time;
    private String currentNum;
    private boolean flag;
    private long currentSec;
    private AlertDialog dialog;
    private ShakeListener mShakeListener;
    private HighLight mHightLight;
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
        Constant.isClick = false ; //初始化
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("快3 和值");
        initUI();
        initIntent();
        getKSData();
        getLeftSec();
        getCurrentNum();
        shake();

        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.CHOOSE_STYLE = 6;
                Constant.isClick = true;
                startActivity(new Intent(K3HeZhiActivity.this,ShakeActivity.class));
            }
        });
    }

    private void initUI() {
        int margin = 0;
        int diameter = 0;
        // 3:4:6:8
        if (160 < densityDpi && densityDpi <= 240) {
            diameter = 100;
            margin = 5;
        } else if (240 < densityDpi && densityDpi <= 320) {
            diameter = 120;
            margin = 6;
        } else if (320 < densityDpi && densityDpi <= 480) {
            diameter = 160;
            margin = 9;
        } else if (480 < densityDpi && densityDpi <= 640) {
            diameter = 210;
            margin = 12;
        }
        int redNum = 14;
        initBallView(redNum, margin, diameter, screenwidth);
    }

    private void initBallView(int redNum, int margin, int diameter, int layoutwidth) {
        // 计算一行有多少列 col = (w-(col+1)*m)/d;
        double colData = (double) (layoutwidth - margin) / (diameter + margin);
        // 如果不为整数的话，那就重新计算margin的值
        // 舍掉小数取整
        col = (int) Math.floor(colData);

        int min = layoutwidth - diameter * (col + 1);
        if (min > 5 * (col + 1)) {
            col = col + 1;
            margin = (new BigDecimal((double) (min / (col + 1))).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
        } else if (min < 0) {
            col = col - 1;
            margin = (new BigDecimal((double) (layoutwidth - diameter * col) / (col + 1)).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
        } else {
            margin = (new BigDecimal((double) (layoutwidth - diameter * col) / (col + 1)).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue();
        }

        // 计算总共有多少行
        int redRow = getRow(redNum, col);

        // 画和值的view
        drawBallView(col, redRow, diameter, margin, redNum, layoutHeZhi);

    }

    private void drawBallView(final int col, int row, int diameter, int margin, int num, LinearLayout linearLayout) {
        for (int i = 0; i < row; i++) {
            m_LinearLayout = new LinearLayout(this);//创建LinearLayout布局对象
            m_LinearLayout.setOrientation(LinearLayout.HORIZONTAL);//设置水平
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 0);
            m_LinearLayout.setLayoutParams(params);
            if (i != row - 1) {
                for (int j = i * col; j < i * col + col; j++) {
                    final int id = j;
                    LinearLayout ballLayout = new LinearLayout(this);
                    ballLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearParams.height = diameter;// 控件的高强制设成diameter
                    linearParams.width = diameter;// 控件的宽强制设成diameter
                    if (j != i * col + col - 1) {
                        linearParams.setMargins(margin, 2, 0, 0);
                    } else {
                        linearParams.setMargins(margin, 2, screenwidth - col * margin, 0);
                    }
                    ballLayout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                    ballLayout.setBackground(getResources().getDrawable(R.drawable.graybutton));
                    TextView redBall = new TextView(this);
//                    redBall.setId(j+3);
                    mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    redBall.setLayoutParams(mParams);
//                    if (j + 4 < 10) {
//                        redBall.setText("0" + (j + 4));
//                    } else {
//                        redBall.setText("" + (j + 4));
//                    }
                    redBall.setText("" + (j + 4));
                    redBall.setTextSize(18);
                    redBall.setGravity(Gravity.CENTER);
                    redBall.setPadding(0, 20, 0, 0);
                    redBall.setTextColor(getResources().getColor(R.color.red));
                    mRedAllList1.add(redBall);

                    ballLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickRedBall((LinearLayout) v, id, col);
                        }
                    });
                    ballLayout.addView(redBall);
                    TextView tv = new TextView(this);
                    if (j + 4 == 4 || j + 4 == 17) {
                        tv.setText("奖金80元");
                    } else if (j + 4 == 5 || j + 4 == 16) {
                        tv.setText("奖金40元");
                    } else if (j + 4 == 6 || j + 4 == 15) {
                        tv.setText("奖金25元");
                    } else if (j + 4 == 7 || j + 4 == 14) {
                        tv.setText("奖金16元");
                    } else if (j + 4 == 8 || j + 4 == 13) {
                        tv.setText("奖金12元");
                    } else if (j + 4 == 9 || j + 4 == 12) {
                        tv.setText("奖金10元");
                    } else if (j + 4 == 10 || j + 4 == 11) {
                        tv.setText("奖金9元");
                    }
                    tv.setTextSize(12);
                    tv.setGravity(Gravity.CENTER);
                    mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(mParams);
                    ballLayout.addView(tv);
                    m_LinearLayout.addView(ballLayout);


                }
            } else {
                for (int j = i * col; j < num; j++) {
                    final int id = j;
                    LinearLayout ballLayout = new LinearLayout(this);
                    ballLayout.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearParams.height = diameter;// 控件的高强制设成diameter
                    linearParams.width = diameter;// 控件的宽强制设成diameter
                    if (j != i * col + col - 1) {
                        linearParams.setMargins(margin, 2, 0, 0);
                    } else {
                        linearParams.setMargins(margin, 2, screenwidth - col * margin, 0);
                    }
                    ballLayout.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                    ballLayout.setBackground(getResources().getDrawable(R.drawable.graybutton));
                    TextView redBall = new TextView(this);
                    mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    if (j + 4 < 10) {
                        redBall.setText("0" + (j + 4));
                    } else {
                        redBall.setText("" + (j + 4));
                    }
                    redBall.setTextSize(18);
                    redBall.setGravity(Gravity.CENTER);
                    redBall.setPadding(0, 20, 0, 0);
                    redBall.setTextColor(getResources().getColor(R.color.red));
                    mRedAllList1.add(redBall);

                    ballLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickRedBall((LinearLayout) v, id, col);
                        }
                    });
                    ballLayout.addView(redBall);
                    TextView tv = new TextView(this);
                    if (j + 4 == 4 || j + 4 == 17) {
                        tv.setText("奖金80元");
                    } else if (j + 4 == 5 || j + 4 == 16) {
                        tv.setText("奖金40元");
                    } else if (j + 4 == 6 || j + 4 == 15) {
                        tv.setText("奖金25元");
                    } else if (j + 4 == 7 || j + 4 == 14) {
                        tv.setText("奖金16元");
                    } else if (j + 4 == 8 || j + 4 == 13) {
                        tv.setText("奖金12元");
                    } else if (j + 4 == 9 || j + 4 == 12) {
                        tv.setText("奖金10元");
                    } else if (j + 4 == 10 || j + 4 == 11) {
                        tv.setText("奖金9元");
                    }

                    tv.setTextSize(12);
                    tv.setGravity(Gravity.CENTER);
                    mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ballLayout.addView(tv);
                    m_LinearLayout.addView(ballLayout);
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
//                        if (Constant.isClick = true){
//
//                        }else if (Constant.isClick = false){
//                            handler.obtainMessage(START).sendToTarget();
//                        }
                        if (Constant.isClick == true){
                            try {
                                Thread.sleep(1500);
                                handler.obtainMessage(START).sendToTarget();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else if (Constant.isClick == false){
                            handler.obtainMessage(START).sendToTarget();
                        }


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

    private void clickRedBall(LinearLayout view, int num, int col) {
//        m_LinearLayout = (LinearLayout) layoutHeZhi.getChildAt(num / col);
//        LinearLayout layout = (LinearLayout) m_LinearLayout.getChildAt(num % col);
//        TextView tv = (TextView) layout.getChildAt(0);
        TextView tv = (TextView) view.getChildAt(0);
        if (view.getTag() == null || view.getTag() == "0") {
            // 选中
            view.setTag("1");
            view.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
            tv.setTextColor(getResources().getColor(R.color.white));
            mRedList1.add(tv.getText().toString());
            mRedClickList1.add(tv);
        } else {
            // 取消
            view.setTag("0");
            tv.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.graybutton));
            mRedList1.remove(tv.getText().toString());
            mRedClickList1.remove(view);
        }
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 50}, -1);
        changeZhusuAccount();
        changeChooseBtn();
        changeFourButton();
    }

    // 显示共多少注，共多少钱
    private void changeZhusuAccount() {
        // 算法：m*n*w
        zs = mRedList1.size();

        if (zs != 0) {
            if (Integer.parseInt(String.valueOf(price))>9999){
                ok.setEnabled(false);
                Toast.makeText(mContext,"超出金额",Toast.LENGTH_LONG).show();
            }else {
                ok.setEnabled(true);
                price = zs * 2;
                tvZhu.setText("共 " + zs + " 注");
                tvPrice.setText(" " + price + " 元");
            }

        } else {
            tvZhu.setText("");
            tvPrice.setText("");
        }
    }

    private void changeFourButton() {
        StringBuffer sb1 = new StringBuffer();
        // 对号码进行排序
        Collections.sort(mRedList1, new Comparator<String>() {
            public int compare(String arg0, String arg1) {
                return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
            }
        });
        for (int i = 0; i < mRedList1.size(); i++) {
            sb1.append(mRedList1.get(i) + ",");
        }
        if (zs == 4) {
            if (sb1.toString().equals("5,7,9,")) {
                doClickView(tvXiao);
                doClickView(tvDan);
            } else if (sb1.toString().equals("4,6,8,10,")) {
                doClickView(tvXiao);
                doClickView(tvShuang);
            } else if (sb1.toString().equals("11,13,15,17,")) {
                doClickView(tvDan);
                doClickView(tvDan);
            } else if (sb1.toString().equals("12,14,16,")) {
                doClickView(tvDa);
                doClickView(tvShuang);
            }
        } else if (zs == 8) {
            Log.i(TAG, sb1.toString());
            if (sb1.toString().equals("4,5,6,7,8,9,10,")) {
                doClickView(tvXiao);
            } else if (sb1.toString().equals("11,12,13,14,15,16,17,")) {
                doClickView(tvDa);
            } else if (sb1.toString().equals("4,6,8,10,12,14,16,")) {
                doClickView(tvShuang);
            } else if (sb1.toString().equals("5,7,9,11,13,15,17,")) {
                doClickView(tvDan);
            }
        } else {
            undoClickView(tvDa);
            undoClickView(tvXiao);
            undoClickView(tvDan);
            undoClickView(tvShuang);
        }
    }

    private int getRow(int num, int col) {
        return num % col != 0 ? num / col + 1 : num / col;
    }

    private void clearClickBall() {
        for (int i = 0; i < mRedClickList1.size(); i++) {
            TextView textView = mRedClickList1.get(i);
            textView.setTextColor(getResources().getColor(R.color.red));
            int num = Integer.valueOf(textView.getText().toString()) - 4;
            m_LinearLayout = (LinearLayout) layoutHeZhi.getChildAt(num / col);
            LinearLayout layout = (LinearLayout) m_LinearLayout.getChildAt(num % col);
            layout.setBackground(getResources().getDrawable(R.drawable.graybutton));
            layout.setTag(1);
        }
        undoClickView(tvDa);
        undoClickView(tvXiao);
        undoClickView(tvDan);
        undoClickView(tvShuang);
        tvZhu.setText("");
        tvPrice.setText("");
        mRedList1.clear();
        mRedClickList1.clear();
        changeChooseBtn();
    }

    private void changeChooseBtn() {
        if (mRedList1.size() != 0) {
            tvAutoChoose.setText("清空");
            tvAutoChoose.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvAutoChoose.setText("机选");
            tvAutoChoose.setTextColor(getResources().getColor(R.color.yellow));
        }
    }

    private void doClickView(TextView tv) {
        tv.setTag("0");
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
    }

    private void undoClickView(TextView tv) {
        tv.setTag("1");
        tv.setTextColor(getResources().getColor(R.color.red));
        tv.setBackground(getResources().getDrawable(R.drawable.graybutton));
    }

    private void doClickBall() {
        for (int i = 0; i < mRedList1.size(); i++) {
            int num = Integer.valueOf(mRedList1.get(i)) - 4;
            m_LinearLayout = (LinearLayout) layoutHeZhi.getChildAt(num / col);
            LinearLayout layout = (LinearLayout) m_LinearLayout.getChildAt(num % col);
            TextView tv = (TextView) layout.getChildAt(0);
            layout.setTag(1);
            layout.setBackground(getResources().getDrawable(R.drawable.graybutton));
            tv.setTextColor(getResources().getColor(R.color.red));
        }
        mRedList1.clear();
        mRedClickList1.clear();
        // 选择了大
        if (tvDa.getTag() == "0") {

            // 大和单:11,13,15,17
            if (tvDan.getTag() == "0") {
                for (int i = 11; i < 18; i++) {
                    if (i % 2 != 0) {
                        mRedList1.add("" + i);
                    }
                }
            }
            // 大和双
            else if (tvShuang.getTag() == "0") {
                for (int i = 11; i < 18; i++) {
                    if (i % 2 == 0) {
                        mRedList1.add(i + "");
                    }
                }
            } else {
                for (int i = 11; i < 18; i++) {
                    mRedList1.add(i + "");
                }
            }
        }
        // 选择了小
        else if ( tvXiao.getTag() == "0") {
            // 小和单:3,5,7,9
            if (tvDan.getTag() == "0") {
                for (int i = 4; i < 11; i++) {
                    if (i % 2 != 0) {
                        if (i < 10) {
                            mRedList1.add("0" + i);
                        } else {
                            mRedList1.add(i + "");
                        }
                    }
                }
            }
            // 大和双
            else if (tvShuang.getTag() == "0") {
                for (int i = 4; i < 11; i++) {
                    if (i % 2 == 0) {
                        if (i < 10) {
                            mRedList1.add("0" + i);
                        } else {
                            mRedList1.add(i + "");
                        }
                    }
                }
            }
            // 小
            else {
                for (int i = 4; i < 11; i++) {
                    if (i < 10) {
                        mRedList1.add("0" + i);
                    } else {
                        mRedList1.add(i + "");
                    }

                }
            }
        }
        // 小和大都没选
        else {
            // 单
            if (tvDan.getTag() == "0") {
                for (int i = 4; i < 18; i++) {
                    if (i % 2 != 0) {
                        if (i < 10) {
                            mRedList1.add("0" + i);
                        } else {
                            mRedList1.add(i + "");
                        }
                    }
                }
            }
            // 双
            else if (tvShuang.getTag() == "0") {
                for (int i = 4; i < 18; i++) {
                    if (i % 2 == 0) {
                        if (i < 10) {
                            mRedList1.add("0" + i);
                        } else {
                            mRedList1.add(i + "");
                        }
                    }
                }
            }
            // 清空
            else {
                clearClickBall();
            }
        }
        clickBallFromList();
    }

    private void clickBallFromList() {
        for (int i = 0; i < mRedList1.size(); i++) {
            int num = Integer.valueOf(mRedList1.get(i)) - 4;
            m_LinearLayout = (LinearLayout) layoutHeZhi.getChildAt(num / col);
            LinearLayout layout = (LinearLayout) m_LinearLayout.getChildAt(num % col);
            TextView tv = (TextView) layout.getChildAt(0);
            layout.setTag(0);
            layout.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
            tv.setTextColor(getResources().getColor(R.color.white));
            mRedClickList1.add(tv);
        }
        changeZhusuAccount();
        changeChooseBtn();
    }

    // 机选一注
    private void autoChooseOne(int num) {
        for (int n = 0; n < num; n++) {
            Random random = new Random();
            ArrayList<String> redList1 = new ArrayList<String>();
            // 1.在0-9个红球中生成1个不重复的随机数,分别是个十百位
            int arcNum = random.nextInt(16);
            String numString1 = "";
            if (arcNum + 4 < 10) {
                numString1 = "0" + (arcNum + 4);
            } else {
                numString1 = "" + (arcNum);
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
            chooseList.add(0, bet);
        }
        if (!TextUtils.isEmpty(currentNum)) {
            Intent intent = new Intent(this, K3PlayBetActivity.class);
            intent.putExtra("list", chooseList);
            intent.putExtra("tag", TAG);
            intent.putExtra("currentNum",currentNum);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(mContext,"当前网络不稳定，请稍等一会！！！",Toast.LENGTH_LONG).show();
        }

    }

    private void initIntent() {
        clearClickBall();
        if (null != getIntent().getSerializableExtra("bean")) {// 点击进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("position", -1);
            mRedList1 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList();
            for (int i = 0; i < mRedList1.size(); i++) {
                TextView textView = mRedAllList1.get(Integer.valueOf(mRedList1.get(i)) - 4);
                textView.setTextColor(getResources().getColor(R.color.white));
                int num = Integer.valueOf(textView.getText().toString()) - 4;
                m_LinearLayout = (LinearLayout) layoutHeZhi.getChildAt(num / col);
                LinearLayout layout = (LinearLayout) m_LinearLayout.getChildAt(num % col);
                layout.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
                layout.setTag(0);
            }
            changeChooseBtn();
            changeZhusuAccount();
            changeFourButton();
        } else if (null != getIntent().getSerializableExtra("List")) {// 从自选进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        }
    }

    @OnClick({R.id.tv_da, R.id.tv_xiao, R.id.tv_dan, R.id.tv_shuang, R.id.tv_auto_choose, R.id.ok})
    public void onClick(View view) {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        switch (view.getId()) {
            case R.id.tv_da:
                if (tvDa.getTag() != "0") {
                    doClickView(tvDa);
                    if (tvXiao.getTag() == "0") {
                        undoClickView(tvXiao);
                    }
                } else {
                    undoClickView(tvDa);
                }
                // 点亮球
                doClickBall();
                vibrator.vibrate(new long[]{0, 50}, -1);
                break;
            case R.id.tv_xiao:
                if (tvXiao.getTag() != "0") {
                    doClickView(tvXiao);
                    if (tvDa.getTag() == "0") {
                        undoClickView(tvDa);
                    }
                } else {
                    undoClickView(tvXiao);
                }
                // 点亮球
                doClickBall();
                vibrator.vibrate(new long[]{0, 50}, -1);
                break;
            case R.id.tv_dan:
                if (tvDan.getTag() != "0") {
                    doClickView(tvDan);
                    if (tvShuang.getTag() == "0") {
                        undoClickView(tvShuang);
                    }
                } else {
                    undoClickView(tvDan);
                }
                // 点亮球
                doClickBall();
                vibrator.vibrate(new long[]{0, 50}, -1);
                break;
            case R.id.tv_shuang:
                if (tvShuang.getTag() != "0") {
                    doClickView(tvShuang);
                    if (tvDan.getTag() == "0") {
                        undoClickView(tvDan);
                    }
                } else {
                    undoClickView(tvShuang);
                }
                // 点亮球
                doClickBall();
                vibrator.vibrate(new long[]{0, 50}, -1);
                break;
            case R.id.tv_auto_choose:

                if (tvAutoChoose.getText().equals("机选")) {
                    DialogPlus dialog = DialogPlus.newDialog(this)
                            .setContentHolder(new GridHolder(3))
                            .setAdapter(new SimpleAdapter(K3HeZhiActivity.this, true))
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    switch (position) {
                                        case 0:
                                            if (TextUtils.isEmpty(PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID))){
                                                startActivity(new Intent(mContext, LoginActivity.class));
                                            }else {
                                                autoChooseOne(1);
                                            }
                                            break;
                                        case 1:
                                            if (TextUtils.isEmpty(PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID))){
                                                startActivity(new Intent(mContext, LoginActivity.class));
                                            }else {
                                                autoChooseOne(5);
                                            }
                                            break;
                                        case 2:
                                            if (TextUtils.isEmpty(PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID))){
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
                if (TextUtils.isEmpty(PreferencesUtils.getString(getApplicationContext(),Constant.USER.USERID))){
                    startActivity(new Intent(mContext, LoginActivity.class));
                }else {
                    if (zs != 0 && !TextUtils.isEmpty(currentNum)) {
                        Intent intent = new Intent(this, K3PlayBetActivity.class);
                        BetBean bet = new BetBean();
                        StringBuffer sb1 = new StringBuffer();
                        for (int i = 0; i < mRedList1.size(); i++) {
                            if (i != mRedList1.size() - 1) {
                                sb1.append(mRedList1.get(i) + "  ");
                            } else {
                                sb1.append(mRedList1.get(i) + "[和值]");
                            }
                        }
                        bet.setRedNums(sb1.toString());
                        bet.setBlueNums("");
                        bet.setType("和值");
                        bet.setPrice(price + "");
                        bet.setZhu(zs + "");
                        bet.setRedList(mRedList1);
                        if (chooseList.size() != 0 && position != -1) {
                            chooseList.set(position, bet);
                        } else {
                            chooseList.add(0, bet);
                        }
                        intent.putExtra("list", chooseList);
                        intent.putExtra("tag", TAG);
                        intent.putExtra("currentNum", currentNum);
                        intent.putExtra("currentSec", currentSec);
                        startActivity(intent);
                        finish();
                    } else if (TextUtils.isEmpty(currentNum)){
                        showToast("当前网络不稳定，请稍等一会！！！");
                    }else {
                        showToast("请至少选择一注");
                    }
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
                    currentNum = res.getData().getIssue_num();
//                    currentNum = Long.valueOf(res.getData().getIssue_num());
                } else {
                    showToast("getCurrentNum"+res.getMessage());
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
                System.out.print(res);
                if (res.getCode().equals("200")) {
                    if (res.getEndTime() < 0 && !flag) {
                        tvTime.setText(getString(R.string.stop_bet));
                        flag = true;
//                    } else if (res.getEndTime() < 180 && !flag) {//时间小于3分钟就会截至购买
//                        showMessageDialog(getString(R.string.last_min_tips), mContext);
//                        flag = true;
//
                    }
//                    else if (res.getEndTime() > 570 && !flag) {
//                        showDialogMessage(getString(R.string.first_min_tips));
//                        flag = true;
//                    }
                    time = new TimeCount((long) res.getEndTime() * 1000, 1000);
                    time.start();
//                    if (res.getEndTime() > 0) {
//
//                    }

                } else {
                    showToast("getLeftSec"+res.getMessage());
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
                    String num =  bean.getLottery_num();
                    tvNum.setText("上期开奖:" + num);
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
            if (!TextUtils.isEmpty(currentNum))
                tvTime.setText("第" + currentNum + "期  截止" + StringUtils.getMillionsToMin((int) millisUntilFinished / 1000));
            if (millisUntilFinished / 1000 == 540)
                getKSData();
            if (millisUntilFinished / 1000 == 60) {
//                showMessageDialog(getString(R.string.last_min_tips), mContext);
                showMessageDialog("还有最后一分钟，购买即将截至", mContext);
            }
//            if (millisUntilFinished / 1000 == 600) {
//                showDialogMessage(getString(R.string.first_min_tips));
//            }
            /**
             * millisUntilFinished / 1000 > 180 设置三分钟以内不能点击确定
             * */
//            if (millisUntilFinished / 1000 > 180) {
//                tvAutoChoose.setClickable(true);
//                ok.setClickable(true);
//            } else {
//                tvAutoChoose.setClickable(false);
//                ok.setClickable(false);
//            }
            /**
             * 消除三分钟限制
             * */
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
        return R.layout.ac_k3_he_zhi;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zoushi,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.introduce:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("TAG","K3");
                startActivity(intent);
                break;
            case R.id.zoushi:
                showToast("暂无数据");
                break;
            case R.id.zhidao:
                showNextKnownTipView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNextKnownTipView(){
        mHightLight = new HighLight(K3HeZhiActivity.this)
                .anchor(findViewById(R.id.ac_k3_he_zhi_rootview))//rootview
                .addHighLight(R.id.layout_he_zhi,R.layout.info_gravity_left_down,new OnBottomPosCallback(60),new RectLightShape())
                .addHighLight(R.id.layout_sort_choose,R.layout.info_gravity_left_down,new OnBottomPosCallback(60),new RectLightShape())
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
