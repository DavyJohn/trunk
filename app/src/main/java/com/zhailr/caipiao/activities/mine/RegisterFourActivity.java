package com.zhailr.caipiao.activities.mine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
 * Created by 腾翔信息 on 2017/1/5.
 */

public class RegisterFourActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "RegisterFourActivity";
    private String identifying;
    private String tel;
    private String tag;
    @Bind(R.id.recycle_four_view)
    PullableRecyclerView recycleView;
    @Bind(R.id.refresh_four_view)
    TZCPPullRefresh refreshView;
    private LinearLayoutManager mLayoutManager;
    private SiteAdapter mAdapter;
    private List<SiteListResponse.DataBean.SiteListsBean> mData = new ArrayList<>();
    private AlertDialog dialog;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("选择站点");
        initData();
        addSiteListData();
    }
    private void initData(){
        identifying = getIntent().getStringExtra("identifying");
        tel = getIntent().getStringExtra("tel");
        tag = getIntent().getStringExtra("tag");
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
                showConfirmDialog("是否设置" + bean.getSite_name()+"为站点", bean.getSite_id(), mContext);
                Constant.POS = position;
                PreferencesUtils.putString(getApplicationContext(),Constant.SiteName, bean.getSite_name());

            }
        });
        recycleView.setAdapter(mAdapter);
        refreshView.setOnRefreshListener(this);
        refreshView.autoRefresh();
    }

    protected void showConfirmDialog(final String str, final String siteId, Context context) {
        dialog = new AlertDialog.Builder(context)
                .setMessage(str).setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferencesUtils.putString(getApplicationContext(),Constant.USER.SITEID,siteId);
                        Intent intent = new Intent(RegisterFourActivity.this,RegisterThreeActivity.class);
                        intent.putExtra("siteId",siteId);
                        intent.putExtra("identifying",identifying);
                        intent.putExtra("tel",tel);
                        intent.putExtra("tag",tag);
                        startActivity(intent);
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
    private void addSiteListData(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        mOkHttpHelper.get(mContext, Constant.COMMONURL + Constant.FINDSITELIST, map, TAG, new SpotsCallBack<SiteListResponse>(mContext,false) {
            @Override
            public void onSuccess(Response response, SiteListResponse siteListResponse) {
                refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (siteListResponse.getCode().equals("200")) {
                    List<SiteListResponse.DataBean.SiteListsBean> newsList = siteListResponse.getData().getSiteLists();
                    if (null != newsList) {
                        mData.addAll(newsList);
                        mAdapter.setData(mData,pos);
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
    public int getLayoutId() {
        return R.layout.register_four_main_layout;
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mData.clear();
        addSiteListData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
