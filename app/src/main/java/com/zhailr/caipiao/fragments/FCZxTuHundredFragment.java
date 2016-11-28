package com.zhailr.caipiao.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.TrendChartViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2016/11/28.
 */

public class FCZxTuHundredFragment extends BaseFragment {
    private static final String TAG = FCZxTuHundredFragment.class.getSimpleName();
    @Bind(R.id.fc_zx_hunder_tcvg)
    TrendChartViewGroup mTcvg;
    private ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean> mList = new ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fc_zx_tu_hunder_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        getData();
    }
    //福彩直选
    private void getData() {
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FCSDRECORD, null, TAG, new SpotsCallBack<FCSDRecordResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, FCSDRecordResponse data) {
                if (null != data.getData()) {
                    mList = (ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean>) data.getData().getHistoryFCSdList();
                    mTcvg.setFZ(mList,"zx","100");
                } else {

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }
}
