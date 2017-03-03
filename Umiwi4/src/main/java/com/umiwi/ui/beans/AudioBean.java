package com.umiwi.ui.beans;

import java.util.List;

/**
 * Created by sll on 2017/2/28.
 */
public class AudioBean {


    /**
     * page : {"totalpage":5,"rows":"20","currentpage":1}
     * record : [{"process":"15%","watchnum":"0","id":"17","cat":"","title":"独立音频收费2","price":"￥20","playtime":"15:24"},{"process":"15%","watchnum":"0","id":"18","cat":"","title":"独立音频收费3","price":"￥22","playtime":"15:24"},{"process":"15%","watchnum":"0","id":"5","cat":"实体业转型","title":"凡客诚品--音频","price":"￥0","playtime":"00:00"},{"process":"15%","watchnum":"22","id":"2","cat":"","title":"专栏音频","price":"￥299","playtime":"00:00"}]
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
         * totalpage : 5
         * rows : 20
         * currentpage : 1
         */

        private int totalpage;
        private String rows;
        private int currentpage;

        public int getTotalpage() {
            return totalpage;
        }

        public void setTotalpage(int totalpage) {
            this.totalpage = totalpage;
        }

        public String getRows() {
            return rows;
        }

        public void setRows(String rows) {
            this.rows = rows;
        }

        public int getCurrentpage() {
            return currentpage;
        }

        public void setCurrentpage(int currentpage) {
            this.currentpage = currentpage;
        }
    }

    public static class RecordBean {
        /**
         * process : 15%
         * watchnum : 0
         * id : 17
         * cat :
         * title : 独立音频收费2
         * price : ￥20
         * playtime : 15:24
         */

        private String process;
        private String watchnum;
        private String id;
        private String cat;
        private String title;
        private String price;
        private String playtime;

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCat() {
            return cat;
        }

        public void setCat(String cat) {
            this.cat = cat;
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

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }
    }

    @Override
    public String toString() {
        return "AudioBean{" +
                "page=" + page +
                ", record=" + record +
                '}';
    }
}