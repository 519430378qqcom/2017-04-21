package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;

import cn.youmi.framework.model.BaseModel;

/**
 * Created by Administrator on 2017/3/12.
 */

public class DelayAnswerVoiceBean extends BaseModel {

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RDelayAnserBeans  rDelayAnserBeans;

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

    public RDelayAnserBeans getrDelayAnserBeans() {
        return rDelayAnserBeans;
    }

    public void setrDelayAnserBeans(RDelayAnserBeans rDelayAnserBeans) {
        this.rDelayAnserBeans = rDelayAnserBeans;
    }

    public static class RDelayAnserBeans{
        @SerializedName("source")
        private String source;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
