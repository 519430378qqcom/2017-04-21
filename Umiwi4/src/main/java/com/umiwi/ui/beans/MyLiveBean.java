package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dong on 2017/5/3.
 * 我的直播实体类
 */

public class MyLiveBean extends BaseGsonBeans{

    /**
     * e : 9999
     * m : 操作成功
     * r : {"record":[{"id":"3","title":"创业之初的难题","price":"¥0.99","isbuy":false,"status":"直播中","partakenum":"0","roomid":"8711573","subtitle":"创业之初的难题","limage":"http://i1.umivi.net/v/image/telecast/20170425/0_1ba41364a0c5cf79.jpg","profit":"¥0","live_time":"04月26日 16:23开始","detailurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=3"},{"id":"5","title":"怎样留住高端人才","price":"¥0.10","isbuy":false,"status":"直播中","partakenum":"0","roomid":"8692675","subtitle":"怎样留住高端人才","limage":"http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg","profit":"¥0","live_time":"04月28日 18:25开始","detailurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=5"}],"page":{"currentpage":1,"rows":"2","totalpage":1}}
     */
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
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
         * record : [{"id":"3","title":"创业之初的难题","price":"¥0.99","isbuy":false,"status":"直播中","partakenum":"0","roomid":"8711573","subtitle":"创业之初的难题","limage":"http://i1.umivi.net/v/image/telecast/20170425/0_1ba41364a0c5cf79.jpg","profit":"¥0","live_time":"04月26日 16:23开始","detailurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=3"},{"id":"5","title":"怎样留住高端人才","price":"¥0.10","isbuy":false,"status":"直播中","partakenum":"0","roomid":"8692675","subtitle":"怎样留住高端人才","limage":"http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg","profit":"¥0","live_time":"04月28日 18:25开始","detailurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=5"}]
         * page : {"currentpage":1,"rows":"2","totalpage":1}
         */
        @SerializedName("page")
        private PageBean page;
        @SerializedName("record")
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

        public static class RecordBean {
            /**
             * id : 3
             * title : 创业之初的难题
             * price : ¥0.99
             * isbuy : false
             * status : 直播中
             * partakenum : 0
             * roomid : 8711573
             * subtitle : 创业之初的难题
             * limage : http://i1.umivi.net/v/image/telecast/20170425/0_1ba41364a0c5cf79.jpg
             * profit : ¥0
             * live_time : 04月26日 16:23开始
             * detailurl : http://i.v.youmi.cn/Telecast/getlivedetail?id=3
             */
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
            @SerializedName("roomid")
            private String roomid;
            @SerializedName("subtitle")
            private String subtitle;
            @SerializedName("limage")
            private String limage;
            @SerializedName("profit")
            private String profit;
            @SerializedName("live_time")
            private String live_time;
            @SerializedName("detailurl")
            private String detailurl;

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

            public boolean isIsbuy() {
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

            public String getRoomid() {
                return roomid;
            }

            public void setRoomid(String roomid) {
                this.roomid = roomid;
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

            public String getProfit() {
                return profit;
            }

            public void setProfit(String profit) {
                this.profit = profit;
            }

            public String getLive_time() {
                return live_time;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public String getDetailurl() {
                return detailurl;
            }

            @Override
            public String toString() {
                return "RecordBean{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", price='" + price + '\'' +
                        ", isbuy=" + isbuy +
                        ", status='" + status + '\'' +
                        ", partakenum='" + partakenum + '\'' +
                        ", roomid='" + roomid + '\'' +
                        ", subtitle='" + subtitle + '\'' +
                        ", limage='" + limage + '\'' +
                        ", profit='" + profit + '\'' +
                        ", live_time='" + live_time + '\'' +
                        ", detailurl='" + detailurl + '\'' +
                        '}';
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
            }
        }
    }
}
