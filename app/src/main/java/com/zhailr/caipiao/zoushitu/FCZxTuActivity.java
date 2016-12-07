package com.zhailr.caipiao.zoushitu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.FCZxTuHundredFragment;
import com.zhailr.caipiao.fragments.FCZxTuTenFragment;
import com.zhailr.caipiao.fragments.FCZxTuUnitFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.widget.MiddleView;
import com.zhailr.caipiao.widget.NoScrollViewPager;
import com.zhailr.caipiao.widget.scrollview.MyHorizontalScrollView;

import org.w3c.dom.Text;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

import static android.view.Gravity.CENTER;

/**
 * Created by 腾翔信息 on 2016/11/28.
 */

public class FCZxTuActivity extends BaseActivity {

    private static final String TAG = FCZxTuActivity.class.getSimpleName();
    @Bind(R.id.fc_zx_tu_tab)
    SlidingTabLayout mTab;
    @Bind(R.id.fc_zx_tu_vp)
    NoScrollViewPager mVp;
    @Bind(R.id.fc_zx_tu_text_num_tips)
    TextView mtextTips;

    @Bind(R.id.fc_zx_tu_scrollview)
    MyHorizontalScrollView mChooseScrollview;
    @Bind(R.id.fc_zx_tu_chosed_scrollview)
    MyHorizontalScrollView mDisPlayScrollView;
    @Bind(R.id.fc_zx_tu_linearlayout)
    LinearLayout mChooseLayout;
    @Bind(R.id.fc_zx_tu_chosed_ball)
    LinearLayout mDisPalyLayout;
    @Bind(R.id.fc_zx_tu_zoushitu_buy_text)
    TextView mBuyText;
    @OnClick(R.id.fc_zx_tu_zoushitu_buy_text) void buy (){
        showToast("");
    }
    private String[] mTitles = {"百位","十位","个位"};
    private List<Fragment> listFragment = new ArrayList<>();
    private MyPagerAdapter adapter;
    private List<String> hundereds = new ArrayList<>();
    private List<String> tens = new ArrayList<>();
    private List<String> units = new ArrayList<>();
    private String CONDITION = "100";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("走势图");
        initView();
        setData();
    }


    private void initView(){

        mVp.setNoScroll(true);

        Fragment hunder = new FCZxTuHundredFragment();
        Fragment ten = new FCZxTuTenFragment();
        Fragment unit = new FCZxTuUnitFragment();

        listFragment.add(hunder);
        listFragment.add(ten);
        listFragment.add(unit);
        mVp.setCurrentItem(0);
        mtextTips.setText("百位：");
        adapter = new MyPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return listFragment.get(position);
            }

            @Override
            public int getCount() {
                return listFragment.size();
            }
        };
        mVp.setAdapter(adapter);
        mTab.setViewPager(mVp,mTitles);
        mTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mVp.setCurrentItem(position);
                switch (position){
                    case 0:
                        mtextTips.setText("百位：");
                        CONDITION = "100";
                        break;
                    case 1:
                        mtextTips.setText("十位：");
                        CONDITION = "10";
                        break;
                    case 2:
                        mtextTips.setText("个位：");
                        CONDITION = "1";
                        break;
                }
                setData();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }


    private void setData(){
        mChooseLayout.removeAllViews();
        for (int i=0;i<10;i++){
            final TextView mText = new TextView(mContext);
            mText.setTag("cancel");//未点击
            mText.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            mText.setGravity(CENTER);
            mText.setTextSize(12);
            mText.setWidth(MiddleView.cellWitch);
            mText.setHeight(MiddleView.cellHeight);
            mText.setTextColor(ContextCompat.getColor(mContext,R.color.black));
            mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.whiteball));
            mText.setText(String.valueOf(i));
            switch (CONDITION){
                case "100":
                    if (hundereds.size() != 0){
                        for (int m=0;m<hundereds.size();m++){
                            if (i == Integer.parseInt(hundereds.get(m))){
                                mText.setTag("click");
                                mText.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                                mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
                            }
                        }
                    }
                    break;
                case "10":
                    if (hundereds.size() != 0){
                        for (int m=0;m<tens.size();m++){
                            if (i == Integer.parseInt(tens.get(m))){
                                mText.setTag("click");
                                mText.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                                mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
                            }
                        }
                    }
                    break;
                case "1":
                    if (hundereds.size() != 0){
                        for (int m=0;m<units.size();m++){
                            if (i == Integer.parseInt(units.get(m))){
                                mText.setTag("click");
                                mText.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                                mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
                            }
                        }
                    }
                    break;
            }

            mChooseLayout.addView(mText);
            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mText.getTag().equals("cancel")){
                        // TODO: 2016/12/7 添加选中数据
                        mText.setTag("click");
                        mText.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
                        switch (CONDITION){
                            case "100":
                                hundereds.add(mText.getText().toString());
                                break;
                            case "10":
                                tens.add(mText.getText().toString());
                                break;
                            case "1":
                                units.add(mText.getText().toString());
                                break;
                        }
                        disPaly(hundereds,tens,units);

                    }else if (mText.getTag().equals("click")){
                        // TODO: 2016/12/7  删除选中数据
                        mText.setTag("cancel");
                        mText.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                        mText.setBackground(ContextCompat.getDrawable(mContext,R.drawable.whiteball));
                        switch (CONDITION){
                            case "100":
                                removeData(hundereds,mText.getText().toString());
                                break;
                            case "10":
                                removeData(tens,mText.getText().toString());
                                break;
                            case "1":
                                removeData(units,mText.getText().toString());
                                break;
                        }
                        disPaly(hundereds,tens,units);
                    }

                }
            });
        }
    }

    // TODO: 2016/12/7  绘制结果
    private void disPaly(List<String> data1 ,List<String> data2 ,List<String> data3){
        mDisPalyLayout.removeAllViews();
        TextView mText  = new TextView(mContext);
        String hun = "" ;
        String ten = "";
        String un = "";

//        Log.e("====================1",data1+"");
//        Log.e("====================2",data2+"");
//        Log.e("====================3",data3+"");

        for (String d1 : data1){
            hun = hun+d1+","+"";
        }
        for (String d2 : data2){
            ten = ten+d2+","+"";
        }
        for (String d3 : data3){
            un = un+d3+","+"";
        }
        if (data1.size() == 0 ||data2.size() == 0 || data3.size() == 0){

        }else {
            mText.setText("百位："+hun+"十位："+ten+"个位"+un);
            mDisPalyLayout.addView(mText);
        }
        Log.e("====================4",hun);
    }
    @Override
    public int getLayoutId() {
        return R.layout.fc_zx_tu_layout;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }
    }

    private void removeData(List<String> data, String  num){
        for (int i=0;i<data.size();i++){
            if (num.equals(data.get(i))){
                data.remove(i);
                --i;
            }
        }
    }

    // 显示共多少注，共多少钱
//    private void changeZhusuAccount() {
//        // 算法：m*n*w
//        int n = hundereds.size();
//        int m = tens.size();
//        int w = units.size();
//
//        zs = m * n * w;
//        if (zs != 0) {
//            price = zs * 2;
//            tvZhu.setText("共 " + zs + " 注");
//            tvPrice.setText(" " + price + " 元");
//        } else {
//            tvZhu.setText("");
//            tvPrice.setText("");
//        }
//    }
}
