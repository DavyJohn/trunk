package com.zhailr.caipiao.activities.LotteryHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.LotteryHall.DoubleColorBallChooseActivity;
import com.zhailr.caipiao.adapter.SSQRecordAdpter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.pullableview.PullableRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/13.
 * 双色球所有获奖信息
 */
public class SSQRecordListActivity extends BaseActivity {
    private static final String TAG = "SSQRecordListActivity";
    @Bind(R.id.recycle_view)
    PullableRecyclerView recycleView;
    private SSQRecordAdpter mAdapter;
    private ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean> mList = new ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("双色球");
        initUI();
    }

    private void initUI() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
//        RecyclerViewHeader header = RecyclerViewHeader.fromXml(mContext, R.layout.list_header_tips);
//        header.attachTo(recycleView);
        mAdapter = new SSQRecordAdpter(this);
        recycleView.setAdapter(mAdapter);
        recycleView.setCanPullDown(false);
        recycleView.setCanPullUp(false);
    }


    @OnClick(R.id.tou_zhu_btn)
    public void onClick() {
        startActivity(new Intent(this, DoubleColorBallChooseActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SSQRECORD, null, TAG, new SpotsCallBack<SSQRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, SSQRecordResponse data) {
                if (null != data.getData()) {
                    mList = (ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>) data.getData().getHistorySsqList();
                    mAdapter.setData(mList);
                } else {

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.cancelTag(TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_record_list;
    }

}
