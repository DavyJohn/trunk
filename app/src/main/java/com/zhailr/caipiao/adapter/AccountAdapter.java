package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.AccountDetailResponse;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhailiangrong on 22/7/6.
 */
public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AccountDetailResponse.DataBean.DetailListBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnCloseClickListener mOnCloseClickListener;

    public AccountAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<AccountDetailResponse.DataBean.DetailListBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }


    public AccountDetailResponse.DataBean.DetailListBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_account, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            AccountDetailResponse.DataBean.DetailListBean bean = mData.get(position);
            if (bean == null) {
                return;
            }
            ((ItemViewHolder) holder).orderId.setText(bean.getOrderId()==null?"订单号：":"订单号：" + bean.getOrderId());
            ((ItemViewHolder) holder).type.setText(bean.getCategory());
            ((ItemViewHolder) holder).price.setText(StringUtils.Double2String(Double.valueOf(bean.getAmount())) + "元");
            int len = bean.getTradeTime().length();
            ((ItemViewHolder) holder).time.setText("交易时间：" + bean.getTradeTime());
//            ((ItemViewHolder) holder).time.setText("交易时间:" + bean.getTradeTime().substring(5, 10) + "  " + bean.getTradeTime().substring(len - 8, len - 3));

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
        @Bind(R.id.order_id)
        TextView orderId;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.price)
        TextView price;

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
