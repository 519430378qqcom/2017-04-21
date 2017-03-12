package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/12.
 */

public class AlreadyAskBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAlreadyAnser r;

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

    public RAlreadyAnser getR() {
        return r;
    }

    public void setR(RAlreadyAnser r) {
        this.r = r;
    }

    public static class RAlreadyAnser{
        @SerializedName("page")
        private PageBean page;
        @SerializedName("question")
        private ArrayList<Question> question;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public ArrayList<Question> getQuestions() {
            return question;
        }

        public void setQuestions(ArrayList<Question> question) {
            this.question = question;
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

        public static class Question {
            @SerializedName("id")
            private String id;
            @SerializedName("tuid")
            private String tuid;
            @SerializedName("title")
            private String title;
            @SerializedName("tavatar")
            private String tavatar;
            @SerializedName("answertime")
            private String answertime;
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
            private String goodstate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTuid() {
                return tuid;
            }

            public void setTuid(String tuid) {
                this.tuid = tuid;
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

            public String getButtontag() {
                return buttontag;
            }

            public void setButtontag(String buttontag) {
                this.buttontag = buttontag;
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

            public String getGoodstate() {
                return goodstate;
            }

            public void setGoodstate(String goodstate) {
                this.goodstate = goodstate;
            }
        }
    }

}
