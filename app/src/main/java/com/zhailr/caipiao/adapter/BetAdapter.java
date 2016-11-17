package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.bean.BetBean;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/6.
 */
public class BetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BetBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private OnCloseClickListener mOnCloseClickListener;

    public BetAdapter(Context context) {
        this.mContext = context;
    }


    public void setData(List<BetBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }



    public BetBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bet, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {

            BetBean bet = mData.get(position);
            if(bet == null) {
                return;
            }
            System.out.print(bet.getRedNums());
            ((ItemViewHolder) holder).mNums.setText(bet.getRedNums() + bet.getBlueNums());
            ((ItemViewHolder) holder).mType.setText(bet.getType());
            ((ItemViewHolder) holder).mPrice.setText(bet.getZhu() + "注 " + bet.getPrice() + "元");
            SpannableStringBuilder builder = new SpannableStringBuilder(((ItemViewHolder) holder).mNums.getText().toString());
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
            int m = bet.getRedNums().length();
            int n = bet.getBlueNums().length();
            builder.setSpan(blueSpan, m, m + n, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((ItemViewHolder) holder).mNums.setText(builder);
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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mNums;
        public TextView mType;
        public TextView mPrice;
        public ImageView mClose;

        public ItemViewHolder(View v) {
            super(v);
            mNums = (TextView) v.findViewById(R.id.nums);
            mType = (TextView) v.findViewById(R.id.type);
            mPrice = (TextView) v.findViewById(R.id.price);
            mClose = (ImageView) v.findViewById(R.id.close);
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnCloseClickListener.onCloseClick(v, getPosition());
                }
            });
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getPosition());
            }
        }
    }
}
