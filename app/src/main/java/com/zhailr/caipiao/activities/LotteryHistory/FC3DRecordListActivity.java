package com.zhailr.caipiao.activities.LotteryHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.LotteryHall.FC3DChooseActivity;
import com.zhailr.caipiao.adapter.FC3DRecordAdpter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;
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
public class FC3DRecordListActivity extends BaseActivity {
    private static final String TAG = "FC3DRecordListActivity";
    @Bind(R.id.tou_zhu_btn)
    TextView touZhuBtn;
    @Bind(R.id.recycle_view)
    PullableRecyclerView recycleView;
    private FC3DRecordAdpter mAdapter;
    private ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean> mList = new ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("福彩3D");
        initUI();
    }

    private void initUI() {
        touZhuBtn.setText("投注福彩3D");
        recycleView.setLayoutManager(new LinearLayoutManager(this));
//        RecyclerViewHeader header = RecyclerViewHeader.fromXml(mContext, R.layout.list_header_tips);
//        TextView tv = (TextView) header.findViewById(R.id.tv_tips);
//        tv.setText("每天20:30开奖，点击打开开奖推送");
//        header.attachTo(recyclerView);
        mAdapter = new FC3DRecordAdpter(this);
        recycleView.setAdapter(mAdapter);
        recycleView.setCanPullDown(false);
        recycleView.setCanPullUp(false);
    }


    @OnClick(R.id.tou_zhu_btn)
    public void onClick() {
        startActivity(new Intent(this, FC3DChooseActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FCSDRECORD, null, TAG, new SpotsCallBack<FCSDRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, FCSDRecordResponse data) {
                if (null != data.getData()) {
                    mList = (ArrayList<FCSDRecordResponse.DataBean.HistoryFCSdListBean>) data.getData().getHistoryFCSdList();
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
