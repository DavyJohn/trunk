package com.zhailr.caipiao.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2017/1/4.
 */

public class MapViewActivity extends BaseActivity implements AMapLocationListener {
    private AMap aMap;
    @Bind(R.id.mapview)
    MapView mapView;
    //声明mLocationOption对象
    private UiSettings mUiSetting;
    private LocationManager manager;
    private Location location;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mapLocationClient = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mLocationOption = new AMapLocationClientOption();
        mapLocationClient = new AMapLocationClient(this);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapLocationClient.setLocationListener((AMapLocationListener) this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mapLocationClient.setLocationOption(mLocationOption);
        mapLocationClient.startLocation();
        mapView.onCreate(savedInstanceState);

        Criteria criteria = new Criteria();
        String bestProvider = manager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = manager.getLastKnownLocation(bestProvider);
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSetting = aMap.getUiSettings();
        }
        mUiSetting.setScaleControlsEnabled(true);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.setMyLocationEnabled(true);
        aMap = mapView.getMap();
        aMap.setTrafficEnabled(true);
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {

            }

            @Override
            public void deactivate() {

            }
        });

        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 18, 45, 0)));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
}
