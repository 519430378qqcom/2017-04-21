package com.umiwi.ui.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class ExperDetailsAlbumbean {

    /**
     * attention : [{"word":"有品"}]
     * content : [{"word":"凡客"}]
     * image : http://i1.umivi.net/v/image/tutorcolumn/20170224/0_f0fe7af5db02a4af.png
     * isbuy : false
     * last_record : [{"audiotype":"1","id":"17","onlinetime":"02-25","title":"独立音频收费2"},{"audiotype":"1","id":"18","onlinetime":"02-25","title":"独立音频收费3"},{"audiotype":"1","id":"5","onlinetime":"02-24","title":"凡客诚品--音频"},{"audiotype":"1","id":"2","onlinetime":"-0001-11-30","title":"专栏音频"},{"audiotype":"1","id":"3","onlinetime":"-0001-11-30","title":"独立音频文件－免费"},{"audiotype":"1","id":"4","onlinetime":"-0001-11-30","title":"独立音频文件－收费"},{"audiotype":"2","id":"6","onlinetime":"-0001-11-30","title":"凡客诚品--音频2"},{"audiotype":"1","id":"7","onlinetime":"-0001-11-30","title":"健康生活--音频"},{"audiotype":"1","id":"8","onlinetime":"-0001-11-30","title":"健康生活--音频2"},{"audiotype":"1","id":"9","onlinetime":"-0001-11-30","title":"天猫购物--音频"},{"audiotype":"1","id":"10","onlinetime":"-0001-11-30","title":"天猫购物--音频2"},{"audiotype":"1","id":"11","onlinetime":"-0001-11-30","title":"未来很有趣"},{"audiotype":"1","id":"12","onlinetime":"-0001-11-30","title":"未来很有趣2--音频"},{"audiotype":"1","id":"13","onlinetime":"-0001-11-30","title":"小米科技--音频3"},{"audiotype":"1","id":"14","onlinetime":"-0001-11-30","title":"小米音频"},{"audiotype":"1","id":"15","onlinetime":"-0001-11-30","title":"小米音频--2"},{"audiotype":"1","id":"16","onlinetime":"-0001-11-30","title":"小米音频--3"},{"audiotype":"1","id":"19","onlinetime":"-0001-11-30","title":"独立音频收费4"},{"audiotype":"1","id":"20","onlinetime":"-0001-11-30","title":"独立音频收费5"},{"audiotype":"1","id":"21","onlinetime":"-0001-11-30","title":"独立音频收费6"}]
     * price : 8.88
     * priceinfo : ￥8.88/年
     * salenum : 订阅0人
     * shortcontent : 舒适男女装
     * targetuser : 18-45男女士
     * title : 凡客诚品
     * tutor_name : 陈年
     * tutor_title : 凡客诚品创始人 董事长
     * tutor_uid : 70000010
     * updatedescription :
     */
    private String image;
    private boolean isbuy;
    private String price;
    private String priceinfo;
    private String salenum;
    private String shortcontent;
    private String targetuser;
    private String title;
    private String tutor_name;
    private String tutor_title;
    private String tutor_uid;
    private String updatedescription;
    private List<AttentionBean> attention;
    private List<ContentBean> content;
    private List<LastRecordBean> last_record;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIsbuy() {
        return isbuy;
    }

    public void setIsbuy(boolean isbuy) {
        this.isbuy = isbuy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceinfo() {
        return priceinfo;
    }

    public void setPriceinfo(String priceinfo) {
        this.priceinfo = priceinfo;
    }

    public String getSalenum() {
        return salenum;
    }

    public void setSalenum(String salenum) {
        this.salenum = salenum;
    }

    public String getShortcontent() {
        return shortcontent;
    }

    public void setShortcontent(String shortcontent) {
        this.shortcontent = shortcontent;
    }

    public String getTargetuser() {
        return targetuser;
    }

    public void setTargetuser(String targetuser) {
        this.targetuser = targetuser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTutor_uid() {
        return tutor_uid;
    }

    public void setTutor_uid(String tutor_uid) {
        this.tutor_uid = tutor_uid;
    }

    public String getUpdatedescription() {
        return updatedescription;
    }

    public void setUpdatedescription(String updatedescription) {
        this.updatedescription = updatedescription;
    }

    public List<AttentionBean> getAttention() {
        return attention;
    }

    public void setAttention(List<AttentionBean> attention) {
        this.attention = attention;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public List<LastRecordBean> getLast_record() {
        return last_record;
    }

    public void setLast_record(List<LastRecordBean> last_record) {
        this.last_record = last_record;
    }

    public static class AttentionBean {
        /**
         * word : 有品
         */

        private String word;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    public static class ContentBean {
        /**
         * word : 凡客
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
         * audiotype : 1
         * id : 17
         * onlinetime : 02-25
         * title : 独立音频收费2
         */

        private String audiotype;
        private String id;
        private String onlinetime;
        private String title;

        public String getAudiotype() {
            return audiotype;
        }

        public void setAudiotype(String audiotype) {
            this.audiotype = audiotype;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOnlinetime() {
            return onlinetime;
        }

        public void setOnlinetime(String onlinetime) {
            this.onlinetime = onlinetime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
