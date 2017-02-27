package com.umiwi.ui.beans.updatebeans;

import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class CelebrityBean extends BaseGsonBeans {

    /**
     * content : [{"album":"视频(36)","audio":"音频(6)","image":"http://i1.umivi.net/v/teacher/avatar/163.jpg","name":"雷军","question":"问答(0次回答)","title":"小米科技创始人 ","tutorcolumn":"专栏(0人订阅)","tutoruid":"70000026"}]
     * pinyin : 荐
     * pinyinname : 荐
     */

    private String pinyin;
    private String pinyinname;
    private List<ContentBean> content;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinname() {
        return pinyinname;
    }

    public void setPinyinname(String pinyinname) {
        this.pinyinname = pinyinname;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * album : 视频(36)
         * audio : 音频(6)
         * image : http://i1.umivi.net/v/teacher/avatar/163.jpg
         * name : 雷军
         * question : 问答(0次回答)
         * title : 小米科技创始人
         * tutorcolumn : 专栏(0人订阅)
         * tutoruid : 70000026
         */

        private String album;
        private String audio;
        private String image;
        private String name;
        private String question;
        private String title;
        private String tutorcolumn;
        private String tutoruid;

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTutorcolumn() {
            return tutorcolumn;
        }

        public void setTutorcolumn(String tutorcolumn) {
            this.tutorcolumn = tutorcolumn;
        }

        public String getTutoruid() {
            return tutoruid;
        }

        public void setTutoruid(String tutoruid) {
            this.tutoruid = tutoruid;
        }
    }
}