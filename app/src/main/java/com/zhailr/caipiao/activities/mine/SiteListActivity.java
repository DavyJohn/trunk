package com.zhailr.caipiao.activities.mine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.adapter.SiteAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.BaseResponse;
import com.zhailr.caipiao.model.response.SiteListResponse;
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
 * Created by zhailiangrong on 16/7/18.
 */
public class SiteListActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "SiteListActivity";
    @Bind(R.id.recycle_view)
    PullableRecyclerView recycleView;
    @Bind(R.id.refresh_view)
    TZCPPullRefresh refreshView;
    private LinearLayoutManager mLayoutManager;
    private SiteAdapter mAdapter;
    private List<SiteListResponse.DataBean.SiteListsBean> mData = new ArrayList<>();
    private int mPage = 1;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("站点选择");
        initUI();
    }

    private void initUI() {
        recycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setCanPullUp(false);
        mAdapter = new SiteAdapter(this);
        mAdapter.setOnItemClickListener(new SiteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SiteListResponse.DataBean.SiteListsBean bean = mData.get(position);
                showConfirmDialog("是否确定将当前站点更改为" + bean.getSite_name(), bean.getSite_id(), mContext);
             /*   LinkedHashMap<String, String> map = new LinkedHashMap<>();
                map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
                map.put("user_name", PreferencesUtils.getString(mContext, Constant.USER.USERNAME));
                map.put("siteId", PreferencesUtils.getString(mContext, Constant.USER.SITEID));
                mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.MODUSERINFO, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {
                    @Override
                    public void onSuccess(Response response, BaseResponse res) {
                        if (res.getCode().equals("200")) {
                            mAdapter = new SiteAdapter(this);
                            mAdapter.setOnItemClickListener(new SiteAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    SiteListResponse.DataBean.SiteListsBean bean = mData.get(position);
                                    showConfirmDialog("是否确定将当前站点更改为" + bean.getSite_name(), bean.getSite_id(), mContext);
                        }
                    });
                            recycleView.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });*/
            }
        });
        recycleView.setAdapter(mAdapter);
        refreshView.setOnRefreshListener(this);
        refreshView.autoRefresh();
    }

    protected void showConfirmDialog(String str, final String siteId, Context context) {
        dialog = new AlertDialog.Builder(context)
                .setMessage(str).setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferencesUtils.putString(getApplicationContext(), Constant.USER.SITEID, siteId);
                        LinkedHashMap<String, String> map = new LinkedHashMap<>();
                        map.put("user_id", PreferencesUtils.getString(mContext, Constant.USER.USERID));
                        map.put("user_name", PreferencesUtils.getString(mContext, Constant.USER.USERNAME));
                        map.put("siteId", PreferencesUtils.getString(mContext, Constant.USER.SITEID));
                        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.MODUSERINFO, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {
                            @Override
                            public void onSuccess(Response response, BaseResponse res) {
                                if (res.getCode().equals("200")) {
                                    System.out.println("success");
                                }
                            }

                            @Override
                            public void onError(Response response, int code, Exception e) {

                            }
                        });
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("pageSize", "10");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDSITELIST, map, TAG, new SpotsCallBack<SiteListResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, SiteListResponse res) {
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (res.getCode().equals("200")) {
                    List<SiteListResponse.DataBean.SiteListsBean> newsList = res.getData().getSiteLists();
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
        mData.clear();
        getData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }


    @Override
    public int getLayoutId() {
        return R.layout.account_main_layout;
    }
}
