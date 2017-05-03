package com.umiwi.ui.main;

/**
 * umiwi所有接口
 *
 * @author tangxiyong 2013-11-20下午3:26:49
 *         // ===============================请这种形式按模块写接口===========================================//
 */
public final class UmiwiAPI {
    // ===============================直播  相关===================================================//

    /**
     * 直播页面 数据
     */
    public static final String UMIWI_AUIDOLIVE = "http://i.v.youmi.cn/Telecast/getlive?status=%s&p=%s";
    /**
     * 直播详情
     */
    public static final String LIVE_DETAILS = "http://i.v.youmi.cn/Telecast/getlivedetail?id=";
    /**
     * 获取网易云信账户信息
     */
    public static final String NIM_ACCOUNT = "http://i.v.youmi.cn/Yxaccount/getAccidapi";
    // ===============================登录  相关===================================================//

    /**
     * 手机注册
     */
    public static final String USER_REGISTER_BY_PHONE = "http://passport.youmi.cn/mobile/newreg/mobile=%s&password=%s&username=%s&captcha=%s";
    /**
     * 找回密码页面获取验证码
     */
    public static final String USER_IFORGOT_SEND_PHONES_CODE = "http://passport.youmi.cn/retrieve/sendsms?mobile=%s&_=%s";
    /**
     * 找回密码页面获取验证码
     * -captcode 需要图片验证码
     */
    public static final String USER_IFORGOT_SEND_PHONES_CODE_WITH_CAPTCODE = "http://passport.youmi.cn/retrieve/sendsms?captcode=%s&mobile=%s&_=%s";
    /**
     * 向手机发送验证码
     */
    public static final String USER_SEND_PHONES_CODE = "http://passport.youmi.cn/mobile/send/?mobile=%s";
    /**
     * 获取图片验证码
     */
    public static final String USER_GET_IMAGE_CODE = "http://passport.youmi.cn/include/showcaptcha?x=%s";
    /**
     * 获取手机验证码
     */
    public static final String USER_MOBILE_CAPTCHA = "http://passport.youmi.cn/mobile/send?mobile=%s";
    /**
     * 获取手机验证码
     * -randcaptcha 需要图片验证码
     */
    public static final String USER_MOBILE_CAPTCHA_IMAGE = "http://passport.youmi.cn/mobile/send?mobile=%s&randcaptcha=%s";
    /**
     * 绑定手机
     * -captcha 发送验证码到手机
     */
    public static final String USER_BIND_MOBILE = "http://passport.youmi.cn/mobile/verify?mobile=%s&captcha=%s";
    /**
     * 上传头像
     */
    public static final String UMIWI_AVATAR_UPLOAD = "http://mili.youmi.cn/avatar/uploadmobile/";
    /**
     * 修改密码
     */
    public static final String UMIWI_CHANGE_PASSPORT = "http://mili.youmi.cn/setting/changePassword/?oldpassword=%s&newpassword=%s";
    /**
     * 新第三方登录 http://passport.youmi.cn/mobile/oauthlogin?type=%s&connectid=%s&token=%s&secret=%s&refresh_token=%s&ref=%s
     */
    public static final String UMIWI_THIRD_LOGIN = "http://passport.youmi.cn/mobile/oauthlogin?";
    /**
     * 微信AccessTokenURL
     */
    public static final String WEIXIN_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 手机找回密码
     */
    public static final String SET_PASSWORD_URL = "http://passport.youmi.cn/retrieve/doResetByMobile/?password=%s&chptcode=%s&mobile=%s";
    /**
     * 设置密码(注册设置密码)
     */
    public static final String COMMIT_URL = "http://passport.youmi.cn/api/setUserNamePasswordIsNotOldPasswod/?username=%s&password=%s";
    /**
     * 验证码校验
     */
    public static final String CHECK_CONFIRM__URL = "http://passport.youmi.cn/Retrieve/verifyHashByMobile?chptcode=%s&mobile=%s";
    /**
     * 会员信息
     */
    public static final String UMIWI_USER_INFO = "http://i.v.youmi.cn/apimember/getUserinfo?app=android";
    /**
     * 会员Token
     */
    // "http://passport.youmi.cn/login/login/?" + str + "&expire=315360000"
    public static final String UMIWI_USER_TOKEN = "http://passport.youmi.cn/login/login/?%s&expire=315360000";

    /**
     * 用户忘记密码跳转到web页面
     */
    public static final String UMIWI_USER_WEB_FORGET_PASSWORD = "http://passport.youmi.cn/retrieve";
    /**
     * 判断用户的登录权限，账号共用会被禁
     */
    public static final String UMIWI_USER_CANLOGIN = "http://i.v.youmi.cn/ClientApi/canlogin";
    /**
     * 用户退出时 清除服务器cookie
     */
    public static final String UMIWI_USER_LOGOUT = "http://passport.youmi.cn/login/logout/";

    // =================================登录  相关=================================================//

    //================================原生订单接口===================================//
    /**
     * 原生订单接口
     * id:商品ID（专辑的ID 或者会员的身份ID）
     * type:商品类型 （2 代表专辑 3代表会员）
     * couponid:用户选择的优惠券ID号（接口那到后可以计算价格）
     * couponcode:用户选择的优惠码（同上）
     * discounttype:用户选择的优惠类型（如：timed、yddyh等英文代表的）
     * ext1:移动端设备型号
     */
    public static final String UMIWI_PAY_API = "http://v.youmi.cn/morders/show?id=%s&type=%s";
    /**
     * 充值
     */
    public static final String UMIWI_PAY_RECHARGE_API = "http://v.youmi.cn/payment/mconfirm/?amount=%s";
    /**
     * 从订单页面支付
     */
    public static final String UMIWI_PAY_FOR_ORDER = "http://v.youmi.cn/payment/mconfirm/?order_id=%s";

    /**
     * 开卡10位
     */
    public static final String UMIWI_TCODE_CHANGE_URL_10 = "http://v.youmi.cn/tcode/change?pcode=%s";//&security_code=%s

    /**
     * 开卡14位
     */
    public static final String UMIWI_TCODE_CHANGE_URL_14 = "http://v.youmi.cn/card/convert?code=%s";//&security_code=%s

    //================================原生订单接口===================================//

    //================================首页模块===================================//
    /**
     * 获取首页的弹窗图并保存白银会员cookie
     */
    public static final String ADANDSILVERCOOKIE = "http://i.v.youmi.cn/appver/register?app=android&deviceid=%s&channel=%s&model=%s&version=%s";
    /**
     * 轮播 添加专题&version=2
     */
    public static final String VIDEO_LUNBO = "http://i.v.youmi.cn/lunbo/list?android";
    public static final String VIDEO_LUNBO_NEW = "http://i.v.youmi.cn/lunbo/list?imgsizetype=2";
    /**
     * h5页面的分享
     */
    public static final String UMIWI_H5SHARE = "http://i.v.youmi.cn/api8/getShare?url=%s";
//	public static final String VIDEO_LUNBO = "http://i.v.youmi.cn/ClientApi/list?type=lunbo&version=2";//old api
    /**
     * 首页推荐
     */
    public static final String VIDEO_TUIJIAN = "http://v.youmi.cn/api8/index";
    /**
     * 精选专题-换一批
     */
    public static final String UMIWI_CHOICENESS = "http://v.youmi.cn/api8/Albumlist?p=%s";

    /**
     * 查看专题
     */
    public static final String UMIWI_LBUMLIST = "http://v.youmi.cn/ClientApi/getNewZhuanti2List?p=%s&type=%s&order=%s&catid=%s";
    /**
     * 热门视频
     */
    public static final String UMIWI_HOTVIDEO_HUAN = "http://api.v.youmi.cn/api8/hotvideo?p=%s";
    /**
     * 首页广告图
     */
    public static final String UMIWI_ADVERTISE= "http://api.v.youmi.cn/api8/ad";
    //================================抽屉模块===================================//

    /**
     * 热门分类 精品专题
     */
    public static final String HOT_LIST_JPZT = "http://api.v.youmi.cn/ClientApi/getZhuanti2List?p=%s&pagenum=5";
    /**
     * 讲师列表
     */
    public static final String LECTURER_LIST = "http://i.v.youmi.cn/apireader/fromCategoryGetTutorList?catid=0&pinyin=(null)&tag=0&app=android";
    /**
     * 最近更新
     */
    public static final String RECENTCHANGE_LIST = "http://api.v.youmi.cn/clientapi2/newCourseRecommend/?p=%s&num=10&g=%s";
    /**
     * 摇一摇 获取优惠列表图片
     */
    public static final String COUPON_LIST_URL = "http://v.youmi.cn/lottery/list?version=2";
    /**
     * 摇一摇 获取结果图片
     */
    public static final String COUPON_CONTENT_URL = "http://v.youmi.cn/lottery/draw";
    /**
     * 我摇到的东西
     */
    public static final String MINE_SHAKE_COUPONS = "http://v.youmi.cn/lottery/mylottery?";
    /**
     * 摇到门票的用户信息提交
     */
    public static final String MENPIAO_SUBMIT = "http://v.youmi.cn/lotteryuser/menpiaosubmit?id=%s&name=%s&mobile=%s&address=%s";


    // ==========================我的账号============================================//
    /**
     * 我的课程Vip
     */
    public static final String VIDEO_VIP_MYCOURSE = "http://i.v.youmi.cn/ClientApi/mycourse?pagenum=8";
    /**
     * 优惠券  all=1 吐出所有的已消费未消费的
     */
    public static final String UMIWI_MY_COUPON = "http://i.v.youmi.cn/ClientApi/mycoupon?p=%s&pagenum=10&all=1";
    /**
     * 播放记录
     */
    public static final String UMIWI_MY_RECORD = "http://i.v.youmi.cn/ClientApi/mywatchlog?p=%s&pagenum=10";
    /**
     * 视频浏览记录
     */
    public static final String UMIWI_VIDEO_RECORD= "http://i.v.youmi.cn/ClientApi/videoLog";
    /**
     * 音频浏览记录
     */
    public static final String UMIWI_AUDIO_RECORD= "http://i.v.youmi.cn/audioalbumlog/audiolistApi?p=%s";
    /**
     * 删除播放记录
     */
    public static final String MY_RECORD_DELETE_BY_IDS = "http://v.youmi.cn/watchlog/delByAlbumids?";
    /**
     * 清空播放记录
     */
    public static final String UMIWI_CLEAR_MYRECORD = "http://i.v.youmi.cn/watchlog/clear/";
    /**
     * 我的订单
     */
    public static final String UMIWI_MY_ORDER = "http://i.v.youmi.cn/ClientApi/myorders?p=%s&pagenum=10&notcode=y";
    /**
     * 我的兑换卡兑换号
     *///http://v.youmi.cn/clientApi/exchange
    public static final String UMIWI_MY_CARD = "http://v.youmi.cn/clientApi/exchange?p=%s&pagenum=10";
    /**
     * 我的收藏列表
     */
    public static final String UMIWI_MY_FAV = "http://i.v.youmi.cn/ClientApi/myfavlist?pagenum=10&p=%s";
    /**
     * 视频收藏列表
     */
    public static final String UMIWI_ENSH_VIDEO="http://i.v.youmi.cn/api8/favvideolist?p=%s";
    /**
     * 音频收藏列表
     */
    public static final String UMIWI_ENSH_AUDIO="http://i.v.youmi.cn/api8/favaudiolist?p=%s";
    /**
     * 点击收藏视频
     */
    public static final String UMIWI_FAV_ADD_VIDEO_ALBUMID = "http://i.v.youmi.cn/ClientApi/putfav?id=%s";
    /**
     * 点击收藏音频
     */
    public static final String UMIWI_FAV_ADD_AUDIO_ALBUMID = "http://i.v.youmi.cn/api8/fav?albumid=%s";
    /**
     * 取消收藏音频
     */
    public static final String UMIWI_FAV_REMOVE_AUDIO_ALBUMID = "http://i.v.youmi.cn/api8/unfav?albumid=%s";
    /**
     * 删除收藏视频 http://i.v.youmi.cn/favalbum/remove/?id=xxx&isalbum=y
     * - id 收藏的ID，或者专辑的ID
     * - isalbum 当id为专辑的ID时 此处 isalbum=y
     */
    public static final String FAV_REMOVE_VIDEO_ALBUMID = "http://i.v.youmi.cn/favalbum/remove/?id=%s";
    /**
     * 删除收藏视频
     */
    public static final String UMIWI_FAV_DELETE_VIDEO_FAVID = "http://i.v.youmi.cn/favalbum/remove?id=%s";
    /**
     * 我的笔记
     */
    public static final String MINE_NOTE = "http://i.v.youmi.cn/apireader/getNotes?p=%s&pagenum=10";
    /**
     * 用户意见反馈
     */
    public static final String FEEDBACK_URL = "http://v.youmi.cn/feedback/add?";
    /**
     * 上传app运行日记
     */
    public static final String RUNNING_LOG = "http://v.youmi.cn/api/runningLog/";
    /**
     * 签到
     */
    public static final String LOTTERY = "http://v.youmi.cn/signin/lottery?";
    /**
     * 积分
     */
    public static final String INTEGRAL = "http://v.youmi.cn/mobile/cdetail/";
    /**
     * 积分商城
     */
    public static final String INTEGRAL_SHOP = "http://v.youmi.cn/mobile/cdetail/?type=shop";
    /**
     * 周报
     */
    public static final String WEEK_REPORT = "http://v.youmi.cn/weekreport/";
    /**
     * 我的私信
     */
    public static final String MINE_MESSAGE = "http://api.v.youmi.cn/ClientApi/mymessage?pagenum=8";
    // ===============================================================================//


    /**
     * 统计用户在线播放时长接口；开发人（张小波） 提交视频打点和更新播放记录
     * http://v.youmi.cn/watchlog/add?log=D4:20:6D:BA:CB:AF
     * |6030780,1341,1348642126,1348642142,16;&os=android2.0
     */
    public static final String PLAY_RECORDS = "http://v.youmi.cn/watchlog/add?";
    /**
     * 同步用户对专辑的过期时间 废弃
     */
    public static final String GET_EXPIRETIME_BY_ALBUMIDS = "http://i.v.youmi.cn/ClientApi/getExpiretimeByAlbumIds?albumids=%s";

    /**
     * 会员相关课程
     */
    public static final String VIDEO_VIP_RELATE = "http://i.v.youmi.cn/ClientApi/getRelate?id=%s&n=6";
    /**
     * 搜索
     */
    public static final String SEARCH = "http://i.v.youmi.cn/ClientApi/search?pagenum=8&q=%s&filter=%s&order=%s&p=%s";
    /**
     * 建议词
     */
    public static final String SUGGEST_SEARCH = "http://v.youmi.cn/ClientApi/suggest?q=%s";
    /**
     * 搜索云标签
     */
    public static final String SEARCH_CLOUD = "http://i.v.youmi.cn/clientapi2/suggest?pagenum=10";
    /**
     * 获取笔记截图
     */
    public static final String NOTE_VIDEO_SHOT = "http://api.v.youmi.cn/api/cutnotespic/?videoid=%s&time=%s&albumid=%s";
    /**
     * 保存笔记
     */
    public static final String SAVE_NOTE = "http://mili.youmi.cn/api/addnotes?";
    /**
     * 获取专辑的视频列表
     */
    public static final String ALBUM_VIDEOS_URL = "http://i.v.youmi.cn/ClientApi/getAlbumDetail?id=%s&courseonly=1";
    /**
     * 获取笑话
     */
    public static final String JOKE_URL = "http://v.youmi.cn/duanzi/download?";
    /**
     * 绑定推送id
     */
    public static final String PUSH_BIND = "http://i.v.youmi.cn/C2c/pushbind";
    /**
     * 优惠大礼包
     */
    public static final String APP_GIFT = "http://v.youmi.cn/appgift/getgift?app=android&version=%s&deviceid=%s&channel=%s";
    /**
     * 活动列表 http://i.v.youmi.cn/ClientApi/huodonglist/?p=%s&pagenum=10&address=%s&inprogress=%s 不传地址代表北京
     */
    public static final String HUO_DONG_LIST = "http://i.v.youmi.cn/ClientApi/huodonglist/?pagenum=8&inprogress=%s&p=%s";
    /**
     * 发现我的课程，用户测试画像
     */
    public static final String USER_TEST_COURSE = "http://i.v.youmi.cn/clientapi2/usertest/";
    /**
     * 首页
     */
    public static final String HOME_INDEX = "http://i.v.youmi.cn/api7/indexSection1/";
    /**
     * 最新免费
     */
    public static final String NEW_FREE = "http://i.v.youmi.cn/api8/newfree/";
    /**
     * 分类
     */
    public static final String CATEGORY_LIST = "http://i.v.youmi.cn/api7/categorylist";
    /**
     * 排行榜
     */
    public static final String RANK_LIST = "http://i.v.youmi.cn/api7/ranklist/";
    /**
     * 优米移动触屏版
     */
    public static final String YOUMI_WEB = "http://v.youmi.cn/mobile/index";
    /**
     * 会员权益
     */
    public static final String VIP_MEMBERINTRO = "http://i.v.youmi.cn/api7/memberintro";
    /**
     * 首页的钻石专享
     */
    public static final String DIAMOND_FEAST = "http://i.v.youmi.cn/apireader/fromCategoryOrTutorGetAlbumList?subject=5&pagenum=10&p=";
    /**
     * 课程列表
     */
    public static final String COURSE_LIST = "http://i.v.youmi.cn/apireader/fromCategoryOrTutorGetAlbumList?pagenum=8&orderby=%s%s&p=%s";
    /**
     * 课程 评论列表
     */
    public static final String COMMENT_REPLY_LIST = "http://i.v.youmi.cn/thread/listcommentandreply/?albumid=%s&pagenum=10&p=%s";
    /**
     * 发表评论
     */
    public static final String COMMENT_SEND = "http://v.youmi.cn/thread/addthread/";

    // ==================================================================================================


    /**
     * 微信 登录AppID
     */
    public static final String WEIXIN_APP_ID = "wx81fb7c3811a9cb6e";
    /**
     * 微信 支付 AppID
     */
    public static final String WEIXIN_APP_ID_PAY = "wx6eb28a78527d2f69";
    /**
     * 微信 登录AppKey
     */
    public static final String WEIXIN_APP_KEY = "9a5c583da9796b6fb963d53d8225efeb";

    // 新浪APP_KEY
    public static final String SINA_APP_KEY = "2202907156";
    // 新浪授权回调URL
    public static final String SINA_REDITE_URL = "https://api.weibo.com/oauth2/default.html";
    // 新浪授权作用域
    public static final String SINA_SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    // QZoneAPP_ID
    public static final String QZone_APP_ID = "218165";
    // QZone授权作用域
    public static final String QZone_SCOPE = "all";


    // =================================已购相关=================================================//


    // =================================新优米首页相关=================================================//
    public static final String CELEBRTYY_LIST = "http://i.v.youmi.cn/apireader/tutorList";

    public static final String CELEBRTYY_DETAILS = "http://i.v.youmi.cn/api8/gettutor/?uid=";
    //专栏列表
    public static final String TUTORCOLUMN = "http://i.v.youmi.cn/tutorcolumn/listApi/?p=";
    //已购专栏
    public static final String ALREADY_COLUMN = "http://i.v.youmi.cn/tutorcolumn/buylist?p=%s&uid=%s";

    /**
     * 已购专题
     */
    public static final String UMIWI_BUYSPECIAL= "http://i.v.youmi.cn/tutorcolumn/buyspeciallist?p=%s";
    //我的—回答（已回答，待回答）
    public static final String Mine_Answer = "http://i.v.youmi.cn/api8/myquestion?p=%s&answerstate=%s";
    //提交音频
    public static final String COMMIT_VOICE = "http://i.v.youmi.cn/question/answerApi";

    //未购专栏详情页
    public static final String No_buy_column = "http://i.v.youmi.cn/tutorcolumn/detailApi?id=";

    /**
     * 专栏阅读详情页面
     */
    public static final String UMIWI_COLUMN_READ = "http://i.v.youmi.cn/audioalbum/detailApi?id=%s";
    /**
     * 未购专栏详情页面
     */
    public static final String UMIWI_NOBUY_COLUMN="http://i.v.youmi.cn/tutorcolumn/detailApi?id=%s";
    /**
     * 已购专栏详情页面用户留言
     */
    public static final String UMIWI_COLUM_MESSAGE = "http://i.v.youmi.cn/tmessage/listapi?id=%s&p=%s";
    /**
     * 已购专栏提交留言
     */
    public static final String UMIWI_MESSAGE_COMMIT = "http://i.v.youmi.cn/tmessage/addapi?aid=%s&content=%s";
    //已购买
    public static final String Ask_Hear = "http://i.v.youmi.cn/api8/buyquestion";
    //已购-视频
    public static final String ALREADY_VIDEO = "http://i.v.youmi.cn/api8/buyvideolist";

    public static final String ALREADY_VOICE = "http://i.v.youmi.cn/audioalbum/buylist";

    public static final String Hear_url = "http://i.v.youmi.cn/api8/questionlisten";
//
//    public static final String QUESTION_LIST = "http://i.v.youmi.cn/api8/questionlist";
//    //试读页面
//    public static final String Logincal_thinking = "http://i.v.youmi.cn/api8/tutorcolumn/detailpurchasedApi";


    public static final String QUESTION_LIST = "http://i.v.youmi.cn/api8/questionlist?p=%s&tuid=%s";

    public static final String NAMED_QUESTIONA = "http://i.v.youmi.cn/question/namedquestionApi";
    //新增问题
    public static final String ADD_QUESTIONA = "http://i.v.youmi.cn/question/addApi?tutoruid=%s&title=%s";
    //提问问题生成订单
    public static final String CREATE_QUESTIN_ORDERID = "http://i.v.youmi.cn/orders/buyquestion?format=%s&qid=%s";
    //付费音频生成订单
    public static final String CREATE_BUYAUDIO = "http://i.v.youmi.cn/orders/buyaudio?format=%s&id=%s&spm=%s";
    //专栏购买生成订单
    public static final String CREATE_SUBSCRIBER_ORDERID = "http://i.v.youmi.cn/orders/buycolumn?format=%s&id=%s";
    //音频专题购买生成订单
    public static  final String BUY_SPECIAL_AUDIO = "http://i.v.youmi.cn/orders/buyaudiotopic?format=%s&id=%s";
    //热播榜
    public static final String REBO_BANG = "http://i.v.youmi.cn/api8/hottop";
    //热销榜
    public static final String REXIAO_BANG = "http://i.v.youmi.cn/api8/saletop";
    //热评榜
    public static final String REPING_BANG = "http://i.v.youmi.cn/api8/threadtop";
    //首页音频页面数据
    public static final String Login_Audio = "http://i.v.youmi.cn/api8/audiolist?p=%s&catid=%s&price=%s&orderby=%s";
    /**
     * 创业 职场 新趋势页面数据
     */
    public static final String UMIWI_BUS_WORK_TEND = "http://i.v.youmi.cn/api8/indexclass?p=%s&catid=%s&type=%s&price=%s&orderby=%s";
    //首页视频页面数据
    public static final String Login_Video = "http://i.v.youmi.cn/api8/videoindex";
    //首页音频分类
    public static final String audio_head = "http://i.v.youmi.cn/api8/tagTreeApi/?from=audio";
    //首页视频分类
    public static final String video_head = "http://i.v.youmi.cn/api8/tagTreeApi/?from=video";
    //试读页面
    public static final String Logincal_thinking = "http://i.v.youmi.cn/tutorcolumn/detailpurchasedApi?id=%s&orderby=%s";
    //音频评论
    public static final String audio_tmessage = "http://i.v.youmi.cn/api8/commentadd?from=audioalbum&albumid=%s&question=%s";
    //音频评论列表
    public static final String audio_tmessage_list = "http://i.v.youmi.cn/api8/commentlist?p=%s&tuid=%s&from=%s&albumid=%s";

    public static final String VODEI_URL = "http://i.v.youmi.cn/ClientApi/getAlbumDetail?id=";

    public static final String QUESTION_DES = "http://i.v.youmi.cn/question/getapi";

    public static final String ZAN = "http://i.v.youmi.cn/good/goodapi?qid=%s&status=%s";

    //一元听获取orderid
    public static final String yiyuan_listener = "http://i.v.youmi.cn/orders/buylisten?qid=%s&format=%s";
    //快速问答
    public static final String FAST_QUIZ = "http://i.v.youmi.cn/tutor/askListApi?tagname=%s&p=%s";

    //免费阅读拼接
    public static final String MIANFEI_YUEDU = "http://i.v.youmi.cn/audioalbum/getApi?id=%S";

    //已购专栏
    public static final String COlUMN_LIST = "http://i.v.youmi.cn/tutorcolumn/buylist?=p=%s&uid=%s";
    //收藏音频
    public static final String SHOUCANG_AUDIO = "http://i.v.youmi.cn/api8/favaudiolist?p=%s";
    /**
     * 音频专题
     */
    public static final String UMIWI_AUDIO_SPECIAL_DETAIL = "http://i.v.youmi.cn/tutorcolumn/specialdetails?id=%s&order=%s";
    /**
     * 视频专题
     */
    public static  final String UMIWI_VIDEO_SPECIAL_DETAIL = "http://i.v.youmi.cn/ClientApi/zhuanti2Detail?id=%s";

    /////////////========================================优雅的分割线 历史接口=================================================////////////
//    /** 我的课程未登录默认九宫 */
//	public static final String VIDEO_DEFAULT_IN_HOME = "http://api.v.youmi.cn/ClientApi/memberindex";
//	/** 黄金会员 */
//	public static final String VIDEO_VIP_GOLD = "http://api.v.youmi.cn/ClientApi/list?type=gold&pagenum=20";
//	/** 黄金会员 课程分类标签 */
//	public static final String VIDEO_VIP_GOLD_CATEGORY = "http://api.v.youmi.cn/ClientApi/list?type=gold&pagenum=20&category=";
//    /** 钻石会员 课程分类标签 */
//	public static final String VIDEO_VIP_DIAMOND_CATEGORY = "http://api.v.youmi.cn/ClientApi/list?type=diamond&pagenum=20&category=";
//	/** 白银会员 */
//	public static final String VIDEO_VIP_SILVER = "http://api.v.youmi.cn/ClientApi/list?type=silver&pagenum=20";
//	/** 白银会员 课程分类标签 */
//	public static final String VIDEO_VIP_SILVER_CATEGORY = "http://api.v.youmi.cn/ClientApi/list?type=silver&pagenum=20&category=";
//	/** 体验会员 */
//	public static final String VIDEO_VIP_EXP = "http://api.v.youmi.cn/ClientApi/list?type=exp&p=%s&pagenum=7";
//	/** 品牌视频 */
//	public static final String VIDEO_PINPAI = "http://api.v.youmi.cn/ClientApi/list?type=pinpailist&p=%s&pagenum=20";
//	/** 热门分类 */
//	private static final String HOT_LIST = "http://api.v.youmi.cn/ClientApi/recommendlist/?";
//	/** 热门分类 创业商机 */
//	public static final String HOT_LIST_CYSJ = HOT_LIST + "s=cysj&n=20&p=%s";
//	/** 热门分类 管理之道 */
//	public static final String HOT_LIST_GLZD = HOT_LIST + "s=gljj&n=20&p=%s";
//	/** 热门分类 市场营销 */
//	public static final String HOT_LIST_SCYX = HOT_LIST + "s=scxs&n=20&p=%s";
//	/** 热门分类 职业/技能 */
//	public static final String HOT_LIST_ZYJN = HOT_LIST + "s=zyjn&n=20&p=%s";
//	/** 热门分类 职场新人 */
//	public static final String HOT_LIST_ZCXR = HOT_LIST + "s=zcxr&n=20&p=%s";
//	/** 热门分类 大学生 */
//	public static final String HOT_LIST_DXS = HOT_LIST + "s=dxs&n=20&p=%s";
//	/** 日排行榜 */
//	public static final String CHARTSLIST_DAY = "http://api.v.youmi.cn/ClientApi/getToptoClient?type=day";
//	/** 好评榜 */
//	public static final String CHARTSLIST_BEST = "http://api.v.youmi.cn/ClientApi/getToptoClient?type=best";
//	/** 付费榜 */
//	public static final String CHARTSLIST_PAY = "http://api.v.youmi.cn/ClientApi/getToptoClient?type=pay";
//	/** 购买专辑开头 */
//	public static final String BUY_ALBUMSTART = BASE_BUY + "/albumstart";
//	/** 钻石会员 */
//	public static final String BUY_VIP_DIAMOND = BASE_BUY + "/vipstart?id=23";	
//	/** 黄金会员 */
//	public static final String BUY_VIP_GOLD = BASE_BUY + "/vipstart?id=22";
//	/** 白银会员 */
//	public static final String BUY_VIP_SILVER = BASE_BUY + "/vipstart?id=20";
//	/** 抽屉第三块 创业课程等*/
//	public static final String LEFT_HOT_CATEGORY_LIST = "http://i.v.youmi.cn/apireader/fromCategoryOrTutorGetAlbumList?pagenum=20&catid=%s&orderby=ctime";
//	/** 免费体验7天*/
//	public static final String EXP_LIST_CONTENT_HOME = UMIWI_BASE_URL +"/list?type=exp&version=201402&uid=%s";
//	/** 服务器获得随机语录 */
//	public static final String QUOTATIONS = "http://app.youmi.cn/api/video/get_sayinglist.php?k=1ef34aed524ef5c6f856bcb12492fd42&num=1&orderby=rand&cate=saying";
//	 限时免费
//	public static final String FREE_LIST = "http://i.v.youmi.cn/ClientApi/list?type=exp&version=201402";
//	/** vip会员 课程类别 头字节：课程分类索引 */
//	public static final String VIDEO_VIP_HEADER = "http://api.v.youmi.cn/ClientApi/typelist";
//	/** 广告图 */
//	public static final String UMIWI_AD = "http://api.v.youmi.cn/ClientApi/loadimg?d=android";
//	 讲师推荐
//	public static final String LECTURER_RECOMMEND = "http://i.v.youmi.cn/apireader/indexTutors";
//	/**收藏列表全部数据*/
//	public static final String UMIWI_MY_FAV_ALL = "http://i.v.youmi.cn/ClientApi/myfavlist?p=1&pagenum=1000";
//	/** 优惠活动列表  : 摇一摇, 签到打卡*/
//	public static final String LUCKY_CONTENTS_URL = "http://i.v.youmi.cn/clientApi/luckymenu";
    ///===========
//	// 与游戏相关的课程
//	public static final String GAME_RELATED_COURSE_URL = "http://i.v.youmi.cn/ClientApi/list?type=game&gameid=%s";
//	// 上传游戏记录
//	public static final String GAME_RECORD_URL = "http://v.youmi.cn/gamescore/add/";
//创业谈谈谈，淘宝故事
//    public static final String URL_CARVERX_TAOBAO = "http://i.v.youmi.cn/api7/sectionSecond/";
//    /**
//     * 热播课程或猜你喜欢
//     */
//    public static final String HOMERECOMMEND_LIST_CONTENT_HOME = "http://i.v.youmi.cn/clientapi2/homerecommend?p=%s&pagenum=10";
//    /**
//     * 删除猜你喜欢
//     */
//    public static final String DETELE_RECOMMEND = "http://i.v.youmi.cn/clientapi2/dislikealbum?albumids=%s";
//    /**
//     * 应用推荐
//     */
//    public static final String APP_RECOMMEND = "http://app.youmi.cn/api/video/get_application.php?k=1ef34aed524ef5c6f856bcb12492fd42&type=android";
//    /**
//     * 搜索热词
//     */
//    public static final String SEARCH_HOT_WORDS = "http://app.youmi.cn/api/video/hotwords.php?k=1ef34aed524ef5c6f856bcb12492fd42";
//   // 讲师的评论总分
//    public static final String LECTURE_JUDGE_TOTAL_URL = "http://api.v.youmi.cn/c2c/getTotalEvaluate?uid=%s";
//
//    // 关注讲师
//    public static final String FOLLOW_LECTURE_URL = "http://i.v.youmi.cn/c2c/followTutor?tutoruid=%s";
//
//    // 讲师的评价列表
//    public static final String LECTURE_JUDGE_LIST_URL = "http://api.v.youmi.cn/c2c/getevaluatelist?uid=%s&pagenum=10&p=%s";
//
//    // 评价讲师
//    public static final String JUDGE_LECTURE_URL = "http://i.v.youmi.cn/c2c/addEvaluate?uid=%s&content=%s&score=%s";
//
//    // 获取权限
//    public static final String GET_PERMISSION_URL = "http://i.v.youmi.cn/C2c/freeOrders?answeruid=%s&askuid=%s";
//
//    // 创业类别
//    public static final String STAGE_SECTION_URL = "http://i.v.youmi.cn/api7/albumCategoryList/";
//
//    /**
//     * 公开课
//     */
//    public static final String CHARTSLIST_PUBLIC_URL = "http://v.youmi.cn/apireader/fromCategoryOrTutorGetAlbumList?catid=1284&orderby=ctime&pagenum=8";
//
//    /**
//     * 发现页面的免费url
//     */
//    public static final String FREE_URL = "http://i.v.youmi.cn/apireader/fromCategoryOrTutorGetAlbumList?catid=0&grade=1&pagenum=8";
//
//    /**
//     * 发现页面的赢在中国url
//     */
//    public static final String WIN_OF_CHINA_URL = "http://i.v.youmi.cn/ClientApi/search?pagenum=8&q=%s";
//    /**
//     * 游戏列表
//     */
//    public static final String URL_GAME_LIST = "http://i.v.youmi.cn/ClientApi/found?";
//
//    /**
//     * 首页，分类，发现，我的点击事件统计
//     */
//    public static final String URL_CLICK_STATISTICS = "http://i.v.youmi.cn/clientuv/add?uid=%s&deviceid=%s&name=%s";
//    /**
//     * 验证视频有效期，并返回些视频的已播放时间
//     */
//    public static final String VIP_VIDEO_RECORD_TIME = "http://i.v.youmi.cn/ClientApi/validate?aid=%s&vid=%s&time=1";
//    // 手机获取验证码
//    public static final String CONFIRM_URL = "http://passport.youmi.cn/mobile/clientregsend?mobile=%s&_=%s";
//    // 手机注册
//    public static final String MOBILE_REGISTE_URL = "http://passport.youmi.cn/mobile/clientreg?mobile=%s&captcha=%s";
//    // 邮箱注册
//    public static final String EMAIL_REGISTE = "http://passport.youmi.cn/mobile/clientreg/?email=%s&password=%s";
//
//    // new email register
//    public static final String REGISTER_EMAIL = "http://passport.youmi.cn/register/reg/?username=%s&email=%s&password=%s";
//    /**
//     * 用户注册
//     */
//    public static final String UMIWI_USER_REGISTER = "http://passport.youmi.cn/register/reg/?";
//    /**
//     * 用户注册跳转到web页面
//     */
//    public static final String UMIWI_USER_WEB_REGISTER = "http://passport.youmi.cn/register/?app=1";
//    /**
//     * 钻石会员
//     */
//    public static final String VIDEO_VIP_DIAMOND = "http://api.v.youmi.cn/ClientApi/list?type=diamond&pagenum=10";
//
//    /**
//     * 免费好课
//     */
//    public static final String CHARTSLIST_FREE = "http://api.v.youmi.cn/ClientApi/getToptoClient?type=free&order=lastweek";
//    /**
//     * 下载最多
//     */
//    public static final String CHARTSLIST_DOWN = "http://api.v.youmi.cn/ClientApi/getToptoClient?type=down";
//
//    /**
//     * 观看最多
//     */
//    public static final String CHARTSLIST_WATCH = "http://api.v.youmi.cn/ClientApi/getToptoClient?type=7day";
//    /**
//     * 口碑最好
//     */
//    public static final String CHARTSLIST_USERPRAISE = "http://api.v.youmi.cn/ClientApi/getToptoClient?type=useful";
//    /**
//     * 抽屉创业课程等的标签
//     */
//    public static final String DRAWER_LABEL = "http://i.v.youmi.cn/apireader/newTagList?tagname=%s";
////可咨询讲师列表
//    public static final String LECTURER_Consult_LIST = "http://api.v.youmi.cn/C2c/memberSellList?pagenum=10&p=%s";
//    /**
//     * 今日限免
//     */
//    public static final String VIDEO_TOADY_FREE = "http://i.v.youmi.cn/ClientApi/getTodayLimitedFree?";
    /////////////========================================优雅的分割线 历史接口=================================================////////////

}
