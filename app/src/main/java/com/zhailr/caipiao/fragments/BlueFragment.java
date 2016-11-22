package com.zhailr.caipiao.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.TrendChartViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;


/**
 * Created by 腾翔信息 on 2016/11/17.
 *
 */

public class BlueFragment extends BaseFragment {

    private static final String  TAG = BlueFragment.class.getSimpleName();
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>();
    @Bind(R.id.blue_tcv)
    TrendChartViewGroup mTcv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blue_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getData();

    }
    private void getData() {
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SSQRECORD, null, TAG, new SpotsCallBack<SSQRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, SSQRecordResponse data) {
                if (null != data.getData()) {
                    mList = (ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>) data.getData().getHistorySsqList();
                    mTcv.setQS(mList,"blue");
//                    Constant.isRED = 1;
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }
}
