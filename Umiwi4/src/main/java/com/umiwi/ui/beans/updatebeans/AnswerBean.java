package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/11.
 */

public class AnswerBean extends BaseGsonBeans {

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAnser r;

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

    public RAnser getR() {
        return r;
    }

    public void setR(RAnser r) {
        this.r = r;
    }

    public static class RAnser{
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
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("name")
            private String name;
            @SerializedName("answertime")
            private String answertime;
            @SerializedName("playsource")
            private String playsource;
            @SerializedName("playtime")
            private String playtime;
            @SerializedName("price")
            private String price;

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
                return avatar;
            }

            public void setTavatar(String avatar) {
                this.avatar = avatar;
            }

            public String getTname() {
                return name;
            }

            public void setTname(String name) {
                this.name = name;
            }

            public String getAnswertime() {
                return answertime;
            }

            public void setAnswertime(String answertime) {
                this.answertime = answertime;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
