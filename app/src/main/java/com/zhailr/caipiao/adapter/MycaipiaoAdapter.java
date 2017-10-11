package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.OrderDetailResponse;
import com.zhailr.caipiao.utils.StringUtils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Fengjiandong on 2016/9/6.
 */
public class MycaipiaoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private String styp;
    private List<OrderDetailResponse.DataBean.TicketinfoBean> mData;
    public MycaipiaoAdapter(Context context,String s) {
        this.mContext = context;
        this.styp = s;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (onClickItemListener != null){
            ((ItemViewHolder)holder).mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemListener.OnClickItem(position,((ItemViewHolder) holder).mChange,((ItemViewHolder) holder).mLayout);
        }
    });
        }


        if (holder instanceof ItemViewHolder) {

            OrderDetailResponse.DataBean.TicketinfoBean TicketinfoBean = mData.get(position);
            if (TicketinfoBean == null) {
                return;
            }
            if (TicketinfoBean.getType_code().equals("KS")){
                ((ItemViewHolder)holder).mLayout.setVisibility(View.GONE);
            }else {
                ((ItemViewHolder)holder).mLayout.setVisibility(View.VISIBLE);
            }
            if (styp.equals("已开奖")){
                ((ItemViewHolder) holder).mLayout.setEnabled(false);
                ((ItemViewHolder) holder).mChange.setBackground(ContextCompat.getDrawable(mContext,R.color.dark_gray));
            }
            ((ItemViewHolder)holder).mBetnum.setText(TicketinfoBean.getContent());//彩票号码
            ((ItemViewHolder)holder).mPlaytype.setText(TicketinfoBean.getPlay_way());//玩法类型
            ((ItemViewHolder)holder).mNum.setText(TicketinfoBean.getIssue_num());//彩票骑术
            ((ItemViewHolder)holder).mNode.setText(TicketinfoBean.getNode());//彩票注数
            ((ItemViewHolder)holder).mMultiple.setText(TicketinfoBean.getMultiple());//彩票倍数
            switch (Integer.parseInt(TicketinfoBean.getTake_ticket_way())){
                case 0:
                    ((ItemViewHolder)holder).mTicketWay.setText("取票异常");
                    break;
                case 1:
                    ((ItemViewHolder)holder).mTicketWay.setText("线上取票");
                    break;
                case 2:
                    ((ItemViewHolder)holder).mTicketWay.setText("线下取票");
                    ((ItemViewHolder) holder).mLayout.setEnabled(false);
                    ((ItemViewHolder) holder).mChange.setBackground(ContextCompat.getDrawable(mContext,R.color.dark_gray));

                    break;
                case 3:
                    ((ItemViewHolder)holder).mTicketWay.setText("线下已取票");
                    break;

            }

            //彩票状态
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

//        public TextView mPlaytype;  各位 我改不动了 名字 不改了 我注释还不行么

        public TextView mNum;//期号
        public TextView mPlaytype;//玩法类型
        public TextView mBetnum;//彩票号码
        public TextView mLotterystation;//彩票状态
        public TextView mLotteryQK;//中奖情况
        public TextView mMoney;//中将金额
        public TextView mNode;//注数
        public TextView mMultiple;//倍数
        public TextView mTicketWay;
        public LinearLayout mLayout;
        public TextView mChange;
        public ItemViewHolder(View v) {
            super(v);
            mChange = (TextView) v.findViewById(R.id.my_caipiao_change);
            mLayout = (LinearLayout) v.findViewById(R.id.caipiao_ticket_way);
            mTicketWay = (TextView) v.findViewById(R.id.lottery_ticket_way);
            mNode = (TextView) v.findViewById(R.id.node);
            mMultiple = (TextView) v.findViewById(R.id.multiple_num);
            mNum = (TextView) v.findViewById(R.id.my_issue);
            mPlaytype = (TextView) v.findViewById(R.id.play_type);
            mBetnum = (TextView) v.findViewById(R.id.bet_num);
            mMoney = (TextView) v.findViewById(R.id.money);
            mLotteryQK = (TextView) v.findViewById(R.id.lottery_qingkuang);
            mLotterystation = (TextView) v.findViewById(R.id.lottery_station);
        }

    }


    public interface OnClickItemListener{
        void OnClickItem(int postion,TextView text,LinearLayout layout);
    }
    public OnClickItemListener onClickItemListener;

    public void setOnClickItemListener(OnClickItemListener onClickItemListener){
        this.onClickItemListener = onClickItemListener;
    }
}
