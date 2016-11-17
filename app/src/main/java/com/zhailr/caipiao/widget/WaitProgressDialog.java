package com.zhailr.caipiao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.zhailr.caipiao.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by losg on 2015/11/4.
 */
public class WaitProgressDialog extends Dialog {

    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;
    @Bind(R.id.wait_message)
    TextView mWaitMessage;

    private String mWaitMessageText;

    public WaitProgressDialog(Context context, String waitMessageText) {
        this(context, R.style.ProgressDialog);
        mWaitMessageText = waitMessageText;
        initView();
    }

    public WaitProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void initView() {
        setContentView(R.layout.view_wait_progress_dialog);
        ButterKnife.bind(this);

        mWaitMessage.setText(mWaitMessageText);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public void setmWaitMessage(String waitMessage){
        mWaitMessage.setText(waitMessage);
    }

    public  void setMessageSize(float size){
        mWaitMessage.setTextSize(size);
    }

    public void setWaitDisable(){
        mProgressWheel.setVisibility(View.GONE);
    }

    public void setNormal(){
        mProgressWheel.setVisibility(View.VISIBLE);
    }


    @Override
    public void show() {
        super.show();
        setNormal();
        mWaitMessage.setText(mWaitMessageText);
    }

    public void setReult(String result){
        setmWaitMessage(result);
        setWaitDisable();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1000);
    }

}
