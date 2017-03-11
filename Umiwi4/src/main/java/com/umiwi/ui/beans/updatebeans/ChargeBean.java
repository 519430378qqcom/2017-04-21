package com.umiwi.ui.beans.updatebeans;

/**
 * 类描述：最新免费-换一换
 * Created by Gpsi on 2017-03-08.
 */

public class ChargeBean {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"record":[{"id":"23","type":"audio","title":"企业管理中的两个魔术数字","playtime":"01:55","url":"http://i.v.youmi.cn/audioalbum/getapi?id=23"},{"id":"22","type":"audio","title":"创业前三年最关键的一点","playtime":"01:34","url":"http://i.v.youmi.cn/audioalbum/getapi?id=22"},{"id":"611","type":"video","title":"如何发现我的客户","playtime":"01:09:31","url":"http://i.v.youmi.cn/ClientApi/getAlbumDetail?id=611"}],"page":{"currentpage":1,"rows":"12","totalpage":4}}
     */

    private String e;
    private String m;
    /**
     * record : [{"id":"23","type":"audio","title":"企业管理中的两个魔术数字","playtime":"01:55","url":"http://i.v.youmi.cn/audioalbum/getapi?id=23"},{"id":"22","type":"audio","title":"创业前三年最关键的一点","playtime":"01:34","url":"http://i.v.youmi.cn/audioalbum/getapi?id=22"},{"id":"611","type":"video","title":"如何发现我的客户","playtime":"01:09:31","url":"http://i.v.youmi.cn/ClientApi/getAlbumDetail?id=611"}]
     * page : {"currentpage":1,"rows":"12","totalpage":4}
     */

    private RecommendBean.RBean.ChargeBean r;

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

    public RecommendBean.RBean.ChargeBean getR() {
        return r;
    }

    public void setR(RecommendBean.RBean.ChargeBean r) {
        this.r = r;
    }
}
