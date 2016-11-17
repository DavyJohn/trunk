package com.zhailr.caipiao.activities.LotteryHall;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
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
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/7.
 */
public class DoubleColorDantuoActivity extends BaseActivity {
    private static final String TAG = "DoubleColorDantuoActivity";
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.layout_red_ball_dan)
    LinearLayout layoutRedBallDan;
    @Bind(R.id.layout_red_ball_tuo)
    LinearLayout layoutRedBallTuo;
    @Bind(R.id.layout_blue_ball)
    LinearLayout layoutBlueBall;
    @Bind(R.id.tv_zhu)
    TextView tvZhu;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.ok)
    TextView ok;
    // 代码创建的layout
    private LinearLayout m_LinearLayout;

    private ArrayList<String> mRedList1 = new ArrayList<String>();
    private ArrayList<String> mRedList2 = new ArrayList<String>();
    private ArrayList<String> mBlueList = new ArrayList<String>();
    private ArrayList<TextView> mRedClickList1 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedClickList2 = new ArrayList<TextView>();
    private ArrayList<TextView> mBlueClickList = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList1 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList2 = new ArrayList<TextView>();
    private ArrayList<TextView> mBlueAllList = new ArrayList<TextView>();
    // 注数
    private int zs;
    private int price;
    private int position = -1;
    // 点确定后，需要传递的list
    ArrayList<BetBean> chooseList = new ArrayList<BetBean>();
    private String currentNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle(R.string.double_dan_tuo_play);
        getCurrentNum();
        initUI();
        initIntent();

    }

    private void getCurrentNum() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "SSQ");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                if (res.getCode().equals("200")) {
                    currentNum = res.getData().getIssue_num();
                    tvTime.setText("第" + currentNum + "期  " + "截止:" + res.getData().getTime_draw());
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

    private void initUI() {
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

        // 画胆码区红球的view
        drawBallView(col, redRow, diameter, margin, redNum, "1", layoutRedBallDan);
        // 画拖码区红球的view
        drawBallView(col, redRow, diameter, margin, redNum, "2", layoutRedBallTuo);
        // 画蓝球的view
        drawBallView(col, blueRow, diameter, margin, blueNum, "3", layoutBlueBall);

    }

    private void drawBallView(int col, int row, int diameter, int margin, int num, final String tag, LinearLayout linearLayout) {
        for (int i = 0; i < row; i++) {
            m_LinearLayout = new LinearLayout(this);//创建LinearLayout布局对象
            m_LinearLayout.setOrientation(LinearLayout.HORIZONTAL);//设置水平
            if (i != row - 1) {
                for (int j = i * col; j < i * col + col; j++) {
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
                    if (tag.equals("1")) {
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList1.add(redBall);
                    } else if (tag.equals("2")) {
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList2.add(redBall);
                    } else if (tag.equals("3")) {
                        redBall.setTextColor(getResources().getColor(R.color.blue));
                        mBlueAllList.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.whiteball));

                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tag.equals("1") || tag.equals("2")) {
                                clickRedBall((TextView) v, tag);
                            } else if (tag.equals("3")) {
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
                    if (tag.equals("1")) {
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList1.add(redBall);
                    } else if (tag.equals("2")) {
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList2.add(redBall);
                    } else if (tag.equals("3")) {
                        redBall.setTextColor(getResources().getColor(R.color.blue));
                        mBlueAllList.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.whiteball));
                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tag.equals("1") || tag.equals("2")) {
                                clickRedBall((TextView) v, tag);
                            } else if (tag.equals("3")) {
                                clickBlueBall((TextView) v);
                            }
                        }
                    });
                    m_LinearLayout.addView(redBall);
                }
            }
            linearLayout.addView(m_LinearLayout);

        }
    }

    private void clickRedBall(TextView view, String tag) {
        if ( view.getTag() == null ||view.getTag() == "0") {
            if (checkRedNum(tag)) {
                if (checkCanClick(tag, view.getText().toString())) {
                    // 选中
                    view.setTag("1");
                    view.setTextColor(getResources().getColor(R.color.white));
                    view.setBackground(getResources().getDrawable(R.drawable.redball));
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
                String toast = tag.equals("1") ? "胆码区最多只能选5个" : "拖码区最多只能选16个";
                showToast(toast);
            }

        } else {
            // 取消
            view.setTag("0");
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.whiteball));
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
        if (mRedList1.size() + mRedList2.size() > 6) {
            changeZhusuAccount();
        }else{
            tvZhu.setText("");
            tvPrice.setText("");
        }

    }

    private boolean checkRedNum(String tag) {
        return tag.equals("1") ? !(mRedList1.size()>4) : !(mRedList2.size() > 15);
    }

    private boolean checkCanClick(String tag, String text) {
        return tag.equals("1") ? !mRedList2.contains(text) : !mRedList1.contains(text);
    }

    private void clickBlueBall(TextView view) {
        if (view.getTag() == null ||view.getTag() == "0") {
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
        if (mRedList1.size() + mRedList2.size() > 6) {
            changeZhusuAccount();
        }
    }

    // 显示共多少注，共多少钱
    private void changeZhusuAccount() {
        //设红色球区胆码个数为n（1≤n≤5），红色球区拖码个数为m（7－n≤m≤20），
        // 蓝色球区所选个数为w，则此胆拖投注的注数个数为：combin(m,6-n)×combin(w,1)。
        int w = mBlueList.size();
        int n = mRedList1.size();
        int m = mRedList2.size();

        //    a是m！
        BigInteger a = new BigInteger("1");
        for (int i = 1; i < m + 1; i++) {
            a = a.multiply(new BigInteger(String.valueOf(i)));
        }

        //    b是(6-n)!
        BigInteger b = new BigInteger("1");
        for (int i = 1; i < 6 - n + 1; i++) {
            b = b.multiply(new BigInteger(String.valueOf(i)));
        }

        //    c是(m-7+n)!
        int p = (m - 6 + n);
        BigInteger c = new BigInteger("1");
        for (int i = 1; i < p + 1; i++) {
            c = c.multiply(new BigInteger(String.valueOf(i)));
        }

        zs = (a.multiply(new BigInteger(String.valueOf(w)))).divide((b.multiply(c))).intValue();
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
        for (int i=0; i < mBlueClickList.size(); i++) {
            TextView textView = mBlueClickList.get(i);
            textView.setTag(1);
            textView.setTextColor(getResources().getColor(R.color.blue));
            textView.setBackground(getResources().getDrawable(R.drawable.whiteball));
        }
        for (int i=0; i < mRedClickList1.size(); i++) {
            TextView textView = mRedClickList1.get(i);
            textView.setTag(1);
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setBackground(getResources().getDrawable(R.drawable.whiteball));
        }
        for (int i=0; i < mRedClickList2.size(); i++) {
            TextView textView = mRedClickList2.get(i);
            textView.setTag(1);
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setBackground(getResources().getDrawable(R.drawable.whiteball));
        }
        tvZhu.setText("");
        tvPrice.setText("");
        mRedList1.clear();
        mRedList2.clear();
        mBlueList.clear();
        mRedClickList1.clear();
        mRedClickList2.clear();
        mBlueClickList.clear();
    }

    private void initIntent() {
        clearClickBall();
        if (null != getIntent().getSerializableExtra("bean")) {// 点击进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("position", -1);
            mRedList1 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList();
            mRedList2 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList2();
            mBlueList = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getBlueList();
            for (int i=0; i < mBlueList.size(); i++) {
                TextView textView = mBlueAllList.get(Integer.valueOf(mBlueList.get(i))-1);
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.blueball));
            }
            for (int i=0; i < mRedList1.size(); i++) {
                TextView textView = mRedAllList1.get(Integer.valueOf(mRedList1.get(i))-1);
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.redball));
            }
            for (int i=0; i < mRedList2.size(); i++) {
                TextView textView = mRedAllList2.get(Integer.valueOf(mRedList2.get(i))-1);
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.redball));
            }
            changeZhusuAccount();
        } else if (null != getIntent().getSerializableExtra("List")) {// 从自选进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        }
    }


    @OnClick({R.id.tv_clear, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                // 清空
                clearClickBall();
                break;
            case R.id.ok:
                if (StringUtils.isEmpty(tvPrice.getText())) {
                    showToast("请至少选择7个红球，1个蓝球");
                } else if (price > 200000) {
                    showToast("金额上限不能超过20万");
                } else if (mRedClickList1.size() == 0){
                    showToast("请至少选择一个胆码");
                }else {
                    Intent intent = new Intent(this, DoubleDantuoBetActivity.class);
                    BetBean bet = new BetBean();
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    // 对号码进行排序
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
                    Collections.sort(mBlueList, new Comparator<String>() {
                        public int compare(String arg0, String arg1) {
                            return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
                        }
                    });
                    for (int i=0; i < mRedList1.size(); i++) {
                        sb1.append(mRedList1.get(i) + "  ");
                    }
                    sb1.append("#  ");
                    for (int i=0; i < mRedList2.size(); i++) {
                        sb1.append(mRedList2.get(i) + "  ");
                    }
                    for (int i=0; i < mBlueList.size(); i++) {
                        sb2.append(mBlueList.get(i) + "  ");
                    }
                    bet.setRedNums(sb1.toString());
                    bet.setBlueNums(sb2.toString());
                    bet.setType("胆拖投注");
                    bet.setPrice(price + "");
                    bet.setZhu(zs + "");
                    bet.setBlueList(mBlueList);
                    bet.setRedList(mRedList1);
                    bet.setRedList2(mRedList2);
                    if (chooseList.size() != 0 && position != -1) {
                        chooseList.set(position, bet);
                    } else {
                        chooseList.add(0, bet);
                    }
                    intent.putExtra("list", chooseList);
                    intent.putExtra("currentNum", currentNum);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_double_dan_tuo;
    }
}
