package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class BuyAudioLiveBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RBuyAudioLive r;

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

    public RBuyAudioLive getR() {
        return r;
    }

    public void setR(RBuyAudioLive r) {
        this.r = r;
    }
    public static class RBuyAudioLive{
        @SerializedName("record")
        private ArrayList<BuyAudioLiveRecord> record;
        @SerializedName("page")
        private BuyAudioLivePage page;

        public static class BuyAudioLiveRecord{
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("price")
            private String price;
            @SerializedName("isbuy")
            private boolean isbuy;
            @SerializedName("status")
            private String status;
            @SerializedName("partakenum")
            private String partakenum;
            @SerializedName("subtitle")
            private String subtitle;
            @SerializedName("limage")
            private String limage;
            @SerializedName("detailurl")
            private String detailurl;
            @SerializedName("roomid")
            private String roomid;

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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPartakenum() {
                return partakenum;
            }

            public void setPartakenum(String partakenum) {
                this.partakenum = partakenum;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getLimage() {
                return limage;
            }

            public void setLimage(String limage) {
                this.limage = limage;
            }

            public String getDetailurl() {
                return detailurl;
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
            }

            public String getRoomid() {
                return roomid;
            }

            public void setRoomid(String roomid) {
                this.roomid = roomid;
            }
        }
        public static class BuyAudioLivePage{
            @SerializedName("currentpage")
            private int currentpage;
            @SerializedName("rows")
            private String rows;
            @SerializedName("totalpage")
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

        public ArrayList<BuyAudioLiveRecord> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<BuyAudioLiveRecord> record) {
            this.record = record;
        }

        public BuyAudioLivePage getPage() {
            return page;
        }

        public void setPage(BuyAudioLivePage page) {
            this.page = page;
        }
    }
}
