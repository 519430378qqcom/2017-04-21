package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

/**
 * Created by Administrator on 2017/3/13.
 */

public class ZanBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;

    @Override
    public String toString() {
        return "ZanBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                '}';
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }
}
