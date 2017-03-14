package com.umiwi.ui.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FastQuizBeans implements Serializable {

    /**
     * tag : [{"tagname":"互联网"},{"tagname":"媒体"},{"tagname":"教育培训"}]
     * record : [{"uid":"70100554","name":"王利芬","title":"优米网创始人、总裁      ","image":"http://i1.umivi.net/v/teacher/avatar/826.jpg","askprice":"29","askpriceinfo":"￥29"},{"uid":"70100581","name":"叶陈华","title":"优米运营负责人        ","image":"http://i1.umivi.net/v/teacher/avatar/853.jpg","askprice":"1","askpriceinfo":"￥1"},{"uid":"70100627","name":"卢菲菲","title":"体验式全脑记忆法创始人   ","image":"http://i1.umivi.net/v/teacher/avatar/899.jpg","askprice":"8","askpriceinfo":"￥8"},{"uid":"70100861","name":"李伟","title":"尚妆网创始人兼CEO  ","image":"http://i1.umivi.net/v/teacher/avatar/1133.jpg","askprice":"9.90","askpriceinfo":"￥9.90"},{"uid":"7106276","name":"吕钗","title":"优米技术      ","image":"http://i1.umivi.net/v/teacher/avatar/1297.jpg","askprice":"1","askpriceinfo":"￥1"},{"uid":"70101028","name":"范海涛","title":"畅销书作者，美国哥伦比亚大学硕士   ","image":"http://i1.umivi.net/v/teacher/avatar/1299.jpg","askprice":"15","askpriceinfo":"￥15"},{"uid":"70101035","name":"陶峻","title":"CCTV1《谢天谢地你来啦》 主编   ","image":"http://i1.umivi.net/v/teacher/avatar/1306.png","askprice":"15","askpriceinfo":"￥15"},{"uid":"70101036","name":"童小言","title":"品牌、策略、营销达人 ","image":"http://i1.umivi.net/v/teacher/avatar/1307.jpg","askprice":"6.60","askpriceinfo":"￥6.60"},{"uid":"70101038","name":"袁月","title":"狗尾草资本CEO，\u201c双天使\u201d孵化模式发起人 ","image":"http://i1.umivi.net/v/teacher/avatar/1309.jpg","askprice":"5","askpriceinfo":"￥5"}]
     * page : {"currentpage":1,"rows":"9","totalpage":1}
     */

    private PageBean page;
    private List<TagBean> tag;
    private List<RecordBean> record;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<TagBean> getTag() {
        return tag;
    }

    public void setTag(List<TagBean> tag) {
        this.tag = tag;
    }

    public List<RecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<RecordBean> record) {
        this.record = record;
    }

    public static class PageBean {
        /**
         * currentpage : 1
         * rows : 9
         * totalpage : 1
         */

        private int currentpage;
        private String rows;
        private int totalpage;

        public int getCurrentpage() {
            return currentpage;
        }

        public void setCurrentpage(int currentpage) {
            this.currentpage = currentpage;
        }

        public String getRows() {
            return rows;
        }

        public void setRows(String rows) {
            this.rows = rows;
        }

        public int getTotalpage() {
            return totalpage;
        }

        public void setTotalpage(int totalpage) {
            this.totalpage = totalpage;
        }
    }

    public static class TagBean {
        /**
         * tagname : 互联网
         */

        private String tagname;

        public String getTagname() {
            return tagname;
        }

        public void setTagname(String tagname) {
            this.tagname = tagname;
        }
    }

    public static class RecordBean {
        /**
         * uid : 70100554
         * name : 王利芬
         * title : 优米网创始人、总裁
         * image : http://i1.umivi.net/v/teacher/avatar/826.jpg
         * askprice : 29
         * askpriceinfo : ￥29
         */

        private String uid;
        private String name;
        private String title;
        private String image;
        private String askprice;
        private String askpriceinfo;

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

        public String getAskprice() {
            return askprice;
        }

        public void setAskprice(String askprice) {
            this.askprice = askprice;
        }

        public String getAskpriceinfo() {
            return askpriceinfo;
        }

        public void setAskpriceinfo(String askpriceinfo) {
            this.askpriceinfo = askpriceinfo;
        }
    }
}
