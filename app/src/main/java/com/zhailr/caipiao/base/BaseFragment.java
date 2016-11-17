package com.zhailr.caipiao.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.http.OkHttpHelper;

/**
 * Created by zhailiangrong on 16/6/9.
 */
public class BaseFragment extends Fragment {

    public Context mContext;
    public OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstace();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        getActivity().startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.anmi_in_right_left, R.anim.anmi_out_right_left);
    }
}
