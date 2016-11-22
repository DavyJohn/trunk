package com.zhailr.caipiao.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
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

    private String isRed;
    private int s;
    private List<String> data = new ArrayList<>();
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<>();
    private int qs =0;
    QiShuTools qiShuTools;//定义接口对象 未开发完全

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
        if (isRed.equals("red")){
            s = 33;
        }else if (isRed.equals("blue")){
            s = 16;
        }
        top_linearlayout.removeAllViews();
        left_linearlayout.removeAllViews();
        bottom_linearlayout.removeAllViews();
        for (int i = 0; i < s; i++) {
            TextView t = new TextView(mContext);
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.zoushi_text_color));
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            int random = i + 1;
            t.setText(String.valueOf(random));
            top_linearlayout.addView(t);
        }

        //期数
        for (int i = 0; i < mList.size(); i++) {
            TextView t = new TextView(mContext);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.zoushi_text_color));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setPadding(0,15,0,0);
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            t.setText("第"+mList.get(i).getIssue_num().substring(4,7)+"期");
            left_linearlayout.addView(t);
        }
        //创建底部
        for (int i = 0; i < s; i++) {
            final TextView t = new TextView(mContext);
            t.setTag("0");//未点击
            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.red));
            t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.whiteball));
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
                            t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.redball));
                            t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        }

                    }else if (t.getTag().equals("1")){
                        //初始化并删除list里面的这个元素
                        t.setTag("0");
                        t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.whiteball));
                        t.setTextColor(ContextCompat.getColor(mContext,R.color.red));
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
        //下面代码是让底部也一起滚动 但是这样会有小bug 当顶部滚动到末端 中间部分也是在最后但是 底部却不再末端 暂时将它注释掉 不跟他们一起滚动
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
    //定义一个期数方法 真不想说什么 希望后面的人看到 自己好好修改吧
    public void setQS(ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList,String Red){
        this.mList = mList;
        if (mList.size() != 0){
            isRed = Red;
            addData();
            middleView.setData(mList,Red);
        }
    }

}
