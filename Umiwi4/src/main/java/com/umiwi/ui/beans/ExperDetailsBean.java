package com.umiwi.ui.beans;

/**
 * Created by Administrator on 2017/3/1.
 */

public class ExperDetailsBean {

    /**
     * result : {"tcolumnurl":"","audioalbumurl":"","albumurl":"http://i.v.youmi.cn/api8/videolist?tuid=6028806","questionurl":"","threadurl":""}
     * uid : 6028806
     * name : 周鸿祎
     * tutortitle : 奇虎360董事长
     * tutorimage : http://i1.umivi.net/v/album/avatar/61.jpg
     * description : 　　奇虎360董事长。2005年离开雅虎中国，2006年成立天使投资基金，同年投资奇虎公司并任董事长，2011年3月纽交所上市。主张“微天使”和“微投资”，以较少资本投入、占有较小股份，投资多个项目。2011年奇虎360成立投资部，关注移动互联行业新热点。
     * share : {"sharetitle":"优米特约行家:周鸿祎奇虎360董事长 ","shareurl":"http://v.youmi.cn/tutor/tutordetails?uid=6028806","shareimg":"http://i1.umivi.net/v/album/avatar/61.jpg","sharecontent":"　　真正很多成功的公司，都在做一些别人不太看好甚至比较冷门的东西，但是坚持做下去，市场慢慢起来之后，你就能处在一个有利的位置。\r\n所谓的成功都是马后炮，回过头去看，只不过是在正确的时间，无意中做了一件正确的事而已。"}
     */

    private ResultBean result;
    private String uid;
    private String name;
    private String tutortitle;
    private String tutorimage;
    private String description;
    private String isopenask;
    private ShareBean share;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
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

    public String getIsopenask() {
        return isopenask;
    }

    public void setIsopenask(String isopenask) {
        this.isopenask = isopenask;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    public static class ResultBean {
        /**
         * tcolumnurl :
         * audioalbumurl :
         * albumurl : http://i.v.youmi.cn/api8/videolist?tuid=6028806
         * questionurl :
         * threadurl :
         */

        private String tcolumnurl;
        private String audioalbumurl;
        private String albumurl;
        private String questionurl;
        private String threadurl;

        public String getTcolumnurl() {
            return tcolumnurl;
        }

        public void setTcolumnurl(String tcolumnurl) {
            this.tcolumnurl = tcolumnurl;
        }

        public String getAudioalbumurl() {
            return audioalbumurl;
        }

        public void setAudioalbumurl(String audioalbumurl) {
            this.audioalbumurl = audioalbumurl;
        }

        public String getAlbumurl() {
            return albumurl;
        }

        public void setAlbumurl(String albumurl) {
            this.albumurl = albumurl;
        }

        public String getQuestionurl() {
            return questionurl;
        }

        public void setQuestionurl(String questionurl) {
            this.questionurl = questionurl;
        }

        public String getThreadurl() {
            return threadurl;
        }

        public void setThreadurl(String threadurl) {
            this.threadurl = threadurl;
        }
    }

    public static class ShareBean {
        /**
         * sharetitle : 优米特约行家:周鸿祎奇虎360董事长
         * shareurl : http://v.youmi.cn/tutor/tutordetails?uid=6028806
         * shareimg : http://i1.umivi.net/v/album/avatar/61.jpg
         * sharecontent : 　　真正很多成功的公司，都在做一些别人不太看好甚至比较冷门的东西，但是坚持做下去，市场慢慢起来之后，你就能处在一个有利的位置。
         所谓的成功都是马后炮，回过头去看，只不过是在正确的时间，无意中做了一件正确的事而已。
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
}
