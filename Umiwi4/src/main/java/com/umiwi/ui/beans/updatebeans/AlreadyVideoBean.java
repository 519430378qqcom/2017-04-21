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
           public String getId() {
               return id;
           }

           public void setId(String id) {
               this.id = id;
           }

           public String getLimage() {
               return limage;
           }

           public void setLimage(String limage) {
               this.limage = limage;
           }

           public String getShortx() {
               return shortx;
           }

           public void setShortx(String shortx) {
               this.shortx = shortx;
           }

           public String getPrice() {
               return price;
           }

           public void setPrice(String price) {
               this.price = price;
           }

           public String getName() {
               return name;
           }

           public void setName(String name) {
               this.name = name;
           }

           public String getTutortitle() {
               return tutortitle;
           }

           public void setTutortitle(String tutortitle) {
               this.tutortitle = tutortitle;
           }

           public String getWatchnum() {
               return watchnum;
           }

           public void setWatchnum(String watchnum) {
               this.watchnum = watchnum;
           }

           public String getTime() {
               return time;
           }

           public void setTime(String time) {
               this.time = time;
           }

           public String getPlaytime() {
               return playtime;
           }

           public void setPlaytime(String playtime) {
               this.playtime = playtime;
           }

           @SerializedName("id")
           private String id;
           @SerializedName("limage")
           private String limage;
           @SerializedName("short")
           private String shortx;
           @SerializedName("price")
           private String price;
           @SerializedName("name")
           private String name;
           @SerializedName("tutortitle")
           private String tutortitle;
           @SerializedName("watchnum")
           private String watchnum;
           @SerializedName("time")
           private String time;
           @SerializedName("playtime")
           private String playtime;

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
