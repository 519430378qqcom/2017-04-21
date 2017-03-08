package com.umiwi.ui.beans.updatebeans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */

public class ExperDetailsVoiceBean {

 /**
  * id : 16
  * title : 小米音频--3
  * image : http://i1.umivi.net/v/image/audioalbum/20170224/0_0f21b726c6dfcc01.jpg
  * audiotype : 1
  * watchnum : 390
  * onlinetime : 2017-02-24
  * playtime : 00:00
  * content : [{"word":"小米音频--3"}]
  * uid : 70000026
  * name : 雷军
  * tutortitle : 小米科技创始人
  * tutorimage : http://i1.umivi.net/v/teacher/avatar/163.jpg
  * threadnum : 0
  * favalbum : false
  * ispay : true
  * price : 0
  * process : 0%
  * audiofile : [{"aid":"10","atitle":"独立音频--测试1","aplaytime":"05:28","source":"http://i.v.youmi.cn/audioalbum/playsourceapi?audioalbumid=16&audiofileid=10","try":true},{"aid":"11","atitle":"独立音频--测试2","aplaytime":"05:28","source":"http://i.v.youmi.cn/audioalbum/playsourceapi?audioalbumid=16&audiofileid=11","try":true}]
  * share : {"sharetitle":"小米音频--3","shareurl":"http://v.youmi.cn/audioalbum/audiodetails?id=16","shareimg":"http://i1.umivi.net/v/teacher/avatar/163.jpg","sharecontent":""}
  */

 private String id;
 private String title;
 private String image;
 private String audiotype;
 private String watchnum;
 private String onlinetime;
 private String playtime;
 private String uid;
 private String name;
 private String tutortitle;
 private String tutorimage;
 private String threadnum;
 private boolean favalbum;
 private boolean ispay;
 private int price;
 private String process;
 private ShareBean share;
 private List<ContentBean> content;
 private List<AudiofileBean> audiofile;

 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public String getTitle() {
  return title;
 }

 public void setTitle(String title) {
  this.title = title;
 }

 public String getImage() {
  return image;
 }

 public void setImage(String image) {
  this.image = image;
 }

 public String getAudiotype() {
  return audiotype;
 }

 public void setAudiotype(String audiotype) {
  this.audiotype = audiotype;
 }

 public String getWatchnum() {
  return watchnum;
 }

 public void setWatchnum(String watchnum) {
  this.watchnum = watchnum;
 }

 public String getOnlinetime() {
  return onlinetime;
 }

 public void setOnlinetime(String onlinetime) {
  this.onlinetime = onlinetime;
 }

 public String getPlaytime() {
  return playtime;
 }

 public void setPlaytime(String playtime) {
  this.playtime = playtime;
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

 public String getTutortitle() {
  return tutortitle;
 }

 public void setTutortitle(String tutortitle) {
  this.tutortitle = tutortitle;
 }

 public String getTutorimage() {
  return tutorimage;
 }

 public void setTutorimage(String tutorimage) {
  this.tutorimage = tutorimage;
 }

 public String getThreadnum() {
  return threadnum;
 }

 public void setThreadnum(String threadnum) {
  this.threadnum = threadnum;
 }

 public boolean isFavalbum() {
  return favalbum;
 }

 public void setFavalbum(boolean favalbum) {
  this.favalbum = favalbum;
 }

 public boolean isIspay() {
  return ispay;
 }

 public void setIspay(boolean ispay) {
  this.ispay = ispay;
 }

 public int getPrice() {
  return price;
 }

 public void setPrice(int price) {
  this.price = price;
 }

 public String getProcess() {
  return process;
 }

 public void setProcess(String process) {
  this.process = process;
 }

 public ShareBean getShare() {
  return share;
 }

 public void setShare(ShareBean share) {
  this.share = share;
 }

 public List<ContentBean> getContent() {
  return content;
 }

 public void setContent(List<ContentBean> content) {
  this.content = content;
 }

 public List<AudiofileBean> getAudiofile() {
  return audiofile;
 }

 public void setAudiofile(List<AudiofileBean> audiofile) {
  this.audiofile = audiofile;
 }

 public static class ShareBean {
  /**
   * sharetitle : 小米音频--3
   * shareurl : http://v.youmi.cn/audioalbum/audiodetails?id=16
   * shareimg : http://i1.umivi.net/v/teacher/avatar/163.jpg
   * sharecontent :
   */

  private String sharetitle;
  private String shareurl;
  private String shareimg;
  private String sharecontent;

  public String getSharetitle() {
   return sharetitle;
  }

  public void setSharetitle(String sharetitle) {
   this.sharetitle = sharetitle;
  }

  public String getShareurl() {
   return shareurl;
  }

  public void setShareurl(String shareurl) {
   this.shareurl = shareurl;
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
 }

 public static class ContentBean {
  /**
   * word : 小米音频--3
   */

  private String word;

  public String getWord() {
   return word;
  }

  public void setWord(String word) {
   this.word = word;
  }
 }

 public static class AudiofileBean {
  /**
   * aid : 10
   * atitle : 独立音频--测试1
   * aplaytime : 05:28
   * source : http://i.v.youmi.cn/audioalbum/playsourceapi?audioalbumid=16&audiofileid=10
   * try : true
   */

  private String aid;
  private String atitle;
  private String aplaytime;
  private String source;
  @SerializedName("try")
  private boolean tryX;

  public String getAid() {
   return aid;
  }

  public void setAid(String aid) {
   this.aid = aid;
  }

  public String getAtitle() {
   return atitle;
  }

  public void setAtitle(String atitle) {
   this.atitle = atitle;
  }

  public String getAplaytime() {
   return aplaytime;
  }

  public void setAplaytime(String aplaytime) {
   this.aplaytime = aplaytime;
  }

  public String getSource() {
   return source;
  }

  public void setSource(String source) {
   this.source = source;
  }

  public boolean isTryX() {
   return tryX;
  }

  public void setTryX(boolean tryX) {
   this.tryX = tryX;
  }
 }
}
