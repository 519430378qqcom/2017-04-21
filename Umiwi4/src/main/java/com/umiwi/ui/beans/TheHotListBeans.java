package com.umiwi.ui.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shangshuaibo on 2017/3/12 15:24
 */

public class TheHotListBeans implements Serializable {


    /**
     * from : album
     * id : 12545
     * pricetag : 免费
     * title : 还在等着房价下降吗？别做梦了！
     * name : 优米
     * tutortitle : 优米音频工作室
     * watchnum : 播放1469
     * playtime : 04:15
     * hrefurl : http://i.v.youmi.cn/ClientApi/getAlbumDetail?id=12545
     */

    private List<RecordBean> record;

    public List<RecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<RecordBean> record) {
        this.record = record;
    }

    public static class RecordBean {
        private String from;
        private String id;
        private String pricetag;
        private String title;
        private String name;
        private String tutortitle;
        private String watchnum;
        private String playtime;
        private String hrefurl;
        private String threadnum;
        private String salenum;
        private String type;
        private String numtag;

        public String getNumtag() {
            return numtag;
        }

        public void setNumtag(String numtag) {
            this.numtag = numtag;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getThreadnum() {
            return threadnum;
        }

        public void setThreadnum(String threadnum) {
            this.threadnum = threadnum;
        }

        public String getSalenum() {
            return salenum;
        }

        public void setSalenum(String salenum) {
            this.salenum = salenum;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPricetag() {
            return pricetag;
        }

        public void setPricetag(String pricetag) {
            this.pricetag = pricetag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getHrefurl() {
            return hrefurl;
        }

        public void setHrefurl(String hrefurl) {
            this.hrefurl = hrefurl;
        }

        @Override
        public String toString() {
            return "RecordBean{" +
                    "from='" + from + '\'' +
                    ", id='" + id + '\'' +
                    ", pricetag='" + pricetag + '\'' +
                    ", title='" + title + '\'' +
                    ", name='" + name + '\'' +
                    ", tutortitle='" + tutortitle + '\'' +
                    ", watchnum='" + watchnum + '\'' +
                    ", playtime='" + playtime + '\'' +
                    ", hrefurl='" + hrefurl + '\'' +
                    ", threadnum='" + threadnum + '\'' +
                    ", salenum='" + salenum + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TheHotListBeans{" +
                "record=" + record +
                '}';
    }
}
