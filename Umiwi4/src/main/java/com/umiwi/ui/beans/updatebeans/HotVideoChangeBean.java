package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

/**
 * Created by Administrator on 2017/3/24.
 */

public class HotVideoChangeBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RecommendBean.RBean.HotVideoBean r;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public RecommendBean.RBean.HotVideoBean getR() {
        return r;
    }

    public void setR(RecommendBean.RBean.HotVideoBean r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "HotVideoChangeBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }
}
