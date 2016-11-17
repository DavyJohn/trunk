package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.UserInfoResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/20.
 */
public class PersonInfoActivity extends BaseActivity {
    private static final String TAG = "PersonInfoActivity";
    @Bind(R.id.head_img)
    ImageView headImg;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.id_tv)
    TextView idTv;
    @Bind(R.id.rel_name_tv)
    TextView relNameTv;
    @Bind(R.id.rl_rel_name)
    RelativeLayout rlRelName;
    @Bind(R.id.rl_id)
    RelativeLayout rlId;
    @Bind(R.id.img_arrow)
    ImageView imgArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle(R.string.per_info);
    }

    private void getUserData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("userId", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.FINDUSERSETTINGINFO, map, TAG, new SpotsCallBack<UserInfoResponse>(mContext) {

            @Override
            public void onSuccess(Response response, UserInfoResponse res) {
                if (res.getCode().equals("200")) {
                    PreferencesUtils.putString(mContext, Constant.USER.ISPAY, res.getIs_pay());
                    PreferencesUtils.putString(mContext, Constant.USER.REALNAME, res.getReal_name());
                    nameTv.setText(TextUtils.isEmpty(res.getUserName()) ? "待不全" : res.getUserName());

                    if (!TextUtils.isEmpty(res.getId_card()) && !TextUtils.isEmpty(res.getReal_name())) {
                        rlRelName.setVisibility(View.VISIBLE);
                        if (res.getReal_name().length() == 2) {
                            relNameTv.setText(res.getReal_name().substring(0, 1) + "*");

                        } else if (res.getReal_name().length() == 3) {
                            relNameTv.setText(res.getReal_name().substring(0, 1) + "**");
                        } else {
                            relNameTv.setText(res.getReal_name().substring(0, 1) + "***");
                        }
                        if (res.getId_card().length() == 15) {
                            idTv.setText(res.getId_card().substring(0, 2) + "***********" + res.getId_card().substring(res.getId_card().length() - 2));
                        } else {
                            idTv.setText(res.getId_card().substring(0, 2) + "**************" + res.getId_card().substring(res.getId_card().length() - 2));
                        }
                        rlId.setClickable(false);
                        imgArrow.setVisibility(View.GONE);

                    } else {
                        rlRelName.setVisibility(View.GONE);
                        rlId.setClickable(true);
                        idTv.setText("待补全");
                        imgArrow.setVisibility(View.VISIBLE);
                    }
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i(TAG, response.toString());
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_per_info;
    }

    @OnClick({R.id.rl_head, R.id.rl_name, R.id.rl_id})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                showToast("尚未开放");
                break;
            case R.id.rl_name:
                startActivity(new Intent(this, EditNameActivity.class));
                break;
            case R.id.rl_id:
                startActivity(new Intent(this, EditIDActivity.class));
                break;
        }
    }
}
