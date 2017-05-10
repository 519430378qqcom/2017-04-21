package com.umiwi.ui.beans.updatebeans;

import com.umiwi.ui.beans.BaseGsonBeans;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class ColumnAudioBean extends BaseGsonBeans {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"type":"free","source":"http://i5.umivi.net/audio/audiofile/2017/04/12//20170412/0_5b3c0daf6a8050c2.mp3"}
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
         * type : free
         * source : http://i5.umivi.net/audio/audiofile/2017/04/12//20170412/0_5b3c0daf6a8050c2.mp3
         */

        private String type;
        private String source;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
