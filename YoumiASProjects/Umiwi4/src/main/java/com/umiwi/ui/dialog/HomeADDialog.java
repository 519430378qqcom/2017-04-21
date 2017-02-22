package com.umiwi.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.WebFragment;

import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.SingletonFactory;

/**
 * 首页广告活动图
 * 
 * @author tangxiyong 2014-2-15下午4:36:28
 * 
 */
public class HomeADDialog extends DialogFragment {

	public static HomeADDialog getInstance() {
		return SingletonFactory.getInstance(HomeADDialog.class);
	}

	public void showDialog(Context mContext, String ad_str,
			final String web_url, String width, String height, String type) {

		ViewHolder holder = new ViewHolder(R.layout.dialog_home_ad);
		DialogPlus dialogPlus = new DialogPlus.Builder(mContext)
				.setContentHolder(holder)
				.setGravity(Gravity.CENTER)
				.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(DialogPlus dialog, View view) {
						switch (view.getId()) {
						case R.id.cancel:
							dialog.dismiss();
							break;
						case R.id.iv_home_ad:
							Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
							i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
							i.putExtra(WebFragment.WEB_URL, web_url);
							getActivity().startActivity(i);
							dialog.dismiss();
							break;

						default:
							break;
						}
					}
				})
				.create();
		
		View view = dialogPlus.getHolderView();
		
		ImageView iv_ad = (ImageView) view.findViewById(R.id.iv_home_ad);
		if (TextUtils.isEmpty(height) && TextUtils.isEmpty(width)) {
			height = "4";
			width = "3";
		}
		LayoutParams para = iv_ad.getLayoutParams();
		para.width = (getHeighAndWidth().widthPixels) - DimensionUtil.dip2px(112);
		para.height = (para.width * Integer.parseInt(height)) / Integer.parseInt(width);
		iv_ad.setLayoutParams(para);

		ImageLoader mImageLoader = new ImageLoader(getActivity());
		mImageLoader.loadImage(ad_str, iv_ad);

		dialogPlus.show();

	}

	public DisplayMetrics getHeighAndWidth() {
		Display display = (getActivity()).getWindowManager()
				.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics;
	}
}
