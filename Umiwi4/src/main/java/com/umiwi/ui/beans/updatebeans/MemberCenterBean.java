package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class MemberCenterBean extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RMemberCenter r;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public RMemberCenter getR() {
        return r;
    }

    public void setR(RMemberCenter r) {
        this.r = r;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
    public static class RMemberCenter{
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("username")
        private String username;
        @SerializedName("identity")
        private String identity;
        @SerializedName("expire")
        private String expire;
        @SerializedName("diamond")
        private ArrayList<DiamondBean> diamond;
        @SerializedName("platinum")
        private ArrayList<PlatinumBean> platinum;

        public static class PlatinumBean{
            @SerializedName("name")
            private String name;
            @SerializedName("price")
            private String price;
            @SerializedName("costprice")
            private String costprice;
            @SerializedName("click")
            private boolean click;
            @SerializedName("buyurl")
            private String buyurl;

            public String getBuyurl() {
                return buyurl;
            }

            public void setBuyurl(String buyurl) {
                this.buyurl = buyurl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCostprice() {
                return costprice;
            }

            public void setCostprice(String costprice) {
                this.costprice = costprice;
            }

            public boolean isClick() {
                return click;
            }

            public void setClick(boolean click) {
                this.click = click;
            }
        }
        public static class DiamondBean{
            @SerializedName("name")
            private String name;
            @SerializedName("price")
            private String price;
            @SerializedName("costprice")
            private String costprice;
            @SerializedName("click")
            private boolean click;
            @SerializedName("buyurl")
            private String buyurl;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCostprice() {
                return costprice;
            }

            public void setCostprice(String costprice) {
                this.costprice = costprice;
            }

            public boolean isClick() {
                return click;
            }

            public void setClick(boolean click) {
                this.click = click;
            }

            public String getBuyurl() {
                return buyurl;
            }

            public void setBuyurl(String buyurl) {
                this.buyurl = buyurl;
            }
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getExpire() {
            return expire;
        }

        public void setExpire(String expire) {
            this.expire = expire;
        }

        public ArrayList<DiamondBean> getDiamond() {
            return diamond;
        }

        public void setDiamond(ArrayList<DiamondBean> diamond) {
            this.diamond = diamond;
        }

        public ArrayList<PlatinumBean> getPlatinum() {
            return platinum;
        }

        public void setPlatinum(ArrayList<PlatinumBean> platinum) {
            this.platinum = platinum;
        }
    }
}
