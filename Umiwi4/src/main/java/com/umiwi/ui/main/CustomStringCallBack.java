package com.umiwi.ui.main;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by lws on 2016/11/18.
 * OkHttpUtils回调
 */

public abstract class CustomStringCallBack extends StringCallback {



    public CustomStringCallBack() {

    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onFaild();
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String data = jsonObject.getString("r");
            onSucess(data);
        } catch (JSONException e) {
            onFaild();
        }
    }


    public abstract void onFaild();

    public abstract void onSucess(String data);
}
