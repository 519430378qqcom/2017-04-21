package com.umiwi.ui.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class VoiceBean  {
    /**
     * record : [{"id":"5","title":"凡客诚品--音频","cat":"实体业转型","price":"￥0","playtime":"00:00","watchnum":"0","process":"0%","hrefurl":"http://i.v.youmi.cn/audioalbum/getApi?id=5"},{"id":"6","title":"凡客诚品--音频2","cat":"","price":"￥66","playtime":"00:00","watchnum":"0","process":"0%","hrefurl":"http://i.v.youmi.cn/audioalbum/getApi?id=6"}]
     * page : {"currentpage":1,"rows":"2","totalpage":1}
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
         * rows : 2
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
         * id : 5
         * title : 凡客诚品--音频
         * cat : 实体业转型
         * price : ￥0
         * playtime : 00:00
         * watchnum : 0
         * process : 0%
         * hrefurl : http://i.v.youmi.cn/audioalbum/getApi?id=5
         */

        private String id;
        private String title;
        private String cat;
        private String price;
        private String playtime;
        private String watchnum;
        private String process;
        private String hrefurl;

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

        public String getCat() {
            return cat;
        }

        public void setCat(String cat) {
            this.cat = cat;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public String getHrefurl() {
            return hrefurl;
        }

        public void setHrefurl(String hrefurl) {
            this.hrefurl = hrefurl;
        }
    }
}
