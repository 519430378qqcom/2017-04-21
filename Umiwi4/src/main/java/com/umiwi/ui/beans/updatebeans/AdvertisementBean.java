package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class AdvertisementBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private ArrayList<RAdvertBean> r;

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

    public ArrayList<RAdvertBean> getR() {
        return r;
    }

    public void setR(ArrayList<RAdvertBean> r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "AdvertisementBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }
    public static class RAdvertBean{
        @SerializedName("isbuy")
        private boolean isbuy;
        @SerializedName("image")
        private String image;
        @SerializedName("type")
        private String type;
        @SerializedName("url")
        private String url;
        @SerializedName("albumid")
        private String albumid;

        public boolean isbuy() {
            return isbuy;
        }

        public void setIsbuy(boolean isbuy) {
            this.isbuy = isbuy;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }

        @Override
        public String toString() {
            return "RAdvertBean{" +
                    "isbuy=" + isbuy +
                    ", image='" + image + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", albumid='" + albumid + '\'' +
                    '}';
        }
    }
}
