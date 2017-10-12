package com.zhailr.caipiao.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.fragments.CartFragment;
import com.zhailr.caipiao.fragments.HomeFragment;
import com.zhailr.caipiao.fragments.MineFragment;
import com.zhailr.caipiao.fragments.SortFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.UpdataApk;
import com.zhailr.caipiao.utils.CommonUtil;
import com.zhailr.caipiao.utils.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/6/10.
 */

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    private AlertDialog dialog;
    @Bind(R.id.flContainer)
    FrameLayout flContainer;
    @Bind(R.id.tabHost)
    FragmentTabHost tabHost;
    @Bind(R.id.rbHome)
    RadioButton rbHome;
    @Bind(R.id.rbSort)
    RadioButton rbSort;
    @Bind(R.id.rbCart)
    RadioButton rbCart;
    @Bind(R.id.rbMine)
    RadioButton rbMine;
    @Bind(R.id.rgTab)
    RadioGroup rgTab;

    private int versioncode = 0;

    private Class[] fragments = {HomeFragment.class, SortFragment.class, CartFragment.class, MineFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        showToast("更新");
        PackageManager pm  = mContext.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versioncode = pi.versionCode;
        updata(versioncode);
        init();
    }

    void updata(final int code){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put("channel","android");
        mOkHttpHelper.post(mContext, Constant.UpdataApk, map, TAG, new SpotsCallBack<UpdataApk>(mContext) {
            @Override
            public void onSuccess(Response response, final UpdataApk data) {
                if (Integer.parseInt(data.getEdition()) - code > 0){
                    //检测更新
                    dialog = new AlertDialog.Builder(mContext)
                            .setTitle("更新")
                            .setMessage("检测到有新版本发布！是否下载？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downApk(data.getUrl());
                                }
                            }).create();
                    dialog.show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void downApk(final String url){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout,null);
        final ProgressBar bar = (ProgressBar) view.findViewById(R.id.dialog_bar);
        dialog = new AlertDialog.Builder(mContext)
                .setTitle("更新")
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        try {
            String[] args2 = { "chmod", "777", Environment.getExternalStorageDirectory().getAbsolutePath()};
            Runtime.getRuntime().exec(args2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(String.valueOf(mContext.getExternalCacheDir()));
        file.setWritable(true);
        file.setReadable(true);
        OkHttpUtils
                .get()
                .url(url)
                .tag(HomeActivity.this)
                .build()
                .execute(new FileCallBack(String.valueOf(mContext.getExternalCacheDir()),"CPANDROID") {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                        bar.setProgress((int)(100*progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showMessageDialog("下载失败！",mContext);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(response), "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                });
    }
    private void init() {
        getToolBar().setTitle(R.string.home).setDisplayHomeAsUpEnabled(false);
        tabHost.setup(this, getSupportFragmentManager(), R.id.flContainer);
        for (int i = 0; i < fragments.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(String.valueOf(i)).setIndicator(String.valueOf(i));
            tabHost.addTab(tabSpec, fragments[i], null);
        }
        tabHost.setCurrentTab(0);
        rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbHome:
                        hasToolBar(true);
                        tabHost.setCurrentTab(0);
                        getToolBar().setTitle(R.string.home).setDisplayHomeAsUpEnabled(false);
                        break;
                    case R.id.rbSort:
                        hasToolBar(true);
                        tabHost.setCurrentTab(1);
                        getToolBar().setTitle(R.string.oneyuan).setDisplayHomeAsUpEnabled(false);
                        break;
                    case R.id.rbCart:
                        hasToolBar(true);
                        tabHost.setCurrentTab(2);
                        getToolBar().setTitle(R.string.result).setDisplayHomeAsUpEnabled(false);
                        break;
                    case R.id.rbMine:
                        tabHost.setCurrentTab(3);
                        hasToolBar(true);
                        getToolBar().setTitle(R.string.mine).setDisplayHomeAsUpEnabled(false);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            CommonUtil.exitBy2Click(this);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_homepage;

    }
}
