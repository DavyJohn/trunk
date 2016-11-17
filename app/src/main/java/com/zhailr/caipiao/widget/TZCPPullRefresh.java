package com.zhailr.caipiao.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.utils.SharedPreferencesUtil;
import com.zhailr.caipiao.widget.pullableview.PullToRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by come on 2016/1/15.
 */
public class TZCPPullRefresh extends PullToRefreshLayout {

    private ImageView mRefreshImage;
    private TextView mRefreshStatus;
    private TextView mRefreshTime;
    private AnimationDrawable mbearAnim;
    private AnimationDrawable mfooterBearAnim;
    private ImageView mLoadingImage;
    private TextView mLoadingStatus;
    private String tag;

    public TZCPPullRefresh(Context context) {
        super(context);
    }

    public TZCPPullRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TZCPPullRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getLoadView() {
        View footerView = View.inflate(getContext(), R.layout.view_refresh_footer, null);
        mLoadingImage = (ImageView) footerView.findViewById(R.id.loading_image);
        mLoadingStatus = (TextView) footerView.findViewById(R.id.loading_info);
        mfooterBearAnim = (AnimationDrawable) mLoadingImage.getDrawable();
        return footerView;
    }

    @Override
    protected View getHeaderView() {
        View headerView = View.inflate(getContext(), R.layout.view_refresh_head, null);
        mRefreshImage = (ImageView) headerView.findViewById(R.id.refresh_image);
        mRefreshStatus = (TextView) headerView.findViewById(R.id.refresh_info);
        mRefreshTime = (TextView) headerView.findViewById(R.id.last_refresh_time);
        mbearAnim = (AnimationDrawable) mRefreshImage.getDrawable();
        mRefreshTime.setText(getRefreshTime());
        return headerView;
    }

    @Override
    protected void loadSuccess() {
        mRefreshStatus.setText("加载成功");
        mfooterBearAnim.stop();
    }

    @Override
    protected void loadFailure() {
        mLoadingStatus.setText("加载失败");
        mfooterBearAnim.stop();
    }

    @Override
    protected void loading() {
        mLoadingStatus.setText("正在加载...");
        mfooterBearAnim.start();
    }

    @Override
    protected void loadRelease() {
        mLoadingStatus.setText("释放加载...");
    }

    @Override
    protected void refreshing() {
        mRefreshStatus.setText("正在刷新...");
        mbearAnim.start();
    }

    @Override
    protected void refreshFailure() {
        mRefreshStatus.setText("刷新失败...");
        mbearAnim.stop();
    }

    @Override
    protected void refreshSuccess() {
        mRefreshStatus.setText("刷新成功...");
        mbearAnim.stop();
    }


    @Override
    protected void refreshRelease() {
        mRefreshStatus.setText("释放可以刷新...");
    }

    @Override
    protected void refreshInit() {
        mRefreshStatus.setText("下拉可以刷新...");
        mRefreshTime.setText(getRefreshTime());

    }

    @Override
    protected void clearPushAnimation() {
        super.clearPushAnimation();
    }

    @Override
    protected void clearUpAnimation() {
        super.clearUpAnimation();
    }

    public void setTAG(String TAG) {
        tag = TAG;
    }

    private String getRefreshTime() {
        String time = SharedPreferencesUtil.getInstance(getContext()).getString(tag + ":refresh_time");
        if (TextUtils.isEmpty(time)) {
            SharedPreferencesUtil.getInstance(getContext()).putString(tag + ":refresh_time", Calendar.getInstance().getTimeInMillis()+"");
            return "暂无刷新纪录";
        }
        long lastTime = Long.parseLong(time);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        long todayZero = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        long beforeZero = calendar.getTimeInMillis();
        Log.i(tag, "上次刷新: 今天 " + new SimpleDateFormat("HH:mm").format(lastTime));
        //一天内
        if (lastTime > todayZero) {
            SharedPreferencesUtil.getInstance(getContext()).putString(tag + ":refresh_time", Calendar.getInstance().getTimeInMillis() + "");
            return ("上次刷新: 今天 " + new SimpleDateFormat("HH:mm").format(lastTime));
        } else if (beforeZero < lastTime) {
            SharedPreferencesUtil.getInstance(getContext()).putString(tag + ":refresh_time", Calendar.getInstance().getTimeInMillis()+"");
            return ("上次刷新: 昨天 " + new SimpleDateFormat("HH:mm").format(lastTime));
        } else {
            SharedPreferencesUtil.getInstance(getContext()).putString(tag + ":refresh_time", Calendar.getInstance().getTimeInMillis()+"");
            return ("上次刷新:  " + new SimpleDateFormat("yyyy.MM.dd HH:mm").format(lastTime));
        }
    }
}
