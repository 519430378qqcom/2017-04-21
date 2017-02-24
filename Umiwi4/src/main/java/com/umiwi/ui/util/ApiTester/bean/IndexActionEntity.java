package com.umiwi.ui.util.ApiTester.bean;

import com.google.gson.annotations.SerializedName;
import com.umiwi.ui.beans.BaseGsonBeans;

import java.util.List;

/**
 * indexAction : 首页（6.6.0ok）
 */
public class IndexActionEntity extends BaseGsonBeans {

    /**
     * e : 9999
     * m : 操作成功
     * r : {"sec_free_title":"免费音频","sec_free_huan":"换一批","sec_free_huanurl":"http://i.v.youmi.cn/api8/newfree","sec_tutor_title":"行家推荐","sec_tutor_more":"查看全部","sec_tutor_moreurl":"http://i.v.youmi.cn/tutor/listapi","sec_ask_title":"行家问答","sec_ask_more":"更多","sec_ask_moreurl":"http://i.v.youmi.cn/question/listapi","sec_ask_quick":"快速提问","sec_charge_title":"付费精选","sec_charge_huan":"换一批","sec_charge_huanurl":"http://i.v.youmi.cn/api8/chargelist","sec_dalao_title":"优米大咖","sec_huodong_title":"线下活动","sec_huodong_more":"更多","sec_huodong_moreurl":"http://i.v.youmi.cn/viphuodong/listapi","sec_feedback":"意见反馈","free":{"record":[{"id":"3","type":"audio","title":"独立音频文件－免费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=3"},{"id":"611","type":"video","title":"如何发现我的客户","playtime":"01:09:31","url":"http://i.v.youmi.cn/album/getApi?id=611"},{"id":"359","type":"video","title":"传统企业的电子商务之路","playtime":"01:23:02","url":"http://i.v.youmi.cn/album/getApi?id=359"}],"page":{"currentpage":1,"rows":"3","totalpage":1}},"charge":{"record":[{"id":"595","image":"http://i1.umivi.net/v/album/2014-05/20140509145409.jpg","pricetag":"¥64","type":"video","title":"什么是创业的必要条件","playtime":"01:13:27","url":"http://i.v.youmi.cn/album/getApi?id=595"},{"id":"586","image":"http://i1.umivi.net/v/album/2013-03/20130326184314.jpg","pricetag":"¥29","type":"video","title":"女性职场性骚扰的雇主责任","playtime":"08:48","url":"http://i.v.youmi.cn/album/getApi?id=586"},{"id":"4","image":"","pricetag":"¥29","type":"audio","title":"独立音频文件－收费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=4"}],"page":{"currentpage":1,"rows":"3","totalpage":1}},"tutor":[{"uid":"70000026","image":"http://i1.umivi.net/v/image/tutorcolumn/20170219/0_c952d797041b1cea.jpg","name":"雷军","title":"精英日课","price":"￥2.99/年","tutortitle":"小米科技创始人 ","updatetime":"02-19","updateaudio":"专栏音频","salenum":"0","url":"http://i.v.youmi.cn/tutorcolumn/getApi?id=2"},{"uid":"70000125","image":"http://i1.umivi.net/v/image/tutorcolumn/20170223/0_06344c13689b8830.jpg","name":"韩小红","title":"健康生活","price":"￥9.99/年","tutortitle":"慈铭健康体检管理集团总裁 ","updatetime":"","updateaudio":"","salenum":"0","url":"http://i.v.youmi.cn/tutorcolumn/getApi?id=3"},{"uid":"70000052","image":"http://i1.umivi.net/v/image/tutorcolumn/20170224/0_df848494a1e114ae.jpg","name":"马云","title":"天猫国际","price":"￥89.99/年","tutortitle":"阿里巴巴集团董事局主席 ","updatetime":"","updateaudio":"","salenum":"0","url":"http://i.v.youmi.cn/tutorcolumn/getApi?id=4"}],"dalao":[{"uid":"70100192","name":"柳传志","thumb":"http://i1.umivi.net/2013/1119/1384852401649.jpg","title":"联想集团名誉董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100192"},{"uid":"70000052","name":"马云","thumb":"http://i1.umivi.net/2013/1119/1384851747852.jpg","title":"阿里巴巴集团董事局主席兼首席执行官","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000052"},{"uid":"70100072","name":"董明珠","thumb":"http://i1.umivi.net/2013/1119/1384851789972.jpg","title":"珠海格力集团有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100072"},{"uid":"70000182","name":"俞敏洪","thumb":"http://i1.umivi.net/2013/1119/1384851892619.jpg","title":"新东方教育科技集团董事长兼总裁","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000182"},{"uid":"70000212","name":"曹国伟","thumb":"http://i1.umivi.net/2013/1119/1384851940449.jpg","title":"新浪董事长兼CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000212"},{"uid":"70000121","name":"王石","thumb":"http://i1.umivi.net/2013/1119/1384852065171.jpg","title":"万科企业股份有限公司董事会主席","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000121"},{"uid":"70000168","name":"刘强东","thumb":"http://i1.umivi.net/2013/1119/1384851991773.jpg","title":"京东商城CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000168"},{"uid":"70000043","name":"孙振耀","thumb":"http://i1.umivi.net/2013/1119/1384852203668.jpg","title":"致行教育科技有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000043"},{"uid":"70100177","name":"李连杰","thumb":"http://i1.umivi.net/2013/1119/1384852772327.jpg","title":"世界著名功夫巨星，壹基金创始人","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100177"},{"uid":"70000026","name":"雷军","thumb":"http://i1.umivi.net/2013/1119/1384852255884.jpg","title":"小米科技创始人、董事长兼CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000026"},{"uid":"70000016","name":"冯仑","thumb":"http://i1.umivi.net/2013/1119/1384852283184.jpg","title":"万通地产董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000016"},{"uid":"70000011","name":"史玉柱","thumb":"http://i1.umivi.net/2013/1119/1384852309857.jpg","title":"巨人网络集团有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000011"},{"uid":"6027699","name":"潘石屹","thumb":"http://i1.umivi.net/2013/1119/1384852490195.jpg","title":"SOHO中国有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=6027699"},{"uid":"70100454","name":"逍遥子","thumb":"http://i1.umivi.net/2014/0428/1398652920202.jpg","title":"阿里巴巴集团首席运营官","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100454"},{"uid":"6027750","name":"王中军","thumb":"http://i1.umivi.net/2014/0303/1393816483317.jpg","title":"华谊兄弟传媒股份有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=6027750"},{"uid":"70000004","name":"任志强","thumb":"http://i1.umivi.net/2013/1119/1384852538443.jpg","title":"北京华远地产股份有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000004"},{"uid":"166","name":"林正刚","thumb":"http://i1.umivi.net/2013/1119/1384852748763.jpg","title":"刚逸领导力公司CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=166"},{"uid":"6028806","name":"周鸿祎","thumb":"http://i1.umivi.net/2013/1119/1384852630198.jpg","title":"奇虎360董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=6028806"},{"uid":"6044384","name":"杜子建","thumb":"http://i1.umivi.net/2013/1119/1384852717109.jpg","title":"华艺传媒公司创始人","url":"http://i.v.youmi.cn/tutor/getApi?uid=6044384"},{"uid":"70000010","name":"陈年","thumb":"http://i1.umivi.net/2014/0303/1393815896785.jpg","title":"凡客诚品创始人","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000010"}],"asktutor":[{"uid":"70100192","name":"柳传志","thumb":"http://i1.umivi.net/2013/1119/1384852401649.jpg","title":"联想集团名誉董事长","thumbtype":"1"}],"question":[{"id":"2","title":"微信小程序能够进行直播吗","tuid":"7106276","tname":"柳传志","tavatar":"http://i2.umivi.net/avatar/76/7106276b.jpg","ttitle":"苏宁董事长","listentype":"2","buttontag":"1元偷偷听","listennum":"听过232人","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=2","playtime":"58''"}],"huodong":[{"id":"415","image":"http://sss","title":"互联网大会","content":"互联网大会，旨在在全国范围内树立品牌地位","status":"1","url":"http://v.youmi.cn/huodong/hd20170317","type":"h5"}],"bottom":[{"image":"http://i1.umivi.net/v/lunbo/1c7f38ab9d5739fe190d8eb949c1181d.png","type":"bang","url":"http://i.v.youmi.cn/api5/bangdanApi?type=1&frm=lunbo_course&click=1450","albumid":"1"}]}
     */

    @SerializedName("e")
    private String e;
    @SerializedName("m")
    private String m;
    @SerializedName("r")
    private RBean r;

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public RBean getR() {
        return r;
    }

    public void setR(RBean r) {
        this.r = r;
    }

    public static class RBean {
        /**
         * sec_free_title : 免费音频
         * sec_free_huan : 换一批
         * sec_free_huanurl : http://i.v.youmi.cn/api8/newfree
         * sec_tutor_title : 行家推荐
         * sec_tutor_more : 查看全部
         * sec_tutor_moreurl : http://i.v.youmi.cn/tutor/listapi
         * sec_ask_title : 行家问答
         * sec_ask_more : 更多
         * sec_ask_moreurl : http://i.v.youmi.cn/question/listapi
         * sec_ask_quick : 快速提问
         * sec_charge_title : 付费精选
         * sec_charge_huan : 换一批
         * sec_charge_huanurl : http://i.v.youmi.cn/api8/chargelist
         * sec_dalao_title : 优米大咖
         * sec_huodong_title : 线下活动
         * sec_huodong_more : 更多
         * sec_huodong_moreurl : http://i.v.youmi.cn/viphuodong/listapi
         * sec_feedback : 意见反馈
         * free : {"record":[{"id":"3","type":"audio","title":"独立音频文件－免费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=3"},{"id":"611","type":"video","title":"如何发现我的客户","playtime":"01:09:31","url":"http://i.v.youmi.cn/album/getApi?id=611"},{"id":"359","type":"video","title":"传统企业的电子商务之路","playtime":"01:23:02","url":"http://i.v.youmi.cn/album/getApi?id=359"}],"page":{"currentpage":1,"rows":"3","totalpage":1}}
         * charge : {"record":[{"id":"595","image":"http://i1.umivi.net/v/album/2014-05/20140509145409.jpg","pricetag":"¥64","type":"video","title":"什么是创业的必要条件","playtime":"01:13:27","url":"http://i.v.youmi.cn/album/getApi?id=595"},{"id":"586","image":"http://i1.umivi.net/v/album/2013-03/20130326184314.jpg","pricetag":"¥29","type":"video","title":"女性职场性骚扰的雇主责任","playtime":"08:48","url":"http://i.v.youmi.cn/album/getApi?id=586"},{"id":"4","image":"","pricetag":"¥29","type":"audio","title":"独立音频文件－收费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=4"}],"page":{"currentpage":1,"rows":"3","totalpage":1}}
         * tutor : [{"uid":"70000026","image":"http://i1.umivi.net/v/image/tutorcolumn/20170219/0_c952d797041b1cea.jpg","name":"雷军","title":"精英日课","price":"￥2.99/年","tutortitle":"小米科技创始人 ","updatetime":"02-19","updateaudio":"专栏音频","salenum":"0","url":"http://i.v.youmi.cn/tutorcolumn/getApi?id=2"},{"uid":"70000125","image":"http://i1.umivi.net/v/image/tutorcolumn/20170223/0_06344c13689b8830.jpg","name":"韩小红","title":"健康生活","price":"￥9.99/年","tutortitle":"慈铭健康体检管理集团总裁 ","updatetime":"","updateaudio":"","salenum":"0","url":"http://i.v.youmi.cn/tutorcolumn/getApi?id=3"},{"uid":"70000052","image":"http://i1.umivi.net/v/image/tutorcolumn/20170224/0_df848494a1e114ae.jpg","name":"马云","title":"天猫国际","price":"￥89.99/年","tutortitle":"阿里巴巴集团董事局主席 ","updatetime":"","updateaudio":"","salenum":"0","url":"http://i.v.youmi.cn/tutorcolumn/getApi?id=4"}]
         * dalao : [{"uid":"70100192","name":"柳传志","thumb":"http://i1.umivi.net/2013/1119/1384852401649.jpg","title":"联想集团名誉董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100192"},{"uid":"70000052","name":"马云","thumb":"http://i1.umivi.net/2013/1119/1384851747852.jpg","title":"阿里巴巴集团董事局主席兼首席执行官","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000052"},{"uid":"70100072","name":"董明珠","thumb":"http://i1.umivi.net/2013/1119/1384851789972.jpg","title":"珠海格力集团有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100072"},{"uid":"70000182","name":"俞敏洪","thumb":"http://i1.umivi.net/2013/1119/1384851892619.jpg","title":"新东方教育科技集团董事长兼总裁","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000182"},{"uid":"70000212","name":"曹国伟","thumb":"http://i1.umivi.net/2013/1119/1384851940449.jpg","title":"新浪董事长兼CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000212"},{"uid":"70000121","name":"王石","thumb":"http://i1.umivi.net/2013/1119/1384852065171.jpg","title":"万科企业股份有限公司董事会主席","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000121"},{"uid":"70000168","name":"刘强东","thumb":"http://i1.umivi.net/2013/1119/1384851991773.jpg","title":"京东商城CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000168"},{"uid":"70000043","name":"孙振耀","thumb":"http://i1.umivi.net/2013/1119/1384852203668.jpg","title":"致行教育科技有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000043"},{"uid":"70100177","name":"李连杰","thumb":"http://i1.umivi.net/2013/1119/1384852772327.jpg","title":"世界著名功夫巨星，壹基金创始人","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100177"},{"uid":"70000026","name":"雷军","thumb":"http://i1.umivi.net/2013/1119/1384852255884.jpg","title":"小米科技创始人、董事长兼CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000026"},{"uid":"70000016","name":"冯仑","thumb":"http://i1.umivi.net/2013/1119/1384852283184.jpg","title":"万通地产董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000016"},{"uid":"70000011","name":"史玉柱","thumb":"http://i1.umivi.net/2013/1119/1384852309857.jpg","title":"巨人网络集团有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000011"},{"uid":"6027699","name":"潘石屹","thumb":"http://i1.umivi.net/2013/1119/1384852490195.jpg","title":"SOHO中国有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=6027699"},{"uid":"70100454","name":"逍遥子","thumb":"http://i1.umivi.net/2014/0428/1398652920202.jpg","title":"阿里巴巴集团首席运营官","url":"http://i.v.youmi.cn/tutor/getApi?uid=70100454"},{"uid":"6027750","name":"王中军","thumb":"http://i1.umivi.net/2014/0303/1393816483317.jpg","title":"华谊兄弟传媒股份有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=6027750"},{"uid":"70000004","name":"任志强","thumb":"http://i1.umivi.net/2013/1119/1384852538443.jpg","title":"北京华远地产股份有限公司董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000004"},{"uid":"166","name":"林正刚","thumb":"http://i1.umivi.net/2013/1119/1384852748763.jpg","title":"刚逸领导力公司CEO","url":"http://i.v.youmi.cn/tutor/getApi?uid=166"},{"uid":"6028806","name":"周鸿祎","thumb":"http://i1.umivi.net/2013/1119/1384852630198.jpg","title":"奇虎360董事长","url":"http://i.v.youmi.cn/tutor/getApi?uid=6028806"},{"uid":"6044384","name":"杜子建","thumb":"http://i1.umivi.net/2013/1119/1384852717109.jpg","title":"华艺传媒公司创始人","url":"http://i.v.youmi.cn/tutor/getApi?uid=6044384"},{"uid":"70000010","name":"陈年","thumb":"http://i1.umivi.net/2014/0303/1393815896785.jpg","title":"凡客诚品创始人","url":"http://i.v.youmi.cn/tutor/getApi?uid=70000010"}]
         * asktutor : [{"uid":"70100192","name":"柳传志","thumb":"http://i1.umivi.net/2013/1119/1384852401649.jpg","title":"联想集团名誉董事长","thumbtype":"1"}]
         * question : [{"id":"2","title":"微信小程序能够进行直播吗","tuid":"7106276","tname":"柳传志","tavatar":"http://i2.umivi.net/avatar/76/7106276b.jpg","ttitle":"苏宁董事长","listentype":"2","buttontag":"1元偷偷听","listennum":"听过232人","playsource":"http://i.v.youmi.cn/question/playsourceapi?id=2","playtime":"58''"}]
         * huodong : [{"id":"415","image":"http://sss","title":"互联网大会","content":"互联网大会，旨在在全国范围内树立品牌地位","status":"1","url":"http://v.youmi.cn/huodong/hd20170317","type":"h5"}]
         * bottom : [{"image":"http://i1.umivi.net/v/lunbo/1c7f38ab9d5739fe190d8eb949c1181d.png","type":"bang","url":"http://i.v.youmi.cn/api5/bangdanApi?type=1&frm=lunbo_course&click=1450","albumid":"1"}]
         */

        @SerializedName("sec_free_title")
        private String sec_free_title;
        @SerializedName("sec_free_huan")
        private String sec_free_huan;
        @SerializedName("sec_free_huanurl")
        private String sec_free_huanurl;
        @SerializedName("sec_tutor_title")
        private String sec_tutor_title;
        @SerializedName("sec_tutor_more")
        private String sec_tutor_more;
        @SerializedName("sec_tutor_moreurl")
        private String sec_tutor_moreurl;
        @SerializedName("sec_ask_title")
        private String sec_ask_title;
        @SerializedName("sec_ask_more")
        private String sec_ask_more;
        @SerializedName("sec_ask_moreurl")
        private String sec_ask_moreurl;
        @SerializedName("sec_ask_quick")
        private String sec_ask_quick;
        @SerializedName("sec_charge_title")
        private String sec_charge_title;
        @SerializedName("sec_charge_huan")
        private String sec_charge_huan;
        @SerializedName("sec_charge_huanurl")
        private String sec_charge_huanurl;
        @SerializedName("sec_dalao_title")
        private String sec_dalao_title;
        @SerializedName("sec_huodong_title")
        private String sec_huodong_title;
        @SerializedName("sec_huodong_more")
        private String sec_huodong_more;
        @SerializedName("sec_huodong_moreurl")
        private String sec_huodong_moreurl;
        @SerializedName("sec_feedback")
        private String sec_feedback;
        @SerializedName("free")
        private FreeBean free;
        @SerializedName("charge")
        private ChargeBean charge;
        @SerializedName("tutor")
        private List<TutorBean> tutor;
        @SerializedName("dalao")
        private List<DalaoBean> dalao;
        @SerializedName("asktutor")
        private List<AsktutorBean> asktutor;
        @SerializedName("question")
        private List<QuestionBean> question;
        @SerializedName("huodong")
        private List<HuodongBean> huodong;
        @SerializedName("bottom")
        private List<BottomBean> bottom;

        public String getSec_free_title() {
            return sec_free_title;
        }

        public void setSec_free_title(String sec_free_title) {
            this.sec_free_title = sec_free_title;
        }

        public String getSec_free_huan() {
            return sec_free_huan;
        }

        public void setSec_free_huan(String sec_free_huan) {
            this.sec_free_huan = sec_free_huan;
        }

        public String getSec_free_huanurl() {
            return sec_free_huanurl;
        }

        public void setSec_free_huanurl(String sec_free_huanurl) {
            this.sec_free_huanurl = sec_free_huanurl;
        }

        public String getSec_tutor_title() {
            return sec_tutor_title;
        }

        public void setSec_tutor_title(String sec_tutor_title) {
            this.sec_tutor_title = sec_tutor_title;
        }

        public String getSec_tutor_more() {
            return sec_tutor_more;
        }

        public void setSec_tutor_more(String sec_tutor_more) {
            this.sec_tutor_more = sec_tutor_more;
        }

        public String getSec_tutor_moreurl() {
            return sec_tutor_moreurl;
        }

        public void setSec_tutor_moreurl(String sec_tutor_moreurl) {
            this.sec_tutor_moreurl = sec_tutor_moreurl;
        }

        public String getSec_ask_title() {
            return sec_ask_title;
        }

        public void setSec_ask_title(String sec_ask_title) {
            this.sec_ask_title = sec_ask_title;
        }

        public String getSec_ask_more() {
            return sec_ask_more;
        }

        public void setSec_ask_more(String sec_ask_more) {
            this.sec_ask_more = sec_ask_more;
        }

        public String getSec_ask_moreurl() {
            return sec_ask_moreurl;
        }

        public void setSec_ask_moreurl(String sec_ask_moreurl) {
            this.sec_ask_moreurl = sec_ask_moreurl;
        }

        public String getSec_ask_quick() {
            return sec_ask_quick;
        }

        public void setSec_ask_quick(String sec_ask_quick) {
            this.sec_ask_quick = sec_ask_quick;
        }

        public String getSec_charge_title() {
            return sec_charge_title;
        }

        public void setSec_charge_title(String sec_charge_title) {
            this.sec_charge_title = sec_charge_title;
        }

        public String getSec_charge_huan() {
            return sec_charge_huan;
        }

        public void setSec_charge_huan(String sec_charge_huan) {
            this.sec_charge_huan = sec_charge_huan;
        }

        public String getSec_charge_huanurl() {
            return sec_charge_huanurl;
        }

        public void setSec_charge_huanurl(String sec_charge_huanurl) {
            this.sec_charge_huanurl = sec_charge_huanurl;
        }

        public String getSec_dalao_title() {
            return sec_dalao_title;
        }

        public void setSec_dalao_title(String sec_dalao_title) {
            this.sec_dalao_title = sec_dalao_title;
        }

        public String getSec_huodong_title() {
            return sec_huodong_title;
        }

        public void setSec_huodong_title(String sec_huodong_title) {
            this.sec_huodong_title = sec_huodong_title;
        }

        public String getSec_huodong_more() {
            return sec_huodong_more;
        }

        public void setSec_huodong_more(String sec_huodong_more) {
            this.sec_huodong_more = sec_huodong_more;
        }

        public String getSec_huodong_moreurl() {
            return sec_huodong_moreurl;
        }

        public void setSec_huodong_moreurl(String sec_huodong_moreurl) {
            this.sec_huodong_moreurl = sec_huodong_moreurl;
        }

        public String getSec_feedback() {
            return sec_feedback;
        }

        public void setSec_feedback(String sec_feedback) {
            this.sec_feedback = sec_feedback;
        }

        public FreeBean getFree() {
            return free;
        }

        public void setFree(FreeBean free) {
            this.free = free;
        }

        public ChargeBean getCharge() {
            return charge;
        }

        public void setCharge(ChargeBean charge) {
            this.charge = charge;
        }

        public List<TutorBean> getTutor() {
            return tutor;
        }

        public void setTutor(List<TutorBean> tutor) {
            this.tutor = tutor;
        }

        public List<DalaoBean> getDalao() {
            return dalao;
        }

        public void setDalao(List<DalaoBean> dalao) {
            this.dalao = dalao;
        }

        public List<AsktutorBean> getAsktutor() {
            return asktutor;
        }

        public void setAsktutor(List<AsktutorBean> asktutor) {
            this.asktutor = asktutor;
        }

        public List<QuestionBean> getQuestion() {
            return question;
        }

        public void setQuestion(List<QuestionBean> question) {
            this.question = question;
        }

        public List<HuodongBean> getHuodong() {
            return huodong;
        }

        public void setHuodong(List<HuodongBean> huodong) {
            this.huodong = huodong;
        }

        public List<BottomBean> getBottom() {
            return bottom;
        }

        public void setBottom(List<BottomBean> bottom) {
            this.bottom = bottom;
        }

        public static class FreeBean {
            /**
             * record : [{"id":"3","type":"audio","title":"独立音频文件－免费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=3"},{"id":"611","type":"video","title":"如何发现我的客户","playtime":"01:09:31","url":"http://i.v.youmi.cn/album/getApi?id=611"},{"id":"359","type":"video","title":"传统企业的电子商务之路","playtime":"01:23:02","url":"http://i.v.youmi.cn/album/getApi?id=359"}]
             * page : {"currentpage":1,"rows":"3","totalpage":1}
             */

            @SerializedName("page")
            private PageBean page;
            @SerializedName("record")
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
                 * rows : 3
                 * totalpage : 1
                 */

                @SerializedName("currentpage")
                private int currentpage;
                @SerializedName("rows")
                private String rows;
                @SerializedName("totalpage")
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
                 * id : 3
                 * type : audio
                 * title : 独立音频文件－免费
                 * playtime : 00:00
                 * url : http://i.v.youmi.cn/audioalbum/getapi?id=3
                 */

                @SerializedName("id")
                private String id;
                @SerializedName("type")
                private String type;
                @SerializedName("title")
                private String title;
                @SerializedName("playtime")
                private String playtime;
                @SerializedName("url")
                private String url;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPlaytime() {
                    return playtime;
                }

                public void setPlaytime(String playtime) {
                    this.playtime = playtime;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

        public static class ChargeBean {
            /**
             * record : [{"id":"595","image":"http://i1.umivi.net/v/album/2014-05/20140509145409.jpg","pricetag":"¥64","type":"video","title":"什么是创业的必要条件","playtime":"01:13:27","url":"http://i.v.youmi.cn/album/getApi?id=595"},{"id":"586","image":"http://i1.umivi.net/v/album/2013-03/20130326184314.jpg","pricetag":"¥29","type":"video","title":"女性职场性骚扰的雇主责任","playtime":"08:48","url":"http://i.v.youmi.cn/album/getApi?id=586"},{"id":"4","image":"","pricetag":"¥29","type":"audio","title":"独立音频文件－收费","playtime":"00:00","url":"http://i.v.youmi.cn/audioalbum/getapi?id=4"}]
             * page : {"currentpage":1,"rows":"3","totalpage":1}
             */

            @SerializedName("page")
            private PageBeanX page;
            @SerializedName("record")
            private List<RecordBeanX> record;

            public PageBeanX getPage() {
                return page;
            }

            public void setPage(PageBeanX page) {
                this.page = page;
            }

            public List<RecordBeanX> getRecord() {
                return record;
            }

            public void setRecord(List<RecordBeanX> record) {
                this.record = record;
            }

            public static class PageBeanX {
                /**
                 * currentpage : 1
                 * rows : 3
                 * totalpage : 1
                 */

                @SerializedName("currentpage")
                private int currentpage;
                @SerializedName("rows")
                private String rows;
                @SerializedName("totalpage")
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

            public static class RecordBeanX {
                /**
                 * id : 595
                 * image : http://i1.umivi.net/v/album/2014-05/20140509145409.jpg
                 * pricetag : ¥64
                 * type : video
                 * title : 什么是创业的必要条件
                 * playtime : 01:13:27
                 * url : http://i.v.youmi.cn/album/getApi?id=595
                 */

                @SerializedName("id")
                private String id;
                @SerializedName("image")
                private String image;
                @SerializedName("pricetag")
                private String pricetag;
                @SerializedName("type")
                private String type;
                @SerializedName("title")
                private String title;
                @SerializedName("playtime")
                private String playtime;
                @SerializedName("url")
                private String url;

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

                public String getPricetag() {
                    return pricetag;
                }

                public void setPricetag(String pricetag) {
                    this.pricetag = pricetag;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getPlaytime() {
                    return playtime;
                }

                public void setPlaytime(String playtime) {
                    this.playtime = playtime;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

        public static class TutorBean {
            /**
             * uid : 70000026
             * image : http://i1.umivi.net/v/image/tutorcolumn/20170219/0_c952d797041b1cea.jpg
             * name : 雷军
             * title : 精英日课
             * price : ￥2.99/年
             * tutortitle : 小米科技创始人
             * updatetime : 02-19
             * updateaudio : 专栏音频
             * salenum : 0
             * url : http://i.v.youmi.cn/tutorcolumn/getApi?id=2
             */

            @SerializedName("uid")
            private String uid;
            @SerializedName("image")
            private String image;
            @SerializedName("name")
            private String name;
            @SerializedName("title")
            private String title;
            @SerializedName("price")
            private String price;
            @SerializedName("tutortitle")
            private String tutortitle;
            @SerializedName("updatetime")
            private String updatetime;
            @SerializedName("updateaudio")
            private String updateaudio;
            @SerializedName("salenum")
            private String salenum;
            @SerializedName("url")
            private String url;

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTutortitle() {
                return tutortitle;
            }

            public void setTutortitle(String tutortitle) {
                this.tutortitle = tutortitle;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class DalaoBean {
            /**
             * uid : 70100192
             * name : 柳传志
             * thumb : http://i1.umivi.net/2013/1119/1384852401649.jpg
             * title : 联想集团名誉董事长
             * url : http://i.v.youmi.cn/tutor/getApi?uid=70100192
             */

            @SerializedName("uid")
            private String uid;
            @SerializedName("name")
            private String name;
            @SerializedName("thumb")
            private String thumb;
            @SerializedName("title")
            private String title;
            @SerializedName("url")
            private String url;

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

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
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
        }

        public static class AsktutorBean {
            /**
             * uid : 70100192
             * name : 柳传志
             * thumb : http://i1.umivi.net/2013/1119/1384852401649.jpg
             * title : 联想集团名誉董事长
             * thumbtype : 1
             */

            @SerializedName("uid")
            private String uid;
            @SerializedName("name")
            private String name;
            @SerializedName("thumb")
            private String thumb;
            @SerializedName("title")
            private String title;
            @SerializedName("thumbtype")
            private String thumbtype;

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

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumbtype() {
                return thumbtype;
            }

            public void setThumbtype(String thumbtype) {
                this.thumbtype = thumbtype;
            }
        }

        public static class QuestionBean {
            /**
             * id : 2
             * title : 微信小程序能够进行直播吗
             * tuid : 7106276
             * tname : 柳传志
             * tavatar : http://i2.umivi.net/avatar/76/7106276b.jpg
             * ttitle : 苏宁董事长
             * listentype : 2
             * buttontag : 1元偷偷听
             * listennum : 听过232人
             * playsource : http://i.v.youmi.cn/question/playsourceapi?id=2
             * playtime : 58''
             */

            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("tuid")
            private String tuid;
            @SerializedName("tname")
            private String tname;
            @SerializedName("tavatar")
            private String tavatar;
            @SerializedName("ttitle")
            private String ttitle;
            @SerializedName("listentype")
            private String listentype;
            @SerializedName("buttontag")
            private String buttontag;
            @SerializedName("listennum")
            private String listennum;
            @SerializedName("playsource")
            private String playsource;
            @SerializedName("playtime")
            private String playtime;

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

            public String getTuid() {
                return tuid;
            }

            public void setTuid(String tuid) {
                this.tuid = tuid;
            }

            public String getTname() {
                return tname;
            }

            public void setTname(String tname) {
                this.tname = tname;
            }

            public String getTavatar() {
                return tavatar;
            }

            public void setTavatar(String tavatar) {
                this.tavatar = tavatar;
            }

            public String getTtitle() {
                return ttitle;
            }

            public void setTtitle(String ttitle) {
                this.ttitle = ttitle;
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

            public String getListennum() {
                return listennum;
            }

            public void setListennum(String listennum) {
                this.listennum = listennum;
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
        }

        public static class HuodongBean {
            /**
             * id : 415
             * image : http://sss
             * title : 互联网大会
             * content : 互联网大会，旨在在全国范围内树立品牌地位
             * status : 1
             * url : http://v.youmi.cn/huodong/hd20170317
             * type : h5
             */

            @SerializedName("id")
            private String id;
            @SerializedName("image")
            private String image;
            @SerializedName("title")
            private String title;
            @SerializedName("content")
            private String content;
            @SerializedName("status")
            private String status;
            @SerializedName("url")
            private String url;
            @SerializedName("type")
            private String type;

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

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class BottomBean {
            /**
             * image : http://i1.umivi.net/v/lunbo/1c7f38ab9d5739fe190d8eb949c1181d.png
             * type : bang
             * url : http://i.v.youmi.cn/api5/bangdanApi?type=1&frm=lunbo_course&click=1450
             * albumid : 1
             */

            @SerializedName("image")
            private String image;
            @SerializedName("type")
            private String type;
            @SerializedName("url")
            private String url;
            @SerializedName("albumid")
            private String albumid;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getAlbumid() {
                return albumid;
            }

            public void setAlbumid(String albumid) {
                this.albumid = albumid;
            }
        }
    }
}