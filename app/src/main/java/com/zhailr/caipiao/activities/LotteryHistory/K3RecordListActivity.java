package com.zhailr.caipiao.activities.LotteryHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.LotteryHall.K3ChooseActivity;
import com.zhailr.caipiao.adapter.K3RecordAdpter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.KSRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.widget.pullableview.PullableRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class K3RecordListActivity extends BaseActivity {
    private static final String TAG = "K3RecordListActivity";
    @Bind(R.id.tou_zhu_btn)
    TextView touZhuBtn;
    @Bind(R.id.recycle_view)
    PullableRecyclerView recycleView;
    private K3RecordAdpter mAdapter;
    private ArrayList<KSRecordResponse.DataBean.HitoryQSBean> mList = new ArrayList<KSRecordResponse.DataBean.HitoryQSBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("快三");
        initUI();
    }

    private void initUI() {
        touZhuBtn.setText("投注快三");
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new K3RecordAdpter(this);
        recycleView.setAdapter(mAdapter);
        recycleView.setCanPullDown(false);
        recycleView.setCanPullUp(false);
    }


    @OnClick(R.id.tou_zhu_btn)
    public void onClick() {
        startActivity(new Intent(this, K3ChooseActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.KSRECORD, null, TAG, new SpotsCallBack<KSRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, KSRecordResponse data) {
                if (null != data.getData()) {
                    mList = (ArrayList<KSRecordResponse.DataBean.HitoryQSBean>) data.getData().getHitoryQS();
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
