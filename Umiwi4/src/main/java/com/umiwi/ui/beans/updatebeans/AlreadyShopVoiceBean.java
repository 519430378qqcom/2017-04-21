package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import cn.youmi.framework.model.BaseModel;

/**
 * Created by Administrator on 2017/3/12.
 */

public class AlreadyShopVoiceBean extends BaseModel {

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAlreadyVoice r;

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

    public RAlreadyVoice getR() {
        return r;
    }

    public void setR(RAlreadyVoice r) {
        this.r = r;
    }

    public static class RAlreadyVoice{
        @SerializedName("page")
        private PageBean page;
        @SerializedName("record")
        private ArrayList<Record> record;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public ArrayList<Record> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<Record> record) {
            this.record = record;
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

        public static class Record {
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("cat")
            private String cat;
            @SerializedName("price")
            private String price;
            @SerializedName("playtime")
            private String playtime;
            @SerializedName("watchnum")
            private String watchnum;
            @SerializedName("process")
            private String process;
            @SerializedName("hrefurl")
            private String hrefurl;

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

            public String getCat() {
                return cat;
            }

            public void setCat(String cat) {
                this.cat = cat;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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

            public String getProcess() {
                return process;
            }

            public void setProcess(String process) {
                this.process = process;
            }

            public String getHrefurl() {
                return hrefurl;
            }

            public void setHrefurl(String hrefurl) {
                this.hrefurl = hrefurl;
            }
        }
    }
}
