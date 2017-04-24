package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/15.
 */

public class AlreadShopColumnBean extends BaseGsonBeans {

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RalreadyColumn r;

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

    public RalreadyColumn getR() {
        return r;
    }

    public void setR(RalreadyColumn r) {
        this.r = r;
    }

    public static class RalreadyColumn{
        @SerializedName("page")
        private PageBean page;

        @SerializedName("record")
        private ArrayList<RecordColumn> record;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public ArrayList<RecordColumn> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<RecordColumn> record) {
            this.record = record;
        }

        public static class RecordColumn{
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getTutortitle() {
                return tutortitle;
            }

            public void setTutortitle(String tutortitle) {
                this.tutortitle = tutortitle;
            }

            public String getSalenum() {
                return salenum;
            }

            public void setSalenum(String salenum) {
                this.salenum = salenum;
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

            public String getColumnurl() {
                return columnurl;
            }

            public void setColumnurl(String columnurl) {
                this.columnurl = columnurl;
            }

            @SerializedName("id")
             private String id;
            @SerializedName("uid")
            private String uid;
            @SerializedName("name")
            private String name;
            @SerializedName("title")
            private String title;
            @SerializedName("image")
            private String image;
            @SerializedName("tutortitle")
            private String tutortitle;
            @SerializedName("salenum")
            private String salenum;
            @SerializedName("updatetime")
            private String updatetime;
            @SerializedName("updateaudio")
            private String updateaudio;
            @SerializedName("columnurl")
            private String columnurl;
            @SerializedName("price")
            private String price;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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
