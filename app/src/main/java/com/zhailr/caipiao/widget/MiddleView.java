package com.zhailr.caipiao.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;

/**
 * Created by zzh on 16/11/115.
 * 数据没有开发结束
 */

public class MiddleView extends ViewGroup {

    int initX = 0;
    int initY = 0;
    int x = 0, y = 0;
    int tempInitX = 0;
    int tempInitY = 0;

    boolean isRestart = false;
    String isRed = "red";
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<>();

    List<String> datas = new ArrayList<>();//存放红球

    public static int cellWitch = 80;
    public static int cellHeight = 80;
    private int borderRight = 0;  //  右边距  包括没展示区域的总宽度
    private int borderBottom = 0;//   底边距  包括没展示区域的总高度
    private Context mContext;
    private int s ;
//测试红球数据
    String[] test = {"4","7","9","12","20","30"};
    /**
     * 回调
     */
    public interface middleTouchEventListener {
        void middleOnTouchEvent(int gapX, int gapY);
    }

    private middleTouchEventListener eventListener;

    public void setMonTouchEventListener(middleTouchEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public MiddleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setFocusable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        removeAllViews();
        addData();
    }

    /**
     * 判断边界
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                tempInitX = initX;
                tempInitY = initY;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                int gapX = moveX - x;
                int gapY = moveY - y;
                initX = tempInitX + gapX;
                initY = tempInitY + gapY;
                checkBorder();
                scrollTo(-initX, -initY);
                if (eventListener != null) {
                    eventListener.middleOnTouchEvent(initX, initY);
                }
                break;
        }
        return true;
    }

    /**
     * 添加测试数据
     * "4","7","9","12","20","30"
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addData(){
        if (isRed.equals("red")){
            s = 33;
        }else if (isRed.equals("blue")){
            s = 16;
        }
//        String data_json = JsonTools.getJson(mContext,"test_data.json");
//        parseJson(data_json);

        int left = 0;
        int right = cellWitch;
        int top = 0;
        int bottom = cellHeight;
        for (int i = 0; i < s; i++) {//有33列
            int start = 0;//数字
            for (int j = 0; j < mList.size(); j++) { //行

                TextView t = new TextView(mContext);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,8,8);
                t.setLayoutParams(layoutParams);
                t.setGravity(CENTER);
                t.setTextSize(12);
                t.setHeight(MiddleView.cellHeight);
                t.setWidth(MiddleView.cellWitch);
                t.setPadding(0,20,0,0);//没有包裹布局是直接写死  要改
                if (isRed.equals("red")){
                    t.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                }else if (isRed.equals("blue")){
                    t.setTextColor(ContextCompat.getColor(mContext,R.color.blue));
                }
                t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.whiteball));
                if (isRestart == true){
                    start = 0;
                    isRestart = false;
                }
                //先将所有数字排出来
                start++;
                t.setText(String.valueOf(start));

                //添加所有期的中奖号码
                for (int k=0;k<mList.size();k++){//                    boolean isMSTOP = false;
                    datas.clear();
                    if (isRed.equals("red")){
                        datas.add(mList.get(j).getRed_num1());
                        datas.add(mList.get(j).getRed_num2());
                        datas.add(mList.get(j).getRed_num3());
                        datas.add(mList.get(j).getRed_num4());
                        datas.add(mList.get(j).getRed_num5());
                        datas.add(mList.get(j).getRed_num6());
                    }else if (isRed.equals("blue")){
                        datas.add(mList.get(j).getBlue_num());
                    }
                        //将所有的中奖号码全部拿出来进行对比
                        for (int m = 0;m<datas.size();m++){
                            if (i+1 == Integer.parseInt(datas.get(m))){
                                t.setText(datas.get(m));
                                t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                                if (isRed.equals("red")){
                                    t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.redball));
                                }else if (isRed.equals("blue")){
                                    t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.blueball));
                                }
                                isRestart = true;
                            }
                        }

                }

                t.layout(left, top, right, bottom);
                bottom += cellWitch;
                top += cellWitch;
                addView(t);
            }
            top = 0;
            right += cellWitch;
            left +=cellWitch;
            bottom = cellWitch;
        }
        //边界
        borderRight = s * cellWitch;
        borderBottom = mList.size() * cellHeight;
    }

//    private void parseJson(String data_json) {
//        try {
//            JSONArray jsonArray = new JSONArray(data_json);
//            for (int i= 0;i<jsonArray.length();i++){
//                JSONObject NAME = jsonArray.optJSONObject(i);
//                String data= NAME.getString("name");
//                //获取号码数组
//                JSONArray numArray = NAME.optJSONArray("data");
//                for (int s = 0 ; s <numArray.length();s++){
//                    String ss = (String) numArray.get(s);
//                    test_data.add(ss);
//                }
//                datas.addAll(test_data);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 检查边界
     */
    private void checkBorder() {

        if (initX >= 0) {
            initX = 0;
        }
        if (initY >= 0) {
            initY = 0;
        }
        if (initX < -borderRight + getWidth()) {
            initX = -borderRight + getWidth();
        }
        if (initY < -borderBottom + getHeight()) {
            initY = -borderBottom + getHeight();
        }
    }

    /**
     * 外部调用滚动
     */
    public void setScrollXY(int x, int y) {
        initX = x;
        initY = y;
        scrollTo(-initX,-initY);
    }
    //给中间部分传递数据
    public void setData(ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList, String Red){
        this.mList = mList;
        System.out.print(mList);
        if (mList.size() != 0){
            isRed = Red;
            addData();
        }
    }
}

