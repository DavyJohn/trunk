package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.FCSDRecordResponse;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhailiangrong on 16/7/13.
 */
public class FC3DRecordAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FCSDRecordResponse.DataBean.HistoryFCSdListBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnTipClickListener mOnTipClickListener;

    public FC3DRecordAdpter(Context context) {
        this.mContext = context;
    }


    public void setData(List<FCSDRecordResponse.DataBean.HistoryFCSdListBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }


    public FCSDRecordResponse.DataBean.HistoryFCSdListBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adpter_fc3d_record, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            FCSDRecordResponse.DataBean.HistoryFCSdListBean bean = mData.get(position);
            if (bean == null) {
                return;
            }

            ((ItemViewHolder) holder).ssqDate.setText("第" + bean.getIssueNum() + "期");
            ((ItemViewHolder) holder).ssqTime.setText(bean.getOpenTime());
            ((ItemViewHolder) holder).ssqOne.setText(bean.getNumOne());
            ((ItemViewHolder) holder).ssqTwo.setText(bean.getNumTwo());
            ((ItemViewHolder) holder).ssqThree.setText(bean.getNumThree());
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
