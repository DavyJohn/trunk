package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.response.SiteListResponse;
import com.zhailr.caipiao.utils.Constant;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhailiangrong on 22/7/6.
 */
public class SiteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SiteListResponse.DataBean.SiteListsBean> mData;
    private int pos;
    private OnItemClickListener mOnItemClickListener;
    private OnCloseClickListener mOnCloseClickListener;

    public SiteAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<SiteListResponse.DataBean.SiteListsBean> data,int postion) {
        this.mData = data;
        this.pos = postion;
        this.notifyDataSetChanged();
    }


    public SiteListResponse.DataBean.SiteListsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_site, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            SiteListResponse.DataBean.SiteListsBean bean = mData.get(position);
            if (bean == null) {
                return;
            }
            ((ItemViewHolder) holder).name.setText(bean.getSite_name());
            ((ItemViewHolder) holder).no.setText("站点" + bean.getSite_no());
            if (pos == position){
                ((ItemViewHolder) holder).mImage.setVisibility(View.VISIBLE);
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
        @Bind(R.id.no)
        TextView no;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.adapter_site_gou)
        ImageView mImage;
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
