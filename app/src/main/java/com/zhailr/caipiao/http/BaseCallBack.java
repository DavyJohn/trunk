package com.zhailr.caipiao.http;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhailiangrong on 16/6/11.
 */
public abstract class BaseCallBack<T> {

    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superClass = subclass.getGenericSuperclass();
        if (superClass instanceof  Class) {
            throw new RuntimeException("Missing type parameter");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public BaseCallBack() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void onRequestBefore(Request request);

    public abstract void onFailure(Request request, Exception e);

    public abstract void onSuccess(Response response, T t);// 介于200与300之间

    public abstract void onError(Response response, int code, Exception e);

    public abstract void onResponse(Response response);
}
