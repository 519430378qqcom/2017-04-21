package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

/**
 * Created by Administrator on 2017/3/13.
 */

public class QuestionBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RQuestionr r;

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

    public RQuestionr getR() {
        return r;
    }

    public void setR(RQuestionr r) {
        this.r = r;
    }

    public static class RQuestionr{
        @SerializedName("id")
        private String id;
        @SerializedName("tuid")
        private String tuid;
        @SerializedName("title")
        private String title;
        @SerializedName("tavatar")
        private String tavatar;
        @SerializedName("listennum")
        private String listennum;
        @SerializedName("answertime")
        private String answertime;
        @SerializedName("listentype")
        private String listentype;
        @SerializedName("buttontag")
        private String buttontag;
        @SerializedName("playsource")
        private String playsource;
        @SerializedName("playtime")
        private String playtime;
        @SerializedName("goodnum")
        private String goodnum;
        @SerializedName("goodstate")
        private boolean goodstate;
        @SerializedName("tutor_name")
        private String tutor_name;
        @SerializedName("tutor_title")
        private String tutor_title;

        @SerializedName("tutor_avatar")
        private String tutor_avatar;

        @SerializedName("tutor_description")
        private String tutor_description;
        @SerializedName("tutor_ask_desc")
        private String tutor_ask_desc;
        @SerializedName("allquestionurl")
        private String allquestionurl;

        @SerializedName("share")
        private Share  share;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTuid() {
            return tuid;
        }

        public void setTuid(String tuid) {
            this.tuid = tuid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTavatar() {
            return tavatar;
        }

        public void setTavatar(String tavatar) {
            this.tavatar = tavatar;
        }

        public String getListennum() {
            return listennum;
        }

        public void setListennum(String listennum) {
            this.listennum = listennum;
        }

        public String getAnswertime() {
            return answertime;
        }

        public void setAnswertime(String answertime) {
            this.answertime = answertime;
        }

        public String getListentype() {
            return listentype;
        }

        public void setListentype(String listentype) {
            this.listentype = listentype;
        }

        public String getButtontag() {
            return buttontag;
        }

        public void setButtontag(String buttontag) {
            this.buttontag = buttontag;
        }

        public String getPlaysource() {
            return playsource;
        }

        public void setPlaysource(String playsource) {
            this.playsource = playsource;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getGoodnum() {
            return goodnum;
        }

        public void setGoodnum(String goodnum) {
            this.goodnum = goodnum;
        }

        public boolean getGoodstate() {
            return goodstate;
        }

        public void setGoodstate(boolean goodstate) {
            this.goodstate = goodstate;
        }

        public String getTutor_name() {
            return tutor_name;
        }

        public void setTutor_name(String tutor_name) {
            this.tutor_name = tutor_name;
        }

        public String getTutor_title() {
            return tutor_title;
        }

        public void setTutor_title(String tutor_title) {
            this.tutor_title = tutor_title;
        }

        public String getTutor_avatar() {
            return tutor_avatar;
        }

        public void setTutor_avatar(String tutor_avatar) {
            this.tutor_avatar = tutor_avatar;
        }

        public String getTutor_description() {
            return tutor_description;
        }

        public void setTutor_description(String tutor_description) {
            this.tutor_description = tutor_description;
        }

        public String getTutor_ask_desc() {
            return tutor_ask_desc;
        }

        public void setTutor_ask_desc(String tutor_ask_desc) {
            this.tutor_ask_desc = tutor_ask_desc;
        }

        public String getAllquestionurl() {
            return allquestionurl;
        }

        public void setAllquestionurl(String allquestionurl) {
            this.allquestionurl = allquestionurl;
        }

        public Share getShare() {
            return share;
        }

        public void setShare(Share share) {
            this.share = share;
        }

        public static class Share{
            @SerializedName("sharetitle")
            private String sharetitle;
            @SerializedName("shareurl")
            private String shareurl;
            @SerializedName("sharecontent")
            private String sharecontent;
            @SerializedName("shareimg")
            private String shareimg;

            public String getSharetitle() {
                return sharetitle;
            }

            public void setSharetitle(String sharetitle) {
                this.sharetitle = sharetitle;
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

            public String getShareimg() {
                return shareimg;
            }

            public void setShareimg(String shareimg) {
                this.shareimg = shareimg;
            }
        }


    }

}
