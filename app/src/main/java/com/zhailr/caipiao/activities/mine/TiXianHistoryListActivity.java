package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.adapter.TiXianAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.TiXianHistroyResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.widget.TZCPPullRefresh;
import com.zhailr.caipiao.widget.pullableview.PullToRefreshLayout;
import com.zhailr.caipiao.widget.pullableview.PullableRecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/8/15.
 */
public class TiXianHistoryListActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "OrderListActivity";
    @Bind(R.id.recycle_view)
    PullableRecyclerView recycleView;
    @Bind(R.id.refresh_view)
    TZCPPullRefresh refreshView;
    private LinearLayoutManager mLayoutManager;
    private TiXianAdapter mAdapter;
    private List<TiXianHistroyResponse.DataBean.SearchWithdrawRecordsBean> mData = new ArrayList<>();
    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("提现记录");
        initUI();
    }

    private void initUI() {
        recycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TiXianAdapter(this);
//        mAdapter.setOnItemClickListener(new TiXianAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(mContext, OrderDetailActivity.class);
//                intent.putExtra("orderId", mData.get(position).getOrderId());
//                startActivity(intent);
//            }
//        });
        recycleView.setAdapter(mAdapter);
        refreshView.setOnRefreshListener(this);
        refreshView.autoRefresh();
    }

    private void getData(int page) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("pageSize", "10");
        map.put("pageNo", page + "");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SEARCHWITHDRAWRECORDS, map, TAG, new SpotsCallBack<TiXianHistroyResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, TiXianHistroyResponse res) {
                Log.e("tag", res.toString());
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                if (res.getCode().equals("200")) {
                    List<TiXianHistroyResponse.DataBean.SearchWithdrawRecordsBean> newsList = res.getData().getSearchWithdrawRecords();
                    if (null != newsList) {
                        mData.addAll(newsList);
                        mAdapter.setData(mData);
                    }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPage = 1;
        mData.clear();
        getData(mPage);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPage++;
        getData(mPage);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_order_list;
    }
}
