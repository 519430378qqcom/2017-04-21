package com.umiwi.ui.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */
public class VideoHeadBean {

    /**
     * subtree : [{"id":"1300","pid":"1291","name":"大数据"}]
     * info : {"id":"1291","name":"创业"}
     */

    private InfoBean info;
    private List<SubtreeBean> subtree;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<SubtreeBean> getSubtree() {
        return subtree;
    }

    public void setSubtree(List<SubtreeBean> subtree) {
        this.subtree = subtree;
    }

    public static class InfoBean {
        /**
         * id : 1291
         * name : 创业
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "InfoBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class SubtreeBean {
        /**
         * id : 1300
         * pid : 1291
         * name : 大数据
         */

        private String id;
        private String pid;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SubtreeBean{" +
                    "id='" + id + '\'' +
                    ", pid='" + pid + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
