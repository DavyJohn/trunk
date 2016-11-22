package com.zhailr.caipiao.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.BlueFragment;
import com.zhailr.caipiao.fragments.RedFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.NoScrollViewPager;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2016/11/17.
 */

public class ZouShiTuActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ZouShiTuActivity.class.getSimpleName();
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>();
    private NoScrollViewPager vp;
    private LinearLayout redBall,blueBall;
    private TextView mRedBallText,mBlueBallText;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("走势图");
        initData();
    }


    private void initData(){
        getData();
        redBall = (LinearLayout) findViewById(R.id.red_ball);
        blueBall = (LinearLayout) findViewById(R.id.blue_ball);
        mRedBallText = (TextView) findViewById(R.id.red_ball_text);
        mBlueBallText = (TextView) findViewById(R.id.blue_ball_text);
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
                mRedBallText.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
                vp.setCurrentItem(0);
                mRedBallText.setTextSize(16);
                break;
            case 1:
                mBlueBallText.setTextColor(ContextCompat.getColor(this,R.color.colorAccent));
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
//                Constant.isRED = 0;
                break;
            case R.id.blue_ball:
                setTab(1);
//                Constant.isRED =1 ;
                break;
        }
    }
}
