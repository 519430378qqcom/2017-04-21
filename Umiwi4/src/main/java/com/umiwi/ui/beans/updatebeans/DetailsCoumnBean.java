package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */

public class DetailsCoumnBean extends BaseGsonBeans {


    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RDetailsInfo r;

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

    public RDetailsInfo getR() {
        return r;
    }

    public void setR(RDetailsInfo r) {
        this.r = r;
    }

    public static class RDetailsInfo {
        public String getTutor_uid() {
            return tutor_uid;
        }

        public void setTutor_uid(String tutor_uid) {
            this.tutor_uid = tutor_uid;
        }

        public String getTutor_name() {
            return tutor_name;
        }

        public void setTutor_name(String tutor_name) {
            this.tutor_name = tutor_name;
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

        public String getShortcontent() {
            return shortcontent;
        }

        public void setShortcontent(String shortcontent) {
            this.shortcontent = shortcontent;
        }

        public String getUpdatedescription() {
            return updatedescription;
        }

        public void setUpdatedescription(String updatedescription) {
            this.updatedescription = updatedescription;
        }

        public String getSalenum() {
            return salenum;
        }

        public void setSalenum(String salenum) {
            this.salenum = salenum;
        }

        public ArrayList<ContentBean> getContent() {
            return content;
        }

        public void setContent(ArrayList<ContentBean> content) {
            this.content = content;
        }

        public String getTargetuser() {
            return targetuser;
        }

        public void setTargetuser(String targetuser) {
            this.targetuser = targetuser;
        }

        public ArrayList<Attentioninfo> getAttention() {
            return attention;
        }

        public void setAttention(ArrayList<Attentioninfo> attention) {
            this.attention = attention;
        }

        public ArrayList<LastBean> getLast_record() {
            return last_record;
        }

        public void setLast_record(ArrayList<LastBean> last_record) {
            this.last_record = last_record;
        }

        public boolean getIsbuy() {
            return isbuy;
        }

        public void setIsbuy(boolean isbuy) {
            this.isbuy = isbuy;
        }

        public String getPriceinfo() {
            return priceinfo;
        }

        public void setPriceinfo(String priceinfo) {
            this.priceinfo = priceinfo;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

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

        @SerializedName("tutor_uid")
        private String tutor_uid;
        @SerializedName("tutor_name")
        private String tutor_name;
        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("shortcontent")
        private String shortcontent;
        @SerializedName("updatedescription")
        private String updatedescription;
        @SerializedName("salenum")
        private String salenum;
        @SerializedName("content")
        private ArrayList<ContentBean> content;


        public static class ContentBean {
            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            @SerializedName("word")
            private String word;
        }

        @SerializedName("targetuser")
        private String targetuser;

        @SerializedName("attention")
        private ArrayList<Attentioninfo> attention;

        public static class Attentioninfo {
            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            @SerializedName("word")
            private String word;
        }

        @SerializedName("last_record")
        private ArrayList<LastBean> last_record;

        public static class LastBean {

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

            public String getOnlinetime() {
                return onlinetime;
            }

            public void setOnlinetime(String onlinetime) {
                this.onlinetime = onlinetime;
            }

            public String getAudiotype() {
                return audiotype;
            }

            public void setAudiotype(String audiotype) {
                this.audiotype = audiotype;
            }

            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("onlinetime")
            private String onlinetime;
            @SerializedName("audiotype")
            private String audiotype;

        }

        @SerializedName("isbuy")
        private boolean isbuy;
        @SerializedName("priceinfo")
        private String priceinfo;

        @SerializedName("price")
        private String price;
        @SerializedName("image")
        private String image;
        @SerializedName("sharetitle")
        private String sharetitle;

        @SerializedName("shareurl")
        private String shareurl;
        @SerializedName("sharecontent")
        private String sharecontent;
        @SerializedName("shareimg")
        private String shareimg;

    }
}
