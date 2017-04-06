package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class VideoSpecialDetailBean extends BaseGsonBeans {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("subtitle")
    private String subtitle;
    @SerializedName("introduce")
    private String introduce;
    @SerializedName("salenum")
    private String salenum;
    @SerializedName("sharetext")
    private String sharetext;
    @SerializedName("totalnum")
    private String totalnum;
    @SerializedName("price")
    private String price;
    @SerializedName("isbuy")
    private boolean isbuy;
    @SerializedName("raw_price")
    private String raw_price;
    @SerializedName("image")
    private String image;
    @SerializedName("total")
    private String total;
    @SerializedName("record")
    private ArrayList<VideoSpecialRecord> record;
    @SerializedName("share")
    private VideoSpecialShare share;

    public VideoSpecialShare getShare() {
        return share;
    }

    public void setShare(VideoSpecialShare share) {
        this.share = share;
    }
    public static class VideoSpecialShare{
        @SerializedName("sharetitle")
        private String sharetitle;
        @SerializedName("shareimg")
        private String shareimg;
        @SerializedName("shareurl")
        private String shareurl;
        @SerializedName("sharecontent")
        private String sharecontent;

        public String getSharetitle() {
            return sharetitle;
        }

        public void setSharetitle(String sharetitle) {
            this.sharetitle = sharetitle;
        }

        public String getShareimg() {
            return shareimg;
        }

        public void setShareimg(String shareimg) {
            this.shareimg = shareimg;
        }

        public String getShareurl() {
            return shareurl;
        }

        public void setShareurl(String shareurl) {
            this.shareurl = shareurl;
        }

        public String getSharecontent() {
            return sharecontent;
        }

        public void setSharecontent(String sharecontent) {
            this.sharecontent = sharecontent;
        }

        @Override
        public String toString() {
            return "VideoSpecialShare{" +
                    "sharetitle='" + sharetitle + '\'' +
                    ", shareimg='" + shareimg + '\'' +
                    ", shareurl='" + shareurl + '\'' +
                    ", sharecontent='" + sharecontent + '\'' +
                    '}';
        }
    }
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getSalenum() {
        return salenum;
    }

    public void setSalenum(String salenum) {
        this.salenum = salenum;
    }

    public String getSharetext() {
        return sharetext;
    }

    public void setSharetext(String sharetext) {
        this.sharetext = sharetext;
    }

    public String getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(String totalnum) {
        this.totalnum = totalnum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isbuy() {
        return isbuy;
    }

    public void setIsbuy(boolean isbuy) {
        this.isbuy = isbuy;
    }

    public String getRaw_price() {
        return raw_price;
    }

    public void setRaw_price(String raw_price) {
        this.raw_price = raw_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<VideoSpecialRecord> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<VideoSpecialRecord> record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "VideoSpecialDetailBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", introduce='" + introduce + '\'' +
                ", salenum='" + salenum + '\'' +
                ", sharetext='" + sharetext + '\'' +
                ", totalnum='" + totalnum + '\'' +
                ", price='" + price + '\'' +
                ", isbuy=" + isbuy +
                ", raw_price='" + raw_price + '\'' +
                ", image='" + image + '\'' +
                ", total='" + total + '\'' +
                ", record=" + record +
                '}';
    }
    public static class VideoSpecialRecord{
        @SerializedName("id")
        private String id;
        @SerializedName("watchnum")
        private String watchnum;
        @SerializedName("title")
        private String title;
        @SerializedName("price")
        private String price;
        @SerializedName("time")
        private String time;
        @SerializedName("playtime")
        private String playtime;
        @SerializedName("isbuy")
        private String isbuy;
        @SerializedName("detailurl")
        private String detailurl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
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

        public String getIsbuy() {
            return isbuy;
        }

        public void setIsbuy(String isbuy) {
            this.isbuy = isbuy;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        @Override
        public String toString() {
            return "VideoSpecialRecord{" +
                    "id='" + id + '\'' +
                    ", watchnum='" + watchnum + '\'' +
                    ", title='" + title + '\'' +
                    ", price='" + price + '\'' +
                    ", time='" + time + '\'' +
                    ", playtime='" + playtime + '\'' +
                    ", isbuy='" + isbuy + '\'' +
                    ", detailurl='" + detailurl + '\'' +
                    '}';
        }

    }
}
