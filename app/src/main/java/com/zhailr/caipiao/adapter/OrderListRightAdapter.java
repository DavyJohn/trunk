package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.OrderListResponse;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 腾翔信息 on 2016/12/20.
 * 追号记录
 */

public class OrderListRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<OrderListResponse.DataBean.LotteryordersBean> mData;
    private OrderListRightAdapter.OnItemClickListener mOnItemClickListener;
    private OrderListRightAdapter.OnCloseClickListener mOnCloseClickListener;
    public OrderListRightAdapter(Context context) {
        this.mContext = context;
    }
    public void setData(List<OrderListResponse.DataBean.LotteryordersBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public OrderListResponse.DataBean.LotteryordersBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_right_main_layout, parent, false);
        OrderListRightAdapter.ItemViewHolder vh = new OrderListRightAdapter.ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderListRightAdapter.ItemViewHolder) {

            OrderListResponse.DataBean.LotteryordersBean bean = mData.get(position);
            if (bean == null) {
                return;
            }
            if (null != bean.getType_code()) {
                if (bean.getType_code().equals("双色球")) {
                    ((OrderListRightAdapter.ItemViewHolder) holder).icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_ssq));
                } else if (bean.getType_code().equals("福彩3D")) {
                    ((OrderListRightAdapter.ItemViewHolder) holder).icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_3dfc));
                } else if (bean.getType_code().equals("快3")) {
                    ((OrderListRightAdapter.ItemViewHolder) holder).icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_k3));
                }
            }
//            ((OrderListRightAdapter.ItemViewHolder) holder).tvIcon.setText(bean.getType_code());
            ((OrderListRightAdapter.ItemViewHolder) holder).num.setText("第" + bean.getIssue_num() + "期");
            if (bean.getStatus().equals("0")) {
                ((OrderListRightAdapter.ItemViewHolder) holder).status.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.weizhifu));
            } else if (bean.getStatus().equals("1")) {
                ((OrderListRightAdapter.ItemViewHolder) holder).status.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.weikaijiang));
            } else if (bean.getStatus().equals("2")) {
                ((OrderListRightAdapter.ItemViewHolder) holder).status.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.yiquxiao));
            } else if (bean.getStatus().equals("3")) {
                if (bean.getIs_win().equals("1")) {
                    ((OrderListRightAdapter.ItemViewHolder) holder).status.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.yizhongjiang));
                } else {
                    ((OrderListRightAdapter.ItemViewHolder) holder).status.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.weizhongjiang));

                }
            }
            ((OrderListRightAdapter.ItemViewHolder) holder).price.setText("投注" + StringUtils.Double2String(Double.valueOf(bean.getBet_amount())) + "元");
            int len = bean.getBet_time().length();
            ((OrderListRightAdapter.ItemViewHolder) holder).time.setText(bean.getBet_time());

        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }
    public void setOnItemClickListener(OrderListRightAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnCloseClickListener(OrderListRightAdapter.OnCloseClickListener onCloseClickListener) {
        this.mOnCloseClickListener = onCloseClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnCloseClickListener {
        public void onCloseClick(View view, int position);
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.order_list_left_logo)
        ImageView icon;

        @Bind(R.id.order_list_right_logo)
        ImageView status;
        @Bind(R.id.order_list_times)
        TextView time;
        @Bind(R.id.order_list_money)
        TextView price;
        @Bind(R.id.order_list_qishu)
        TextView num;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getPosition());
            }
        }
    }
}
