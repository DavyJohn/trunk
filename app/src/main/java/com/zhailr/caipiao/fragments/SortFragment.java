package com.zhailr.caipiao.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseFragment;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.SSQRecordResponse;
import com.zhailr.caipiao.utils.Constant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/5.
 * 一元购
 */
public class SortFragment extends BaseFragment {
    private static final String TAG = "SortFragment";
    @Bind(R.id.test_net)
    TextView mText;

    private View rootView;//缓存Fragment view

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.fm_sort, container, false);
            getData();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        ButterKnife.bind(this, rootView);
        Toast.makeText(mContext,"这是一元购界面",Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private void getData(){
        LinkedHashMap<String ,String> map = new LinkedHashMap<String, String>();//用来携带参数的
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.SSQRECORD, map, TAG, new SpotsCallBack<SSQRecordResponse>(mContext,false) {

            @Override
            public void onSuccess(Response response, SSQRecordResponse ssqRecordResponse) {
                mText.setText(ssqRecordResponse+"");
                System.out.print(ssqRecordResponse);
                List<SSQRecordResponse.DataBean.HistorySsqListBean>  list = new ArrayList<SSQRecordResponse.DataBean.HistorySsqListBean>();
                list.addAll(ssqRecordResponse.getData().getHistorySsqList());
                System.out.print(list);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(mContext,"error",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
