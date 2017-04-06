package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class LbumListBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RLbumlist r;

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

    public RLbumlist getR() {
        return r;
    }

    public void setR(RLbumlist r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "LbumListBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }

    public static class RLbumlist{
        @SerializedName("page")
        private LbumlistPage page;
        @SerializedName("record")
        private ArrayList<LbumlistRecord> record;

        public LbumlistPage getPage() {
            return page;
        }

        public void setPage(LbumlistPage page) {
            this.page = page;
        }

        public ArrayList<LbumlistRecord> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<LbumlistRecord> record) {
            this.record = record;
        }

        @Override
        public String toString() {
            return "RLbumlist{" +
                    "page=" + page +
                    ", record=" + record +
                    '}';
        }

        public static class LbumlistPage{
            @SerializedName("rows")
            private String rows;
            @SerializedName("totalpage")
            private int totalpage;
            @SerializedName("currentpage")
            private int currentpage;


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

            public int getCurrentpage() {
                return currentpage;
            }

            public void setCurrentpage(int currentpage) {
                this.currentpage = currentpage;
            }

            @Override
            public String toString() {
                return "LbumlistPage{" +
                        "rows='" + rows + '\'' +
                        ", totalpage=" + totalpage +
                        ", currentpage=" + currentpage +
                        '}';
            }
        }
        public static  class LbumlistRecord{
            @SerializedName("id")
            private String id;
            @SerializedName("type")
            private String type;
            @SerializedName("image")
            private String image;
            @SerializedName("title")
            private String title;
            @SerializedName("shortcontent")
            private String shortcontent;
            @SerializedName("price")
            private String price;
            @SerializedName("isbuy")
            private boolean isbuy;
            @SerializedName("catname")
            private String catname;
            @SerializedName("audiotitle")
            private String audiotitle;
            @SerializedName("onlinetime")
            private String onlinetime;
            @SerializedName("watchnum")
            private String watchnum;
            @SerializedName("recordtype")
            private String recordtype;
            @SerializedName("detailurl")
            private String detailurl;

            public String getDetailurl() {
                return detailurl;
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
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

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getShortcontent() {
                return shortcontent;
            }

            public void setShortcontent(String shortcontent) {
                this.shortcontent = shortcontent;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public boolean isbuy() {
                return isbuy;
            }

            public void setIsbuy(boolean isbuy) {
                this.isbuy = isbuy;
            }

            public String getCatname() {
                return catname;
            }

            public void setCatname(String catname) {
                this.catname = catname;
            }

            public String getAudiotitle() {
                return audiotitle;
            }

            public void setAudiotitle(String audiotitle) {
                this.audiotitle = audiotitle;
            }

            public String getOnlinetime() {
                return onlinetime;
            }

            public void setOnlinetime(String onlinetime) {
                this.onlinetime = onlinetime;
            }

            public String getWatchnum() {
                return watchnum;
            }

            public void setWatchnum(String watchnum) {
                this.watchnum = watchnum;
            }

            public String getRecordtype() {
                return recordtype;
            }

            public void setRecordtype(String recordtype) {
                this.recordtype = recordtype;
            }

            @Override
            public String toString() {
                return "LbumlistRecord{" +
                        "id='" + id + '\'' +
                        ", type='" + type + '\'' +
                        ", image='" + image + '\'' +
                        ", title='" + title + '\'' +
                        ", shortcontent='" + shortcontent + '\'' +
                        ", price='" + price + '\'' +
                        ", isbuy=" + isbuy +
                        ", catname='" + catname + '\'' +
                        ", audiotitle='" + audiotitle + '\'' +
                        ", onlinetime='" + onlinetime + '\'' +
                        ", watchnum='" + watchnum + '\'' +
                        ", recordtype='" + recordtype + '\'' +
                        ", detailurl='" + detailurl + '\'' +
                        '}';
            }
        }

    }
}
