package com.umiwi.ui.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import cn.youmi.framework.dialog.BaseDialog;

import com.umiwi.ui.R;

public class SelectImageDialog extends BaseDialog {
	
	private OnClickListener mTakePictureListener;
	private OnClickListener mUploadLocalListener;


	public void setTakePictureListener(OnClickListener mTakePictureListener) {
		this.mTakePictureListener = mTakePictureListener;
	}


	public void setUploadLocalListener(OnClickListener mUploadLocalListener) {
		this.mUploadLocalListener = mUploadLocalListener;
	}

	@Override
	protected void onPostInflaterView(LayoutInflater inflater, View rootView,
			FrameLayout container) {
		super.onPostInflaterView(inflater, rootView, container);
		View v = inflater.inflate(R.layout.dialog_upload_image, null);
		positiveButton.setVisibility(View.INVISIBLE);
		negativeButton.setVisibility(View.INVISIBLE);
		positiveButton.setEnabled(false);
		negativeButton.setEnabled(false);
		container.addView(v, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		//container.invalidate();
		
		v.findViewById(R.id.take_picture_button).setOnClickListener(mTakePictureListener);
		v.findViewById(R.id.upload_local_button).setOnClickListener(mUploadLocalListener);
	}
}
