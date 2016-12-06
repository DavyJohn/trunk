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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.WebViewActivity;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.adapter.SimpleAdapter;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.ShakeListener;
import com.zhailr.caipiao.zoushitu.FCZxTuActivity;

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
import zhy.com.highlight.position.OnLeftPosCallback;
import zhy.com.highlight.shape.RectLightShape;

/**
 * Created by zhailiangrong on 16/7/8.
 */
public class FC3DNoramlActivity extends BaseActivity {
    private static final String TAG = "FC3DNoramlActivity";
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.layout_ball_bai)
    LinearLayout layoutBallBai;
    @Bind(R.id.layout_ball_shi)
    LinearLayout layoutBallShi;
    @Bind(R.id.layout_ball_ge)
    LinearLayout layoutBallGe;
    @Bind(R.id.tv_clear)
    TextView tvClear;
    @Bind(R.id.tv_zhu)
    TextView tvZhu;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.ac_fc3d_floating_action_button)
    FloatingActionButton mFloatButton;
    // 代码创建的layout
    private LinearLayout m_LinearLayout;

    private HighLight mHightLight;

    private ArrayList<String> mRedList1 = new ArrayList<String>();
    private ArrayList<String> mRedList2 = new ArrayList<String>();
    private ArrayList<String> mRedList3 = new ArrayList<String>();
    private ArrayList<TextView> mRedClickList1 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedClickList2 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedClickList3 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList1 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList2 = new ArrayList<TextView>();
    private ArrayList<TextView> mRedAllList3 = new ArrayList<TextView>();
    // 注数
    private int zs;
    private int price;
    private int position = -1;
    private static final int START = 0;
    // 点确定后，需要传递的list
    ArrayList<BetBean> chooseList = new ArrayList<BetBean>();
    private boolean hasMeasured;
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
        Constant.isClick = false;
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.fc3d_select_paly);
        getCurrentNum();
        shake();

        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.isClick = true;
                startActivity(new Intent(FC3DNoramlActivity.this,ShakeActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(mShakeListener!=null)
            mShakeListener.stop();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 因为在onCreat里，layout还没有被画出来，无法计算宽度，所以要在页面获取焦点后才能计算
        // 加个判断，防止重复调用多次
//        if (!hasMeasured) {
//            initUI();
//            hasMeasured = true;
//        }
        if (mRedAllList1.size() == 0) {
            initUI();
            initIntent();
        }
    }

    private void getCurrentNum() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "FCSD");
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

    private void initIntent() {
        clearClickBall();
        if (null != getIntent().getSerializableExtra("bean")) {// 点击进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("position", -1);
            mRedList1 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList();
            mRedList2 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList2();
            mRedList3 = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList3();
            for (int i=0; i < mRedList1.size(); i++) {
                TextView textView = mRedAllList1.get(Integer.valueOf(mRedList1.get(i)));
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.redball));
            }
            for (int i=0; i < mRedList2.size(); i++) {
                TextView textView = mRedAllList2.get(Integer.valueOf(mRedList2.get(i)));
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.redball));
            }
            for (int i=0; i < mRedList3.size(); i++) {
                TextView textView = mRedAllList3.get(Integer.valueOf(mRedList3.get(i)));
                textView.setTag(0);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackground(getResources().getDrawable(R.drawable.redball));
            }
            changeZhusuAccount();
        } else if (null != getIntent().getSerializableExtra("List")) {// 从自选进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        }
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

        int redNum = 10;
        int layoutwidth = layoutBallBai.getWidth();
        Log.i(TAG, "layoutwidth:" + layoutwidth);
        initBallView(redNum, margin, diameter, layoutwidth);
    }

    private void initBallView(int redNum, int margin, int diameter, int layoutwidth) {
        int col = 0;
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

        // 画百位红球的view
        drawBallView(col, redRow, diameter, margin, redNum, "1", layoutBallBai);
        // 画十位红球的view
        drawBallView(col, redRow, diameter, margin, redNum, "2", layoutBallShi);
        // 画个位红球的view
        drawBallView(col, redRow, diameter, margin, redNum, "3", layoutBallGe);

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
                    redBall.setText("" + j);
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
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList3.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.whiteball));

                    redBall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clickRedBall((TextView) v, tag);
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
                    redBall.setText("" + j);
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
                        redBall.setTextColor(getResources().getColor(R.color.red));
                        mRedAllList3.add(redBall);
                    }
                    redBall.setBackground(getResources().getDrawable(R.drawable.whiteball));
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
                        } else if (Constant.isClick == false) {
                        }
                        handler.obtainMessage(START).sendToTarget();

                    }
                };
                thread.start();
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

    private void clickRedBall(TextView view, String tag) {
        if ( view.getTag() == null || view.getTag() == "0") {
            // 选中
            view.setTag("1");
            view.setTextColor(getResources().getColor(R.color.white));
            view.setBackground(getResources().getDrawable(R.drawable.redball));
            if (tag.equals("1")) {
                mRedList1.add(view.getText().toString());
                mRedClickList1.add(view);
            } else if (tag.equals("2")) {
                mRedList2.add(view.getText().toString());
                mRedClickList2.add(view);
            } else if (tag.equals("3")) {
                mRedList3.add(view.getText().toString());
                mRedClickList3.add(view);
            }
        } else {
            // 取消
            view.setTag("0");
            view.setTextColor(getResources().getColor(R.color.red));
            view.setBackground(getResources().getDrawable(R.drawable.whiteball));
            if (tag.equals("1")) {
                mRedList1.remove(view.getText().toString());
                mRedClickList1.remove(view);
            } else if (tag.equals("2")) {
                mRedList2.remove(view.getText().toString());
                mRedClickList2.remove(view);
            } else if (tag.equals("3")) {
                mRedList3.remove(view.getText().toString());
                mRedClickList3.remove(view);
            }
        }
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 50}, -1);
        changeZhusuAccount();
        changeChooseBtn();
    }

    // 显示共多少注，共多少钱
    private void changeZhusuAccount() {
        // 算法：m*n*w
        int n = mRedList1.size();
        int m = mRedList2.size();
        int w = mRedList3.size();

        zs = m * n * w;
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
        for (int i=0; i < mRedClickList3.size(); i++) {
            TextView textView = mRedClickList3.get(i);
            textView.setTag(1);
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setBackground(getResources().getDrawable(R.drawable.whiteball));
        }
        tvZhu.setText("");
        tvPrice.setText("");
        mRedList1.clear();
        mRedList2.clear();
        mRedList3.clear();
        mRedClickList1.clear();
        mRedClickList2.clear();
        mRedClickList3.clear();
        changeChooseBtn();
    }

    private void changeChooseBtn() {
        if (mRedList1.size()!=0 || mRedList2.size()!=0 || mRedList3.size()!=0) {
            tvClear.setText("清空");
            tvClear.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvClear.setText("机选");
            tvClear.setTextColor(getResources().getColor(R.color.yellow));
        }
    }

    // 机选一注
    private void autoChooseOne(int num) {
        for (int n=0; n<num; n++) {
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
            chooseList.add(0, bet);
        }
        Intent intent = new Intent(this, FC3DNormalBetActivity.class);
        intent.putExtra("list", chooseList);
        intent.putExtra("currentNum", currentNum);
        intent.putExtra("tag", TAG);
        startActivity(intent);
        finish();

    }


    @Override
    public int getLayoutId() {
        return R.layout.ac_fc3d_select;
    }

    @OnClick({R.id.tv_clear, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                if (tvClear.getText().equals("机选")) {
                    DialogPlus dialog = DialogPlus.newDialog(this)
                            .setContentHolder(new GridHolder(3))
                            .setAdapter(new SimpleAdapter(FC3DNoramlActivity.this, true))
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
                if (zs != 0) {
                    Intent intent = new Intent(this, FC3DNormalBetActivity.class);
                    BetBean bet = new BetBean();
                    StringBuffer sb1 = new StringBuffer();
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
                    Collections.sort(mRedList3, new Comparator<String>() {
                        public int compare(String arg0, String arg1) {
                            return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
                        }
                    });
                    for (int i=0; i < mRedList1.size(); i++) {
                        sb1.append(mRedList1.get(i) + "  ");
                    }
                    sb1.append("|  ");
                    for (int i=0; i < mRedList2.size(); i++) {
                        sb1.append(mRedList2.get(i) + "  ");
                    }
                    sb1.append("|  ");
                    for (int i=0; i < mRedList3.size(); i++) {
                        sb1.append(mRedList3.get(i) + "  ");
                    }
                    bet.setRedNums(sb1.toString());
                    bet.setBlueNums("");
                    bet.setType("直选投注");
                    bet.setPrice(price + "");
                    bet.setZhu(zs + "");
                    bet.setRedList(mRedList1);
                    bet.setRedList2(mRedList2);
                    bet.setRedList3(mRedList3);
                    if (chooseList.size() != 0 && position != -1) {
                        chooseList.set(position, bet);
                    } else {
                        chooseList.add(0, bet);
                    }
                    intent.putExtra("list", chooseList);
                    intent.putExtra("currentNum", currentNum);
                    intent.putExtra("tag", TAG);
                    startActivity(intent);
                    finish();
                } else {
                    showToast("请至少选择一注");
                }

                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zoushi,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.zoushi:
                Intent intent = new Intent(mContext, FCZxTuActivity.class);
                startActivity(intent);
                break;
            case R.id.zhidao:
                showNextKnownTipView();
                break;
            case R.id.introduce:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("TAG","FCSD");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //玩法指导
    public void showNextKnownTipView(){
        mHightLight = new HighLight(FC3DNoramlActivity.this)
                .anchor(findViewById(R.id.ac_fc3d_rootview))
                .addHighLight(R.id.layout_ball_bai,R.layout.info_gravity_left_down,new OnBottomPosCallback(60),new RectLightShape())
                .addHighLight(R.id.layout_ball_shi,R.layout.info_gravity_left_down,new OnBottomPosCallback(60),new RectLightShape())
                .addHighLight(R.id.layout_ball_ge,R.layout.info_gravity_left_down,new OnBottomPosCallback(60),new RectLightShape())
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
