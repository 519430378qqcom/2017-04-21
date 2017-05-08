package com.umiwi.ui.beans;

import java.util.List;

/**
 * Created by dong on 2017/5/8.
 * 聊天记录
 */

public class ChatRecordBean {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"record":[{"id":null,"userid":null,"avatar":"http://i2.umivi.net/avatar/66/7668466m.jpg","convtype":"ROOM","toid":"8796823","fromnick":"youmidongge","msgtimestamp":"1494246134563","msgtype":"TEXT","textattach":"回电话好多好多个怪怪的","audioattach":{"md5":"db2696fc1b3185f50539d1c78c415d20","url":"https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8yMzY4MDMyNF8xNDk0MjI4NDU4NDI3XzBkNjMxNGQxLTBlM2MtNGVkMC04MWEyLTViNjUyYmYxMmNmNg==","size":23762,"ext":"aac","dur":6847},"pictureattach":{"md5":"26b8d9aa842fbd6cc19e0755e4b6fe0b","name":"P70504-091555","url":"https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8zODM3MjgxNDhfMTQ5NDI0Mzg5NjgyMF9iY2RkZTI4NS01MTg0LTQ2ODgtODRhNy1mZjMyYTNjMjI4ZWI=","size":2463157,"ext":"jpg","w":4000,"h":3008},"ext":{"username":"youmidongge","isauthor":true,"avatar":"http://i2.umivi.net/avatar/66/7668466b.jpg"},"is_bozhu":false}],"page":{"currentpage":1,"rows":"733","totalpage":49}}
     */

    private String e;
    private String m;
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
         * record : [{"id":null,"userid":null,"avatar":"http://i2.umivi.net/avatar/66/7668466m.jpg","convtype":"ROOM","toid":"8796823","fromnick":"youmidongge","msgtimestamp":"1494246134563","msgtype":"TEXT","textattach":"回电话好多好多个怪怪的","audioattach":{"md5":"db2696fc1b3185f50539d1c78c415d20","url":"https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8yMzY4MDMyNF8xNDk0MjI4NDU4NDI3XzBkNjMxNGQxLTBlM2MtNGVkMC04MWEyLTViNjUyYmYxMmNmNg==","size":23762,"ext":"aac","dur":6847},"pictureattach":{"md5":"26b8d9aa842fbd6cc19e0755e4b6fe0b","name":"P70504-091555","url":"https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8zODM3MjgxNDhfMTQ5NDI0Mzg5NjgyMF9iY2RkZTI4NS01MTg0LTQ2ODgtODRhNy1mZjMyYTNjMjI4ZWI=","size":2463157,"ext":"jpg","w":4000,"h":3008},"ext":{"username":"youmidongge","isauthor":true,"avatar":"http://i2.umivi.net/avatar/66/7668466b.jpg"},"is_bozhu":false}]
         * page : {"currentpage":1,"rows":"733","totalpage":49}
         */

        private PageBean page;
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
             * rows : 733
             * totalpage : 49
             */

            private int currentpage;
            private String rows;
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
             * id : null
             * userid : null
             * avatar : http://i2.umivi.net/avatar/66/7668466m.jpg
             * convtype : ROOM
             * toid : 8796823
             * fromnick : youmidongge
             * msgtimestamp : 1494246134563
             * msgtype : TEXT
             * textattach : 回电话好多好多个怪怪的
             * audioattach : {"md5":"db2696fc1b3185f50539d1c78c415d20","url":"https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8yMzY4MDMyNF8xNDk0MjI4NDU4NDI3XzBkNjMxNGQxLTBlM2MtNGVkMC04MWEyLTViNjUyYmYxMmNmNg==","size":23762,"ext":"aac","dur":6847}
             * pictureattach : {"md5":"26b8d9aa842fbd6cc19e0755e4b6fe0b","name":"P70504-091555","url":"https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8zODM3MjgxNDhfMTQ5NDI0Mzg5NjgyMF9iY2RkZTI4NS01MTg0LTQ2ODgtODRhNy1mZjMyYTNjMjI4ZWI=","size":2463157,"ext":"jpg","w":4000,"h":3008}
             * ext : {"username":"youmidongge","isauthor":true,"avatar":"http://i2.umivi.net/avatar/66/7668466b.jpg"}
             * is_bozhu : false
             */

            private Object id;
            private Object userid;
            private String avatar;
            private String convtype;
            private String toid;
            private String fromnick;
            private String msgtimestamp;
            private String msgtype;
            private String textattach;
            private AudioattachBean audioattach;
            private PictureattachBean pictureattach;
            private ExtBean ext;
            private boolean is_bozhu;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public Object getUserid() {
                return userid;
            }

            public void setUserid(Object userid) {
                this.userid = userid;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getConvtype() {
                return convtype;
            }

            public void setConvtype(String convtype) {
                this.convtype = convtype;
            }

            public String getToid() {
                return toid;
            }

            public void setToid(String toid) {
                this.toid = toid;
            }

            public String getFromnick() {
                return fromnick;
            }

            public void setFromnick(String fromnick) {
                this.fromnick = fromnick;
            }

            public String getMsgtimestamp() {
                return msgtimestamp;
            }

            public void setMsgtimestamp(String msgtimestamp) {
                this.msgtimestamp = msgtimestamp;
            }

            public String getMsgtype() {
                return msgtype;
            }

            public void setMsgtype(String msgtype) {
                this.msgtype = msgtype;
            }

            public String getTextattach() {
                return textattach;
            }

            public void setTextattach(String textattach) {
                this.textattach = textattach;
            }

            public AudioattachBean getAudioattach() {
                return audioattach;
            }

            public void setAudioattach(AudioattachBean audioattach) {
                this.audioattach = audioattach;
            }

            public PictureattachBean getPictureattach() {
                return pictureattach;
            }

            public void setPictureattach(PictureattachBean pictureattach) {
                this.pictureattach = pictureattach;
            }

            public ExtBean getExt() {
                return ext;
            }

            public void setExt(ExtBean ext) {
                this.ext = ext;
            }

            public boolean isIs_bozhu() {
                return is_bozhu;
            }

            public void setIs_bozhu(boolean is_bozhu) {
                this.is_bozhu = is_bozhu;
            }

            public static class AudioattachBean {
                /**
                 * md5 : db2696fc1b3185f50539d1c78c415d20
                 * url : https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8yMzY4MDMyNF8xNDk0MjI4NDU4NDI3XzBkNjMxNGQxLTBlM2MtNGVkMC04MWEyLTViNjUyYmYxMmNmNg==
                 * size : 23762
                 * ext : aac
                 * dur : 6847
                 */

                private String md5;
                private String url;
                private int size;
                private String ext;
                private int dur;

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public int getDur() {
                    return dur;
                }

                public void setDur(int dur) {
                    this.dur = dur;
                }
            }

            public static class PictureattachBean {
                /**
                 * md5 : 26b8d9aa842fbd6cc19e0755e4b6fe0b
                 * name : P70504-091555
                 * url : https://nos.netease.com/nim/MTAyOTIyNA==/bmltYV8zODM3MjgxNDhfMTQ5NDI0Mzg5NjgyMF9iY2RkZTI4NS01MTg0LTQ2ODgtODRhNy1mZjMyYTNjMjI4ZWI=
                 * size : 2463157
                 * ext : jpg
                 * w : 4000
                 * h : 3008
                 */

                private String md5;
                private String name;
                private String url;
                private int size;
                private String ext;
                private int w;
                private int h;

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public int getW() {
                    return w;
                }

                public void setW(int w) {
                    this.w = w;
                }

                public int getH() {
                    return h;
                }

                public void setH(int h) {
                    this.h = h;
                }
            }

            public static class ExtBean {
                /**
                 * username : youmidongge
                 * isauthor : true
                 * avatar : http://i2.umivi.net/avatar/66/7668466b.jpg
                 */

                private String username;
                private boolean isauthor;
                private String avatar;

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public boolean isIsauthor() {
                    return isauthor;
                }

                public void setIsauthor(boolean isauthor) {
                    this.isauthor = isauthor;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }
            }
        }
    }
}
