package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/10.
 */

public class UmiwiAddQuestionBeans extends BaseGsonBeans {

    private static final long serialVersionUID = -6523616940482686747L;
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private MQestion r;

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

    public MQestion getR() {
        return r;
    }

    public void setR(MQestion r) {
        this.r = r;
    }

    public static class MQestion {
        @SerializedName("qid")
        private String qid;
        @SerializedName("ctime")
        private String ctime;
        @SerializedName("price")
        private String price;

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "MQestion{" +
                    "qid='" + qid + '\'' +
                    ", ctime='" + ctime + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }
    }

}
