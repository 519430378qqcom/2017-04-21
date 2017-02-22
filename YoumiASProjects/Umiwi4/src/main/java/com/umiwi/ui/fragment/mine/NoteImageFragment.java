package com.umiwi.ui.fragment.mine;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;

import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.SingletonFactory;

public class NoteImageFragment {

	private ImageView screenshotImageView;

	public static NoteImageFragment getInstance() {
		return SingletonFactory.getInstance(NoteImageFragment.class);
	}

	public void showDialog(Context mContext, String imageURL) {
		
		ViewHolder holder = new ViewHolder(R.layout.image_layout);
		final DialogPlus dialogPlus = new DialogPlus.Builder(mContext)
				.setContentHolder(holder)
				.setGravity(Gravity.CENTER)
				.create();
		dialogPlus.show();

		View view = dialogPlus.getHolderView();
		screenshotImageView = (ImageView) view.findViewById(R.id.image_sc);
		screenshotImageView.setVisibility(View.VISIBLE);
		ImageLoader mImageLoader = new ImageLoader(mContext);
		mImageLoader.loadImage(imageURL, screenshotImageView);
		screenshotImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogPlus.dismiss();
			}
		});
	}

}
