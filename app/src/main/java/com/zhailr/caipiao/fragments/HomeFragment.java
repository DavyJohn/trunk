package com.zhailr.caipiao.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.LotteryHall.DoubleColorBallChooseActivity;
import com.zhailr.caipiao.activities.LotteryHall.FC3DChooseActivity;
import com.zhailr.caipiao.activities.LotteryHall.K3ChooseActivity;
import com.zhailr.caipiao.base.BaseFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.HomeResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.NetworkUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.widget.TZCPPullRefresh;
import com.zhailr.caipiao.widget.pullableview.PullToRefreshLayout;
import com.zhailr.caipiao.widget.pullableview.PullableScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class HomeFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "HomeFragment";
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.fc_time)
    TextView fcTime;
    @Bind(R.id.k_time)
    TextView kTime;
    @Bind(R.id.refresh_view)
    TZCPPullRefresh refreshView;
    @Bind(R.id.scrollView)
    PullableScrollView scrollView;
    private SliderLayout mSliderShow;
    private DefaultSliderView mTextSliderView; //TextSliderView,带标题
    private Context mContext;
    private View rootView;//缓存Fragment view

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fm_home, null);
            ButterKnife.bind(this, rootView);
            initView(rootView);
            initSlider();
            getData();
//        requestImages();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        ButterKnife.bind(this, rootView);
        if (StringUtils.isEmpty(tvTime.getText().toString()) && NetworkUtils.isNetworkAvailable(mContext)) {
            getData();
        }
        return rootView;
//        View view = inflater.inflate(R.layout.fm_home, container, false);
//        ButterKnife.bind(this, view);
//        initView(view);
//        initSlider();
//        getData();
////        requestImages();
//        return view;
    }

    private void getData() {
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.HOME, null, TAG, new SpotsCallBack<HomeResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, HomeResponse homeResponse) {
                System.out.print(homeResponse);
                if (null != refreshView)
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (null != homeResponse.getData()) {
                    try {
                        HomeResponse.DataBean bean = new HomeResponse.DataBean();
                        bean = homeResponse.getData().get(0);
                        tvTime.setText("第" + bean.getIssue_num() + "期" + "  截止" + changeType(bean.getTime_draw()));
                        bean = homeResponse.getData().get(1);
                        fcTime.setText("第" + bean.getIssue_num() + "期" + "  截止" + changeType(bean.getTime_draw()));
                        bean = homeResponse.getData().get(2);
                        kTime.setText("第" + bean.getIssue_num() + "期" + "  截止" + changeType(bean.getTime_draw()));
                    } catch (Exception e) {
                        Log.e("这个地方走过说明了一个问题：",e+"");
                    }

                } else {
                    Toast.makeText(mContext, "时间获取异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                if (null != refreshView)
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);
            }

        });
    }

    private String changeType(String time) {
//        try {
//            int len = time.length();
//            return time.substring(5,10) + "  " + time.substring(len-10, len-5);
//        } catch (Exception e) {
//        }
        return time;
    }

    private void initView(View view) {
        mContext = this.getActivity();
        mSliderShow = (SliderLayout) view.findViewById(R.id.slider);
        scrollView.setCanPullDown(true);
        scrollView.setCanPullUp(false);
        refreshView.setTAG(TAG);
        refreshView.setOnRefreshListener(this);
    }

    private void requestImages() {
//        String url = "http://121.41.130.58/lbt-eshop-web/api/index/index";
//        mOkHttpHelper.get(url, new SpotsCallBack<HomeResponse>(mContext, true) {
//            @Override
//            public void onSuccess(Response response, HomeResponse homeResponse) {
//                List<BannerBean> list = homeResponse.getHomeBean().bannerList;
//                for (BannerBean bean : list) {
//                    mTextSliderView = new DefaultSliderView(mContext);
//                    mTextSliderView.description(bean.getGoodsname()).image(Constant.COMMONIMGURL + bean.getPath());
//                    mSliderShow.addSlider(mTextSliderView);
//                }
//                initSlider();
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//            }
//        });
    }


    private void initSlider() {

        mTextSliderView = new DefaultSliderView(mContext);
        mTextSliderView.image(R.drawable.banner1);
        mTextSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(HomeFragment.this.getActivity(), "点击了第一张图片", Toast.LENGTH_SHORT).show();
            }
        });
        mSliderShow.addSlider(mTextSliderView);
        mTextSliderView = new DefaultSliderView(mContext);
        mTextSliderView.image(R.drawable.banner2);
        mTextSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(HomeFragment.this.getActivity(), "点击了第二张图片", Toast.LENGTH_SHORT).show();
            }
        });
        mSliderShow.addSlider(mTextSliderView);


        // 设置默认指示器位置(默认指示器白色,位置在sliderlayout底部)
        mSliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        // 设置默认过渡效果(可在xml中设置)
//        mSliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
        // 设置TextView自定义动画
//        mSliderShow.setCustomAnimation(new DescriptionAnimation());
        // 设置持续时间
        mSliderShow.setDuration(5000);

    }

    @Override
    public void onStop() {
        mSliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onResume() {
        mSliderShow.startAutoCycle();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_double_color_ball, R.id.rl_fc3d, R.id.rl_fast3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_double_color_ball:
                startActivity(new Intent(mContext, DoubleColorBallChooseActivity.class));
                break;
            case R.id.rl_fc3d:
                startActivity(new Intent(mContext, FC3DChooseActivity.class));
                break;
            case R.id.rl_fast3:
                startActivity(new Intent(mContext, K3ChooseActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.cancelTag(TAG);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getData();
        mSliderShow.startAutoCycle();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
