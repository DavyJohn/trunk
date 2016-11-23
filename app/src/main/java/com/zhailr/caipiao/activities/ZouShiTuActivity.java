package com.zhailr.caipiao.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.BlueFragment;
import com.zhailr.caipiao.fragments.RedFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.MiddleView;
import com.zhailr.caipiao.widget.NoScrollViewPager;
import com.zhailr.caipiao.widget.scrollview.MyHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static android.view.Gravity.CENTER;

/**
 * Created by 腾翔信息 on 2016/11/17.
 */

public class ZouShiTuActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ZouShiTuActivity.class.getSimpleName();
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>();
    private NoScrollViewPager vp;
    private LinearLayout redBall,blueBall,zoushitu,mChooseBall;
    private TextView mRedBallText,mBlueBallText;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter pagerAdapter;
    private MyHorizontalScrollView myHorizontalScrollView;
    private String isRed ="red";
    private int s = 33;
    private List<String> data = new ArrayList<>();
    private List<String> blueData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("走势图");
        initData();
        addXuanHaoData();
    }


    private void initData(){
        getData();
        mChooseBall = (LinearLayout) findViewById(R.id.chosed_ball);
        redBall = (LinearLayout) findViewById(R.id.red_ball);
        blueBall = (LinearLayout) findViewById(R.id.blue_ball);
        mRedBallText = (TextView) findViewById(R.id.red_ball_text);
        mBlueBallText = (TextView) findViewById(R.id.blue_ball_text);
        myHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.zoushitu_scrollview);
        zoushitu = (LinearLayout) findViewById(R.id.zoushitu_linearlayout);
        redBall.setOnClickListener(this);
        blueBall.setOnClickListener(this);
        vp = (NoScrollViewPager) findViewById(R.id.vp);
        vp.setNoScroll(true);//设置为不能滑动
        Fragment redFargment = new RedFragment();
        Fragment blueFargment = new BlueFragment();
        mFragments.add(redFargment);
        mFragments.add(blueFargment);
        setTab(0);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int num = vp.getCurrentItem();
                setTab(num);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    private void setTab(int index){
        clean();
        switch (index){
            case 0:
                mRedBallText.setTextColor(ContextCompat.getColor(this,R.color.red));
                vp.setCurrentItem(0);
                mRedBallText.setTextSize(16);
                break;
            case 1:
                mBlueBallText.setTextColor(ContextCompat.getColor(this,R.color.blue));
                mBlueBallText.setTextSize(16);
                vp.setCurrentItem(1);
                break;
        }
    }

    private void clean(){
        mRedBallText.setTextColor(ContextCompat.getColor(this,R.color.black));
        mRedBallText.setTextSize(12);
        mBlueBallText.setTextColor(ContextCompat.getColor(this,R.color.black));
        mBlueBallText.setTextSize(12);

    }

    @Override
    public int getLayoutId() {
        return R.layout.zoushitu_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.red_ball:
                setTab(0);
                isRed = "red";
                addXuanHaoData();
                break;
            case R.id.blue_ball:
                setTab(1);
                isRed = "blue";
                addXuanHaoData();
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
        //redData != null || blueData != null


        //redData == null && blueData == null
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
                        Toast.makeText(mContext,"点击了"+t.getText().toString(), Toast.LENGTH_SHORT).show();
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
                            Log.e("=====data",data+"");
                            Log.e("=====bluedata",blueData+"");
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
    private void removeRedData(List<String> list, String num){
        for (int i=0;i<list.size();i++){
            if (num.equals(list.get(i))){
                list.remove(i);
                --i;
            }
        }
        Log.e("=====removereddata",list+"");
        addData(list,blueData);
    }

    private void removeBlueData(List<String> list ,String num){
        for (int i=0;i<list.size();i++){
            if (num.equals(list.get(i))){
                list.remove(i);
                --i;
            }
        }
        Log.e("=====removebluedata",list+"");
        addData(list,blueData);
    }
}
