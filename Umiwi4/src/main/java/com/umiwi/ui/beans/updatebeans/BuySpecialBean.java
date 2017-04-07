package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/7 0007.
 */

public class BuySpecialBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RBuySpecial r;

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

    public RBuySpecial getR() {
        return r;
    }

    public void setR(RBuySpecial r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "BuySpecialBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }

    public static class RBuySpecial {
        @SerializedName("record")
        private ArrayList<BuySpecialRecord> record;
        @SerializedName("page")
        private BuySpecialPage page;

        public ArrayList<BuySpecialRecord> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<BuySpecialRecord> record) {
            this.record = record;
        }

        public BuySpecialPage getPage() {
            return page;
        }

        public void setPage(BuySpecialPage page) {
            this.page = page;
        }

        @Override
        public String toString() {
            return "RBuySpecial{" +
                    "record=" + record +
                    ", page=" + page +
                    '}';
        }

        public static class BuySpecialRecord {
            @SerializedName("id")
            private String id;
            @SerializedName("sectionid")
            private String sectionid;
            @SerializedName("image")
            private String image;
            @SerializedName("shortcontent")
            private String shortcontent;
            @SerializedName("title")
            private String title;
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
            @SerializedName("type")
            private String type;
            @SerializedName("detailurl")
            private String detailurl;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSectionid() {
                return sectionid;
            }

            public void setSectionid(String sectionid) {
                this.sectionid = sectionid;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getShortcontent() {
                return shortcontent;
            }

            public void setShortcontent(String shortcontent) {
                this.shortcontent = shortcontent;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDetailurl() {
                return detailurl;
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
            }

            @Override
            public String toString() {
                return "BuySpecialRecord{" +
                        "id='" + id + '\'' +
                        ", sectionid='" + sectionid + '\'' +
                        ", image='" + image + '\'' +
                        ", shortcontent='" + shortcontent + '\'' +
                        ", title='" + title + '\'' +
                        ", price='" + price + '\'' +
                        ", isbuy=" + isbuy +
                        ", catname='" + catname + '\'' +
                        ", audiotitle='" + audiotitle + '\'' +
                        ", onlinetime='" + onlinetime + '\'' +
                        ", watchnum='" + watchnum + '\'' +
                        ", type='" + type + '\'' +
                        ", detailurl='" + detailurl + '\'' +
                        '}';
            }
        }

        public static class BuySpecialPage {
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
                return "BuySpecialPage{" +
                        "currentpage=" + currentpage +
                        ", rows='" + rows + '\'' +
                        ", totalpage=" + totalpage +
                        '}';
            }
        }
    }
}
