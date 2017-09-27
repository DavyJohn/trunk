package com.zhailr.caipiao.adapter;

import android.content.Context;
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
 * Created by zhailiangrong on 16/7/6.
 */
public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<OrderListResponse.DataBean.LotteryordersBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnCloseClickListener mOnCloseClickListener;

    public OrderAdapter(Context context) {
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
                .inflate(R.layout.adapter_order, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            OrderListResponse.DataBean.LotteryordersBean bean = mData.get(position);
            if (bean == null) {
                return;
            }
            if (null != bean.getType_code()) {
                if (bean.getType_code().equals("双色球")) {
                    ((ItemViewHolder) holder).icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_ssq));
                } else if (bean.getType_code().equals("福彩3D")) {
                    ((ItemViewHolder) holder).icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_3dfc));
                } else if (bean.getType_code().equals("快3")) {
                    ((ItemViewHolder) holder).icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_k3));
                }
            }
            ((ItemViewHolder) holder).tvIcon.setText(bean.getType_code());
            ((ItemViewHolder) holder).num.setText("第" + bean.getIssue_num() + "期");
            if (bean.getStatus().equals("0")) {
                ((ItemViewHolder) holder).status.setText("未支付");
            } else if (bean.getStatus().equals("1")) {
                ((ItemViewHolder) holder).status.setText("尚未开奖");
            } else if (bean.getStatus().equals("2")) {
                ((ItemViewHolder) holder).status.setText("订单已取消");
            } else if (bean.getStatus().equals("3")) {
                if (bean.getIs_win().equals("1")) {
                    ((ItemViewHolder) holder).status.setText("已中奖");
                } else {
                    ((ItemViewHolder) holder).status.setText("未中奖");

                }
            }
            ((ItemViewHolder) holder).price.setText("投注" + StringUtils.Double2String(Double.valueOf(bean.getBet_amount())) + "元");
            int len = bean.getBet_time().length();
            ((ItemViewHolder) holder).time.setText(bean.getBet_time());

        }

    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

//    return mData == null ? null : mData.get(position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnCloseClickListener(OnCloseClickListener onCloseClickListener) {
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
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.tv_icon)
        TextView tvIcon;
        @Bind(R.id.status)
        TextView status;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.num)
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
