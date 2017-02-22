package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:最新免费
 */

public class NewFree{

    /**
     * id : 611
     * icontype : video
     * title : 如何发现我的客户
     * playtime : 01:09:31
     */



        @SerializedName("id")
        private String id;
        @SerializedName("icontype")
        private String icontype;
        @SerializedName("title")
        private String title;
        @SerializedName("playtime")
        private String playtime;

        public void setId(String id) {
            this.id = id;
        }

        public void setIcontype(String icontype) {
            this.icontype = icontype;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public String getId() {
            return id;
        }

        public String getIcontype() {
            return icontype;
        }

        public String getTitle() {
            return title;
        }

        public String getPlaytime() {
            return playtime;
        }


}
