package com.zhailr.caipiao.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.BaseResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.PreferencesUtils;

import java.net.URLEncoder;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/7/20.
 */
public class EditNameActivity extends BaseActivity {
    private static final String TAG = "EditNameActivity";
    @Bind(R.id.name)
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("用户信息");
    }



    @OnClick({R.id.clear1, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear1:
                name.setText("");
                break;
            case R.id.save:
                if (checkData()) {
                    saveData();
                }
                break;
        }
    }

    private boolean checkData() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            showToast("请输入用户昵称");
            return false;
        } else if (name.getText().toString().length() > 25) {
            showToast("至多输入25个字");
            return false;
        }else if(name.getText().toString().contains(" ")){
            showToast("用户昵称不能含有空格");
            return false;
        }
        return true;
    }

    private void saveData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String param = name.getText().toString().trim();
        try {
            param = URLEncoder.encode(param, "utf-8").replaceAll("%","%");
        }catch (Exception e){
        }
        map.put("user_id", PreferencesUtils.getString(mContext, Constant.USER.USERID));
        map.put("user_name", param);
        map.put("siteId", PreferencesUtils.getString(mContext, Constant.USER.SITEID));
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.MODUSERINFO, map, TAG, new SpotsCallBack<BaseResponse>(mContext) {

            @Override
            public void onSuccess(Response response, BaseResponse res) {
                if (res.getCode().equals("200")) {
                    showToast("用户昵称修改成功");
                    finish();
                    sendBroadcast(new Intent(Constant.USERINFORECEIVER));
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
    protected void onDestroy() {
        super.onDestroy();
        mOkHttpHelper.cancelTag(TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ac_edit_name;
    }
}
