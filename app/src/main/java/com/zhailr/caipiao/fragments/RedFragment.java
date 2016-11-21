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
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.QiShuTools;
import com.zhailr.caipiao.widget.TrendChartViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;


/**
 * Created by 腾翔信息 on 2016/11/17.
 */

public class RedFragment extends BaseFragment {
    private static final String TAG = RedFragment.class.getSimpleName();

    @Bind(R.id.red_tcv)
    TrendChartViewGroup mTcv;
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>();
    SSQRecordResponse.DataBean.HistorySsqListBean bean;

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
        ButterKnife.bind(this, view);
        getData();
//        mTcv.setQS(new QiShuTools() {
//            @Override
//            public void setQs(int qs) {
        // TODO: 2016/11/17 数据接口
//            }
//        });
    }

    private void getData() {
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SSQRECORD, null, TAG, new SpotsCallBack<SSQRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, SSQRecordResponse data) {
                if (null != data.getData()) {
                    mList = (ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>) data.getData().getHistorySsqList();
                    mTcv.setQS(mList);

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

}
