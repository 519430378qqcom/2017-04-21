package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 * 直播详情的bean类
 */

public class LiveDetailsBean extends BaseGsonBeans{

    /**
     * e : 9999
     * m : 操作成功
     * r : {"record":{"id":"5","title":"怎样留住高端人才","price":"¥0.10","isbuy":false,"status":"直播中","partakenum":"0","roomid":"8692675","live_time":"04月26日 16:25开始","image":"http://i1.umivi.net/v/image/telecast/20170425/0_3ac98e21d7200e5c.jpg","description":[{"word":"1.明确目的，暖场。说明会谈的目的、步骤和时间。"},{"word":"2.员工自评，让员工谈总体感受、能力、收获、不足。"},{"word":"3.上级评价，上级进行业绩评价、能力评价，先说成绩后说不足，不足要举具体事例"},{"word":"4.讨论并确认评价结果，明确告知考核结果，与员工达成一致。"},{"word":"5.针对不足制定改善计划，就能力不足达成共识。"},{"word":"6.共同制定下期工作目标，并确定不同工作的权重，管理者提供工具、支持、承诺。"},{"word":"7.认同后，双方签字确认，表达谢意，记录整理归档。"}],"detailurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=5","isVip":false},"share":{"shareurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=5","sharetitle":"怎样留住高端人才","sharecontent":[{"word":"初创团队急缺高端人才，可是一没充足的资金、二没成形的事业，如果只靠游说很难留住人才，这是每位创始人都非常头疼的问题。用什么吸引他？用什么留住他？看看各位专家是如何解答的。"}],"shareimg":"http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg"}}
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
         * record : {"id":"5","title":"怎样留住高端人才","price":"¥0.10","isbuy":false,"status":"直播中","partakenum":"0","roomid":"8692675","live_time":"04月26日 16:25开始","image":"http://i1.umivi.net/v/image/telecast/20170425/0_3ac98e21d7200e5c.jpg","description":[{"word":"1.明确目的，暖场。说明会谈的目的、步骤和时间。"},{"word":"2.员工自评，让员工谈总体感受、能力、收获、不足。"},{"word":"3.上级评价，上级进行业绩评价、能力评价，先说成绩后说不足，不足要举具体事例"},{"word":"4.讨论并确认评价结果，明确告知考核结果，与员工达成一致。"},{"word":"5.针对不足制定改善计划，就能力不足达成共识。"},{"word":"6.共同制定下期工作目标，并确定不同工作的权重，管理者提供工具、支持、承诺。"},{"word":"7.认同后，双方签字确认，表达谢意，记录整理归档。"}],"detailurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=5","isVip":false}
         * share : {"shareurl":"http://i.v.youmi.cn/Telecast/getlivedetail?id=5","sharetitle":"怎样留住高端人才","sharecontent":[{"word":"初创团队急缺高端人才，可是一没充足的资金、二没成形的事业，如果只靠游说很难留住人才，这是每位创始人都非常头疼的问题。用什么吸引他？用什么留住他？看看各位专家是如何解答的。"}],"shareimg":"http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg"}
         */
        @SerializedName("record")
        private RecordBean record;
        @SerializedName("share")
        private ShareBean share;

        public RecordBean getRecord() {
            return record;
        }

        public void setRecord(RecordBean record) {
            this.record = record;
        }

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public static class RecordBean implements Serializable{
            /**
             * id : 5
             * title : 怎样留住高端人才
             * price : ¥0.10
             * isbuy : false
             * status : 直播中
             * partakenum : 0
             * roomid : 8692675
             * live_time : 04月26日 16:25开始
             * image : http://i1.umivi.net/v/image/telecast/20170425/0_3ac98e21d7200e5c.jpg
             * description : [{"word":"1.明确目的，暖场。说明会谈的目的、步骤和时间。"},{"word":"2.员工自评，让员工谈总体感受、能力、收获、不足。"},{"word":"3.上级评价，上级进行业绩评价、能力评价，先说成绩后说不足，不足要举具体事例"},{"word":"4.讨论并确认评价结果，明确告知考核结果，与员工达成一致。"},{"word":"5.针对不足制定改善计划，就能力不足达成共识。"},{"word":"6.共同制定下期工作目标，并确定不同工作的权重，管理者提供工具、支持、承诺。"},{"word":"7.认同后，双方签字确认，表达谢意，记录整理归档。"}]
             * detailurl : http://i.v.youmi.cn/Telecast/getlivedetail?id=5
             * isVip : false
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
            @SerializedName("partakemun")
            private String partakenum;
            @SerializedName("roomid")
            private String roomid;
            @SerializedName("live_time")
            private String live_time;
            @SerializedName("image")
            private String image;
            @SerializedName("detailurl")
            private String detailurl;
            @SerializedName("isVip")
            private boolean isVip;
            @SerializedName("description")
            private List<DescriptionBean> description;

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

            public String getLive_time() {
                return live_time;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getDetailurl() {
                return detailurl;
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
            }

            public boolean isIsVip() {
                return isVip;
            }

            public void setIsVip(boolean isVip) {
                this.isVip = isVip;
            }

            public List<DescriptionBean> getDescription() {
                return description;
            }

            public void setDescription(List<DescriptionBean> description) {
                this.description = description;
            }

            public static class DescriptionBean {
                /**
                 * word : 1.明确目的，暖场。说明会谈的目的、步骤和时间。
                 */
                @SerializedName("word")
                private String word;

                public String getWord() {
                    return word;
                }

                public void setWord(String word) {
                    this.word = word;
                }
            }
        }

        public static class ShareBean {
            /**
             * shareurl : http://i.v.youmi.cn/Telecast/getlivedetail?id=5
             * sharetitle : 怎样留住高端人才
             * sharecontent : [{"word":"初创团队急缺高端人才，可是一没充足的资金、二没成形的事业，如果只靠游说很难留住人才，这是每位创始人都非常头疼的问题。用什么吸引他？用什么留住他？看看各位专家是如何解答的。"}]
             * shareimg : http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg
             */
            @SerializedName("shareurl")
            private String shareurl;
            @SerializedName("sharetitle")
            private String sharetitle;
            @SerializedName("shareimg")
            private String shareimg;
            @SerializedName("sharecontent")
            private List<SharecontentBean> sharecontent;

            public String getShareurl() {
                return shareurl;
            }

            public void setShareurl(String shareurl) {
                this.shareurl = shareurl;
            }

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

            public List<SharecontentBean> getSharecontent() {
                return sharecontent;
            }

            public void setSharecontent(List<SharecontentBean> sharecontent) {
                this.sharecontent = sharecontent;
            }

            public static class SharecontentBean {
                /**
                 * word : 初创团队急缺高端人才，可是一没充足的资金、二没成形的事业，如果只靠游说很难留住人才，这是每位创始人都非常头疼的问题。用什么吸引他？用什么留住他？看看各位专家是如何解答的。
                 */
                @SerializedName("word")
                private String word;

                public String getWord() {
                    return word;
                }

                public void setWord(String word) {
                    this.word = word;
                }
            }
        }
    }
}
