package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.SiteData;
import com.zhailr.caipiao.utils.Constant;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by 腾翔信息 on 2017/1/12.
 */

public class SiteDataActivity extends BaseActivity {
    private static final String TAG = SiteDataActivity.class.getSimpleName();
    @Bind(R.id.site_data_address)
    TextView mTextAddress;
    @Bind(R.id.site_data_businesses)
    TextView mTextBusinesses;
    @Bind(R.id.site_data_phone)
    TextView mTextPhone;
    @Bind(R.id.site_data_siteName)
    TextView mTextSiteName;
    @Bind(R.id.site_data_siteNo)
    TextView mTextSiteNo;
    @Bind(R.id.site_data_status)
    TextView mTextStatus;
    @Bind(R.id.site_data_type)
    TextView mTextType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        ButterKnife.bind(this);
        getToolBar().setTitle("站点详情");
        addSiteData();



    }
    private void addSiteData(){

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("siteId",  getIntent().getStringExtra("id"));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SILTDATA, map, TAG, new SpotsCallBack<SiteData>(mContext,false) {


            @Override
            public void onSuccess(Response response, SiteData siteData) {
                System.out.print(siteData);
                mTextAddress.setText(siteData.getAddress());
                mTextBusinesses.setText(siteData.getBusinesses());
                mTextPhone.setText(siteData.getPhone());
                mTextSiteName.setText(siteData.getSiteName());
                mTextSiteNo.setText(siteData.getSiteNo());
                switch (Integer.parseInt(siteData.getStatus())){
                    case 0:
                        mTextStatus.setText("停用");
                        break;
                    case 1:
                        mTextStatus.setText("启用");
                        break;
                    case 2:
                        mTextStatus.setText("删除");
                        break;
                }
                switch (Integer.parseInt(siteData.getType())){
                    case 1:
                        mTextType.setText("福彩");
                        break;
                    case 2:
                        mTextType.setText("体彩");
                        break;
                    case 3:
                        mTextType.setText("福彩和体彩");
                        break;
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.site_data_layout;
    }
}
