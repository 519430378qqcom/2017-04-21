package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/21.
 */

public class AttemptBean extends BaseGsonBeans {

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAttenmpInfo r;

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public RAttenmpInfo getR() {
        return r;
    }

    public void setR(RAttenmpInfo r) {
        this.r = r;
    }

    public static class RAttenmpInfo{
        @SerializedName("title")
        private String title;
        @SerializedName("record")
        private ArrayList<RecordsBean> record;
        @SerializedName("page")
        private PageBean page;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<RecordsBean> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<RecordsBean> record) {
            this.record = record;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public static class RecordsBean{
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("istry")
            private boolean istry;
            @SerializedName("audiotype")
            private String audiotype;
            @SerializedName("playtime")
            private String playtime;
            @SerializedName("watchnum")
            private String watchnum;
            @SerializedName("goodnum")
            private String goodnum;
            @SerializedName("description")
            private String description;
            @SerializedName("image")
            private String image;
            @SerializedName("onlinetime")
            private String onlinetime;
            @SerializedName("isbuy")
            private boolean isbuy;

            public boolean isbuy() {
                return isbuy;
            }

            public void setIsbuy(boolean isbuy) {
                this.isbuy = isbuy;
            }
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public boolean istry() {
                return istry;
            }

            public void setIstry(boolean istry) {
                this.istry = istry;
            }

            public String getAudiotype() {
                return audiotype;
            }

            public void setAudiotype(String audiotype) {
                this.audiotype = audiotype;
            }

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public String getWatchnum() {
                return watchnum;
            }

            public void setWatchnum(String watchnum) {
                this.watchnum = watchnum;
            }

            public String getGoodnum() {
                return goodnum;
            }

            public void setGoodnum(String goodnum) {
                this.goodnum = goodnum;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getOnlinetime() {
                return onlinetime;
            }

            public void setOnlinetime(String onlinetime) {
                this.onlinetime = onlinetime;
            }
        }

        public static class PageBean {
            /**
             * currentpage : 1
             * rows : 3
             * totalpage : 1
             */

            @SerializedName("currentpage")
            private int currentpage;
            @SerializedName("rows")
            private String rows;
            @SerializedName("totalpage")
            private int totalpage;

            public int getCurrentpage() {
                return currentpage;
            }

            public void setCurrentpage(int currentpage) {
                this.currentpage = currentpage;
            }

            public String getRows() {
                return rows;
            }

            public void setRows(String rows) {
                this.rows = rows;
            }

            public int getTotalpage() {
                return totalpage;
            }

            public void setTotalpage(int totalpage) {
                this.totalpage = totalpage;
            }
        }
    }
}
