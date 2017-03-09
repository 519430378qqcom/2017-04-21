package com.umiwi.ui.managers;

/**
 * @author tangxiyong
 * @version 2014年11月5日 下午3:45:51
 */
public final class StatisticsUrl {
	
	private StatisticsUrl() {

	}
 /**参数一	位置	点击的位置
	参数二	来源	从订单页面点击过来
	参数三	第几个	第一个 用1
	参数四	专辑id	117
	参数五	价格	11000 单位是分
	参数六	类型	1精品课程2会员购买3专题购买4讲师咨询购买 
	http://wiki.cn.umiwi.com/doku.php?id=spm%E8%A7%84%E5%88%99&#%E5%8F%82%E6%95%B0%E8%AF%A6%E7%BB%86%E8%A7%A3%E9%87%8A
	*/

	
	private static final String BASE_STATISTICS_URL = "http://i.v.youmi.cn/apireader/addToRouteByParam?contro=";
	
	public static final String SPM = "spm";
	
	/** 首页更多按钮*/
	public static final String INDEX_MORE = BASE_STATISTICS_URL + "/jia/indexmore&spm=5.1.0.0.0.0";
	/** 首页免费、新趋势、营销按钮*/
	public static final String HOME_BUTTON = "&spm=1.0.0.0.0.0";
	/** 搜索框*/
	public static final String SEARCH_FRAME = "&spm=9.0.0.0.0.0";
	/** 搜索云标签*/
	public static final String SEARCH_CLOUD = "&spm=8.0.0.0.0.0";
	/** 搜索历史*/
	public static final String SEARCH_HISTORY = "&spm=10.0.%s.0.0.0";
	/** 详情页下载按钮*/
	public static final String DETAIL_DOWNLOAD_BUTTON = BASE_STATISTICS_URL + "/jia/download&spm=11.8.0.%s.0.0";
	/** 下载页下载按钮*/
	public static final String DOWNLOAD_ED_BUTTON = BASE_STATISTICS_URL + "/jia/download&spm=11.3.0.%s.0.0";
	/** 详情页分享按钮*/
	public static final String DETAIL_SHARE_BUTTON = BASE_STATISTICS_URL + "/jia/share&spm=12.8.0.0.0.0";
	
	/** 详情页写评论按钮*/
	public static final String DETAIL_COMMENT_WRITE_BUTTON = "&spm=3.8.0.%s.0.0";
	/** 详情页评论tag*/
	public static final String DETAIL_COMMENT_TAG_BUTTON = "&spm=3.8.%s.%s.0.0";
	
	/** 详情页购买会员*/
	public static final String ORDER_VIP_DETAIL = "&spm=1.8.0.%s.23.2";
	/** 钻石页购买会员*/
	public static final String ORDER_VIP_LIST = "&spm=1.8.0.0.23.2";
	/** 详情页购买专辑*/
	public static final String ORDER_COURSE_DETAIL = "&spm=1.8.0.%s.%s.1";
	/** 购买专题*/
	public static final String ORDER_JPZT_DETAIL = "&spm=3.10.0.%s.%s.3";
	/** 购买讲师咨询*/
	public static final String ORDER_LECTURER = "&spm=2.14.0.%s.%s.4";
	/** 从订单页面支付*/
	public static final String ORDER_LIST = "&spm=1.7.0.0.0.%s";
	
	/** 详情页写评论*/
	public static final String DETAIL_COMMENT_WRITE = BASE_STATISTICS_URL+"/ttag/taglist&spm=2.0.0.0.0.0";
	/** 详情页评论列表*/
	public static final String DETAIL_COMMENT_LIST = "&spm=1.0.0.0.0.0";
}
