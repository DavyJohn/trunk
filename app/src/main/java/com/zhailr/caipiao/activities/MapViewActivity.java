package com.zhailr.caipiao.activities;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/1/4.
 */

public class MapViewActivity extends BaseActivity implements AMapLocationListener,LocationSource {

    @Bind(R.id.mapview)
    MapView mapView;

    private AMap aMap;
    private UiSettings mUiSetting;
    private LocationManager manager;
    private Location location;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mapLocationClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("地图");
        if (aMap == null){
            aMap = mapView.getMap();
            mUiSetting = aMap.getUiSettings();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.map_view_layout;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null){
            if (aMapLocation.getErrorCode() == 0){
                aMapLocation.getLocationType();
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
            }else {
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }


    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

    }

    @Override
    public void deactivate() {

    }
}
