package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.OrderDetailResponse;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Fengjiandong on 2016/9/8.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<OrderDetailResponse.DataBean.ChaseInfo> mData;

    public MyOrderAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<OrderDetailResponse.DataBean.ChaseInfo> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public OrderDetailResponse.DataBean.ChaseInfo getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            OrderDetailResponse.DataBean.ChaseInfo OrderInfoBean = mData.get(position);
            if (OrderInfoBean == null) {
                return;
            }
//            ((ItemViewHolder) holder).mPlayway.setText(OrderInfoBean.getPlay_way());
//            ((ItemViewHolder) holder).mContent.setText(OrderInfoBean.getContent());
//            ((ItemViewHolder) holder).mMultiple.setText(OrderInfoBean.getMultiple()+"倍");
            //期号
            ((ItemViewHolder)holder).mIssue.setText(OrderInfoBean.getIssue_num());
            //玩法类型
            ((ItemViewHolder)holder).mPlayway.setText(OrderInfoBean.getPlay_type());
            //追号号码
            ((ItemViewHolder)holder).mContent.setText(OrderInfoBean.getContent());
            //倍数
            ((ItemViewHolder)holder).mMultiple.setText(OrderInfoBean.getMultiple());
            //金额
            ((ItemViewHolder)holder).money.setText(OrderInfoBean.getAmount());
            //状态
            ((ItemViewHolder)holder).mStatus.setText(OrderInfoBean.getStatus());
        }

    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        //        public TextView mPlaytype;
        public TextView mPlayway;//方式
        public TextView mContent;//彩票号码
        public TextView mMultiple;//倍数
        public TextView mIssue;//期号
        public TextView money;//钱
        public TextView mStatus;//状态

        public ItemViewHolder(View v) {
            super(v);
            //期号
            mIssue = (TextView) v.findViewById(R.id.order_item_issue);
            //类型
            mPlayway = (TextView) v.findViewById(R.id.order_item_type);
            //号码
            mContent = (TextView) v.findViewById(R.id.order_item_num);
            //倍数
            mMultiple = (TextView) v.findViewById(R.id.order_item_multiple);
            //金额
            money = (TextView) v.findViewById(R.id.order_item_money);
            //状态
            mStatus = (TextView) v.findViewById(R.id.order_item_station);
        }

    }

}


