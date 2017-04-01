package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

public class AudioVideoBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private ArrayList<RAUdioVideo> r;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public ArrayList<RAUdioVideo> getR() {
        return r;
    }

    public void setR(ArrayList<RAUdioVideo> r) {
        this.r = r;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public static class RAUdioVideo{
        @SerializedName("id")
        private String id;
        @SerializedName("type")
        private String type;
        @SerializedName("title")
        private String title;
        @SerializedName("tagname")
        private String tagname;
        @SerializedName("price")
        private String price;
        @SerializedName("watchnum")
        private String watchnum;
        @SerializedName("url")
        private String url;
        @SerializedName("playtime")
        private String playtime;

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTagname() {
            return tagname;
        }

        public void setTagname(String tagname) {
            this.tagname = tagname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "RAUdioVideo{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", title='" + title + '\'' +
                    ", tagname='" + tagname + '\'' +
                    ", price='" + price + '\'' +
                    ", watchnum='" + watchnum + '\'' +
                    ", url='" + url + '\'' +
                    ", playtime='" + playtime + '\'' +
                    '}';
        }
    }
}
