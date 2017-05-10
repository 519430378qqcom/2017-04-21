package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dong on 2017/5/3.
 * x
 */

public class ChatRoomDetailsBean {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"record":{"id":"5","roomid":"8692675","gethistory":"http://i.v.youmi.cn/telecast/closeliveDetailapi/","tutor_accid":"1_7106276","cur_accid":"1_7106276","status":"1","istutor":"1"},"share":{"shareurl":"http://i.v.youmi.cn/telecast/closeliveDetailApi/?id=5","sharetitle":"怎样留住高端人才","sharecontent":"初创团队急缺高端人才，可是一没充足的资金、二没成形的事业，如果只靠游说很难留住人才，这是每位创始人都非常头疼的问题。用什么吸引他？用什么留住他？看看各位专家是如何解答的。 下载客户端 http://m.umiwi.com","shareimg":"http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg"}}
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
         * record : {"id":"5","roomid":"8692675","gethistory":"http://i.v.youmi.cn/telecast/closeliveDetailapi/","tutor_accid":"1_7106276","cur_accid":"1_7106276","status":"1","istutor":"1"}
         * share : {"shareurl":"http://i.v.youmi.cn/telecast/closeliveDetailApi/?id=5","sharetitle":"怎样留住高端人才","sharecontent":"初创团队急缺高端人才，可是一没充足的资金、二没成形的事业，如果只靠游说很难留住人才，这是每位创始人都非常头疼的问题。用什么吸引他？用什么留住他？看看各位专家是如何解答的。 下载客户端 http://m.umiwi.com","shareimg":"http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg"}
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

        public static class RecordBean {
            /**
             * id : 5
             * roomid : 8692675
             * title: "怎样留住高端人才",
             *partakenum: "0",
             * gethistory : http://i.v.youmi.cn/telecast/closeliveDetailapi/
             * tutor_accid : 1_7106276
             * cur_accid : 1_7106276
             * status : 1
             * istutor : 1
             */
            @SerializedName("id")
            private String id;
            @SerializedName("roomid")
            private String roomid;
            @SerializedName("title")
            private String title;
            @SerializedName("partakenum")
            private String partakenum;
            @SerializedName("gethistory")
            private String gethistory;
            @SerializedName("tutor_accid")
            private String tutor_accid;
            @SerializedName("cur_accid")
            private String cur_accid;
            @SerializedName("status")
            private String status;
            @SerializedName("istutor")
            private String istutor;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPartakenum() {
                return partakenum;
            }

            public void setPartakenum(String partakenum) {
                this.partakenum = partakenum;
            }
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRoomid() {
                return roomid;
            }

            public void setRoomid(String roomid) {
                this.roomid = roomid;
            }

            public String getGethistory() {
                return gethistory;
            }

            public void setGethistory(String gethistory) {
                this.gethistory = gethistory;
            }

            public String getTutor_accid() {
                return tutor_accid;
            }

            public void setTutor_accid(String tutor_accid) {
                this.tutor_accid = tutor_accid;
            }

            public String getCur_accid() {
                return cur_accid;
            }

            public void setCur_accid(String cur_accid) {
                this.cur_accid = cur_accid;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getIstutor() {
                return istutor;
            }

            public void setIstutor(String istutor) {
                this.istutor = istutor;
            }
        }

        public static class ShareBean {
            /**
             * shareurl : http://i.v.youmi.cn/telecast/closeliveDetailApi/?id=5
             * sharetitle : 怎样留住高端人才
             * sharecontent : 初创团队急缺高端人才，可是一没充足的资金、二没成形的事业，如果只靠游说很难留住人才，这是每位创始人都非常头疼的问题。用什么吸引他？用什么留住他？看看各位专家是如何解答的。 下载客户端 http://m.umiwi.com
             * shareimg : http://i1.umivi.net/v/image/telecast/20170425/0_51e0a33b06b92dcb.jpg
             */
            @SerializedName("shareurl")
            private String shareurl;
            @SerializedName("sharetitle")
            private String sharetitle;
            @SerializedName("sharecontent")
            private String sharecontent;
            @SerializedName("shareimg")
            private String shareimg;

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

            public String getSharecontent() {
                return sharecontent;
            }

            public void setSharecontent(String sharecontent) {
                this.sharecontent = sharecontent;
            }

            public String getShareimg() {
                return shareimg;
            }

            public void setShareimg(String shareimg) {
                this.shareimg = shareimg;
            }
        }
    }
}
