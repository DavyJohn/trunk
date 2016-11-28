package com.zhailr.caipiao.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;
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
    String TAG = "red";
    String TAG2 ="100";
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<>();
    //福彩直选记录
    private ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean> mFcsdList = new ArrayList<>();
    List<String> datas = new ArrayList<>();//存放红球
    List<String> nums = new ArrayList<>();
    public static int cellWitch =100;
    public static int cellHeight= 100;
    private int borderRight = 0;  //  右边距  包括没展示区域的总宽度
    private int borderBottom = 0;//   底边距  包括没展示区域的总高度
    private Context mContext;
    private int s ,q;
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
        if (TAG.equals("red")){
            s = 33;
            q = mList.size();
        }else if (TAG.equals("blue")){
            s = 16;
            q = mList.size();
        }else if (TAG.equals("zx")){
            s = 10;
            q = mFcsdList.size();
        }
        int left = 0;
        int right = cellWitch;
        int top = 0;
        int bottom = cellHeight;
        for (int i = 0; i < s; i++) {//有33列
            int start = 0;//数字
            for (int j = 0; j < q; j++) { //行
                TextView t = new TextView(mContext);
                t.setGravity(CENTER);
                t.setTextSize(12);
                t.setHeight(MiddleView.cellHeight);
                t.setWidth(MiddleView.cellWitch);
                t.setPadding(0,20,0,0);//没有包裹布局是直接写死  要改
                if (TAG.equals("red")){
                    t.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                }else if (TAG.equals("blue")){
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
                for (int k=0;k<q;k++){//                    boolean isMSTOP = false;
                    datas.clear();
                    if (TAG.equals("red")){
                        datas.add(mList.get(j).getRed_num1());
                        datas.add(mList.get(j).getRed_num2());
                        datas.add(mList.get(j).getRed_num3());
                        datas.add(mList.get(j).getRed_num4());
                        datas.add(mList.get(j).getRed_num5());
                        datas.add(mList.get(j).getRed_num6());
                    }else if (TAG.equals("blue")){
                        datas.add(mList.get(j).getBlue_num());
                    }
//                    else if (TAG.equals("zx")){
//                        nums.add(mFcsdList.get(j).getHundredsdigit0());
//                        nums.add(mFcsdList.get(j).getHundredsdigit1());
//                        nums.add(mFcsdList.get(j).getHundredsdigit2());
//                        nums.add(mFcsdList.get(j).getHundredsdigit3());
//                        nums.add(mFcsdList.get(j).getHundredsdigit4());
//                        nums.add(mFcsdList.get(j).getHundredsdigit5());
//                        nums.add(mFcsdList.get(j).getHundredsdigit6());
//                        nums.add(mFcsdList.get(j).getHundredsdigit7());
//                        nums.add(mFcsdList.get(j).getHundredsdigit8());
//                        nums.add(mFcsdList.get(j).getHundredsdigit9());
//                        nums.add(mFcsdList.get(j).getTendigit0());
//                        nums.add(mFcsdList.get(j).getTendigit1());
//                        nums.add(mFcsdList.get(j).getTendigit2());
//                        nums.add(mFcsdList.get(j).getTendigit3());
//                        nums.add(mFcsdList.get(j).getTendigit4());
//                        nums.add(mFcsdList.get(j).getTendigit5());
//                        nums.add(mFcsdList.get(j).getTendigit6());
//                        nums.add(mFcsdList.get(j).getTendigit7());
//                        nums.add(mFcsdList.get(j).getTendigit8());
//                        nums.add(mFcsdList.get(j).getTendigit9());
//                        nums.add(mFcsdList.get(j).getSingledigit0());
//                        nums.add(mFcsdList.get(j).getSingledigit1());
//                        nums.add(mFcsdList.get(j).getSingledigit2());
//                        nums.add(mFcsdList.get(j).getSingledigit3());
//                        nums.add(mFcsdList.get(j).getSingledigit4());
//                        nums.add(mFcsdList.get(j).getSingledigit5());
//                        nums.add(mFcsdList.get(j).getSingledigit6());
//                        nums.add(mFcsdList.get(j).getSingledigit7());
//                        nums.add(mFcsdList.get(j).getSingledigit8());
//                        nums.add(mFcsdList.get(j).getSingledigit9());


//                        if (TAG2.equals("100")){
//                            datas.add(mFcsdList.get(j).getNumOne());
//                        }else if (TAG2.equals("10")){
//                            datas.add(mFcsdList.get(j).getNumTwo());
//                        }else if (TAG2.equals("1")){
//                            datas.add(mFcsdList.get(j).getNumThree());
//                        }
//                    }
                        //将所有的中奖号码全部拿出来进行对比
                        for (int m = 0;m<datas.size();m++){
                            if (i+1 == Integer.parseInt(datas.get(m))){
                                t.setText(datas.get(m));
                                t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                                if (TAG.equals("red")){
                                    t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.redball));
                                }else if (TAG.equals("blue")){
                                    t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.blueball));
                                }
//                                else if (TAG.equals("zx")){
//                                    t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.redball));
//
//                                }
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
        borderBottom = q* cellHeight;
    }


    //添加横向福彩数据
    private void addFcData(){
        int left = 0;
        int right = cellWitch;
        int top = 0;
        int bottom = cellHeight;

        if (TAG.equals("zx")){
            s = 10;
            q = mFcsdList.size();
        }




        for (int i = 0; i < s; i++) {//行
            int start = 0;//数字
            //添加所有期的中奖号码
                datas.clear();
                nums.clear();
                if (TAG.equals("zx")){
                    if (TAG2.equals("100")){
                        nums.add(mFcsdList.get(i).getHundredsdigit0());
                        nums.add(mFcsdList.get(i).getHundredsdigit1());
                        nums.add(mFcsdList.get(i).getHundredsdigit2());
                        nums.add(mFcsdList.get(i).getHundredsdigit3());
                        nums.add(mFcsdList.get(i).getHundredsdigit4());
                        nums.add(mFcsdList.get(i).getHundredsdigit5());
                        nums.add(mFcsdList.get(i).getHundredsdigit6());
                        nums.add(mFcsdList.get(i).getHundredsdigit7());
                        nums.add(mFcsdList.get(i).getHundredsdigit8());
                        nums.add(mFcsdList.get(i).getHundredsdigit9());
                    }else if (TAG2.equals("10")){
                        nums.add(mFcsdList.get(i).getTendigit0());
                        nums.add(mFcsdList.get(i).getTendigit1());
                        nums.add(mFcsdList.get(i).getTendigit2());
                        nums.add(mFcsdList.get(i).getTendigit3());
                        nums.add(mFcsdList.get(i).getTendigit4());
                        nums.add(mFcsdList.get(i).getTendigit5());
                        nums.add(mFcsdList.get(i).getTendigit6());
                        nums.add(mFcsdList.get(i).getTendigit7());
                        nums.add(mFcsdList.get(i).getTendigit8());
                        nums.add(mFcsdList.get(i).getTendigit9());
                    }else if (TAG2.equals("1")){
                        nums.add(mFcsdList.get(i).getSingledigit0());
                        nums.add(mFcsdList.get(i).getSingledigit1());
                        nums.add(mFcsdList.get(i).getSingledigit2());
                        nums.add(mFcsdList.get(i).getSingledigit3());
                        nums.add(mFcsdList.get(i).getSingledigit4());
                        nums.add(mFcsdList.get(i).getSingledigit5());
                        nums.add(mFcsdList.get(i).getSingledigit6());
                        nums.add(mFcsdList.get(i).getSingledigit7());
                        nums.add(mFcsdList.get(i).getSingledigit8());
                        nums.add( mFcsdList.get(i).getSingledigit9());
                    }
                    System.out.print(nums);
                    if (TAG2.equals("100")){
                        datas.add(mFcsdList.get(i).getNumOne());
                    }else if (TAG2.equals("10")){
                        datas.add(mFcsdList.get(i).getNumTwo());
                    }else if (TAG2.equals("1")){
                        datas.add(mFcsdList.get(i).getNumThree());
                    }
                }
            for (int j = 0; j < q; j++) { //列
                TextView t = new TextView(mContext);
                t.setGravity(CENTER);
                t.setTextSize(12);
                t.setHeight(MiddleView.cellHeight);
                t.setWidth(MiddleView.cellWitch);
                t.setPadding(0,20,0,0);//没有包裹布局是直接写死  要改
                if (TAG.equals("red")){
                    t.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                }else if (TAG.equals("blue")){
                    t.setTextColor(ContextCompat.getColor(mContext,R.color.blue));
                }
                t.setBackground(ContextCompat.getDrawable(mContext,R.drawable.whiteball));
                if (isRestart == true){
                    start = 0;
                    isRestart = false;
                }
                //先将所有数字排出来
                t.setText(nums.get(j));
                for (int m = 0;m<datas.size();m++){
                    if (0 == Integer.parseInt(datas.get(m))){
                        t.setText(datas.get(m));
                        t.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                        t.setBackground(ContextCompat.getDrawable(mContext, R.drawable.redball));
                        isRestart = true;
                    }
                }


                t.layout(left, top, right, bottom);
                left += cellWitch;
                right += cellWitch;
                addView(t);
            }
            left = 0;
            right = cellWitch;
            top += cellHeight;
            bottom += cellHeight;
        }
        //边界
        borderRight = s * cellWitch;
        borderBottom = q* cellHeight;

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
        if (mList.size() != 0){
            TAG = Red;
            addData();
        }
    }

    public void setFcZxData(ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean> mList, String string,String s){
        this.mFcsdList = mList;
        if (mList.size() != 0){
            TAG = string;
            TAG2 = s;
//            addData();
            addFcData();
        }
    }
}

