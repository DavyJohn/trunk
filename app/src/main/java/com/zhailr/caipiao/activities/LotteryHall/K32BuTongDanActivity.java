package com.zhailr.caipiao.activities.LotteryHall;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.model.response.KSRecordResponse;
import com.zhailr.caipiao.model.response.LeftSecResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class K32BuTongDanActivity extends BaseActivity {
    private static final String TAG = "K32BuTongDanActivity";
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.layout_dan_ma)
    LinearLayout layoutDanMa;
    @Bind(R.id.layout_tuo_ma)
    LinearLayout layoutTuoMa;
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
    // 代码创建的layout
    private LinearLayout m_LinearLayout;
    private ArrayList<String> mRedList1 = new ArrayList<String>();
    private ArrayList<String> mRedList2 = new ArrayList<String>();
    private ArrayList<TextView> mRedClickList1 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedClickList2 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList1 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList2 = new ArrayList<TextView>();
    // 注数
    private int zs;
    private int price;
    private int position = -1;
    // 点确定后，需要传递的list
    ArrayList<BetBean> chooseList = new ArrayList<BetBean>();
    private int col = 0;
    // 倒计时
    private TimeCount time;
    private long currentNum;
    private boolean flag;
    private long currentSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("二不同号胆拖");
        initUI();
        initIntent();
        getKSData();
        getLeftSec();
        getCurrentNum();
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
        drawBallView(col, redRow, diameter, margin, redNum, "1", layoutDanMa);
        drawBallView(col, redRow, diameter, margin, redNum, "2", layoutTuoMa);
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
                    if (tag.equals("1")) {
                        mRedAllList1.add(redBall);
                    } else if (tag.equals("2")) {
                        mRedAllList2.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.graybutton));

                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickRedBall((TextView) v, tag);
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
                    redBall.setTextSize(18);
                    redBall.setGravity(Gravity.CENTER);
                    redBall.setPadding(0, 0, 0, 4);
                    redBall.setText(j + "");
                    redBall.setTextColor(getResources().getColor(R.color.red));
                    if (tag.equals("1")) {
                        mRedAllList1.add(redBall);
                    } else if (tag.equals("2")) {
                        mRedAllList2.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.graybutton));
                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickRedBall((TextView) v, tag);
                        }
                    });
                    m_LinearLayout.addView(redBall);
                }
            }
            linearLayout.addView(m_LinearLayout);

        }
    }

    private void clickRedBall(TextView view, String tag) {
        if (view.getTag() == null || view.getTag() == "0") {
            if (checkRedNum(tag)) {
                if (checkCanClick(tag, view.getText().toString())) {
                    // 选中
                    view.setTag("1");
                    view.setTextColor(getResources().getColor(R.color.white));
                    view.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
                    if (tag.equals("1")) {
                        mRedList1.add(view.getText().toString());
                        mRedClickList1.add(view);
                    } else {
                        mRedList2.add(view.getText().toString());
                        mRedClickList2.add(view);
                    }
                } else {
                    String toast = tag.equals("1") ?
                            view.getText() + "已为拖码，不能将其改为胆码" : view.getText() + "已为胆码，不能将其改为拖码";
                    showToast(toast);
                }
            } else {
                String toast = mRedList1.size() > 0 ? "胆码区最多只能选1个" : "拖码区最多只能选5个";
                showToast(toast);
            }
        } else {
            // 取消
            view.setTag("0");
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.graybutton));
            if (tag.equals("1")) {
                mRedList1.remove(view.getText().toString());
                mRedClickList1.remove(view);
            } else {
                mRedList2.remove(view.getText().toString());
                mRedClickList2.remove(view);
            }
        }
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 50}, -1);
        changeZhusuAccount();
    }

    private boolean checkRedNum(String tag) {
        return tag.equals("1") ? !(mRedList1.size() > 0) : !(mRedList2.size() > 4);
    }

    private boolean checkCanClick(String tag, String text) {
        return tag.equals("1") ? !mRedList2.contains(text) : !mRedList1.contains(text);
    }


    // 显示共多少注，共多少钱
    private void changeZhusuAccount() {
        if (mRedList1.size() != 0 && mRedList2.size() != 0) {
            int m = mRedList1.size();
            int n = mRedList2.size();
            if (m == 1) {
                zs = n;
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

    private void clearClickBall() {
        for (int i = 0; i < mRedClickList1.size(); i++) {
            TextView view = mRedClickList1.get(i);
            view.setTag(1);
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.graybutton));
        }
        for (int i = 0; i < mRedClickList2.size(); i++) {
            TextView view = mRedClickList2.get(i);
            view.setTag(1);
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.graybutton));
        }
        tvZhu.setText("");
        tvPrice.setText("");
        mRedList1.clear();
        mRedClickList1.clear();
    }

    private int getRow(int num, int col) {
        return num % col != 0 ? num / col + 1 : num / col;
    }

    private void initIntent() {
        clearClickBall();
        if (null != getIntent().getSerializableExtra("bean")) {// 点击进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("position", -1);
            mRedList1 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList();
            mRedList2 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList2();
            for (int i = 0; i < mRedList1.size(); i++) {
                TextView textView = mRedAllList1.get(Integer.valueOf(mRedList1.get(i)) - 1);
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
            }
            for (int i = 0; i < mRedList2.size(); i++) {
                TextView textView = mRedAllList2.get(Integer.valueOf(mRedList2.get(i)) - 1);
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.graybutton_press));
            }
            changeZhusuAccount();
        } else if (null != getIntent().getSerializableExtra("List")) {// 从自选进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        }
    }


    @OnClick({R.id.tv_auto_choose, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auto_choose:
                // 清空
                clearClickBall();
                break;
            case R.id.ok:
                if (zs != 0) {
                    // 跳转
                    Intent intent = new Intent(this, K3DanTuoBetActivity.class);
                    BetBean bet = new BetBean();
                    // 点击进来的
                    if (chooseList.size() != 0 && position != -1) {
                        bet = changeDanXuan(bet);
                        chooseList.set(position, bet);
                    }
                    // 新增的
                    else {
                        bet = changeDanXuan(bet);
                        chooseList.add(0, bet);
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
        Collections.sort(mRedList2, new Comparator<String>() {
            public int compare(String arg0, String arg1) {
                return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
            }
        });
        sb1.append("(  ");
        for (int i = 0; i < mRedList1.size(); i++) {
            sb1.append(mRedList1.get(i) + "  ");
        }
        sb1.append(")  ");
        for (int i = 0; i < mRedList2.size(); i++) {
            sb1.append(mRedList2.get(i) + "  ");
        }
        bet.setRedNums(sb1.toString());
        bet.setBlueNums("");
        bet.setType("二不同号胆拖");
        bet.setPrice(price + "");
        bet.setZhu(zs + "");
        bet.setRedList(mRedList1);
        bet.setRedList2(mRedList2);
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
                    } else if (res.getEndTime() < 60 && res.getEndTime() != 0 && !flag) {
                        showMessageDialog(getString(R.string.last_min_tips), mContext);
                        flag = true;
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
                    tvNum.setText("上期开奖:" + bean.getWinNumber());
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
            if (millisUntilFinished / 1000 == 180) {
                showMessageDialog(getString(R.string.last_min_tips), mContext);
            }
//            if (millisUntilFinished / 1000 == 600) {
//                showDialogMessage(getString(R.string.first_min_tips));
//            }
            if (millisUntilFinished / 1000 > 180) {
                tvAutoChoose.setClickable(true);
                ok.setClickable(true);
            } else {
                tvAutoChoose.setClickable(false);
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
    public int getLayoutId() {
        return R.layout.ac_k3_san_bu_tong_dan;
    }
}
