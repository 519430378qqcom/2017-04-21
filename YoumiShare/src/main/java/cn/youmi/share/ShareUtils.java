package cn.youmi.share;

import android.content.Context;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author tangxiyong
 * @version 2014年8月3日 下午2:08:34
 */
public class ShareUtils {
	
	
	/**
	 * @param SinaWeibo 
	 * @param QQ 
	 * @param WechatFriends 微信好友
	 * @param WechatMoments 微信朋友圈
	 * @param All 
	 *
	 */
	public enum ShareModle {
		SinaWeibo, QQ, WechatFriends, WechatMoments
	}
	
	public static void Share(Context mContext, String appName, String title, String content, String shareUrl, String imageUrl, ShareModle shareModle) {
		Share(mContext, appName, title, content, shareUrl, imageUrl, shareModle, false);
	}
	
	public static void Share(Context mContext, String appName, String title, String content, String shareUrl, String imageUrl, ShareModle shareModle, boolean isImagePath) {
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setDialogMode();
		switch (shareModle) {
		case SinaWeibo:
//			oks.setText(content + shareUrl);//如果平台content中没有拼接上url,使用此方法
			oks.setPlatform(SinaWeibo.NAME);
			break;
		case QQ:
			oks.setSite(appName);
			oks.setSiteUrl(shareUrl);
			oks.setTitle(title);
			oks.setTitleUrl(shareUrl);
			oks.setPlatform(QQ.NAME);
			break;
		case WechatFriends:
			oks.setTitle(title);
			oks.setUrl(shareUrl);
			oks.setPlatform(Wechat.NAME);
			break;
		case WechatMoments:
			oks.setTitle(content);
			oks.setUrl(shareUrl);
			oks.setPlatform(WechatMoments.NAME);
			break;
		}
		
		oks.setText(content);
		if (isImagePath) {
			oks.setImagePath(imageUrl);
		} else {
			oks.setImageUrl(imageUrl);
		}
		oks.setSilent(true);
		
		oks.show(mContext);
	}
}
