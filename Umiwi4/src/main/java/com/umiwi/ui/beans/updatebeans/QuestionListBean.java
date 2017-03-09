package com.umiwi.ui.beans.updatebeans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */

public class QuestionListBean {

    /**
     * page : {"currentpage":1,"rows":"6","totalpage":1}
     * record : [{"answertime":"03-05","buttontag":"1元偷偷听","goodnum":"1","goodstate":false,"id":"45","listennum":"听过0人","listentype":"1","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=45","playtime":"12''","tavatar":"http://i2.umivi.net/avatar/76/7106276s.jpg","title":"Qqqqq"},{"answertime":"03-05","buttontag":"1元偷偷听","goodnum":"2","goodstate":false,"id":"44","listennum":"听过1人","listentype":"1","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=44","playtime":"6''","tavatar":"http://i2.umivi.net/avatar/76/7106276s.jpg","title":"怎样学习java"},{"answertime":"03-04","buttontag":"限时免费听","goodnum":"1","goodstate":false,"id":"43","listennum":"听过1人","listentype":"2","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=43","playtime":"52''","tavatar":"http://i2.umivi.net/avatar/76/7106276s.jpg","title":"Who are you?"},{"answertime":"03-04","buttontag":"1元偷偷听","goodnum":"0","goodstate":false,"id":"42","listennum":"听过0人","listentype":"1","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=42","playtime":"3''","tavatar":"http://i2.umivi.net/avatar/76/7106276s.jpg","title":"几点睡觉？"},{"answertime":"03-04","buttontag":"1元偷偷听","goodnum":"0","goodstate":false,"id":"38","listennum":"听过0人","listentype":"1","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=38","playtime":"60''","tavatar":"http://i2.umivi.net/avatar/76/7106276s.jpg","title":"测试后台回答"},{"answertime":"02-26","buttontag":"1元偷偷听","goodnum":"0","goodstate":false,"id":"32","listennum":"听过1人","listentype":"1","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=32","playtime":"0''","tavatar":"http://i2.umivi.net/avatar/76/7106276s.jpg","title":"Nihao"}]
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
         * rows : 6
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
         * answertime : 03-05
         * buttontag : 1元偷偷听
         * goodnum : 1
         * goodstate : false
         * id : 45
         * listennum : 听过0人
         * listentype : 1
         * playsource : http://i.v.youmi.cn/question/playsourceapi?id=45
         * playtime : 12''
         * tavatar : http://i2.umivi.net/avatar/76/7106276s.jpg
         * title : Qqqqq
         */

        private String answertime;
        private String buttontag;
        private String goodnum;
        private boolean goodstate;
        private String id;
        private String listennum;
        private String listentype;
        private String playsource;
        private String playtime;
        private String tavatar;
        private String title;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getListennum() {
            return listennum;
        }

        public void setListennum(String listennum) {
            this.listennum = listennum;
        }

        public String getListentype() {
            return listentype;
        }

        public void setListentype(String listentype) {
            this.listentype = listentype;
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

        public String getTavatar() {
            return tavatar;
        }

        public void setTavatar(String tavatar) {
            this.tavatar = tavatar;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
