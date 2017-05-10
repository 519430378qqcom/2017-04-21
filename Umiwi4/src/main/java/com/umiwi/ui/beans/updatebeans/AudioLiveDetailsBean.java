package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class AudioLiveDetailsBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAudioLiveDetails r;

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

    public RAudioLiveDetails getR() {
        return r;
    }

    public void setR(RAudioLiveDetails r) {
        this.r = r;
    }

    public static class RAudioLiveDetails{
        @SerializedName("record")
        private AudioLiveDetailsRecord record;
        @SerializedName("share")
        private AudioLiveDetailsShare share;

        public static class AudioLiveDetailsRecord{
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
            @SerializedName("live_time")
            private String live_time;
            @SerializedName("image")
            private String image;
            @SerializedName("description")
            private ArrayList<AudioDetailsDescription> description;
            @SerializedName("isfree")
            private boolean isfree;
            @SerializedName("istutor")
            private String istutor;

            public String getIstutor() {
                return istutor;
            }

            public void setIstutor(String istutor) {
                this.istutor = istutor;
            }

            public static class AudioDetailsDescription{
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
                    return "AudioDetailsDescription{" +
                            "word='" + word + '\'' +
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

            public ArrayList<AudioDetailsDescription> getDescription() {
                return description;
            }

            public void setDescription(ArrayList<AudioDetailsDescription> description) {
                this.description = description;
            }

            public boolean isfree() {
                return isfree;
            }

            public void setIsfree(boolean isfree) {
                this.isfree = isfree;
            }

            @Override
            public String toString() {
                return "AudioLiveDetailsRecord{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", price='" + price + '\'' +
                        ", isbuy=" + isbuy +
                        ", status='" + status + '\'' +
                        ", partakenum='" + partakenum + '\'' +
                        ", roomid='" + roomid + '\'' +
                        ", live_time='" + live_time + '\'' +
                        ", image='" + image + '\'' +
                        ", description=" + description +
                        ", isfree=" + isfree +
                        '}';
            }
        }

        public static class AudioLiveDetailsShare{
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

            @Override
            public String toString() {
                return "AudioLiveDetailsShare{" +
                        "shareurl='" + shareurl + '\'' +
                        ", sharetitle='" + sharetitle + '\'' +
                        ", sharecontent='" + sharecontent + '\'' +
                        ", shareimg='" + shareimg + '\'' +
                        '}';
            }
        }
        public AudioLiveDetailsRecord getRecord() {
            return record;
        }

        public void setRecord(AudioLiveDetailsRecord record) {
            this.record = record;
        }

        public AudioLiveDetailsShare getShare() {
            return share;
        }

        public void setShare(AudioLiveDetailsShare share) {
            this.share = share;
        }

    }
}
