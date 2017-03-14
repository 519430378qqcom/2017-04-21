package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

/**
 * Created by ${Gpsi} on 2017/3/9.
 */

public class AudioResourceBean  extends BaseGsonBeans{

    /**
     * source : http://i5.umivi.net/audio/audiofile/2017/03/08//20170308/0_ed55e1ceaf7016b1.mp3
     * type : free
     */
    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RAudioRes r;

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

    public RAudioRes getR() {
        return r;
    }

    public void setR(RAudioRes r) {
        this.r = r;
    }

    public static class RAudioRes{
        @SerializedName("type")
        private String type;
        @SerializedName("source")
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
