package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import cn.youmi.framework.model.BaseModel;

/**
 * Created by Administrator on 2017/3/11.
 */

public class CommitVoiceBean extends BaseModel {

    private static final long serialVersionUID = -5001017644519778100L;
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RCommmit r;

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

    public RCommmit getR() {
        return r;
    }

    public void setR(RCommmit r) {
        this.r = r;
    }

    public static class RCommmit{
        @SerializedName("id")
        private String id;
        @SerializedName("source")
        private String source;
        @SerializedName("url")
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
