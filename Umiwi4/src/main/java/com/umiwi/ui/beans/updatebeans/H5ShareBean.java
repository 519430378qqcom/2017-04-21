package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class H5ShareBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RShareBean r;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public RShareBean getR() {
        return r;
    }

    public void setR(RShareBean r) {
        this.r = r;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
    public static class RShareBean{
        @SerializedName("id")
        private String id;
        @SerializedName("share_title")
        private String share_title;
        @SerializedName("share_content")
        private String share_content;
        @SerializedName("share_img")
        private String share_img;
        @SerializedName("share_url")
        private String share_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShare_title() {
            return share_title;
        }

        public void setShare_title(String share_title) {
            this.share_title = share_title;
        }

        public String getShare_content() {
            return share_content;
        }

        public void setShare_content(String share_content) {
            this.share_content = share_content;
        }

        public String getShare_img() {
            return share_img;
        }

        public void setShare_img(String share_img) {
            this.share_img = share_img;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        @Override
        public String toString() {
            return "RShareBean{" +
                    "id='" + id + '\'' +
                    ", share_title='" + share_title + '\'' +
                    ", share_content='" + share_content + '\'' +
                    ", share_img='" + share_img + '\'' +
                    ", share_url='" + share_url + '\'' +
                    '}';
        }
    }

}
