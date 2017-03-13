package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/13.
 */

public class HomeAskBean extends BaseGsonBeans {
    @Override
    public String toString() {
        return "HomeAskBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
}

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAlHomeAnser r;

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

    public RAlHomeAnser getR() {
        return r;
    }

    public void setR(RAlHomeAnser r) {
        this.r = r;
    }

    public static class RAlHomeAnser{
        @Override
        public String toString() {
            return "RAlHomeAnser{" +
                    "page=" + page +
                    ", record=" + record +
                    '}';
        }

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

            @Override
            public String toString() {
                return "PageBean{" +
                        "currentpage=" + currentpage +
                        ", rows='" + rows + '\'' +
                        ", totalpage=" + totalpage +
                        '}';
            }
        }

        public static class Record {
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("tavatar")
            private String tavatar;

            @SerializedName("answertime")
            private String answertime;
            @SerializedName("listentype")
            private String listentype;
            @SerializedName("buttontag")
            private String buttontag;
            @SerializedName("listennum")
            private String listennum;
            @SerializedName("playsource")
            private String playsource;
            @SerializedName("playtime")
            private String playtime;
            @SerializedName("goodnum")
            private String goodnum;
            @SerializedName("goodstate")
            private boolean goodstate;

            public String getButtontag() {
                return buttontag;
            }

            public void setButtontag(String buttontag) {
                this.buttontag = buttontag;
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

            public String getTavatar() {
                return tavatar;
            }

            public void setTavatar(String tavatar) {
                this.tavatar = tavatar;
            }

            public String getAnswertime() {
                return answertime;
            }

            public void setAnswertime(String answertime) {
                this.answertime = answertime;
            }

            public String getListentype() {
                return listentype;
            }

            public void setListentype(String listentype) {
                this.listentype = listentype;
            }

            public String getListennum() {
                return listennum;
            }

            public void setListennum(String listennum) {
                this.listennum = listennum;
            }

            public String getPlaysource() {
                return playsource;
            }

            public void setPlaysource(String playsource) {
                this.playsource = playsource;
            }

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public String getGoodnum() {
                return goodnum;
            }

            public void setGoodnum(String goodnum) {
                this.goodnum = goodnum;
            }

            public boolean getGoodstate() {
                return goodstate;
            }

            public void setGoodstate(boolean goodstate) {
                this.goodstate = goodstate;
            }

            @Override
            public String toString() {
                return "Record{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", tavatar='" + tavatar + '\'' +
                        ", answertime='" + answertime + '\'' +
                        ", listentype='" + listentype + '\'' +
                        ", buttontag='" + buttontag + '\'' +
                        ", listennum='" + listennum + '\'' +
                        ", playsource='" + playsource + '\'' +
                        ", playtime='" + playtime + '\'' +
                        ", goodnum='" + goodnum + '\'' +
                        ", goodstate=" + goodstate +
                        '}';
            }
        }
    }
}
