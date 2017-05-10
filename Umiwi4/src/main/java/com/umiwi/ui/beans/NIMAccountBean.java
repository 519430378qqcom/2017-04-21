package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/4/28.
 * 网易云信账户
 */

public class NIMAccountBean extends BaseGsonBeans{

    /**
     * e : 9999
     * m : 操作成功
     * r : {"accid":"","token":""}
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
         * accid :
         * token :
         */
        @SerializedName("accid")
        private String accid;
        @SerializedName("token")
        private String token;

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
