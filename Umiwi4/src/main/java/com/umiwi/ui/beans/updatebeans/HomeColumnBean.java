package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class HomeColumnBean extends BaseGsonBeans {

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RhomeCoulum r;

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

    public RhomeCoulum getR() {
        return r;
    }

    public void setR(RhomeCoulum r) {
        this.r = r;
    }

    public static class RhomeCoulum {

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public ArrayList<HomeColumnInfo> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<HomeColumnInfo> record) {
            this.record = record;
        }

        @SerializedName("page")
        private PageBean page;

        @SerializedName("record")
        private ArrayList<HomeColumnInfo> record;

        public static class HomeColumnInfo{
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public boolean getIsbuy() {
                return isbuy;
            }

            public void setIsbuy(boolean isbuy) {
                this.isbuy = isbuy;
            }

            public String getColumnurl() {
                return columnurl;
            }

            public void setColumnurl(String columnurl) {
                this.columnurl = columnurl;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTutortitle() {
                return tutortitle;
            }

            public void setTutortitle(String tutortitle) {
                this.tutortitle = tutortitle;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public String getUpdateaudio() {
                return updateaudio;
            }

            public void setUpdateaudio(String updateaudio) {
                this.updateaudio = updateaudio;
            }

            public String getSalenum() {
                return salenum;
            }

            public void setSalenum(String salenum) {
                this.salenum = salenum;
            }

            @SerializedName("id")
            private String id;
            @SerializedName("uid")
            private String uid;
            @SerializedName("image")
            private String image;
            @SerializedName("name")
            private String name;
            @SerializedName("price")
            private String price;
            @SerializedName("isbuy")
            private boolean isbuy;
            @SerializedName("columnurl")
            private String columnurl;
            @SerializedName("title")
            private String title;
            @SerializedName("tutortitle")
            private String tutortitle;
            @SerializedName("updatetime")
            private String updatetime;
            @SerializedName("updateaudio")
            private String updateaudio;
            @SerializedName("salenum")
            private String salenum;

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

            @Override
            public String toString() {
                return "PageBean{" +
                        "currentpage=" + currentpage +
                        ", rows='" + rows + '\'' +
                        ", totalpage=" + totalpage +
                        '}';
            }
        }
    }
}
