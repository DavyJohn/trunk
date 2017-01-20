package com.zhailr.caipiao.activities.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zhailr.caipiao.R;
import com.zhailr.caipiao.base.BaseActivity;
import com.zhailr.caipiao.base.MyApplication;
import com.zhailr.caipiao.http.SpotsCallBack;
import com.zhailr.caipiao.model.response.ModUserResponse;
import com.zhailr.caipiao.utils.Constant;
import com.zhailr.caipiao.utils.IDCard;
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
public class EditIDActivity extends BaseActivity {
    private static final String TAG = "EditIDActivity";
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.id_num)
    EditText idNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyApplication.getInstance().add(this);
        getToolBar().setTitle("身份信息");
    }


    @OnClick({R.id.clear1, R.id.clear2, R.id.save})
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
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            showToast("请输入真实姓名");
            return false;
        } else if (!IDCard.IDCardValidate(idNum.getText().toString().trim()).equals("")) {
            showToast(IDCard.IDCardValidate(idNum.getText().toString().trim()));
            return false;
        }
        return true;
    }

    private void saveData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String param1 = name.getText().toString().trim();
        String param2 = idNum.getText().toString().trim();
        try {
            param1 = URLEncoder.encode(param1, "utf-8").replaceAll("%","%");
            param2 = URLEncoder.encode(param2, "utf-8").replaceAll("%","%");
        }catch (Exception e){
        }
        String id = PreferencesUtils.getString(mContext, Constant.USER.USERID);
        map.put("user_id", id);
        map.put("real_name", param1);
        map.put("id_card", param2);
        Log.e("补全信息","用户id"+id+"===="+"真实姓名"+param1+"===="+"id_card"+param2+"");
        mOkHttpHelper.post(mContext, Constant.COMMONURL + Constant.MODUSERINFOFORMATION, map, TAG, new SpotsCallBack<ModUserResponse>(mContext,false) {
            @Override
            public void onSuccess(Response response, ModUserResponse res) {
                if (res.getCode().equals("200")) {
                    finish();
                } else {
                    showToast(res.getMessage());
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                showToast(mContext.getResources().getString(R.string.request_error));
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
        return R.layout.ac_edit_id;
    }
}
