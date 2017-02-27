package com.umiwi.ui.beans.updatebeans;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class CelebrityBean {

    public List<CelebrityBeanRet> record;
public  class CelebrityBeanRet {

    /**
     * pinyinname : 8350
     * pinyin : 8350
     * content : [{"tutoruid":"70000026","name":"96f7519b","title":"5c0f7c7379d16280521b59cb4eba ","image":"http://i1.umivi.net/v/teacher/avatar/163.jpg","tutorcolumn":"4e13680f(04eba8ba29605)","audio":"97f39891(6)","question":"95ee7b54(06b2156de7b54)","album":"89c69891(36)"}]
     */

    private String pinyinname;
    private String pinyin;
    private List<ContentBean> content;

    public String getPinyinname() {
        return pinyinname;
    }

    public void setPinyinname(String pinyinname) {
        this.pinyinname = pinyinname;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public  class ContentBean {
        /**
         * tutoruid : 70000026
         * name : 96f7519b
         * title : 5c0f7c7379d16280521b59cb4eba
         * image : http://i1.umivi.net/v/teacher/avatar/163.jpg
         * tutorcolumn : 4e13680f(04eba8ba29605)
         * audio : 97f39891(6)
         * question : 95ee7b54(06b2156de7b54)
         * album : 89c69891(36)
         */

        private String tutoruid;
        private String name;
        private String title;
        private String image;
        private String tutorcolumn;
        private String audio;
        private String question;
        private String album;

        public String getTutoruid() {
            return tutoruid;
        }

        public void setTutoruid(String tutoruid) {
            this.tutoruid = tutoruid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getTutorcolumn() {
            return tutorcolumn;
        }

        public void setTutorcolumn(String tutorcolumn) {
            this.tutorcolumn = tutorcolumn;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }
    }
}
}