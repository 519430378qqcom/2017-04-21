package com.umiwi.ui.beans.updatebeans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/3.
 */

public class HomeCoumnBean {

    /**
     * record : [{"id":"2","uid":"70100072","image":"http://i1.umivi.net/v/image/tutorcolumn/20170224/0_5c422bb7e68e09c4.jpg","name":"董明珠","price":"￥2.99/年","isbuy":false,"columnurl":"http://i.v.youmi.cn/tutorcolumn/detailApi?id=2","title":"精英日课","updatetime":"","tutortitle":"珠海格力集团有限公司董事长 ","updateaudio":"专栏音频","salenum":"5"},{"id":"3","uid":"70000125","image":"http://i1.umivi.net/v/image/tutorcolumn/20170224/0_54e15d2beb7e495e.jpg","name":"韩小红","price":"￥9.99/年","isbuy":false,"columnurl":"http://i.v.youmi.cn/tutorcolumn/detailApi?id=3","title":"健康生活","updatetime":"","tutortitle":"慈铭健康体检管理集团总裁 ","updateaudio":"健康生活--音频","salenum":"1"},{"id":"4","uid":"70000052","image":"http://i1.umivi.net/v/image/tutorcolumn/20170224/0_1d9ed2b5d374c656.jpg","name":"马云","price":"￥0/年","isbuy":false,"columnurl":"http://i.v.youmi.cn/tutorcolumn/detailApi?id=4","title":"天猫国际","updatetime":"","tutortitle":"阿里巴巴集团董事局主席 ","updateaudio":"天猫购物--音频","salenum":"0"},{"id":"5","uid":"70000026","image":"http://i1.umivi.net/v/image/tutorcolumn/20170224/0_391d7d46d66fc3ea.jpg","name":"雷军","price":"￥6.66/年","isbuy":false,"columnurl":"http://i.v.youmi.cn/tutorcolumn/detailApi?id=5","title":"小米科技","updatetime":"","tutortitle":"小米科技创始人 ","updateaudio":"","salenum":"1"}]
     * page : {"currentpage":1,"rows":"6","totalpage":2}
     */

    private PageBean page;
    private List<RecordBean> record;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
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
         * rows : 6
         * totalpage : 2
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

    public static class RecordBean {
        /**
         * id : 2
         * uid : 70100072
         * image : http://i1.umivi.net/v/image/tutorcolumn/20170224/0_5c422bb7e68e09c4.jpg
         * name : 董明珠
         * price : ￥2.99/年
         * isbuy : false
         * columnurl : http://i.v.youmi.cn/tutorcolumn/detailApi?id=2
         * title : 精英日课
         * updatetime :
         * tutortitle : 珠海格力集团有限公司董事长
         * updateaudio : 专栏音频
         * salenum : 5
         */

        private String id;
        private String uid;
        private String image;
        private String name;
        private String price;
        private boolean isbuy;
        private String columnurl;
        private String title;
        private String updatetime;
        private String tutortitle;
        private String updateaudio;
        private String salenum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isIsbuy() {
            return isbuy;
        }

        public void setIsbuy(boolean isbuy) {
            this.isbuy = isbuy;
        }

        public String getColumnurl() {
            return columnurl;
        }

        public void setColumnurl(String columnurl) {
            this.columnurl = columnurl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getTutortitle() {
            return tutortitle;
        }

        public void setTutortitle(String tutortitle) {
            this.tutortitle = tutortitle;
        }

        public String getUpdateaudio() {
            return updateaudio;
        }

        public void setUpdateaudio(String updateaudio) {
            this.updateaudio = updateaudio;
        }

        public String getSalenum() {
            return salenum;
        }

        public void setSalenum(String salenum) {
            this.salenum = salenum;
        }
    }
}
