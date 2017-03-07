package com.umiwi.ui.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ColumnDetailsBean {

    /**
     * tutor_uid : 70000125
     * tutor_name : 韩小红
     * tutor_title : 慈铭健康体检管理集团总裁
     * id : 3
     * image : http://i1.umivi.net/v/image/tutorcolumn/20170223/0_06344c13689b8830.jpg
     * title : 健康生活
     * shortcontent : 健康生活 共享你我他
     * updatedescription :
     * salenum : 订阅2人
     * content : [{"word":"这是一个健康的栏目"}]
     * targetuser : 18-65健康人士
     * attention : [{"word":"健康"}]
     * last_record : [{"id":"17","title":"独立音频收费2","onlinetime":"02-25","audiotype":"1"},{"id":"18","title":"独立音频收费3","onlinetime":"02-25","audiotype":"1"},{"id":"5","title":"凡客诚品--音频","onlinetime":"02-24","audiotype":"1"},{"id":"2","title":"专栏音频","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"3","title":"独立音频文件－免费","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"4","title":"独立音频文件－收费","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"6","title":"凡客诚品--音频2","onlinetime":"-0001-11-30","audiotype":"2"},{"id":"7","title":"健康生活--音频","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"8","title":"健康生活--音频2","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"9","title":"天猫购物--音频","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"10","title":"天猫购物--音频2","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"11","title":"未来很有趣","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"12","title":"未来很有趣2--音频","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"13","title":"小米科技--音频3","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"14","title":"小米音频","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"15","title":"小米音频--2","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"16","title":"小米音频--3","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"19","title":"独立音频收费4","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"20","title":"独立音频收费5","onlinetime":"-0001-11-30","audiotype":"1"},{"id":"21","title":"独立音频收费6","onlinetime":"-0001-11-30","audiotype":"1"}]
     * isbuy : false
     * priceinfo : ￥9.99/年
     * price : 9.99
     * sharetitle : 优米专栏：韩小红·健康生活
     * shareurl : http://i.v.youmi.cn/tutorcolumn/columndetails?id=3
     * sharecontent : 健康生活 共享你我他
     * shareimg : http://i1.umivi.net/v/image/tutorcolumn/20170224/0_54e15d2beb7e495e.jpg
     */

    private String tutor_uid;
    private String tutor_name;
    private String tutor_title;
    private String id;
    private String image;
    private String title;
    private String shortcontent;
    private String updatedescription;
    private String salenum;
    private String targetuser;
    private boolean isbuy;
    private String priceinfo;
    private String price;
    private String sharetitle;
    private String shareurl;
    private String sharecontent;
    private String shareimg;
    private List<ContentBean> content;
    private List<AttentionBean> attention;
    private List<LastRecordBean> last_record;

    public String getTutor_uid() {
        return tutor_uid;
    }

    public void setTutor_uid(String tutor_uid) {
        this.tutor_uid = tutor_uid;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortcontent() {
        return shortcontent;
    }

    public void setShortcontent(String shortcontent) {
        this.shortcontent = shortcontent;
    }

    public String getUpdatedescription() {
        return updatedescription;
    }

    public void setUpdatedescription(String updatedescription) {
        this.updatedescription = updatedescription;
    }

    public String getSalenum() {
        return salenum;
    }

    public void setSalenum(String salenum) {
        this.salenum = salenum;
    }

    public String getTargetuser() {
        return targetuser;
    }

    public void setTargetuser(String targetuser) {
        this.targetuser = targetuser;
    }

    public boolean isIsbuy() {
        return isbuy;
    }

    public void setIsbuy(boolean isbuy) {
        this.isbuy = isbuy;
    }

    public String getPriceinfo() {
        return priceinfo;
    }

    public void setPriceinfo(String priceinfo) {
        this.priceinfo = priceinfo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

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

    public String getSharecontent() {
        return sharecontent;
    }

    public void setSharecontent(String sharecontent) {
        this.sharecontent = sharecontent;
    }

    public String getShareimg() {
        return shareimg;
    }

    public void setShareimg(String shareimg) {
        this.shareimg = shareimg;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public List<AttentionBean> getAttention() {
        return attention;
    }

    public void setAttention(List<AttentionBean> attention) {
        this.attention = attention;
    }

    public List<LastRecordBean> getLast_record() {
        return last_record;
    }

    public void setLast_record(List<LastRecordBean> last_record) {
        this.last_record = last_record;
    }

    public static class ContentBean {
        /**
         * word : 这是一个健康的栏目
         */

        private String word;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    public static class AttentionBean {
        /**
         * word : 健康
         */

        private String word;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    public static class LastRecordBean {
        /**
         * id : 17
         * title : 独立音频收费2
         * onlinetime : 02-25
         * audiotype : 1
         */

        private String id;
        private String title;
        private String onlinetime;
        private String audiotype;

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

        public String getOnlinetime() {
            return onlinetime;
        }

        public void setOnlinetime(String onlinetime) {
            this.onlinetime = onlinetime;
        }

        public String getAudiotype() {
            return audiotype;
        }

        public void setAudiotype(String audiotype) {
            this.audiotype = audiotype;
        }
    }

    @Override
    public String toString() {
        return "ColumnDetailsBean{" +
                "tutor_uid='" + tutor_uid + '\'' +
                ", tutor_name='" + tutor_name + '\'' +
                ", tutor_title='" + tutor_title + '\'' +
                ", id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", shortcontent='" + shortcontent + '\'' +
                ", updatedescription='" + updatedescription + '\'' +
                ", salenum='" + salenum + '\'' +
                ", targetuser='" + targetuser + '\'' +
                ", isbuy=" + isbuy +
                ", priceinfo='" + priceinfo + '\'' +
                ", price='" + price + '\'' +
                ", sharetitle='" + sharetitle + '\'' +
                ", shareurl='" + shareurl + '\'' +
                ", sharecontent='" + sharecontent + '\'' +
                ", shareimg='" + shareimg + '\'' +
                ", content=" + content +
                ", attention=" + attention +
                ", last_record=" + last_record +
                '}';
    }
}
