package com.umiwi.ui.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 二级详情页
 *
 * @author tangxiyong 2013-12-9下午5:21:48
 */
public class UmiwiListDetailBeans extends BaseGsonBeans {

    @SerializedName("duration")
    private int duration;//unit second

    @SerializedName("watchprogress")
    private int watchProgress;//0 - 100

    /**
     * 品牌视频的vid
     */
    @SerializedName("id")
    private long p_vid;

    @SerializedName("vid")
    private long vid;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("audio")
    private String audio;

    @SerializedName("albumid")
    private String albumid;

    @SerializedName("expiretime")
    private String expiretime;


    public String getAlbumId() {
        return albumid;
    }


    public void setAlbumId(String albumid) {
        this.albumid = albumid;
    }


    public static UmiwiListDetailBeans fromJson(String json) {
        return new Gson().fromJson(json, UmiwiListDetailBeans.class);
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWatchProgress() {
        return watchProgress;
    }

    public void setWatchProgress(int watchProgress) {
        this.watchProgress = watchProgress;
    }

    public long getP_vid() {
        return p_vid;
    }

    public void setP_vid(long p_vid) {
        this.p_vid = p_vid;
    }

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    /**
     * @return the albumid
     */
    public String getAlbumid() {
        return albumid;
    }


    /**
     * @param albumid the albumid to set
     */
    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }


    /**
     * @return the expiretime
     */
    public String getExpiretime() {
        return expiretime;
    }


    /**
     * @param expiretime the expiretime to set
     */
    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public static class ListDetailRequestData {
        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("short")
        private String shorts;

        @SerializedName("image")
        private String image;

        @SerializedName("shareurl")
        private String shareurl;

        @SerializedName("sharecontent")
        private String sharecontent;

        @SerializedName("price")
        private String price;
        @SerializedName("oldprice")
        private String oldprice;

        @SerializedName("isbuy")
        private boolean isbuy;

        @SerializedName("classes")
        private String classes;

        @SerializedName("authorname")
        private String authorname;

        @SerializedName("authornameraw")
        private String authorNameRaw;

        @SerializedName("authortitle")
        private String authorTitle;

        @SerializedName("authoravatar")
        private String authorAvatar;

        @SerializedName("tutoruid")
        private String tutoruid;

        public String getOldprice() {
            return oldprice;
        }

        public void setOldprice(String oldprice) {
            this.oldprice = oldprice;
        }

        @SerializedName("uid")

        private String uid;

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        @SerializedName("isconsult")
        private boolean isconsult;

        @SerializedName("authorprice")
        private String authorprice;

        public String getTutoruid() {
            return tutoruid;
        }

        public void setTutoruid(String tutoruid) {
            this.tutoruid = tutoruid;
        }

        public boolean getIsconsult() {
            return isconsult;
        }

        public void setIsconsult(boolean isconsult) {
            this.isconsult = isconsult;
        }

        public String getAuthorprice() {
            return authorprice;
        }

        public void setAuthorprice(String authorprice) {
            this.authorprice = authorprice;
        }

        @SerializedName("watchnum")
        private int watchNum;

        @SerializedName("canbuy")
        private boolean canbuy;

        @SerializedName("buyurl")
        private String buyurl;

        @SerializedName("summary")
        private String summary;

        @SerializedName("introduce")
        private String introduce;

        @SerializedName("playtime")
        private String playtime;

        @SerializedName("courseurl")
        private String courseurl;

        @SerializedName("notenum")
        private String noteNum;

        @SerializedName("commentcount")
        private String commentCount;

        @SerializedName("dobindmobile")
        private boolean dobindmobile;

        @SerializedName("recordtime")
        private String recordTime;


        public String getNoteNum() {
            return noteNum;
        }

        public void setNoteNum(String noteNum) {
            this.noteNum = noteNum;
        }

        public String getCourseurl() {
            return courseurl;
        }

        public void setCourseurl(String courseurl) {
            this.courseurl = courseurl;
        }

        @SerializedName("gameid")
        private String gameID;

        public String getGameID() {
            return gameID;
        }

        public void setGameID(String gid) {
            this.gameID = gid;
        }

        @SerializedName("course")
        private ArrayList<UmiwiListDetailBeans> course;

        @SerializedName("try")
        private ArrayList<UmiwiListDetailBeans> tryvideo;

        public String getAuthorNameRaw() {
            return authorNameRaw;
        }

        public void setAuthorNameRaw(String authorNameRaw) {
            this.authorNameRaw = authorNameRaw;
        }

        public String getAuthorTitle() {
            return authorTitle;
        }

        public void setAuthorTitle(String authorTitle) {
            this.authorTitle = authorTitle;
        }

        public String getAuthorAvatar() {
            return authorAvatar;
        }

        public void setAuthorAvatar(String authorAvatar) {
            this.authorAvatar = authorAvatar;
        }

        public int getWatchNum() {
            return watchNum;
        }

        public void setWatchNum(int watchNum) {
            this.watchNum = watchNum;
        }

        @SerializedName("indate")
        private String indate;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getShorts() {
            return shorts;
        }

        public String getImage() {
            return image;
        }

        public String getShareurl() {
            return shareurl;
        }

        public String getSharecontent() {
            return sharecontent;
        }

        public String getPrice() {
            return price;
        }

        public boolean isIsbuy() {
            return isbuy;
        }

        public String getClasses() {
            return classes;
        }

        public String getAuthorname() {
            return authorname;
        }

        public boolean isCanbuy() {
            return canbuy;
        }

        public String getBuyurl() {
            return buyurl;
        }

        public String getSummary() {
            return summary;
        }

        public String getIntroduce() {
            return introduce;
        }

        public String getPlaytime() {
            return playtime;
        }

        public ArrayList<UmiwiListDetailBeans> getCourse() {
            return course;
        }

        public ArrayList<UmiwiListDetailBeans> getTryvideo() {
            return tryvideo;
        }

        public String getIndate() {
            return indate;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setShorts(String shorts) {
            this.shorts = shorts;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setShareurl(String shareurl) {
            this.shareurl = shareurl;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setIsbuy(boolean isbuy) {
            this.isbuy = isbuy;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }

        public void setAuthorname(String authorname) {
            this.authorname = authorname;
        }

        public void setCanbuy(boolean canbuy) {
            this.canbuy = canbuy;
        }

        public void setBuyurl(String buyurl) {
            this.buyurl = buyurl;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public void setPlaytime(String playtime) {
            this.playtime = playtime;
        }

        public void setCourse(ArrayList<UmiwiListDetailBeans> course) {
            this.course = course;
        }

        public void setTryvideo(ArrayList<UmiwiListDetailBeans> tryvideo) {
            this.tryvideo = tryvideo;
        }

        public void setIndate(String indate) {
            this.indate = indate;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public boolean isDobindmobile() {
            return dobindmobile;
        }

        public String getRecordTime() {
            return recordTime;
        }
    }
}
