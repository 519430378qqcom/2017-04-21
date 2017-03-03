package com.umiwi.ui.beans.updatebeans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class WenDaBean {

    /**
     * record : [{"id":"31","title":"Nihao","tavatar":"http://i2.umivi.net/avatar/76/70100476s.jpg","listennum":"听过3人","answertime":"02-26","listentype":"1","buttontag":"1元偷偷听","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=31","playtime":"0''","goodnum":"2","goodstate":false}]
     * page : {"currentpage":1,"rows":"1","totalpage":1}
     */

    private PageBean page;
    private List<RecordBean> record;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<RecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<RecordBean> record) {
        this.record = record;
    }

    public static class PageBean {
        /**
         * currentpage : 1
         * rows : 1
         * totalpage : 1
         */

        private int currentpage;
        private String rows;
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

    public static class RecordBean {
        /**
         * id : 31
         * title : Nihao
         * tavatar : http://i2.umivi.net/avatar/76/70100476s.jpg
         * listennum : 听过3人
         * answertime : 02-26
         * listentype : 1
         * buttontag : 1元偷偷听
         * playsource : http://i.v.youmi.cn/question/playsourceapi?id=31
         * playtime : 0''
         * goodnum : 2
         * goodstate : false
         */

        private String id;
        private String title;
        private String tavatar;
        private String listennum;
        private String answertime;
        private String listentype;
        private String buttontag;
        private String playsource;
        private String playtime;
        private String goodnum;
        private boolean goodstate;

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

        public String getListennum() {
            return listennum;
        }

        public void setListennum(String listennum) {
            this.listennum = listennum;
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

        public String getButtontag() {
            return buttontag;
        }

        public void setButtontag(String buttontag) {
            this.buttontag = buttontag;
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

        public boolean isGoodstate() {
            return goodstate;
        }

        public void setGoodstate(boolean goodstate) {
            this.goodstate = goodstate;
        }
    }
}
