package com.umiwi.ui.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ${Gpsi} on 2017/3/15.
 */

public class ShareInfoBean implements Serializable {


    /**
     * id : 58
     * tuid : 70100581
     * title : 作为一个职场人士，知道应该不断的提升自己，确很难坚持下来，有什么好的建议吗
     * tavatar : http://i2.umivi.net/avatar/81/70100581m.jpg
     * listennum : 听过4人
     * answertime : 03-07
     * listentype : 1
     * buttontag : 1元偷偷听
     * playsource : http://i.v.youmi.cn/question/playsourceapi?id=58
     * playtime : 87''
     * goodnum : 2
     * goodstate : false
     * tutor_name : 叶陈华
     * tutor_title : 优米运营负责人
     * tutor_avatar : http://i2.umivi.net/avatar/81/70100581b.jpg
     * tutor_tag : ["互联网"]
     * tutor_description : 曾任巨人教育品牌加盟全国市场总监；2011年进入职业规划领域，获得生涯规划师认证；2013年创立十二家园教育全身心传播12能力模型，此模型于2008年创立，实践检验6年。2013年创立职场精英365成长学院，搭建职场人士互助平台。
     * tutor_ask_desc :
     * allquestionurl : http://i.v.youmi.cn/question/namedquestionapi?tutoruid=70100581
     * sharetitle : 叶陈华回答了“作为一个职场人士，知道应该不断的提升自己，确很难坚持下来，有什么好的建议吗”
     * shareimg : http://i2.umivi.net/avatar/81/70100581m.jpg
     * sharecontent : 花1元就能偷偷听
     * shareurl : http://i.v.youmi.cn/question/questiondetail?id=58
     */

    private String id;
    private String tuid;
    private String title;
    private String tavatar;
    private String listennum;
    private String answertime;
    private String listentype;
    private String buttontag;
    private String playsource;
    private String playtime;
    private String goodnum;
    private boolean goodstate;
    private String tutor_name;
    private String tutor_title;
    private String tutor_avatar;
    private String tutor_description;
    private String tutor_ask_desc;
    private String allquestionurl;
    private String sharetitle;
    private String shareimg;
    private String sharecontent;
    private String shareurl;
    private List<String> tutor_tag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTuid() {
        return tuid;
    }

    public void setTuid(String tuid) {
        this.tuid = tuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTavatar() {
        return tavatar;
    }

    public void setTavatar(String tavatar) {
        this.tavatar = tavatar;
    }

    public String getListennum() {
        return listennum;
    }

    public void setListennum(String listennum) {
        this.listennum = listennum;
    }

    public String getAnswertime() {
        return answertime;
    }

    public void setAnswertime(String answertime) {
        this.answertime = answertime;
    }

    public String getListentype() {
        return listentype;
    }

    public void setListentype(String listentype) {
        this.listentype = listentype;
    }

    public String getButtontag() {
        return buttontag;
    }

    public void setButtontag(String buttontag) {
        this.buttontag = buttontag;
    }

    public String getPlaysource() {
        return playsource;
    }

    public void setPlaysource(String playsource) {
        this.playsource = playsource;
    }

    public String getPlaytime() {
        return playtime;
    }

    public void setPlaytime(String playtime) {
        this.playtime = playtime;
    }

    public String getGoodnum() {
        return goodnum;
    }

    public void setGoodnum(String goodnum) {
        this.goodnum = goodnum;
    }

    public boolean isGoodstate() {
        return goodstate;
    }

    public void setGoodstate(boolean goodstate) {
        this.goodstate = goodstate;
    }

    public String getTutor_name() {
        return tutor_name;
    }

    public void setTutor_name(String tutor_name) {
        this.tutor_name = tutor_name;
    }

    public String getTutor_title() {
        return tutor_title;
    }

    public void setTutor_title(String tutor_title) {
        this.tutor_title = tutor_title;
    }

    public String getTutor_avatar() {
        return tutor_avatar;
    }

    public void setTutor_avatar(String tutor_avatar) {
        this.tutor_avatar = tutor_avatar;
    }

    public String getTutor_description() {
        return tutor_description;
    }

    public void setTutor_description(String tutor_description) {
        this.tutor_description = tutor_description;
    }

    public String getTutor_ask_desc() {
        return tutor_ask_desc;
    }

    public void setTutor_ask_desc(String tutor_ask_desc) {
        this.tutor_ask_desc = tutor_ask_desc;
    }

    public String getAllquestionurl() {
        return allquestionurl;
    }

    public void setAllquestionurl(String allquestionurl) {
        this.allquestionurl = allquestionurl;
    }

    public String getSharetitle() {
        return sharetitle;
    }

    public void setSharetitle(String sharetitle) {
        this.sharetitle = sharetitle;
    }

    public String getShareimg() {
        return shareimg;
    }

    public void setShareimg(String shareimg) {
        this.shareimg = shareimg;
    }

    public String getSharecontent() {
        return sharecontent;
    }

    public void setSharecontent(String sharecontent) {
        this.sharecontent = sharecontent;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public List<String> getTutor_tag() {
        return tutor_tag;
    }

    public void setTutor_tag(List<String> tutor_tag) {
        this.tutor_tag = tutor_tag;
    }
}
