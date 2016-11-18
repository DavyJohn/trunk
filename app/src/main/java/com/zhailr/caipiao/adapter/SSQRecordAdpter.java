package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.SSQRecordResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class SSQRecordAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SSQRecordResponse.DataBean.HistorySsqListBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnTipClickListener mOnTipClickListener;

    public SSQRecordAdpter(Context context) {
        this.mContext = context;
    }


    public void setData(List<SSQRecordResponse.DataBean.HistorySsqListBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }


    public SSQRecordResponse.DataBean.HistorySsqListBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adpter_ssq_record, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            SSQRecordResponse.DataBean.HistorySsqListBean bean = mData.get(position);
            if (bean == null) {
                return;
            }
            ((ItemViewHolder) holder).ssqDate.setText("第" + bean.getIssue_num() + "期");
            ((ItemViewHolder) holder).ssqTime.setText(bean.getOpen_time());
            ((ItemViewHolder) holder).ssqOne.setText(bean.getRed_num1());
            ((ItemViewHolder) holder).ssqTwo.setText(bean.getRed_num2());
            ((ItemViewHolder) holder).ssqThree.setText(bean.getRed_num3());
            ((ItemViewHolder) holder).ssqFour.setText(bean.getRed_num4());
            ((ItemViewHolder) holder).ssqFive.setText(bean.getRed_num5());
            ((ItemViewHolder) holder).ssqSix.setText(bean.getRed_num6());
            ((ItemViewHolder) holder).ssqSeven.setText(bean.getBlue_num());
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

    public void setOnTipClickListener(OnTipClickListener onTipClickListener) {
        this.mOnTipClickListener = onTipClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnTipClickListener {
        public void onCloseClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.ssq_date)
        TextView ssqDate;
        @Bind(R.id.ssq_time)
        TextView ssqTime;
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
