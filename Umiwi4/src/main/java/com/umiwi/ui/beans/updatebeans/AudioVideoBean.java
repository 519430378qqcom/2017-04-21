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
    private RAUdioVideo r;

    public RAUdioVideo getR() {
        return r;
    }

    public void setR(RAUdioVideo r) {
        this.r = r;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "AudioVideoBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }

    public static class RAUdioVideo{
        @SerializedName("record")
        private ArrayList<AudioVideoList> record;
        @SerializedName("page")
        private AudioVideoPageBean page;

        public ArrayList<AudioVideoList> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<AudioVideoList> record) {
            this.record = record;
        }

        public AudioVideoPageBean getPage() {
            return page;
        }

        public void setPage(AudioVideoPageBean page) {
            this.page = page;
        }

        @Override
        public String toString() {
            return "RAUdioVideo{" +
                    "record=" + record +
                    ", page=" + page +
                    '}';
        }

        public static class AudioVideoPageBean{
            @SerializedName("currentpage")
            private String currentpage;
            @SerializedName("rows")
            private int rows;
            @SerializedName("totalpage")
            private int totalpage;

            public String getCurrentpage() {
                return currentpage;
            }

            public void setCurrentpage(String currentpage) {
                this.currentpage = currentpage;
            }

            public int getRows() {
                return rows;
            }

            public void setRows(int rows) {
                this.rows = rows;
            }

            public int getTotalpage() {
                return totalpage;
            }

            public void setTotalpage(int totalpage) {
                this.totalpage = totalpage;
            }

            @Override
            public String toString() {
                return "AudioVideoPageBean{" +
                        "currentpage='" + currentpage + '\'' +
                        ", rows=" + rows +
                        ", totalpage=" + totalpage +
                        '}';
            }
        }
        public static class  AudioVideoList{
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
}
