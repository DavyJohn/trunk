package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.OrderDetailResponse;

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
        public TextView mPlayway;
        public TextView mContent;
        public TextView mMultiple;
        public ItemViewHolder(View v) {
            super(v);
            mPlayway = (TextView) v.findViewById(R.id.play_way);
            mContent = (TextView) v.findViewById(R.id.content);
            mMultiple = (TextView) v.findViewById(R.id.multiple);
        }

    }

}


