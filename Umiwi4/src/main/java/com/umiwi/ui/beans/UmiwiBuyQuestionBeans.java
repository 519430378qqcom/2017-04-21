package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/10.
 */

public class UmiwiBuyQuestionBeans extends BaseGsonBeans {
    private static final long serialVersionUID = -497879362129406698L;
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private Bquestion r;

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

    public Bquestion getR() {
        return r;
    }

    public void setR(Bquestion r) {
        this.r = r;
    }

    public static class Bquestion {
        @SerializedName("payurl")
        private String payurl;
        @SerializedName("order_id")
        private String order_id;

        public String getPayurl() {
            return payurl;
        }

        public void setPayurl(String payurl) {
            this.payurl = payurl;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        @Override
        public String toString() {
            return "Bquestion{" +
                    "payurl='" + payurl + '\'' +
                    ", order_id='" + order_id + '\'' +
                    '}';
        }
    }
}
