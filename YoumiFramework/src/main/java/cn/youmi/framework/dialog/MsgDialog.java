package cn.youmi.framework.dialog;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import cn.youmi.framework.R;
import cn.youmi.framework.main.BaseApplication;

public class MsgDialog extends BaseDialog {
	
	private TextView mMessageTextView;
	private CharSequence mMsg;
	private boolean gravityIsNotCenter;
	public void setMessage(CharSequence msg, boolean gravityIsNotCenter){
		this.gravityIsNotCenter = gravityIsNotCenter;
		setMessage(msg);
	}
	public void setMessage(CharSequence msg){
		this.mMsg = msg;
	}
	
	public void setMsg(int resid){
		String msg = BaseApplication.getContext().getResources().getString(resid);
		setMessage(msg);
	}

	public void setMsgTv(String msg){
		setMessage(msg);
	}

	@Override
	protected void onPostInflaterView(LayoutInflater inflater,View rootView,FrameLayout container) {
		super.onPostInflaterView(inflater,rootView,container);
		mMessageTextView  = (TextView) inflater.inflate(R.layout.view_dialog_msg, null);
		container.addView(mMessageTextView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if(mMsg != null){
			mMessageTextView.setText(mMsg);
			if (!gravityIsNotCenter) {
				mMessageTextView.setGravity(Gravity.CENTER);
			}
		}
	}
	
}
