package com.zhailr.caipiao.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.model.bean.PayType;

import java.util.List;


public class PayTypeListAdapter extends SuperAdapter<PayType> {
    private Context mContext;
    private List<PayType> list;

    public PayTypeListAdapter(Context context, List<PayType> data, int layoutRes) {
        super(context, data, layoutRes);
        this.mContext = context;
        this.list = data;

    }

    public void setData(List<PayType> data) {
        this.list = data;
        this.notifyDataSetChanged();
    }

    @Override
    protected void setData(int pos, View convertView, PayType itemData) {
        LinearLayout ll = getViewFromHolder(convertView, R.id.spinneritem_ll);
        TextView paynametv = getViewFromHolder(convertView, R.id.paytype_title);
        TextView desctv = getViewFromHolder(convertView, R.id.paytype_desc);
        ImageView paytypeimg = getViewFromHolder(convertView, R.id.paytype_img);
        switch (pos) {
            case 0:
                if (itemData.flag == 0) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                    if (list.size() == 3){
                        paytypeimg.setImageResource(R.drawable.icon_alipay);
                    }else {
                        paytypeimg.setImageResource(R.drawable.icon_gold);
                    }
                }

                break;

            case 1:
                if (itemData.flag == 0) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                    paytypeimg.setImageResource(R.drawable.icon_yeb);
                }
                break;

            case 2:
                if (itemData.flag == 0) {
                    ll.setVisibility(View.GONE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                    paytypeimg.setImageResource(R.drawable.icon_gold);
                }
                break;

            default:
                break;
        }

        paynametv.setText(itemData.name);
        desctv.setText(itemData.desc);


    }

    @Override
    protected void getMyDropDownView(int pos, View convertView, PayType itemData) {


    }

}
