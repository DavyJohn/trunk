package com.zhailr.caipiao.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.utils.QiShuTools;
import com.zhailr.caipiao.widget.TrendChartViewGroup;

import butterknife.Bind;


/**
 * Created by 腾翔信息 on 2016/11/17.
 */

public class RedFragment extends android.support.v4.app.Fragment {
    @Bind(R.id.red_tcv)
    TrendChartViewGroup mTcv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.red_fragment_layout,container,false);
        // TODO: 2016/11/17 add views
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: 2016/11/17 ways
        mTcv.setQS(new QiShuTools() {
            @Override
            public void setQs(int qs) {

            }
        });
    }
}
