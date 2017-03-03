package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */
public class VideoBean {


    /**
     * record : [{"id":"12534","short":"最新出炉 陆奇雷军 沈南鹏三位大佬谈工业","limage":"http://i1.umivi.net/v/album/listimage/2017-03/12534_190X14203010726.jpg","watchnum":"1173","name":"王利芬","tutortitle":"优米网创始人、总裁 ","time":"2017-03-01","playtime":"03:04"},{"id":"12533","short":"别让技术影响你的商业判断","limage":"http://i1.umivi.net/v/album/listimage/2017-03/12533_190X14203010449.jpg","watchnum":"642","name":"马云","tutortitle":"阿里巴巴集团董事局主席 ","time":"2017-03-01","playtime":"01:07"},{"id":"12532","short":"证监会为什么要调查赵薇入主的万家文化？","limage":"http://i1.umivi.net/v/album/listimage/2017-03/12532_190X14203010418.jpg","watchnum":"2943","name":"王利芬","tutortitle":"优米网创始人、总裁 ","time":"2017-02-28","playtime":"03:10"},{"id":"12531","short":"做有定位的企业是一种幸福","limage":"http://i1.umivi.net/v/album/listimage/2017-03/12531_190X14203010943.jpg","watchnum":"581","name":"唐侠","tutortitle":"深圳中兴飞贷金融科技有限公司创始人、董事长 ","time":"2017-02-28","playtime":"05:22"}]
     * page : {"currentpage":1,"rows":"1589","totalpage":398}
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
         * rows : 1589
         * totalpage : 398
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
         * id : 12534
         * short : 最新出炉 陆奇雷军 沈南鹏三位大佬谈工业
         * limage : http://i1.umivi.net/v/album/listimage/2017-03/12534_190X14203010726.jpg
         * watchnum : 1173
         * name : 王利芬
         * tutortitle : 优米网创始人、总裁
         * time : 2017-03-01
         * playtime : 03:04
         */

        private String id;
        @SerializedName("short")
        private String shortX;
        private String limage;
        private String watchnum;
        private String name;
        private String tutortitle;
        private String time;
        private String playtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShortX() {
            return shortX;
        }

        public void setShortX(String shortX) {
            this.shortX = shortX;
        }

        public String getLimage() {
            return limage;
        }

        public void setLimage(String limage) {
            this.limage = limage;
        }

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTutortitle() {
            return tutortitle;
        }

        public void setTutortitle(String tutortitle) {
            this.tutortitle = tutortitle;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }
    }
}
