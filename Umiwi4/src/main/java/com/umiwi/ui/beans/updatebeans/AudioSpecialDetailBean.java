package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class AudioSpecialDetailBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAudioSpecial r;

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

    public RAudioSpecial getR() {
        return r;
    }

    public void setR(RAudioSpecial r) {
        this.r = r;
    }

    @Override
    public String toString() {
        return "AudioSpecialDetailBean{" +
                "e='" + e + '\'' +
                ", m='" + m + '\'' +
                ", r=" + r +
                '}';
    }
    public static class RAudioSpecial{
        @SerializedName("id")
        private String id;
        @SerializedName("image")
        private String image;
        @SerializedName("title")
        private String title;
        @SerializedName("shortcontent")
        private String shortcontent;
        @SerializedName("salenum")
        private String salenum;
        @SerializedName("content")
        private ArrayList<RAudioSpecialContent> content;
        @SerializedName("isbuy")
        private boolean isbuy;
        @SerializedName("priceinfo")
        private String priceinfo;
        @SerializedName("price")
        private String price;
        @SerializedName("last_record")
        private ArrayList<LastRecordList> last_record;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public String getSalenum() {
            return salenum;
        }

        public void setSalenum(String salenum) {
            this.salenum = salenum;
        }

        public ArrayList<RAudioSpecialContent> getContent() {
            return content;
        }

        public void setContent(ArrayList<RAudioSpecialContent> content) {
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public ArrayList<LastRecordList> getLast_record() {
            return last_record;
        }

        public void setLast_record(ArrayList<LastRecordList> last_record) {
            this.last_record = last_record;
        }

        @Override
        public String toString() {
            return "RAudioSpecial{" +
                    "id='" + id + '\'' +
                    ", image='" + image + '\'' +
                    ", title='" + title + '\'' +
                    ", shortcontent='" + shortcontent + '\'' +
                    ", salenum='" + salenum + '\'' +
                    ", attention=" + content +
                    ", isbuy=" + isbuy +
                    ", priceinfo='" + priceinfo + '\'' +
                    ", price='" + price + '\'' +
                    ", last_record=" + last_record +
                    '}';
        }
        public static class RAudioSpecialContent{
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
                return "RAudioSpecialContent{" +
                        "word='" + word + '\'' +
                        '}';
            }
        }
        public static class LastRecordList implements Comparable{
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("isaudition")
            private boolean isaudition;
            @SerializedName("onlinetime")
            private String onlinetime;
            @SerializedName("watchnum")
            private String watchnum;
            @SerializedName("playtime")
            private String playtime;
            @SerializedName("url")
            private String url;


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

            public boolean isaudition() {
                return isaudition;
            }

            public void setIsaudition(boolean isaudition) {
                this.isaudition = isaudition;
            }

            public String getOnlinetime() {
                return onlinetime;
            }

            public void setOnlinetime(String onlinetime) {
                this.onlinetime = onlinetime;
            }

            public String getWatchnum() {
                return watchnum;
            }

            public void setWatchnum(String watchnum) {
                this.watchnum = watchnum;
            }

            public String getPlaytime() {
                return playtime;
            }

            public void setPlaytime(String playtime) {
                this.playtime = playtime;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public String toString() {
                return "LastRecordList{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", isaudition='" + isaudition + '\'' +
                        ", onlinetime='" + onlinetime + '\'' +
                        ", watchnum='" + watchnum + '\'' +
                        ", playtime='" + playtime + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }

            @Override
            public int compareTo(Object another) {
                if(another instanceof LastRecordList) {
                    LastRecordList las = (LastRecordList) another;
                    return -this.getOnlinetime().compareTo(las.getOnlinetime());
                }
                return 0;
            }
        }


    }
}
