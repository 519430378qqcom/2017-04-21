package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dong on 2017/5/14.
 */

public class OnlineSumBean {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"partakenum":"101"}
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
         * partakenum : 101
         */
        @SerializedName("partakenum")
        private String partakenum;

        public String getPartakenum() {
            return partakenum;
        }

        public void setPartakenum(String partakenum) {
            this.partakenum = partakenum;
        }
    }
}
