package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14.
 */

public class AlreadyVideoBean extends BaseGsonBeans {

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private  RalreadyVideo r;

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

    public RalreadyVideo getR() {
        return r;
    }

    public void setR(RalreadyVideo r) {
        this.r = r;
    }

    public static class RalreadyVideo{
        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public ArrayList<RecordInfo> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<RecordInfo> record) {
            this.record = record;
        }

        @SerializedName("page")
        private PageBean page;

        @SerializedName("record")
        private ArrayList<RecordInfo> record;

       public static class RecordInfo{
           public PageBean getId() {
               return id;
           }

           public void setId(PageBean id) {
               this.id = id;
           }

           public PageBean getLimage() {
               return limage;
           }

           public void setLimage(PageBean limage) {
               this.limage = limage;
           }

           public PageBean getShortx() {
               return shortx;
           }

           public void setShortx(PageBean shortx) {
               this.shortx = shortx;
           }

           public PageBean getPrice() {
               return price;
           }

           public void setPrice(PageBean price) {
               this.price = price;
           }

           public PageBean getName() {
               return name;
           }

           public void setName(PageBean name) {
               this.name = name;
           }

           public PageBean getTutortitle() {
               return tutortitle;
           }

           public void setTutortitle(PageBean tutortitle) {
               this.tutortitle = tutortitle;
           }

           public PageBean getWatchnum() {
               return watchnum;
           }

           public void setWatchnum(PageBean watchnum) {
               this.watchnum = watchnum;
           }

           public PageBean getTime() {
               return time;
           }

           public void setTime(PageBean time) {
               this.time = time;
           }

           public PageBean getPlaytime() {
               return playtime;
           }

           public void setPlaytime(PageBean playtime) {
               this.playtime = playtime;
           }

           @SerializedName("id")
           private PageBean id;
           @SerializedName("limage")
           private PageBean limage;
           @SerializedName("short")
           private PageBean shortx;
           @SerializedName("price")
           private PageBean price;
           @SerializedName("name")
           private PageBean name;
           @SerializedName("tutortitle")
           private PageBean tutortitle;
           @SerializedName("watchnum")
           private PageBean watchnum;
           @SerializedName("time")
           private PageBean time;
           @SerializedName("playtime")
           private PageBean playtime;

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
