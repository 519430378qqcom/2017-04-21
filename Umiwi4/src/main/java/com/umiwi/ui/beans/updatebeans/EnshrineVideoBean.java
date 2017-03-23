package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/23.
 */

public class EnshrineVideoBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private REnshrineVideo r;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public REnshrineVideo getR() {
        return r;
    }

    public void setR(REnshrineVideo r) {
        this.r = r;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    @Override
    public String toString() {
        return "EnshrineVideoBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }

    public static class REnshrineVideo{
        @SerializedName("page")
        private EnshrineVideoPage page;
        @SerializedName("record")
        private ArrayList<EnshrineVideoInfo> record;

        public EnshrineVideoPage getPage() {
            return page;
        }

        public void setPage(EnshrineVideoPage page) {
            this.page = page;
        }

        public ArrayList<EnshrineVideoInfo> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<EnshrineVideoInfo> record) {
            this.record = record;
        }

        @Override
        public String toString() {
            return "REnshrineVideo{" +
                    "page=" + page +
                    ", record=" + record +
                    '}';
        }

        public static class EnshrineVideoPage{
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
                return "EnshrineVideoPage{" +
                        "currentpage=" + currentpage +
                        ", rows='" + rows + '\'' +
                        ", totalpage=" + totalpage +
                        '}';
            }
        }
        public static class EnshrineVideoInfo{
            @SerializedName("id")
            private String id;
            @SerializedName("issee")
            private boolean issee;
            @SerializedName("shorttitle")
            private String shorttitle;
            @SerializedName("limage")
            private String limage;
            @SerializedName("watchnum")
            private String watchnum;
            @SerializedName("name")
            private String name;
            @SerializedName("tutortitle")
            private String tutortitle;
            @SerializedName("price")
            private String price;
            @SerializedName("time")
            private String time;
            @SerializedName("playtime")
            private String playtime;
            @SerializedName("detailurl")
            private String detailurl;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean issee() {
                return issee;
            }

            public void setIssee(boolean issee) {
                this.issee = issee;
            }

            public String getLimage() {
                return limage;
            }

            public void setLimage(String limage) {
                this.limage = limage;
            }

            public String getWatchnum() {
                return watchnum;
            }

            public void setWatchnum(String watchnum) {
                this.watchnum = watchnum;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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

            public String getDetailurl() {
                return detailurl;
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
            }

            public String getShorttitle() {
                return shorttitle;
            }

            public void setShorttitle(String shorttitle) {
                this.shorttitle = shorttitle;
            }

            @Override
            public String toString() {
                return "EnshrineVideoInfo{" +
                        "id='" + id + '\'' +
                        ", issee=" + issee +
                        ", shorttitle='" + shorttitle + '\'' +
                        ", limage='" + limage + '\'' +
                        ", watchnum='" + watchnum + '\'' +
                        ", name='" + name + '\'' +
                        ", tutortitle='" + tutortitle + '\'' +
                        ", price='" + price + '\'' +
                        ", time='" + time + '\'' +
                        ", playtime='" + playtime + '\'' +
                        ", detailurl='" + detailurl + '\'' +
                        '}';
            }
        }
    }
}
