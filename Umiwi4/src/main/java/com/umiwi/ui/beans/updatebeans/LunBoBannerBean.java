package com.umiwi.ui.beans.updatebeans;

import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class LunBoBannerBean extends BaseGsonBeans {
    private List<RecordBean> record;

    public List<RecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<RecordBean> record) {
        this.record = record;
    }

    public static class RecordBean {
        /**
         * image : http://i1.umivi.net/v/lunbo/20b29a2715ebdc2cb2c680b5dc41ccf3.jpg
         * identity : 3
         * types : column
         * sort : 1
         * isbuy : false
         * albumid : 70101030
         * title : 大数据行业热点分析
         * detailurl : http://i.v.youmi.cn/tutorcolumn/detailApi?tutoruid=70101030&frm=lunbo_course&click=1450
         * url : http://i.v.youmi.cn/tutorcolumn/detailApi?tutoruid=70101030&frm=lunbo_course&click=1450
         * spmurl : http://i.v.youmi.cn/apireader/addToRouteByParam?contro=/lunbo/list&spm=1.0.0.0.0.0
         */

        private String image;
        private String identity;
        private String types;
        private String sort;
        private boolean isbuy;
        private String albumid;
        private String title;
        private String detailurl;
        private String url;
        private String spmurl;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public boolean isIsbuy() {
            return isbuy;
        }

        public void setIsbuy(boolean isbuy) {
            this.isbuy = isbuy;
        }

        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSpmurl() {
            return spmurl;
        }

        public void setSpmurl(String spmurl) {
            this.spmurl = spmurl;
        }
    }

//    @SerializedName("record")
//    private ArrayList<LunBoBannerRecord> record;
//
//    public ArrayList<LunBoBannerRecord> getRecord() {
//        return record;
//    }
//
//    public void setRecord(ArrayList<LunBoBannerRecord> record) {
//        this.record = record;
//    }
//
//    public static class  LunBoBannerRecord{
//        @SerializedName("image")
//        private String image;
//        @SerializedName("identity")
//        private String identity;
//        @SerializedName("types")
//        private String types;
//        @SerializedName("sort")
//        private String sort;
//        @SerializedName("isbuy")
//        private boolean isbuy;
//        @SerializedName("albumid")
//        private String albumid;
//        @SerializedName("title")
//        private String title;
//        @SerializedName("detailurl")
//        private String detailurl;
//        @SerializedName("url")
//        private String url;
//        @SerializedName("spmurl")
//        private String spmurl;
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public String getIdentity() {
//            return identity;
//        }
//
//        public void setIdentity(String identity) {
//            this.identity = identity;
//        }
//
//        public String getTypes() {
//            return types;
//        }
//
//        public void setTypes(String types) {
//            this.types = types;
//        }
//
//        public String getSort() {
//            return sort;
//        }
//
//        public void setSort(String sort) {
//            this.sort = sort;
//        }
//
//        public boolean isbuy() {
//            return isbuy;
//        }
//
//        public void setIsbuy(boolean isbuy) {
//            this.isbuy = isbuy;
//        }
//
//        public String getAlbumid() {
//            return albumid;
//        }
//
//        public void setAlbumid(String albumid) {
//            this.albumid = albumid;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getDetailurl() {
//            return detailurl;
//        }
//
//        public void setDetailurl(String detailurl) {
//            this.detailurl = detailurl;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public String getSpmurl() {
//            return spmurl;
//        }
//
//        public void setSpmurl(String spmurl) {
//            this.spmurl = spmurl;
//        }
//    }

}
