package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ColumnReadBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RColumnRead r;

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

    public RColumnRead getR() {
        return r;
    }

    public void setR(RColumnRead r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "ColumnReadBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }
    public static class RColumnRead{
        @SerializedName("id")
        private String id;
        @SerializedName("tcolumnid")
        private String tcolumnid;
        @SerializedName("title")
        private String title;
        @SerializedName("tcolumntitle")
        private String tcolumntitle;
        @SerializedName("content")
        private ArrayList<ReadContentWord> content;
        @SerializedName("isbuy")
        private boolean isbuy;
        @SerializedName("priceinfo")
        private String priceinfo;
        @SerializedName("audiofile")
        private ReadAudioFile audiofile;
        @SerializedName("share")
        private ReadShare share;
        public static class ReadContentWord{
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
                return "ReadContentWord{" +
                        "word='" + word + '\'' +
                        '}';
            }
        }
        public static class ReadAudioFile{
            @SerializedName("aid")
            private String aid;
            @SerializedName("atitle")
            private String atitle;
            @SerializedName("aplaytime")
            private String aplaytime;
            @SerializedName("source")
            private String source;

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

            @Override
            public String toString() {
                return "ReadAudioFile{" +
                        "aid='" + aid + '\'' +
                        ", atitle='" + atitle + '\'' +
                        ", aplaytime='" + aplaytime + '\'' +
                        ", source='" + source + '\'' +
                        '}';
            }
        }

        public static class ReadShare{
            @SerializedName("sharetitle")
            private String sharetitle;
            @SerializedName("shareurl")
            private String shareurl;
            @SerializedName("shareimg")
            private String shareimg;
            @SerializedName("sharecontent")
            private String sharecontent;

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

            @Override
            public String toString() {
                return "ReadShare{" +
                        "sharetitle='" + sharetitle + '\'' +
                        ", shareurl='" + shareurl + '\'' +
                        ", shareimg='" + shareimg + '\'' +
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

        public String getTcolumnid() {
            return tcolumnid;
        }

        public void setTcolumnid(String tcolumnid) {
            this.tcolumnid = tcolumnid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTcolumntitle() {
            return tcolumntitle;
        }

        public void setTcolumntitle(String tcolumntitle) {
            this.tcolumntitle = tcolumntitle;
        }

        public ArrayList<ReadContentWord> getContent() {
            return content;
        }

        public void setContent(ArrayList<ReadContentWord> content) {
            this.content = content;
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

        public ReadAudioFile getAudiofile() {
            return audiofile;
        }

        public void setAudiofile(ReadAudioFile audiofile) {
            this.audiofile = audiofile;
        }

        public ReadShare getShare() {
            return share;
        }

        public void setShare(ReadShare share) {
            this.share = share;
        }

        @Override
        public String toString() {
            return "RColumnRead{" +
                    "id='" + id + '\'' +
                    ", tcolumnid='" + tcolumnid + '\'' +
                    ", title='" + title + '\'' +
                    ", tcolumntitle='" + tcolumntitle + '\'' +
                    ", content=" + content +
                    ", isbuy=" + isbuy +
                    ", priceinfo='" + priceinfo + '\'' +
                    ", audiofile=" + audiofile +
                    ", share=" + share +
                    '}';
        }
    }
}
