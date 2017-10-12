package com.zhailr.caipiao.base;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.pgyersdk.crash.PgyCrashManager;
import com.zhailr.caipiao.activities.HomeActivity;
import com.zhailr.caipiao.utils.NetworkUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {
	private static MyApplication mcontext;
	private static MyApplication instance;
	public static int mNetWorkState = -1;
	private static Handler mHandler;
	private static Thread	mMainThread;
	private static long		mMainThreadId;
	private static Looper mMainThreadLooper;


//
//	private List<AlipayData> AlipayData = new ArrayList<AlipayData>();
//	private List<Cart> orderSelectList = new ArrayList<Cart>();

	private static List<Activity> activities = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		mcontext = this;
		// handler,用来子线程和主线程通讯
		mHandler = new Handler();
		// 主线程
		mMainThread = Thread.currentThread();
		// id
		// mMainThreadId = mMainThread.getId();
		mMainThreadId = android.os.Process.myTid();
		// looper
		mMainThreadLooper = getMainLooper();

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(10000L, TimeUnit.MILLISECONDS)
				.readTimeout(10000L, TimeUnit.MILLISECONDS)
						//其他配置
				.build();
		OkHttpUtils.initClient(okHttpClient);
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	 	JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}

	public static Handler getHandler()
	{
		return mHandler;
	}

	public static Thread getMainThread()
	{
		return mMainThread;
	}

	public static long getMainThreadId()
	{
		return mMainThreadId;
	}

	public static Looper getMainThreadLooper()
	{
		return mMainThreadLooper;
	}

	public MyApplication() {

	}

	public static MyApplication getInstance() {
		if (instance == null)
			instance = new MyApplication();
		return instance;
	}


	/**
	 * 获取网络类型
	 *
	 * @return
	 */
	public void getConnType() {
		mNetWorkState = NetworkUtils.getNetworkState(mcontext);
	}


	public static MyApplication getAppContext() {
		return mcontext;
	}


//
//	public List<AlipayData> getAlipayData() {
//		return AlipayData;
//	}
//
//	public void setAlipayData(List<AlipayData> alipayData) {
//		AlipayData = alipayData;
//	}


	public void add(Activity activity) {
		if (activity != null) {
			activities.add(activity);
		}
	}

	/**
	 * 销毁当前集合中所有的Activity。
	 */
	public void finishAll() {
		try {
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	public void finishAllExceptHome() {
		try {
			for (Activity activity : activities) {
				if (activity != null && !(activity instanceof HomeActivity))
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
