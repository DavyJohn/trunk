package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.activities.LotteryHall.DoubleColorBallChooseActivity;
import com.zhailr.caipiao.activities.LotteryHall.FC3DChooseActivity;
import com.zhailr.caipiao.activities.LotteryHall.K3ChooseActivity;
import com.zhailr.caipiao.activities.LotteryHall.SelectPayTypeActivity;
import com.zhailr.caipiao.adapter.MyOrderAdapter;
import com.zhailr.caipiao.adapter.MycaipiaoAdapter;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.BaseResponse;
import com.zhailr.caipiao.model.response.OrderDetailResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/23.
 */
public class OrderDetailActivity extends BaseActivity {
    private static final String TAG = "OrderDetailActivity";
    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.tv_icon)
    TextView tvIcon;
    @Bind(R.id.num)
    TextView num;
    @Bind(R.id.amount)
    TextView amount;
    @Bind(R.id.win_amount)
    TextView winAmount;
    @Bind(R.id.multiple)
    TextView multiple;
    @Bind(R.id.count)
    TextView count;
//    @Bind(R.id.u_num)
    TextView content;
    @Bind(R.id.win_content)
    TextView winContent;
    @Bind(R.id.order_id)
    TextView orderId;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.pay_way)
    TextView payWay;
    @Bind(R.id.pay_time)
    TextView payTime;
    @Bind(R.id.del)
    TextView del;
    @Bind(R.id.ok)
    TextView ok;
    @Bind(R.id.play_type)
    TextView playType;
    @Bind(R.id.order_type)
    TextView orderType;
    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.recycle_view_order)
    RecyclerView recycleVieworder;
    private String mOrderId;
    private String mPrice;
    private String mTypeCode;
    private LinearLayoutManager mLayoutManager;
    private MycaipiaoAdapter mAdapter;
    private MyOrderAdapter myOrderAdapter;
    private List<OrderDetailResponse.DataBean.TicketinfoBean> mData= new ArrayList<>();
    private List<OrderDetailResponse.DataBean.OrderInfoBean> MData= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("投注详情");
        mOrderId = getIntent().getStringExtra("orderId");
        showNoContent();
        getData();
    }

    private void getData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("orderId", mOrderId);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.ORDERDETAIL, map, TAG, new SpotsCallBack<OrderDetailResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, OrderDetailResponse res) {
                dimssNoContent();

                if (res.getCode().equals("200")) {
                    OrderDetailResponse.DataBean bean = res.getData();
//                    OrderDetailResponse.DataBean.TicketinfoBean TicketinfoBean = res.getData().getTicketinfo();
//                    OrderDetailResponse.DataBean.OrderInfoBean OrderInfoBean = res.getData().getOrderInfo();
                    mTypeCode = res.getData().getType_code();
                    if (null != mTypeCode) {
                        if (mTypeCode.equals("双色球")) {
                            icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_ssq));
                        } else if (mTypeCode.equals("福彩3D")) {
                            icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_3dfc));
                        } else if (mTypeCode.equals("快3")) {
                            icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_k3));
                        }
                    }
                    playType.setText(bean.getPlay_type());
                    switch (Integer.valueOf(bean.getIs_append().equals("")?"-1":bean.getIs_append())){
                        case 0:
                            orderType.setText("普通投注");
                            break;
                        default:
                            orderType.setText("追加投注");
                            break;
                    }
                    winAmount.setText(bean.getWin_amount().equals("") ? "" : "￥" + bean.getWin_amount());
                    winContent.setText(bean.getBall_num().equals("") ? "等待开奖" : bean.getBall_num());
                    switch (Integer.valueOf(bean.getPay_way().equals("")?"-1":bean.getPay_way())) {
                        case 0:
                            payWay.setText("快捷支付");
                            break;
                        case 1:
                            payWay.setText("现金支付");
                            break;
                        case 2:
                            payWay.setText("金币支付");
                            break;
                        case 3:
                            payWay.setText("支付宝支付");
                            break;
                        case 4:
                            payWay.setText("微信支付");
                            break;
                        default:
                            payWay.setText("未支付");
                            break;
                    }
                    orderId.setText(bean.getOrderId());
                    tvIcon.setText(StringUtils.getStringNotNULL(bean.getType_code()));
                    mPrice = bean.getBet_amount();
                    String tvPrice = StringUtils.Double2String(Double.parseDouble(mPrice));
                    amount.setText("￥" + tvPrice);
                    num.setText("第" + StringUtils.getStringNotNULL(bean.getIssue_num()) + "期");
                    int len = bean.getCreate_time().length();
                    payTime.setText(StringUtils.getStringNotNULL(bean.getCreate_time().substring(5, 10) + "  " + bean.getCreate_time().substring(len - 8, len - 3)));
                    count.setText(StringUtils.getStringNotNULL(bean.getNode_count()) + "注");
                    multiple.setText(StringUtils.getStringNotNULL(bean.getMultiple()) + "倍");
                    switch (Integer.valueOf(StringUtils.getStringNotNULL(bean.getStatus()).equals("") ? "0" : bean.getStatus())) {
                        case 0:
                            status.setText("下单完成");
                            break;
                        case 1:
                            status.setText("支付完成");
                            break;
                        case 2:
                            status.setText("取消订单");
                            break;
                        default:
                            status.setText("已开奖");
                            break;
                    }
                    if (!bean.getStatus().equals("0")) {
                        ok.setVisibility(View.GONE);
                        del.setVisibility(View.GONE);
                    } else {
                        ok.setVisibility(View.VISIBLE);
                        del.setVisibility(View.VISIBLE);
                    }
//                    // ["56113%3,2_1"]
//                    StringBuffer sb = new StringBuffer();
//                    int size = bean.getContentMap().size();
//                    for (int i = 0; i < size; i++) {
//                        String str = bean.getContentMap().get(i);
//                        String string1[] = str.split("%");
//                        String str1 = string1[1];
//                        String string2[] = str1.split("_");
//                        String str2 = string2[0];
//                        if (i != size - 1) {
//                            sb.append(str2 + "\n\n");
//                        } else {
//                            sb.append(str2);
//                        }
//
//                    }
//                    content.setText(sb.toString());
                    LinearLayoutManager MLayoutManager = new LinearLayoutManager(mContext);
                    recycleVieworder.setLayoutManager(MLayoutManager);
                    List<OrderDetailResponse.DataBean.OrderInfoBean> newsorderList = res.getData().getOrderInfo();
                    if (null != newsorderList) {
                        MData.addAll(newsorderList);
                        myOrderAdapter = new MyOrderAdapter(mContext);
                        myOrderAdapter.setData(MData);
                        recycleVieworder.setAdapter(myOrderAdapter);
                    }
                }
                    mLayoutManager = new LinearLayoutManager(mContext);
                    recycleView.setLayoutManager(mLayoutManager);
                        List<OrderDetailResponse.DataBean.TicketinfoBean> newsList = res.getData().getTicketinfo();
                        if (null != newsList) {
                            mData.addAll(newsList);
                            mAdapter = new MycaipiaoAdapter(mContext);
                            mAdapter.setData(mData);
                            recycleView.setAdapter(mAdapter);
                        }
                }


            @Override
            public void onError(Response response, int code, Exception e) {
                dimssNoContent();
            }
        });
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("orderId", mOrderId);
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.CANCELORDER, map, TAG, new SpotsCallBack<BaseResponse>(mContext, false) {
            @Override
            public void onSuccess(Response response, BaseResponse res) {
                if (res.getCode().equals("200")) {
                    showToast("取消订单成功");
                    sendBroadcast(new Intent(Constant.ORDERLISTRECEIVER));
                    finish();

                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                dimssNoContent();
                showToast(getString(R.string.request_error));
            }
        });
    }

    @OnClick({R.id.choose, R.id.del, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose:
                if (mTypeCode.equals("双色球")) {
                    startActivity(new Intent(mContext, DoubleColorBallChooseActivity.class));
                } else if (mTypeCode.equals("福彩3D")) {
                    startActivity(new Intent(mContext, FC3DChooseActivity.class));
                } else if (mTypeCode.equals("快3")) {
                    startActivity(new Intent(mContext, K3ChooseActivity.class));
                }
                break;
            case R.id.del:
                cancelOrder();
                break;
            case R.id.ok:
                Intent intent = new Intent(mContext, SelectPayTypeActivity.class);
                intent.putExtra("orderId", mOrderId);
                intent.putExtra("totalallprice", mPrice);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_order_detail;
    }
}
