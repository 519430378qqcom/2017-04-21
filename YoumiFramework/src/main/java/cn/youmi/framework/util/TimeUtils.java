package cn.youmi.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * @author tangxiyong
 * @version 2014年11月2日 下午6:47:52
 */
public class TimeUtils {
	
	/**
	 * 产品上传、评论时间
	 * @param ctime 注意 传进来的必须是：毫秒
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatUploadedTime(long ctime) {
		SimpleDateFormat dfMD = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dfMD.format(new Date(ctime));
	}
	/**
	 * 产品时长
	 * @param s 注意 传进来的必须是：秒
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String formatPlayTime(long s) {
		long hours = s / 3600;
		s = s % 3600;
		long minutes = s / 60;
		s = s % 60;
		long seconds = s;
		
//		return hours + "小时" + minutes + "'" + seconds + "\"";
		return minutes + "' " + seconds + "\" ";
	}

	/** 用于会话列表MessageListFragment*/
	@SuppressLint("SimpleDateFormat")
	public static String setMessageListTime(long showTime) {
		String timeStr = null;
		SimpleDateFormat dfMD = new SimpleDateFormat("MM月dd日");
		SimpleDateFormat dfHM = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dfH = new SimpleDateFormat("HH");

		long nowTime = System.currentTimeMillis();
		try {
			Date nowDate = dfMD.parse(dfMD.format(nowTime));// 当前系统时间
			Date showDate = dfMD.parse(dfMD.format(showTime));// 消息时间戳
			if (nowDate.getDate() - showDate.getDate() > 1) {// 昨天以前
				timeStr = dfMD.format(new Date(showTime));
			} else if (nowDate.getDate() - showDate.getDate() == 1) {// 昨天
				timeStr = "昨天";
			} else if (nowDate.getDate() - showDate.getDate() == 0) {// 今天
				String temp = dfH.format(new Date(showTime));
				int tempT = Integer.parseInt(temp);
				if (tempT >= 17 & tempT < 24) {
					timeStr = "晚上" + dfHM.format(new Date(showTime));
				} else if (tempT >= 13 & tempT < 17) {
					timeStr = "下午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 12 & tempT < 13) {
					timeStr = "中午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 6 & tempT < 12) {
					timeStr = "上午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 2 & tempT < 6) {
					timeStr = "清晨" + dfHM.format(new Date(showTime));
				} else if (tempT >= 0 & tempT < 2) {
					timeStr = "凌晨" + dfHM.format(new Date(showTime));
				} else {
					timeStr = dfHM.format(new Date(showTime));
				}

			} else {// 昨天以前
				timeStr = dfMD.format(new Date(showTime));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeStr;
	}
	
	/** 用于聊天ChatActivity*/
	@SuppressLint("SimpleDateFormat")
	public static String setChatTime(long showTime) {
		String timeStr = null;
		SimpleDateFormat dfMD = new SimpleDateFormat("MM月dd日");
		SimpleDateFormat dfHM = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dfH = new SimpleDateFormat("HH");

		long nowTime = System.currentTimeMillis();
		try {
			Date nowDate = dfMD.parse(dfMD.format(nowTime));// 当前系统时间
			Date showDate = dfMD.parse(dfMD.format(showTime));// 消息时间戳
			if (nowDate.getDate() - showDate.getDate() > 1) {// 昨天以前
				timeStr = dfMD.format(new Date(showTime));
			} else if (nowDate.getDate() - showDate.getDate() == 1) {// 昨天
				String temp = dfH.format(new Date(showTime));
				int tempT = Integer.parseInt(temp);
				if (tempT >= 17 & tempT < 24) {
					timeStr = "昨天 晚上" + dfHM.format(new Date(showTime));
				} else if (tempT >= 13 & tempT < 17) {
					timeStr = "昨天 下午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 12 & tempT < 13) {
					timeStr = "昨天 中午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 6 & tempT < 12) {
					timeStr = "昨天 上午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 2 & tempT < 6) {
					timeStr = "昨天 清晨" + dfHM.format(new Date(showTime));
				} else if (tempT >= 0 & tempT < 2) {
					timeStr = "昨天 凌晨" + dfHM.format(new Date(showTime));
				} else {
					timeStr = dfHM.format(new Date(showTime));
				}
			} else if (nowDate.getDate() - showDate.getDate() == 0) {// 今天
				String temp = dfH.format(new Date(showTime));
				int tempT = Integer.parseInt(temp);
				if (tempT >= 17 & tempT < 24) {
					timeStr = "晚上" + dfHM.format(new Date(showTime));
				} else if (tempT >= 13 & tempT < 17) {
					timeStr = "下午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 12 & tempT < 13) {
					timeStr = "中午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 6 & tempT < 12) {
					timeStr = "上午" + dfHM.format(new Date(showTime));
				} else if (tempT >= 2 & tempT < 6) {
					timeStr = "清晨" + dfHM.format(new Date(showTime));
				} else if (tempT >= 0 & tempT < 2) {
					timeStr = "凌晨" + dfHM.format(new Date(showTime));
				} else {
					timeStr = dfHM.format(new Date(showTime));
				}

			} else {// 昨天以前
				timeStr = dfMD.format(new Date(showTime));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeStr;
	}
}
