package com.umiwi.ui.beans;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 我的兑换卡 我的兑换码
 *
 * @author xiaobo
 */
public class UmiwiMyCardBeans extends BaseGsonBeans {

    @SerializedName("code")
    private String code;

    @SerializedName("usedtime")
    private String usedtime;

    @SerializedName("type")
    private String type;

    @SerializedName("indate")
    private String indate;

    @SerializedName("products")
    private UmiwiMyCardProducts products;

    public static UmiwiMyCardBeans fromJson(String json) {
        return new Gson().fromJson(json, UmiwiMyCardBeans.class);
    }

    public String getCode() {
        return code;
    }

    public String getUsedtime() {
        return usedtime;
    }

    public String getType() {
        return type;
    }

    public String getIndate() {
        return indate;
    }

    public UmiwiMyCardProducts getProducts() {
        return products;
    }

    public static class MyCardListRequestData {

        @SerializedName("total")
        private int total;

        @SerializedName("curr_page")
        private int curr_page;

        /**
         * 总页数
         */
        @SerializedName("pages")
        private int pages;

        @SerializedName("record")
        private ArrayList<UmiwiMyCardBeans> record;

        public int getTotal() {
            return total;
        }

        public int getCurr_page() {
            return curr_page;
        }

        public int getPages() {
            return pages;
        }

        public ArrayList<UmiwiMyCardBeans> getRecord() {
            return record;
        }

    }
}
