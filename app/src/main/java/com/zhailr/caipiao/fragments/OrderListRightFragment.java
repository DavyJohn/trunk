package com.zhailr.caipiao.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.mine.OrderDetailActivity;
import com.zhailr.caipiao.adapter.OrderAdapter;
import com.zhailr.caipiao.adapter.OrderListRightAdapter;
import com.zhailr.caipiao.base.BaseFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.AccountInfoResponse;
import com.zhailr.caipiao.model.response.OrderListResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.MyDecoration;
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
 * Created by 腾翔信息 on 2016/12/9.
 */

public class OrderListRightFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {

    private static final String TAG = "OrderListRightFragment";
    @Bind(R.id.recycle_view)
    PullableRecyclerView recycleView;
    @Bind(R.id.refresh_view)
    TZCPPullRefresh refreshView;
    private LinearLayoutManager mLayoutManager;
//    private OrderAdapter mAdapter;
    private OrderListRightAdapter mAdapter;
    private List<OrderListResponse.DataBean.LotteryordersBean> mData = new ArrayList<>();
    private int mPage = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_list_left_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        initUI();
//        registerReceiver();
        getAccountData();
    }

    private void initUI() {
        recycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.addItemDecoration(new MyDecoration(mContext, MyDecoration.VERTICAL_LIST));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new OrderListRightAdapter(mContext);
        mAdapter.setOnItemClickListener(new OrderListRightAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("orderId", mData.get(position).getOrderId());
                startActivity(intent);
            }
        });
        recycleView.setAdapter(mAdapter);
        refreshView.setOnRefreshListener(this);
        refreshView.autoRefresh();
    }
    private void getData(int page) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("pageSize", "10");
        map.put("pageNo", page + "");
        map.put("is_append","1");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.USERLOTTERYLIST, map, TAG, new SpotsCallBack<OrderListResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, OrderListResponse res) {
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                if (res.getCode().equals("200")) {
                    List<OrderListResponse.DataBean.LotteryordersBean> newsList = res.getData().getLotteryorders();
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

    private void getAccountData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDUSERSACCOUNTINFO, map, TAG, new SpotsCallBack<AccountInfoResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, AccountInfoResponse res) {
                if (res.getCode().equals("200")) {
                    PreferencesUtils.putString(mContext, Constant.USER.BALANCE, res.getData().getCash_balance());
                    PreferencesUtils.putString(mContext, Constant.USER.GOLD, res.getData().getGold_balance());
                } else {
                    Toast.makeText(mContext, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

//    private void registerReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constant.ORDERLISTRECEIVER);
//        registerReceiver(receiver, filter);
//    }
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(Constant.ORDERLISTRECEIVER)) {
//                refreshView.autoRefresh();
//            }
//        };
//    };

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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        MyApplication.getInstance().finishAllExceptHome();
//    }
}
