package com.umiwi.ui.beans;

import java.io.Serializable;

/**
 * Created by ${Gpsi} on 2017/3/17.
 */

public class DownLoadVoiceBean implements Serializable {

    /**
     * source : http://i5.umivi.net/audio/audiofile/2017/03/16//20170316/0_2e537e430c0355ce.mp3
     * type : free
     */

    private String source;
    private String type;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
