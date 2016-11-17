package com.zhailr.caipiao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.MyApplication;

/**
 * Created by chengmengzhen on 16/6/30.
 */
public class SercurityDialog extends Dialog implements View.OnClickListener {
    private View mRootView;
    private Button mNum1;
    private Button mNum2;
    private Button mNum3;
    private Button mNum4;
    private Button mNum5;
    private Button mNum6;
    private Button mNum7;
    private Button mNum8;
    private Button mNum9;
    private Button mNum0;
    private LinearLayout mDelPwd;

    private ImageView mPwdImg1;
    private ImageView mPwdImg2;
    private ImageView mPwdImg3;
    private ImageView mPwdImg4;
    private ImageView mPwdImg5;
    private ImageView mPwdImg6;

    private TextView mTv;

    private int mPwdCountNum;
    private InputCompleteListener mInputCompleteListener;

    public interface InputCompleteListener {
        public void inputComplete(String passWord);
    }

    public void setOnInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.mInputCompleteListener = inputCompleteListener;
    }

    public SercurityDialog(Context context) {
        super(context, R.style.SercurityDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //给dialog设置布局
        setContentView(R.layout.ui_security_dialog);
        //通过window设置获取dialog参数
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        //获取屏幕的宽高
        WindowManager manager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        //设置dialog的宽
        params.width = width;
        //设置dialog在屏幕中的位置
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        //设置dialog属性
        window.setAttributes(params);

        initView();
        initListener();
    }

    private void initListener() {
        mNum0.setOnClickListener(this);
        mNum1.setOnClickListener(this);
        mNum2.setOnClickListener(this);
        mNum3.setOnClickListener(this);
        mNum4.setOnClickListener(this);
        mNum5.setOnClickListener(this);
        mNum6.setOnClickListener(this);
        mNum7.setOnClickListener(this);
        mNum8.setOnClickListener(this);
        mNum9.setOnClickListener(this);
        mDelPwd.setOnClickListener(this);
    }


    private void initView() {
        mNum0 = (Button) findViewById(R.id.button0);
        mNum1 = (Button) findViewById(R.id.button1);
        mNum2 = (Button) findViewById(R.id.button2);
        mNum3 = (Button) findViewById(R.id.button3);
        mNum4 = (Button) findViewById(R.id.button4);
        mNum5 = (Button) findViewById(R.id.button5);
        mNum6 = (Button) findViewById(R.id.button6);
        mNum7 = (Button) findViewById(R.id.button7);
        mNum8 = (Button) findViewById(R.id.button8);
        mNum9 = (Button) findViewById(R.id.button9);
        mDelPwd = (LinearLayout) findViewById(R.id.button_del);
//        findViewById()findViewById
        mPwdImg1 = (ImageView) findViewById(R.id.pwd_1);
        mPwdImg2 = (ImageView) findViewById(R.id.pwd_2);
        mPwdImg3 = (ImageView) findViewById(R.id.pwd_3);
        mPwdImg4 = (ImageView) findViewById(R.id.pwd_4);
        mPwdImg5 = (ImageView) findViewById(R.id.pwd_5);
        mPwdImg6 = (ImageView) findViewById(R.id.pwd_6);

        //mTv = (TextView) ((Activity)getContext()).findViewById(R.id.tv);
    }

    private String mPassWord = "";

    @Override
    public void onClick(View view) {

        if (mPwdCountNum >= 6) {
            return;
        }
        String PwdNum = "";
        //删除 如果输入密码个数是0 return ，要不就mPwdCountNum 减1
        if (view.getId() == R.id.button_del) {
            if (mPwdCountNum == 0) {
                return;
            } else {
                mPwdCountNum = mPwdCountNum - 1;
                if (mPwdCountNum == 0){
                    mPassWord = mPassWord.substring(0,0);
                }else {
                    mPassWord = mPassWord.substring(0,mPassWord.length()-2);
                }
                showPwdImg();
            }
        }
        //
        else {
            mPwdCountNum++;
            showPwdImg();
            inputPwd(view.getId());
            //
        }
//        RunOnUI
        if (mPwdCountNum == 6) {
            //加密密码
            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(100);
                    dismiss();
                    if (mInputCompleteListener != null) {
                        MyApplication.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mInputCompleteListener.inputComplete(mPassWord);
                            }
                        });
                    }
                }
            }.start();

        }
//        输入完成
    }


    private void inputPwd(int id) {
        if (mPwdCountNum > 1) {
            mPassWord = mPassWord + ",";
        }
        switch (id) {
            case R.id.button0:
                mPassWord = mPassWord + "0";
                break;
            case R.id.button1:
                mPassWord = mPassWord + "1";
                break;
            case R.id.button2:
                mPassWord = mPassWord + "2";
                break;
            case R.id.button3:
                mPassWord = mPassWord + "3";
                break;
            case R.id.button4:
                mPassWord = mPassWord + "4";
                break;
            case R.id.button5:
                mPassWord = mPassWord + "5";
                break;
            case R.id.button6:
                mPassWord = mPassWord + "6";
                break;
            case R.id.button7:
                mPassWord = mPassWord + "7";
                break;
            case R.id.button8:
                mPassWord = mPassWord + "8";
                break;
            case R.id.button9:
                mPassWord = mPassWord + "9";
                break;

        }
    }


    private void showPwdImg() {
        switch (mPwdCountNum) {
            case 0:
                allHidden();
                break;
            case 1:
                showOne();
                break;
            case 2:
                showTwo();
                break;
            case 3:
                showThree();
                break;
            case 4:
                showFour();
                break;
            case 5:
                showFive();
                break;
            case 6:
                showSix();
                break;

        }
    }

    private void showSix() {
        mPwdImg1.setVisibility(View.VISIBLE);
        mPwdImg2.setVisibility(View.VISIBLE);
        mPwdImg3.setVisibility(View.VISIBLE);
        mPwdImg4.setVisibility(View.VISIBLE);
        mPwdImg5.setVisibility(View.VISIBLE);
        mPwdImg6.setVisibility(View.VISIBLE);
    }

    private void showFive() {
        mPwdImg1.setVisibility(View.VISIBLE);
        mPwdImg2.setVisibility(View.VISIBLE);
        mPwdImg3.setVisibility(View.VISIBLE);
        mPwdImg4.setVisibility(View.VISIBLE);
        mPwdImg5.setVisibility(View.VISIBLE);
        mPwdImg6.setVisibility(View.GONE);
    }

    private void showFour() {
        mPwdImg1.setVisibility(View.VISIBLE);
        mPwdImg2.setVisibility(View.VISIBLE);
        mPwdImg3.setVisibility(View.VISIBLE);
        mPwdImg4.setVisibility(View.VISIBLE);
        mPwdImg5.setVisibility(View.GONE);
        mPwdImg6.setVisibility(View.GONE);
    }

    private void showThree() {
        mPwdImg1.setVisibility(View.VISIBLE);
        mPwdImg2.setVisibility(View.VISIBLE);
        mPwdImg3.setVisibility(View.VISIBLE);
        mPwdImg4.setVisibility(View.GONE);
        mPwdImg5.setVisibility(View.GONE);
        mPwdImg6.setVisibility(View.GONE);
    }

    private void showTwo() {
        mPwdImg1.setVisibility(View.VISIBLE);
        mPwdImg2.setVisibility(View.VISIBLE);
        mPwdImg3.setVisibility(View.GONE);
        mPwdImg4.setVisibility(View.GONE);
        mPwdImg5.setVisibility(View.GONE);
        mPwdImg6.setVisibility(View.GONE);
    }

    private void showOne() {
        mPwdImg1.setVisibility(View.VISIBLE);
        mPwdImg2.setVisibility(View.GONE);
        mPwdImg3.setVisibility(View.GONE);
        mPwdImg4.setVisibility(View.GONE);
        mPwdImg5.setVisibility(View.GONE);
        mPwdImg6.setVisibility(View.GONE);
    }


    private void allHidden() {
        mPwdImg1.setVisibility(View.GONE);
        mPwdImg2.setVisibility(View.GONE);
        mPwdImg3.setVisibility(View.GONE);
        mPwdImg4.setVisibility(View.GONE);
        mPwdImg5.setVisibility(View.GONE);
        mPwdImg6.setVisibility(View.GONE);
    }
}
