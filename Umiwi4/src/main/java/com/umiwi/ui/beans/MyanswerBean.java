package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/4.
 */

//我答页面
public class MyanswerBean {

    public static MyanswerBean fromJson(String json) {
        return new Gson().fromJson(json, MyanswerBean.class);
    }

    public static class AddMyAnswerRequestData {

        @SerializedName("e")
        private String e;

        @SerializedName("m")
        private String m;

        @SerializedName("r")
        private String r;
    }
}
