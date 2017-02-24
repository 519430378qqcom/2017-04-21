package com.umiwi.ui.util.ApiTester.bean;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.List;

/**
 * newfreeAction : 首页－最新免费－换一换（6.6.0ok）
 */
public class NewFreeActionEntity extends BaseGsonBeans {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"record":[{"id":"3","type":"audio","title":"独立音频文件－免费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=3"},{"id":"611","type":"video","title":"如何发现我的客户","playtime":"01:09:31","url":"http://i.v.youmi.cn/album/getApi?id=611"},{"id":"359","type":"video","title":"传统企业的电子商务之路","playtime":"01:23:02","url":"http://i.v.youmi.cn/album/getApi?id=359"}],"page":{"currentpage":1,"rows":"3","totalpage":1}}
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
         * record : [{"id":"3","type":"audio","title":"独立音频文件－免费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=3"},{"id":"611","type":"video","title":"如何发现我的客户","playtime":"01:09:31","url":"http://i.v.youmi.cn/album/getApi?id=611"},{"id":"359","type":"video","title":"传统企业的电子商务之路","playtime":"01:23:02","url":"http://i.v.youmi.cn/album/getApi?id=359"}]
         * page : {"currentpage":1,"rows":"3","totalpage":1}
         */

        @SerializedName("page")
        private PageBean page;
        @SerializedName("record")
        private List<RecordBean> record;

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public List<RecordBean> getRecord() {
            return record;
        }

        public void setRecord(List<RecordBean> record) {
            this.record = record;
        }

        public static class PageBean {
            /**
             * currentpage : 1
             * rows : 3
             * totalpage : 1
             */

            @SerializedName("currentpage")
            private int currentpage;
            @SerializedName("rows")
            private String rows;
            @SerializedName("totalpage")
            private int totalpage;

            public int getCurrentpage() {
                return currentpage;
            }

            public void setCurrentpage(int currentpage) {
                this.currentpage = currentpage;
            }

            public String getRows() {
                return rows;
            }

            public void setRows(String rows) {
                this.rows = rows;
            }

            public int getTotalpage() {
                return totalpage;
            }

            public void setTotalpage(int totalpage) {
                this.totalpage = totalpage;
            }
        }

        public static class RecordBean {
            /**
             * id : 3
             * type : audio
             * title : 独立音频文件－免费
             * playtime : 00:00
             * url : http://i.v.youmi.cn/audioalbum/getapi?id=3
             */

            private String id;
            private String type;
            private String title;
            private String playtime;
            private String url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
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
        }
    }
}