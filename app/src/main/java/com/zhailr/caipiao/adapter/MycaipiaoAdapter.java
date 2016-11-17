package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.OrderDetailResponse;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.List;

/**
 * Created by Fengjiandong on 2016/9/6.
 */
public class MycaipiaoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<OrderDetailResponse.DataBean.TicketinfoBean> mData;

    public MycaipiaoAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<OrderDetailResponse.DataBean.TicketinfoBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }
    public OrderDetailResponse.DataBean.TicketinfoBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_caipiao_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            OrderDetailResponse.DataBean.TicketinfoBean TicketinfoBean = mData.get(position);
            if (TicketinfoBean == null) {
                return;
            }
            ((ItemViewHolder) holder).mPlaytype.setText(TicketinfoBean.getPlay_way());
            ((ItemViewHolder) holder).mBetnum.setText(TicketinfoBean.getNode());
            ((ItemViewHolder) holder).mBetmultiple.setText(TicketinfoBean.getMultiple());
            ((ItemViewHolder) holder).mLotterynum.setText(TicketinfoBean.getContent());
            switch (Integer.valueOf(StringUtils.getStringNotNULL(TicketinfoBean.getStatus()).equals("") ? "0" : TicketinfoBean.getStatus())) {
                case 0:
                    ((ItemViewHolder) holder).mLotterystation.setText("未出票");
                    break;
                case 1:
                    ((ItemViewHolder) holder).mLotterystation.setText("取票中");
                    break;
                case 2:
                    ((ItemViewHolder) holder).mLotterystation.setText("正在出票");
                    break;
                case 3:
                    ((ItemViewHolder) holder).mLotterystation.setText("已出票");
                    break;
                case 4:
                    ((ItemViewHolder) holder).mLotterystation.setText("【兑奖】取票中");
                    break;
                case 5:
                    ((ItemViewHolder) holder).mLotterystation.setText("【兑奖】已取票");
                    break;
                case 6:
                    ((ItemViewHolder) holder).mLotterystation.setText("【兑奖】已兑奖");
                    break;
                case 7:
                    ((ItemViewHolder) holder).mLotterystation.setText("出票失败");
                    break;
                default:
                    ((ItemViewHolder) holder).mLotterystation.setText("未知错误，请联系客服");
                    break;
            }
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
        public TextView mBetnum;
        public TextView mBetmultiple;
        public TextView mLotterynum;
        public TextView mLotterystation;
        public TextView mPlaytype;
        public ItemViewHolder(View v) {
            super(v);
            mPlaytype = (TextView) v.findViewById(R.id.play_type);
            mBetnum = (TextView) v.findViewById(R.id.bet_num);
            mBetmultiple = (TextView) v.findViewById(R.id.bet_multiple);
            mLotterynum = (TextView) v.findViewById(R.id.lottery_num);
            mLotterystation = (TextView) v.findViewById(R.id.lottery_station);
        }

    }
}
