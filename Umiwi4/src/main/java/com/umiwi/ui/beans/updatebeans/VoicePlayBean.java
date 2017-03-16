package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/14.
 */

public class VoicePlayBean extends BaseGsonBeans {


    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAnserVoicePlay r;

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

    public RAnserVoicePlay getR() {
        return r;
    }

    public void setR(RAnserVoicePlay r) {
        this.r = r;
    }

    public static class RAnserVoicePlay{
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAudiotype() {
            return audiotype;
        }

        public void setAudiotype(String audiotype) {
            this.audiotype = audiotype;
        }

        public String getWatchnum() {
            return watchnum;
        }

        public void setWatchnum(String watchnum) {
            this.watchnum = watchnum;
        }

        public String getOnlinetime() {
            return onlinetime;
        }

        public void setOnlinetime(String onlinetime) {
            this.onlinetime = onlinetime;
        }

        public String getPlaytime() {
            return playtime;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getTutorimage() {
            return tutorimage;
        }

        public void setTutorimage(String tutorimage) {
            this.tutorimage = tutorimage;
        }

        public String getThreadnum() {
            return threadnum;
        }

        public void setThreadnum(String threadnum) {
            this.threadnum = threadnum;
        }

        public String getFavalbum() {
            return favalbum;
        }

        public void setFavalbum(String favalbum) {
            this.favalbum = favalbum;
        }

        public boolean getIspay() {
            return ispay;
        }

        public void setIspay(boolean ispay) {
            this.ispay = ispay;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public ArrayList<RDudiao> getAudiofile() {
            return audiofile;
        }

        public void setAudiofile(ArrayList<RDudiao> audiofile) {
            this.audiofile = audiofile;
        }

        public ArrayList<Rcontent> getContent() {
            return content;
        }

        public void setContent(ArrayList<Rcontent> content) {
            this.content = content;
        }

        public Share getShare() {
            return share;
        }

        public void setShare(Share share) {
            this.share = share;
        }

        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("image")
        private String image;
        @SerializedName("audiotype")
        private String audiotype;
        @SerializedName("watchnum")
        private String watchnum;
        @SerializedName("onlinetime")
        private String onlinetime;
        @SerializedName("playtime")
        private String playtime;
        @SerializedName("uid")
        private String uid;
        @SerializedName("name")
        private String name;
        @SerializedName("tutortitle")
        private String tutortitle;
        @SerializedName("tutorimage")
        private String tutorimage;
        @SerializedName("threadnum")
        private String threadnum;
        @SerializedName("favalbum")
        private String favalbum;
        @SerializedName("ispay")
        private boolean ispay;
        @SerializedName("price")
        private String price;
        @SerializedName("process")
        private String process;


        @SerializedName("audiofile")
        private ArrayList<RDudiao> audiofile;

        @SerializedName("content")
        private ArrayList<Rcontent> content;

        public static class Rcontent{
           public String getWord() {
               return word;
           }

           public void setWord(String word) {
               this.word = word;
           }

           @SerializedName("word")
           private String word;
           @SerializedName("image")
           private String image;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }

        public static class RDudiao{
            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public String getAtitle() {
                return atitle;
            }

            public void setAtitle(String atitle) {
                this.atitle = atitle;
            }

            public String getAplaytime() {
                return aplaytime;
            }

            public void setAplaytime(String aplaytime) {
                this.aplaytime = aplaytime;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

//            public String getTry1() {
//                return try1;
//            }
//            public boolean isTry(){
//                return isTry;
//            };
//            public void setIsTry(boolean isTry) {
//                this.isTry = isTry;
//            }
//            public void setTry1(String try1) {
//                this.try1 = try1;
//            }

            public boolean isTry1() {
                return try1;
            }

            public void setTry1(boolean try1) {
                this.try1 = try1;
            }

            @SerializedName("aid")
            private String aid;
            @SerializedName("atitle")
            private String atitle;
            @SerializedName("aplaytime")
            private String aplaytime;
            @SerializedName("source")
            private String source;
            @SerializedName("try")
            private boolean try1;

        }
        @SerializedName("share")
        private Share share;

        public static class Share{
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

            public String getShareimg() {
                return shareimg;
            }

            public void setShareimg(String shareimg) {
                this.shareimg = shareimg;
            }

            public String getSharecontent() {
                return sharecontent;
            }

            public void setSharecontent(String sharecontent) {
                this.sharecontent = sharecontent;
            }

            @SerializedName("sharetitle")
            private String sharetitle;
            @SerializedName("shareurl")
            private String shareurl;
            @SerializedName("shareimg")
            private String shareimg;
            @SerializedName("sharecontent")
            private String sharecontent;
        }
    }
}
