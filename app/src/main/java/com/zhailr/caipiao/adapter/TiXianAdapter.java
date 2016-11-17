package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.TiXianHistroyResponse;
import com.zhailr.caipiao.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhailiangrong on 16/7/6.
 */
public class TiXianAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TiXianHistroyResponse.DataBean.SearchWithdrawRecordsBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnCloseClickListener mOnCloseClickListener;

    public TiXianAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<TiXianHistroyResponse.DataBean.SearchWithdrawRecordsBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }


    public TiXianHistroyResponse.DataBean.SearchWithdrawRecordsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tixian, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            TiXianHistroyResponse.DataBean.SearchWithdrawRecordsBean bean = mData.get(position);
            if (bean == null) {
                return;
            }
            ((ItemViewHolder) holder).num.setText(bean.getBank_type().equals("0")?"支付宝账户":"其他账户");

            if (bean.getStatus().equals("0")) {
                ((ItemViewHolder) holder).status.setText("申请中");
            } else if (bean.getStatus().equals("1")) {
                ((ItemViewHolder) holder).status.setText("审核通过");
            } else if (bean.getStatus().equals("2")) {
                ((ItemViewHolder) holder).status.setText("审核不通过");
            } else if (bean.getStatus().equals("3")) {
                ((ItemViewHolder) holder).status.setText("取消");
            }else if (bean.getStatus().equals("4")) {
                ((ItemViewHolder) holder).status.setText("提现成功");
            }
            ((ItemViewHolder) holder).price.setText("提现" + StringUtils.Double2String(Double.valueOf(bean.getAmount())) + "元");
            int len = bean.getApply_time().length();
//            ((ItemViewHolder) holder).time.setText(bean.getApply_time().substring(5, 10) + "  " + bean.getApply_time().substring(len - 8, len - 3));
            ((ItemViewHolder) holder).time.setText(bean.getApply_time());
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
