package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/27.
 */

public class AudioSpecialDetailsBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAudioSpecialDetails r;

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

    public RAudioSpecialDetails getR() {
        return r;
    }

    public void setR(RAudioSpecialDetails r) {
        this.r = r;
    }
    public static class RAudioSpecialDetails{
        /**
         * "tutor_uid": "70000016",
         "tutor_name": "冯仑",
         "tutor_title": "万通地产董事长 ",
         "id": "23",
         "title": "民营企业发展必过的6道坎",
         "shortcontent": " 分享民营企业如何突破增长极限的奥秘",
         "updatedescription": "",
         "salenum": "订阅20人",
         "content": [
         {
         "word": "内容亮点："
         },
         {
         "word": "1影响民营企业增长的内外因素"
         },
         {
         "word": "2企业如何与时俱进设计商业模式"
         },
         {
         "word": "3民营企业管理中如何防止人才流失"
         },
         {
         "word": "4万通地产早期发展中的组织机构调整经验"
         },
         {
         "word": "5一个优秀的企业管理者应该具备什么特征"
         },
         {
         "word": ""
         }
         ],
         "targetuser": "欲清晰企业内部组织结构管理者\r\n有企业发展困惑的创始人及职业经理人\r\n想突破企业增长极限，做大做强的创业者\r\n",
         "attention": [
         {
         "word": "1、本专栏为音频专栏\r\n2、本专栏订阅费49元，订阅成功后即可永久阅读专栏内出品的所有内容\r\n3、本专栏为虚拟内容服务，已经订阅成功概不退款，请您理解\r\n"
         }
         ],
         "last_record": [
         {
         "id": "67",
         "title": "017.企业家个人能力极限的重要性",
         "onlinetime": "03-09",
         "audiotype": "1"
         },
         {
         "id": "66",
         "title": "016.防止人才流失的两个绝招",
         "onlinetime": "03-09",
         "audiotype": "1"
         },
         {
         "id": "65",
         "title": "015.靠能力、靠品牌才是王道",
         "onlinetime": "03-09",
         "audiotype": "1"
         }
         ],
         "isbuy": true,
         "priceinfo": "已订阅",
         "price": "49",
         "share": [],
         "image": "http://i1.umivi.net/v/image/tutorcolumn/20170313/0_ad735170fd5d34ad.png",
         "sharetitle": "优米专栏：冯仑·民营企业发展必过的6道坎",
         "shareurl": "http://i.v.youmi.cn/tutorcolumn/columndetails?id=23",
         "sharecontent": " 分享民营企业如何突破增长极限的奥秘 http://i.v.youmi.cn/tutorcolumn/columndetails?id=23 下载客户端 http://m.umiwi.com",
         "shareimg": "http://i1.umivi.net/v/image/tutorcolumn/20170309/0_5e45eacc5980a665.jpg"
         */
        @SerializedName("tutor_uid")
        private String tutor_uid;
        @SerializedName("tutor_name")
        private String tutor_name;
        @SerializedName("tutor_title")
        private String tutor_title;
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
        private ArrayList<ContentWord> content;
        @SerializedName("targetuser")
        private String targetuser;
        @SerializedName("attention")
        private ArrayList<AttentionWord> attention;
        @SerializedName("last_record")
        private ArrayList<LastRecord> last_record;
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
        public static class LastRecord{
            /**
             * "id": "67",
             "title": "017.企业家个人能力极限的重要性",
             "onlinetime": "03-09",
             "audiotype": "1"
             */
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("onlinetime")
            private String onlinetime;
            @SerializedName("audiotype")
            private String audiotype;

            @Override
            public String toString() {
                return "LastRecord{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", onlinetime='" + onlinetime + '\'' +
                        ", audiotype='" + audiotype + '\'' +
                        '}';
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
        }
        public static class AttentionWord{
            /**
             * "word": "1、本专栏为音频专栏\r\n
             * 2、本专栏订阅费49元，订阅成功后即可永久阅读专栏内出品的所有内容\r\n
             * 3、本专栏为虚拟内容服务，已经订阅成功概不退款，请您理解\r\n"
             */
            @SerializedName("word")
            private String word;

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            @Override
            public String toString() {
                return "AttentionWord{" +
                        "word='" + word + '\'' +
                        '}';
            }
        }
        public static class ContentWord{
            @SerializedName("word")
            private String word;

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            @Override
            public String toString() {
                return "ContentWord{" +
                        "word='" + word + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "RAudioSpecialDetails{" +
                    "tutor_uid='" + tutor_uid + '\'' +
                    ", tutor_name='" + tutor_name + '\'' +
                    ", tutor_title='" + tutor_title + '\'' +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", shortcontent='" + shortcontent + '\'' +
                    ", updatedescription='" + updatedescription + '\'' +
                    ", salenum='" + salenum + '\'' +
                    ", content=" + content +
                    ", targetuser='" + targetuser + '\'' +
                    ", attention=" + attention +
                    ", last_record=" + last_record +
                    ", isbuy=" + isbuy +
                    ", priceinfo='" + priceinfo + '\'' +
                    ", price='" + price + '\'' +
                    ", image='" + image + '\'' +
                    ", sharetitle='" + sharetitle + '\'' +
                    ", shareurl='" + shareurl + '\'' +
                    ", sharecontent='" + sharecontent + '\'' +
                    ", shareimg='" + shareimg + '\'' +
                    '}';
        }

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

        public String getTutor_title() {
            return tutor_title;
        }

        public void setTutor_title(String tutor_title) {
            this.tutor_title = tutor_title;
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

        public ArrayList<ContentWord> getContent() {
            return content;
        }

        public void setContent(ArrayList<ContentWord> content) {
            this.content = content;
        }

        public String getTargetuser() {
            return targetuser;
        }

        public void setTargetuser(String targetuser) {
            this.targetuser = targetuser;
        }

        public ArrayList<AttentionWord> getAttention() {
            return attention;
        }

        public void setAttention(ArrayList<AttentionWord> attention) {
            this.attention = attention;
        }

        public ArrayList<LastRecord> getLast_record() {
            return last_record;
        }

        public void setLast_record(ArrayList<LastRecord> last_record) {
            this.last_record = last_record;
        }

        public boolean isbuy() {
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
    }
}
