package com.umiwi.ui.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/13.
 */

public class AudioTmessageListBeans extends BaseGsonBeans {
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RecordX r;

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

    public RecordX getR() {
        return r;
    }

    public void setR(RecordX r) {
        this.r = r;
    }

    public static class RecordX {
        @SerializedName("totalnum")
        private String totalnum;
        @SerializedName("page")
        private PageBean page;
        @SerializedName("record")
        private ArrayList<Record> record;

        public String getTotalnum() {
            return totalnum;
        }

        public void setTotalnum(String totalnum) {
            this.totalnum = totalnum;
        }

        public PageBean getPage() {
            return page;
        }

        public void setPage(PageBean page) {
            this.page = page;
        }

        public ArrayList<Record> getRecord() {
            return record;
        }

        public void setRecord(ArrayList<Record> record) {
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

        public static class Record {
            @SerializedName("id")
            private String id;
            @SerializedName("uid")
            private String uid;
            @SerializedName("name")
            private String name;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("time")
            private String time;
            @SerializedName("content")
            private String content;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            @Override
            public String toString() {
                return "Record{" +
                        "id='" + id + '\'' +
                        ", uid='" + uid + '\'' +
                        ", name='" + name + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", time='" + time + '\'' +
                        ", content='" + content + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "RecordX{" +
                    "totalnum='" + totalnum + '\'' +
                    ", page=" + page +
                    ", record=" + record +
                    '}';
        }
    }
}
