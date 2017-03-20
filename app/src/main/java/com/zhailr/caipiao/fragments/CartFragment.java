package com.zhailr.caipiao.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.LotteryHistory.FC3DRecordListActivity;
import com.zhailr.caipiao.activities.LotteryHistory.K3RecordListActivity;
import com.zhailr.caipiao.activities.LotteryHistory.SSQRecordListActivity;
import com.zhailr.caipiao.base.BaseFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;
import com.zhailr.caipiao.model.response.KSRecordResponse;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.NetworkUtils;
import com.zhailr.caipiao.utils.StringUtils;
import com.zhailr.caipiao.widget.TZCPPullRefresh;
import com.zhailr.caipiao.widget.pullableview.PullToRefreshLayout;
import com.zhailr.caipiao.widget.pullableview.PullableScrollView;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/5.
 */
public class CartFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = "CartFragment";
    @Bind(R.id.ssq_date)
    TextView ssqDate;
    @Bind(R.id.ssq_time)
    TextView ssqTime;
    @Bind(R.id.fc3d_date)
    TextView fc3dDate;
    @Bind(R.id.fc3d_time)
    TextView fc3dTime;
    @Bind(R.id.k3_date)
    TextView k3Date;
    @Bind(R.id.k3_time)
    TextView k3Time;
    @Bind(R.id.ssq_one)
    TextView ssqOne;
    @Bind(R.id.ssq_two)
    TextView ssqTwo;
    @Bind(R.id.ssq_three)
    TextView ssqThree;
    @Bind(R.id.ssq_four)
    TextView ssqFour;
    @Bind(R.id.ssq_five)
    TextView ssqFive;
    @Bind(R.id.ssq_six)
    TextView ssqSix;
    @Bind(R.id.ssq_seven)
    TextView ssqSeven;
    @Bind(R.id.fc3d_one)
    TextView fc3dOne;
    @Bind(R.id.fc3d_two)
    TextView fc3dTwo;
    @Bind(R.id.fc3d_three)
    TextView fc3dThree;
    @Bind(R.id.k3_one)
    TextView k3One;
    @Bind(R.id.k3_two)
    TextView k3Two;
    @Bind(R.id.k3_three)
    TextView k3Three;
    @Bind(R.id.layout_content)
    LinearLayout layoutContent;
    @Bind(R.id.scrollView)
    PullableScrollView scrollView;
    @Bind(R.id.refresh_view)
    TZCPPullRefresh refreshView;
    private View rootView;//缓存Fragment view

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
          if (msg.what == 0){
              SSQRecordResponse.DataBean.HistorySsqListBean bean = (SSQRecordResponse.DataBean.HistorySsqListBean)msg.obj;
              if (bean.getIssue_num() != null) {
                  ssqDate.setText("第" + bean.getIssue_num() + "期");
                  ssqTime.setText(bean.getOpen_time());
                  ssqOne.setText(bean.getRed_num1());
                  ssqTwo.setText(bean.getRed_num2());
                  ssqThree.setText(bean.getRed_num3());
                  ssqFour.setText(bean.getRed_num4());
                  ssqFive.setText(bean.getRed_num5());
                  ssqSix.setText(bean.getRed_num6());
                  ssqSeven.setText(bean.getBlue_num());
                  // 显示内容
                  layoutContent.setVisibility(View.VISIBLE);
              }
          }else if (msg.what == 1){
              FCSDRecordResponse.DataBean.HistoryFCSdListBean bean = (FCSDRecordResponse.DataBean.HistoryFCSdListBean)msg.obj;
              if (bean.getIssueNum() != null) {
              fc3dDate.setText("第" +bean.getIssueNum() + "期");
              fc3dTime.setText(bean.getOpenTime());
              fc3dOne.setText(bean.getNumOne());
              fc3dTwo.setText(bean.getNumTwo());
              fc3dThree.setText(bean.getNumThree());
              // 显示内容
              layoutContent.setVisibility(View.VISIBLE);
              }
          }else if (msg.what == 2){
              KSRecordResponse.DataBean.HitoryQSBean bean = (KSRecordResponse.DataBean.HitoryQSBean) msg.obj;
              if (bean.getIssueNum() != null) {
                  System.out.println("bean.getIssueNum()"+bean.getIssueNum());
                  k3Date.setText("第" + bean.getIssueNum() + "期");
                  System.out.println("bean.getIssueNum()"+bean.getIssueNum());
                  k3Time.setText(bean.getOpenTime());
                  k3One.setText(bean.getNumOne());
                  k3Two.setText(bean.getNumTwo());
                  k3Three.setText(bean.getNumThree());
                  // 显示内容
                  layoutContent.setVisibility(View.VISIBLE);
              }
          }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fm_cart, null);
            ButterKnife.bind(this, rootView);
            // 先隐藏内容
            layoutContent.setVisibility(View.GONE);
            scrollView.setCanPullDown(true);
            scrollView.setCanPullUp(false);
            refreshView.setTAG(TAG);

            refreshView.setOnRefreshListener(this);
            getSSQData();
            getFCSDData();
            getKSData();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        ButterKnife.bind(this, rootView);
        if (layoutContent.getVisibility() == View.GONE && NetworkUtils.isNetworkAvailable(mContext)) {
            getSSQData();
            getFCSDData();
            getKSData();
        }
        return rootView;

     /*   View view = inflater.inflate(R.layout.fm_cart, container, false);
        ButterKnife.bind(this, view);
        // 先隐藏内容
        layoutContent.setVisibility(View.GONE);
        scrollView.setCanPullDown(true);
        scrollView.setCanPullUp(false);
        refreshView.setTAG(TAG);
        refreshView.setOnRefreshListener(this);
        getSSQData();
        getFCSDData();
        getKSData();
        return view;*/
    }

    //只是获取上一期 不是所有的都返回
    private void getSSQData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("lottery_no_number", "1");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SSQRECORD, map, TAG, new SpotsCallBack<SSQRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, SSQRecordResponse data) {
                if (null != data.getData().getHistorySsqList() && data.getData().getHistorySsqList().size() != 0) {
                        try {
                            final SSQRecordResponse.DataBean.HistorySsqListBean bean = data.getData().getHistorySsqList().get(0);
                            if (!TextUtils.isEmpty(bean.getIssue_num())) {
                                // 开启线程，
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Message msg = handler.obtainMessage();
                                        msg.what = 0;
                                        msg.obj = bean;
                                        handler.sendMessage(msg);
                                    }
                                }).start();
//                        Message msg = handler.obtainMessage();
//                        msg.what = 1;
//                        msg.obj=bean;
//                        handler.sendMessage(msg);
//                        msg.arg1 = Integer.parseInt(bean.getIssueNum());
//                        handler.sendMessage(msg);
//                        fc3dDate.setText("第" + bean.getIssueNum() + "期");
                            }
//                    fc3dTime.setText(bean.getOpenTime());
//                    fc3dOne.setText(bean.getNumOne());
//                    fc3dTwo.setText(bean.getNumTwo());
//                    fc3dThree.setText(bean.getNumThree());
//                    // 显示内容
//                    layoutContent.setVisibility(View.VISIBLE);
                        } catch (Exception e) {

                        }
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                System.out.println("双色球失败");
                Log.i(TAG, response.toString());
            }

        });
    }

    private void initData(SSQRecordResponse.DataBean.HistorySsqListBean bean) {
        //ssqDate.setText("第" + bean.getIssue_num() + "期");
        ssqTime.setText(bean.getOpen_time());
        ssqOne.setText(bean.getRed_num1());
        ssqTwo.setText(bean.getRed_num2());
        ssqThree.setText(bean.getRed_num3());
        ssqFour.setText(bean.getRed_num4());
        ssqFive.setText(bean.getRed_num5());
        ssqSix.setText(bean.getRed_num6());
        ssqSeven.setText(bean.getBlue_num());
        // 显示内容
        layoutContent.setVisibility(View.VISIBLE);
    }

    private void getFCSDData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("lottery_no_number", "1");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FCSDRECORD, map, TAG, new SpotsCallBack<FCSDRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, FCSDRecordResponse data) {
                System.out.println("福彩3D");
                if (null != data.getData().getHistoryFCSdList() && data.getData().getHistoryFCSdList().size()!=0) {
                    try{
                    final FCSDRecordResponse.DataBean.HistoryFCSdListBean bean = data.getData().getHistoryFCSdList().get(0);
                    if (!TextUtils.isEmpty(bean.getIssueNum())) {
                        // 开启线程，
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                Message msg = handler.obtainMessage();
                                msg.what = 1;
                                msg.obj = bean;
                                handler.sendMessage(msg);
                            }
                        }).start();
//                        Message msg = handler.obtainMessage();
//                        msg.what = 1;
//                        msg.obj=bean;
//                        handler.sendMessage(msg);
//                        msg.arg1 = Integer.parseInt(bean.getIssueNum());
//                        handler.sendMessage(msg);
//                        fc3dDate.setText("第" + bean.getIssueNum() + "期");
                    }
//                    fc3dTime.setText(bean.getOpenTime());
//                    fc3dOne.setText(bean.getNumOne());
//                    fc3dTwo.setText(bean.getNumTwo());
//                    fc3dThree.setText(bean.getNumThree());
//                    // 显示内容
//                    layoutContent.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }
                } else {

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                System.out.println("福彩3D失败");
                Log.i(TAG, response.toString());
            }

        });
    }

    private void getKSData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("lottery_no_number", "1");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.KSRECORD, map, TAG, new SpotsCallBack<KSRecordResponse>(mContext, false) {

            @Override
            public void onSuccess(Response response, KSRecordResponse data) {
                System.out.println("快3");
                if (null != refreshView)
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (null != data.getData().getHitoryQS() && data.getData().getHitoryQS().size() != 0) {
                    try{
                    final KSRecordResponse.DataBean.HitoryQSBean bean = data.getData().getHitoryQS().get(0);
                    if (!StringUtils.isEmpty(bean.getIssueNum())) {
                        // 开启线程，
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                Message msg = handler.obtainMessage();
                                msg.what = 2;
//                        msg.arg1 = Integer.parseInt(bean.getIssueNum());
                                msg.obj = bean;
                                handler.sendMessage(msg);
//                        k3Date.setText("第" + bean.getIssueNum() + "期");
                            }
                        }).start();
                    }
//                    k3Time.setText(bean.getOpenTime());
//                    k3One.setText(bean.getNumOne());
//                    k3Two.setText(bean.getNumTwo());
//                    k3Three.setText(bean.getNumThree());
//                    // 显示内容
//                    layoutContent.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
                } else {

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                System.out.println("快3失败");
                if (null != refreshView)
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);
                Log.i(TAG, response.toString());
            }

        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.layou_ssq, R.id.layou_3d, R.id.layou_ks})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layou_ssq:
                startActivity(new Intent(mContext, SSQRecordListActivity.class));
                break;
            case R.id.layou_3d:
                startActivity(new Intent(mContext, FC3DRecordListActivity.class));
                break;
            case R.id.layou_ks:
                startActivity(new Intent(mContext, K3RecordListActivity.class));
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
        getSSQData();
        getFCSDData();
        getKSData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
