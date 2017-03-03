package com.umiwi.ui.beans.updatebeans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class VideoBean {

    /**
     * record : [{"id":"12126","issee":true,"shorttitle":"柳传志是中关村人的教父","limage":"http://i1.umivi.net/v/album/listimage/2015-07/12126_190X1420709.jpg","watchnum":"41671","name":"雷军","tutortitle":"小米科技创始人 ","price":"￥0.00","time":"2015-07-09","playtime":"03:45"},{"id":"11787","issee":true,"shorttitle":"雷军：我们有一个梦想","limage":"http://i1.umivi.net/v/album/listimage/2014-12/11787_190X1421219.jpg","watchnum":"28880","name":"雷军","tutortitle":"小米科技创始人 ","price":"￥0.00","time":"2014-12-18","playtime":"05:35"},{"id":"11786","issee":true,"shorttitle":"雷军：我所理解的创业","limage":"http://i1.umivi.net/v/album/listimage/2014-12/11786_190X1421218.jpg","watchnum":"20857","name":"雷军","tutortitle":"小米科技创始人 ","price":"￥0.00","time":"2014-12-18","playtime":"06:10"},{"id":"11664","issee":true,"shorttitle":"我只想做一家小公司","limage":"http://i1.umivi.net/v/album/listimage/2014-10/11664_190X1421027.jpg","watchnum":"82451","name":"雷军","tutortitle":"小米科技创始人 ","price":"￥0.00","time":"2014-10-27","playtime":"26:03"},{"id":"10092","issee":false,"shorttitle":"雷军：创业第一课","limage":"http://i1.umivi.net/v/album/listimage/2015-02/10092_190X1420226.jpg","watchnum":"85313","name":"雷军","tutortitle":"小米科技创始人 ","price":"￥29.00","time":"2013-04-16","playtime":"01:12:28"}]
     * page : {"currentpage":1,"rows":"6","totalpage":2}
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
         * id : 12126
         * issee : true
         * shorttitle : 柳传志是中关村人的教父
         * limage : http://i1.umivi.net/v/album/listimage/2015-07/12126_190X1420709.jpg
         * watchnum : 41671
         * name : 雷军
         * tutortitle : 小米科技创始人
         * price : ￥0.00
         * time : 2015-07-09
         * playtime : 03:45
         */

        private String id;
        private boolean issee;
        private String shorttitle;
        private String limage;
        private String watchnum;
        private String name;
        private String tutortitle;
        private String price;
        private String time;
        private String playtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIssee() {
            return issee;
        }

        public void setIssee(boolean issee) {
            this.issee = issee;
        }

        public String getShorttitle() {
            return shorttitle;
        }

        public void setShorttitle(String shorttitle) {
            this.shorttitle = shorttitle;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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
