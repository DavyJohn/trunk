package com.zhailr.caipiao.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
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
    private LinearLayout top_linearlayout,left_linearlayout;
    private MyHorizontalScrollView top_scrollview;
    private MyScrollView left_scrollview;
    private MiddleView middleView;
    private String Tag;
    private int s,q;
    private List<String> data = new ArrayList<>();
    //双色球
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<>();
    //福彩直选记录
    private ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean> mFcsdList = new ArrayList<>();
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

        left_linearlayout = (LinearLayout) v.findViewById(R.id.left_linearlayout);

        top_scrollview = (MyHorizontalScrollView) v.findViewById(R.id.top_scrollview);

        left_scrollview = (MyScrollView) v.findViewById(R.id.left_scrollview);

        middleView = (MiddleView) v.findViewById(R.id.middle_view);
        left_scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);   //  根据不同手机适配  不可在第一条还能下拉
        middleView.setMonTouchEventListener(this);

        top_scrollview.setOnHorizontalScrollListener(this);
//        bottom_scrollview.setOnHorizontalScrollListener(this);
        left_scrollview.setOnScrollListener(this);
    }

    /**
     * 添加数据 TOP and left and bottom边角数据
     */
    public void addData() {

        if (Tag.equals("red")){
            s = 33;
            q = mList.size();
        }else if (Tag.equals("blue")){
            s = 16;
            q = mList.size();
        }else if (Tag.equals("zx")){
            s = 10;
            q = mFcsdList.size();
        }
        top_linearlayout.removeAllViews();
        left_linearlayout.removeAllViews();
        //顶部
        for (int i = 0; i < s; i++) {
            TextView t = new TextView(mContext);
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.zoushi_text_color));
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            if (Tag.equals("zx")){
                t.setText(String.valueOf(i));
            }else{
                int random = i + 1;
                t.setText(String.valueOf(random));
            }
            top_linearlayout.addView(t);
        }

        //期数
        for (int i = 0; i < q; i++) {
            TextView t = new TextView(mContext);
            t.setTextColor(ContextCompat.getColor(mContext,R.color.zoushi_text_color));
            t.setGravity(CENTER);
            t.setTextSize(12);
            t.setWidth(MiddleView.cellWitch);
            t.setHeight(MiddleView.cellHeight);
            if (Tag.equals("zx")){
                t.setText("第"+mFcsdList.get(i).getIssueNum().substring(4,7)+"期");
            }else {
                t.setText("第"+mList.get(i).getIssue_num().substring(4,7)+"期");
            }
            left_linearlayout.addView(t);
        }

    }

    @Override
    public void middleOnTouchEvent(final int initX, final int initY) {
        top_scrollview.scrollTo(-initX, 0);
        left_scrollview.scrollTo(0, -initY);
        //下面代码是让底部也一起滚动 但是这样会有小bug 当顶部滚动到末端 中间部分也是在最后但是 底部却不再末端 暂时将它注释掉 不跟他们一起滚动
//        bottom_scrollview.scrollTo(-initX,0);
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
        Log.e("=====removedata",list+"");
    }
    //定义一个期数方法 真不想说什么 希望后面的人看到 自己好好修改吧
    public void setQS(ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList,String string){
        this.mList = mList;
        if (mList.size() != 0){
            Tag = string;
            addData();
            middleView.setData(mList,string);
        }
    }

    //传入福彩直选数据和判断条件
    public void setFZ(ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean> mList,String string,String s){
        this.mFcsdList = mList;
        if (mList.size() != 0){
            Tag = string;
            addData();
            middleView.setFcZxData(mFcsdList,string,s);
        }
    }
}
