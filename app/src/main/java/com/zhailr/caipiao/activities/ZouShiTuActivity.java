package com.zhailr.caipiao.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.LotteryHall.DoubleNoramlBetActivity;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.BlueFragment;
import com.zhailr.caipiao.fragments.RedFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.bean.BetBean;
import com.zhailr.caipiao.model.response.CurrentNumResponse;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.MiddleView;
import com.zhailr.caipiao.widget.NoScrollViewPager;
import com.zhailr.caipiao.widget.scrollview.MyHorizontalScrollView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Response;

import static android.view.Gravity.CENTER;

/**
 * Created by 腾翔信息 on 2016/11/17.
 */

public class ZouShiTuActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ZouShiTuActivity";
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>();
    private NoScrollViewPager vp;
    private LinearLayout zoushitu,mChooseBall;
    private TextView mDisPalyText,mBuyText;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyHorizontalScrollView myHorizontalScrollView ,mChooseScrollView;
    private String isRed ="red";
    private int s = 33;
    private BigInteger zs;
    private BigInteger price;
    private List<String> data = new ArrayList<>();
    private List<String> blueData = new ArrayList<>();
    private boolean isDisPlay = false;
    ArrayList<BetBean> chooseList = new ArrayList<BetBean>();
    private int position = -1;
    private String currentNum;
    private SlidingTabLayout mTab;
    private String[] mTitle = {"红球走势","蓝球走势"};

    private MyPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("走势图");
        initData();
        addXuanHaoData();
        initIntent();
    }

    private void initIntent() {
        if (null != getIntent().getSerializableExtra("bean")) {// 点击进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("list");
            position = getIntent().getIntExtra("position", -1);
            data = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getRedList();
            blueData = (ArrayList<String>) ((BetBean) getIntent().getSerializableExtra("bean")).getBlueList();
//            for (int i = 0; i < blueData.size(); i++) {
//                TextView textView = mBlueAllList.get(Integer.valueOf(mBlueList.get(i)) - 1);
//                textView.setTag(0);
//                textView.setTextColor(getResources().getColor(R.color.white));
//                textView.setBackground(getResources().getDrawable(R.drawable.blueball));
//            }
//            for (int i = 0; i < mRedList.size(); i++) {
//                TextView textView = mRedAllList.get(Integer.valueOf(mRedList.get(i)) - 1);
//                textView.setTag(0);
//                textView.setTextColor(getResources().getColor(R.color.white));
//                textView.setBackground(getResources().getDrawable(R.drawable.redball));
//            }
//            changeZhusuAccount();
//            changeChooseBtn();
        } else if (null != getIntent().getSerializableExtra("List")) {// 从自选进入的
            chooseList = (ArrayList<BetBean>) getIntent().getSerializableExtra("List");
        }
    }

    private void initData(){
        getData();
        mTab = (SlidingTabLayout) findViewById(R.id.zoushitu_sliding);
        mDisPalyText = (TextView) findViewById(R.id.zoushitu_display_text);
        mBuyText = (TextView) findViewById(R.id.zoushitu_buy_text);
        mChooseBall = (LinearLayout) findViewById(R.id.chosed_ball);
//
        myHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.zoushitu_scrollview);
        mChooseScrollView = (MyHorizontalScrollView) findViewById(R.id.chosed_scrollview);
        zoushitu = (LinearLayout) findViewById(R.id.zoushitu_linearlayout);

        mBuyText.setOnClickListener(this);
        vp = (NoScrollViewPager) findViewById(R.id.vp);
        vp.setNoScroll(true);//设置为不能滑动
        Fragment redFargment = new RedFragment();
        Fragment blueFargment = new BlueFragment();
        mFragments.add(redFargment);
        mFragments.add(blueFargment);
        vp.setCurrentItem(0);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        vp.setAdapter(pagerAdapter);
        mTab.setViewPager(vp,mTitle);
        mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp.setCurrentItem(position);
                if (position == 0){
                    isRed = "red";
                }else if (position == 1){
                    isRed = "blue";
                }
                addXuanHaoData();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }
    private void getData(){
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SSQRECORD, null, TAG, new SpotsCallBack<SSQRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, SSQRecordResponse data) {
                if (null != data.getData()) {
                    mList = (ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>) data.getData().getHistorySsqList();
                    Constant.v_line = mList.size();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.zoushitu_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zoushitu_buy_text:
                //TODO 购买
                isDisPlay = true;
                toBuy(zs,price);
                break;
        }
    }

    //已选数据
    private void addData(List<String> redData,List<String> blueData){
        mChooseBall.removeAllViews();
        for (int i=0;i<redData.size()+blueData.size();i++){
            TextView mText = new TextView(mContext);
            mText.setGravity(CENTER);
            mText.setTextSize(12);
            mText.setWidth(MiddleView.cellWitch);
            mText.setHeight(MiddleView.cellHeight);
            mText.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            //区别 红蓝球
            if (redData.size() != 0 && i<redData.size()){
                mText.setText(redData.get(i));
                mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
            }else if (redData.size() == 0 ||i>=redData.size()){
                mText.setText(blueData.get(i-redData.size()));
                mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.blueball));
            }
            mChooseBall.addView(mText);
        }
    }

    //添加选号数据
    private void addXuanHaoData(){

        if (isRed.equals("red")){
            s = 33;
        }else if (isRed.equals("blue")){
            s = 16;
        }
        zoushitu.removeAllViews();

        for (int i =0;i<s;i++){
            final TextView t = new TextView(mContext);
            t.setTag("cancel");//未点击
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            if (isRed.equals("red")){
                t.setTextColor(ContextCompat.getColor(mContext,R.color.red));
            }else if (isRed.equals("blue")){
                t.setTextColor(ContextCompat.getColor(mContext,R.color.blue));
            }
            //设置背景会导致 宽度高度变化
            t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.whiteball));
            final int random = i + 1;
            t.setText(String.valueOf(random));
            //need to know data ？= null before addview()
            if (isRed == "red" && data.size() != 0){
                for (int a =0;a<data.size();a++){
                    if (random ==Integer.parseInt(data.get(a)) ){
                        t.setTag("redClick");
                        t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
                    }
                }
            }else if (isRed == "blue" && blueData.size() != 0){
                for (int b=0;b<blueData.size();b++){
                    if (random == Integer.parseInt(blueData.get(b))){
                        t.setTag("blueClick");
                        t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.blueball));
                    }
                }
            }
            zoushitu.addView(t);

            t.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    if (t.getTag().equals("cancel")){
                        if (data.size() >= 16){
                            Toast.makeText(mContext,"超出16个红球", Toast.LENGTH_SHORT).show();
                        }else {
                            if (isRed.equals("red")){
                                t.setTag("redClick");
                                data.add(t.getText().toString());
                            }else if (isRed.equals("blue")){
                                t.setTag("blueClick");
                                blueData.add(t.getText().toString());
                            }

                            addData(data,blueData);//添加数据
                            disPlay(data,blueData);
                            if (isRed.equals("red")){
                                t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
                            }else if (isRed.equals("blue")){
                                t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.blueball));
                            }
                            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        }

                    }else if (t.getTag().equals("redClick")||t.getTag().equals("blueClick")){
                        //初始化并删除list里面的这个元素
                        if (t.getTag().equals("redClick")){
                            removeRedData(data,t.getText().toString());
                        }else if (t.getTag().equals("blueClick")){
                            removeBlueData(blueData,t.getText().toString());
                        }
                        t.setTag("cancel");
                        t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.whiteball));
                        if (isRed.equals("red")){
                            t.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                        }else if (isRed.equals("blue")){
                            t.setTextColor(ContextCompat.getColor(mContext,R.color.blue));
                        }
                        Toast.makeText(mContext,"取消了"+t.getText().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void removeRedData(List<String> redData, String num){
        for (int i=0;i<redData.size();i++){
            if (num.equals(redData.get(i))){
                redData.remove(i);
                --i;
            }
        }
        addData(redData,blueData);
        disPlay(redData,blueData);
    }

    private void removeBlueData(List<String> blueDatas ,String num){
        for (int i=0;i<blueDatas.size();i++){
            if (num.equals(blueDatas.get(i))){
                blueDatas.remove(i);
                --i;
            }
        }
        addData(data,blueDatas);
        disPlay(data,blueDatas);
    }

    //双色球算法
    private void disPlay(List<String> red,List<String> blue){
        BigInteger m = BigInteger.valueOf(blue.size());
        BigInteger n = BigInteger.valueOf(red.size());
        zs = ((((((m.multiply(n)).multiply(n.subtract(new BigInteger("1")))).multiply(n.subtract(new BigInteger("2")))).multiply(n.subtract(new BigInteger("3")))).multiply(n.subtract(new BigInteger("4")))).multiply(n.subtract(new BigInteger("5")))).divide(new BigInteger("720"));
        if (zs.compareTo(new BigInteger("0")) != 0) {
            price = zs.multiply(new BigInteger("2"));
            mDisPalyText.setText("共 " + zs + " 注"+" "+price+" 元");
        } else {
            mDisPalyText.setText("共 " + 0 + " 注"+" "+0+" 元");
        }
        toBuy(zs,price);
    }

    private void toBuy(BigInteger zs,BigInteger price){
        getCurrentNum();
        if (isDisPlay == true){
            isDisPlay =false;
            if (zs.compareTo(new BigInteger("0")) == 0){
                showToast("请至少选择6个红球，1个蓝球");
            }else if (price.compareTo(new BigInteger("200000")) == 1){
                showToast("金额上限不能超过20万");
            }else {
                showToast("支付");
                BetBean bet = new BetBean();
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                Collections.sort(data, new Comparator<String>() {
                    public int compare(String arg0, String arg1) {
                        return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
                    }
                });
                for (int i = 0; i < data.size(); i++) {
                    sb1.append(data.get(i) + "  ");
                }
                for (int i = 0; i < blueData.size(); i++) {
                    sb2.append(blueData.get(i) + "  ");
                }
                bet.setRedNums(sb1.toString());
                bet.setBlueNums(sb2.toString());
                bet.setType("普通投注");
                bet.setPrice(price + "");
                bet.setZhu(zs + "");
                bet.setBlueList((ArrayList<String>) blueData);
                bet.setRedList((ArrayList<String>) data);
                System.out.print(bet);

                if (chooseList.size() != 0 && position != -1) {
                    chooseList.set(position, bet);
                } else {
                    chooseList.add(0, bet);
                }
                // TODO: 2016/11/25  继续选号 出问题
//                chooseList.add(0, bet);
                Intent intent = new Intent(this, DoubleNoramlBetActivity.class);
                intent.putExtra("list", chooseList);
                intent.putExtra("currentNum", currentNum);
                intent.putExtra("TAG",TAG);
                startActivity(intent);
                finish();
            }
        }

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
    private void getCurrentNum(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("type_code", "SSQ");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDNEWAWARD, map, TAG, new SpotsCallBack<CurrentNumResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, CurrentNumResponse res) {
                if (res.getCode().equals("200")) {
                    currentNum = res.getData().getIssue_num();
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
}
