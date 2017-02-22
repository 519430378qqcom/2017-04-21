package cn.youmi.share;

import java.util.HashMap;

import android.os.Handler;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * @author tangxiyong
 * @version 2014年8月3日 下午2:23:58
 */
public class OneKeyShareCallback implements PlatformActionListener {
	Handler mHandler = new Handler();
	@Override
	public void onCancel(Platform arg0, int arg1) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
//				Toast.makeText(UmiwiApplication.getContext(), "分享取消,这样真的好吗?!",Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
//				Toast.makeText(UmiwiApplication.getContext(), "分享成功,感脚萌萌哒!", Toast.LENGTH_LONG).show();
			}
		});
//		ShareArticleFragment.getInstance().getDialog().dismiss();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
//				Toast.makeText(UmiwiApplication.getContext(), "被汪星人吃掉了^_^!,请重试",Toast.LENGTH_LONG).show();
			}
		});
	}

}
