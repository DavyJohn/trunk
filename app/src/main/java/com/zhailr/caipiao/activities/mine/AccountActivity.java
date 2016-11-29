package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.adapter.AccountAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.AccountDetailResponse;
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
 * Created by zhailiangrong on 16/7/22.
 * 账户明细
 */
public class AccountActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "AccountActivity";
    @Bind(R.id.recycle_view)
    PullableRecyclerView recycleView;
    @Bind(R.id.refresh_view)
    TZCPPullRefresh refreshView;
    private LinearLayoutManager mLayoutManager;
    private AccountAdapter mAdapter;
    private List<AccountDetailResponse.DataBean.DetailListBean> mData = new ArrayList<>();
    private int mPage = 1;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("账户明细");
        type = getIntent().getStringExtra("type");
        initUI();
    }

    private void initUI() {

        recycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AccountAdapter(this);
        recycleView.setAdapter(mAdapter);
        refreshView.setOnRefreshListener(this);
        refreshView.autoRefresh();
    }

    private void getData(int page) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("pageSize", "10");
        map.put("pageNo", page + "");
        map.put("type_code",type);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.USERACCOUNTDETAIL, map, TAG, new SpotsCallBack<AccountDetailResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, AccountDetailResponse res) {
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                if (res.getCode().equals("200")) {
                    List<AccountDetailResponse.DataBean.DetailListBean> newsList = res.getData().getDetailList();
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
