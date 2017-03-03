package com.umiwi.ui.beans.updatebeans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class CommentBean {

    /**
     * record : [{"id":"800930","time":"2017-03-01 16:51:40","content":"The best ","uid":"7106276","name":"吕钗","avatar":"http://i2.umivi.net/avatar/76/7106276b.jpg?t=24808758"}]
     * totalnum : 1
     * page : {"currentpage":1,"rows":"1","totalpage":1}
     */

    private String totalnum;
    private PageBean page;
    private List<RecordBean> record;

    public String getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(String totalnum) {
        this.totalnum = totalnum;
    }

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
         * id : 800930
         * time : 2017-03-01 16:51:40
         * content : The best
         * uid : 7106276
         * name : 吕钗
         * avatar : http://i2.umivi.net/avatar/76/7106276b.jpg?t=24808758
         */

        private String id;
        private String time;
        private String content;
        private String uid;
        private String name;
        private String avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
