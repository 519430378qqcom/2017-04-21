package com.umiwi.ui.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */
public class LogincalThinkingBean {

    /**
     * record : [{"id":"7","title":"健康生活--音频","istry":true,"audiotype":"1","playtime":"05:30","watchnum":"32人读过","goodnum":"0","description":"","image":"http://i1.umivi.net/v/image/audioalbum/20170224/0_52968410f76dc381.jpg","onlinetime":"02-23"}]
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
         * id : 7
         * title : 健康生活--音频
         * istry : true
         * audiotype : 1
         * playtime : 05:30
         * watchnum : 32人读过
         * goodnum : 0
         * description :
         * image : http://i1.umivi.net/v/image/audioalbum/20170224/0_52968410f76dc381.jpg
         * onlinetime : 02-23
         */

        private String id;
        private String title;
        private boolean istry;
        private String audiotype;
        private String playtime;
        private String watchnum;
        private String goodnum;
        private String description;
        private String image;
        private String onlinetime;

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

        public boolean isIstry() {
            return istry;
        }

        public void setIstry(boolean istry) {
            this.istry = istry;
        }

        public String getAudiotype() {
            return audiotype;
        }

        public void setAudiotype(String audiotype) {
            this.audiotype = audiotype;
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

        public String getGoodnum() {
            return goodnum;
        }

        public void setGoodnum(String goodnum) {
            this.goodnum = goodnum;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getOnlinetime() {
            return onlinetime;
        }

        public void setOnlinetime(String onlinetime) {
            this.onlinetime = onlinetime;
        }
    }
}
