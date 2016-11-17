package com.zhailr.caipiao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by come on 2015/12/3.
 */
public class SharedPreferencesUtil {

    private static SharedPreferencesUtil sharedPreferences;
    private SharedPreferences sp;
    private final  String SHARED = "tzcp";

    private SharedPreferencesUtil(Context context){
        sp = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    }
    public static SharedPreferencesUtil getInstance(Context context){
        if(sharedPreferences == null){
            synchronized (SharedPreferencesUtil.class){
                if(sharedPreferences == null){
                    sharedPreferences = new SharedPreferencesUtil(context);
                }
            }
        }
        return sharedPreferences;
    }

    public String getString(String key){
        return sp.getString(key, "");
    }

    public void putString(String key, String value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public void putBoolean(String key, boolean value){
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public boolean getBoolean(String key){
       return sp.getBoolean(key,false);
    }



}
