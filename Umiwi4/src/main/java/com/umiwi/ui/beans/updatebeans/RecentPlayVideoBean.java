package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/22.
 */

public class RecentPlayVideoBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private ArrayList<RecentPlayVideo> r;

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public ArrayList<RecentPlayVideo> getR() {
        return r;
    }

    public void setR(ArrayList<RecentPlayVideo> r) {
        this.r = r;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public static class RecentPlayVideo{
        @SerializedName("tutor_name")
        private String tutor_name;
        @SerializedName("tutor_title")
        private String tutor_title;
        @SerializedName("watchnum")
        private String watchnum;
        @SerializedName("albumid")
        private String albumid;
        @SerializedName("onlinetime")
        private String onlinetime;
        @SerializedName("canbuy")
        private boolean canbuy;
        @SerializedName("vid")
        private String vid;
        @SerializedName("title")
        private String title;
        @SerializedName("image")
        private String image;
        @SerializedName("playtime")
        private String playtime;
        @SerializedName("url")
        private String url;
        @SerializedName("detailurl")
        private String detailurl;
        @SerializedName("price")
        private String price;
        @SerializedName("tagname")
        private String tagname;

        public String getTagname() {
            return tagname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setTagname(String tagname) {
            this.tagname = tagname;
        }

        public String getTutor_name() {
            return tutor_name;
        }

        public void setTutor_name(String tutor_name) {
            this.tutor_name = tutor_name;
        }

        public String getTutor_title() {
            return tutor_title;
        }

        public void setTutor_title(String tutor_title) {
            this.tutor_title = tutor_title;
        }

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
        }

        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }

        public String getOnlinetime() {
            return onlinetime;
        }

        public void setOnlinetime(String onlinetime) {
            this.onlinetime = onlinetime;
        }

        public boolean isCanbuy() {
            return canbuy;
        }

        public void setCanbuy(boolean canbuy) {
            this.canbuy = canbuy;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }
    }
}
