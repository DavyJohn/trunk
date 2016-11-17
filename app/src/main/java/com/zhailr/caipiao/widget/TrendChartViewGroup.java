package com.zhailr.caipiao.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zhailr.caipiao.R;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.QiShuTools;
import com.zhailr.caipiao.widget.scrollview.MyHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;


/**
 * Created by zhkqy on 15/8/11.
 */
public class TrendChartViewGroup extends RelativeLayout implements MiddleView.middleTouchEventListener, MyScrollView.OnScrollListener, MyHorizontalScrollView.OnHorizontalScrollListener {

    private Context mContext;
    private LinearLayout top_linearlayout,bottom_linearlayout,left_linearlayout;
    private MyHorizontalScrollView top_scrollview,bottom_scrollview;
    private MyScrollView left_scrollview;
    private MiddleView middleView;

    private List<String> data = new ArrayList<>();

    QiShuTools qiShuTools;//定义接口对象

    public void setQS(QiShuTools qiShuTools){
        this.qiShuTools = qiShuTools;
    }

    public TrendChartViewGroup(Context context) {
        super(context);
        initView(context);
        setFocusable(true);

    }

    public TrendChartViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View v = View.inflate(context, R.layout.view_trend_chart, null);
        findById(v);
        addData();
        addView(v);
    }

    private void findById(View v) {
        top_linearlayout = (LinearLayout) v.findViewById(R.id.top_linearlayout);
        bottom_linearlayout = (LinearLayout) v.findViewById(R.id.bottom_linearlayout);
        left_linearlayout = (LinearLayout) v.findViewById(R.id.left_linearlayout);

        top_scrollview = (MyHorizontalScrollView) v.findViewById(R.id.top_scrollview);
        bottom_scrollview = (MyHorizontalScrollView) v.findViewById(R.id.bottom_scrollview);
        left_scrollview = (MyScrollView) v.findViewById(R.id.left_scrollview);

        middleView = (MiddleView) v.findViewById(R.id.middle_view);
        left_scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);   //  根据不同手机适配  不可在第一条还能下拉
        middleView.setMonTouchEventListener(this);

        top_scrollview.setOnHorizontalScrollListener(this);
        bottom_scrollview.setOnHorizontalScrollListener(this);
        left_scrollview.setOnScrollListener(this);
    }

    /**
     * 添加数据 TOP and left and bottom边角数据
     */
    public void addData() {
        int num = Constant.v_line;
        System.out.print(num);
        Log.e("============v_line",Constant.v_line+"");
        top_linearlayout.removeAllViews();
        left_linearlayout.removeAllViews();
        bottom_linearlayout.removeAllViews();
        for (int i = 0; i < 33; i++) {
            TextView t = new TextView(mContext);
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            int random = i + 1;
            t.setText(String.valueOf(random));
            top_linearlayout.addView(t);
        }

        //期数
        for (int i = 0; i < Constant.v_line; i++) {

            TextView t = new TextView(mContext);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            int random = i + 1;
            t.setText(String.valueOf(random)+"期");
            left_linearlayout.addView(t);
        }
        //创建底部
        for (int i = 0; i < 33; i++) {
            final TextView t = new TextView(mContext);
            t.setTag("0");//未点击
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setWidth(80);
            t.setHeight(80);
            final int random = i + 1;
            t.setText(String.valueOf(random));
            bottom_linearlayout.addView(t);

            t.setOnClickListener(new OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    if (t.getTag().equals("0")){
                        Toast.makeText(mContext,"点击了"+t.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (data.size() >= 6){
                            Toast.makeText(mContext,"超出6个红球", Toast.LENGTH_SHORT).show();
                        }else {
                            t.setTag("1");//点击
                            data.add(t.getText().toString());
                            t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape));
                        }

                    }else if (t.getTag().equals("1")){
                        //初始化并删除list里面的这个元素
                        t.setTag("0");
                        t.setBackground(ContextCompat.getDrawable(mContext, Integer.parseInt(null)));
                        Toast.makeText(mContext,"取消了"+t.getText().toString(), Toast.LENGTH_SHORT).show();
                        removedata(data,t.getText().toString());
                    }

                }
            });
        }
    }

    @Override
    public void middleOnTouchEvent(final int initX, final int initY) {
        top_scrollview.scrollTo(-initX, 0);
        left_scrollview.scrollTo(0, -initY);
        bottom_scrollview.scrollTo(-initX,0);
    }

    @Override
    public void onScroll(int scrollX, int scrollY) {
        middleView.setScrollXY(-scrollX, -scrollY);
    }

    private void removedata(List<String> list, String num){
        for (int i=0;i<list.size();i++){
            if (num.equals(list.get(i))){
                list.remove(i);
                --i;
            }
        }
    }



}
