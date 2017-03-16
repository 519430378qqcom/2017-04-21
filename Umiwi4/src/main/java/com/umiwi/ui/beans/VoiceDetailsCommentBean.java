package com.umiwi.ui.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${Gpsi} on 2017/3/17.
 */

public class VoiceDetailsCommentBean implements Serializable {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"record":[{"id":"801331","time":"2017-03-17 00:04:50","content":"姐姐过分","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801326","time":"2017-03-16 23:57:08","content":"你虎","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801319","time":"2017-03-16 23:53:19","content":"好好的","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801310","time":"2017-03-16 23:17:48","content":"回家非常棒","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801309","time":"2017-03-16 23:16:50","content":"好好补补","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801308","time":"2017-03-16 23:15:25","content":"黄河鬼棺","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801306","time":"2017-03-16 23:14:36","content":"坎坎坷坷回合肥","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801305","time":"2017-03-16 23:14:26","content":"坎坎坷坷","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801301","time":"2017-03-16 23:10:35","content":"发个哈哈v刚刚回家斤斤计较","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801300","time":"2017-03-16 23:10:25","content":"发个哈哈v刚刚回家","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"}],"totalnum":"20","page":{"currentpage":1,"rows":"20","totalpage":2}}
     */

    private String e;
    private String m;
    private RBean r;

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

    public RBean getR() {
        return r;
    }

    public void setR(RBean r) {
        this.r = r;
    }

    public static class RBean {
        /**
         * record : [{"id":"801331","time":"2017-03-17 00:04:50","content":"姐姐过分","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801326","time":"2017-03-16 23:57:08","content":"你虎","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801319","time":"2017-03-16 23:53:19","content":"好好的","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801310","time":"2017-03-16 23:17:48","content":"回家非常棒","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801309","time":"2017-03-16 23:16:50","content":"好好补补","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801308","time":"2017-03-16 23:15:25","content":"黄河鬼棺","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801306","time":"2017-03-16 23:14:36","content":"坎坎坷坷回合肥","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801305","time":"2017-03-16 23:14:26","content":"坎坎坷坷","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801301","time":"2017-03-16 23:10:35","content":"发个哈哈v刚刚回家斤斤计较","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"},{"id":"801300","time":"2017-03-16 23:10:25","content":"发个哈哈v刚刚回家","uid":"7658529","name":"","avatar":"http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006"}]
         * totalnum : 20
         * page : {"currentpage":1,"rows":"20","totalpage":2}
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
             * rows : 20
             * totalpage : 2
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
             * id : 801331
             * time : 2017-03-17 00:04:50
             * content : 姐姐过分
             * uid : 7658529
             * name :
             * avatar : http://i2.umivi.net/avatar/29/7658529b.jpg?t=24828006
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
}
