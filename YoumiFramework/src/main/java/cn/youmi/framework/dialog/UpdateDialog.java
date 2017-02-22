package cn.youmi.framework.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import cn.youmi.framework.R;
import cn.youmi.framework.util.UpdateUtils;
import cn.youmi.framework.view.NumberProgressBar;

/**
 * 
 * @author tangxiong
 * @version 2014年7月17日 上午10:38:17
 */
public class UpdateDialog extends BaseDialog {

	private View view;
	private UpdateUtils updateManager;
	private static String downUrl;
	private boolean downError;
	private NumberProgressBar mProgressDialog;
	private TextView error;
	
	public static String TAG = "UpdateDialog";
	
	public void setDownUrl(String url) {
		downUrl = url;
	}
	
	@Override
	protected void onPostInflaterView(LayoutInflater inflater, View rootView,
			FrameLayout container) {
		super.onPostInflaterView(inflater, rootView, container);
		setOnTouchCancelable(false);
		setTitle(R.string.update_msg);
		setNegativeButtonText(R.string.update_cancel);
		setNegativeButtonListener(new CanceleClickListener());
		setPositiveButtonText(R.string.update_downing);
		setPositiveButtonListener(new DowningClickListener());
		
		view = inflater.inflate(R.layout.dialog_update, null);
		mProgressDialog = (NumberProgressBar) view.findViewById(R.id.progressbar);
		error = (TextView) view.findViewById(R.id.update_error);

		updateManager = new UpdateUtils(getActivity(), appUpdate);
		updateManager.isUpdate(downUrl);
		
		container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}


	class CanceleClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			dismiss();
			appUpdate.downloadCanceled();
		}

	}

	class DowningClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (downError) {
				mProgressDialog.setVisibility(View.VISIBLE);
				error.setVisibility(View.GONE);
				setPositiveButtonText(R.string.update_downing);
				updateManager.isUpdate(downUrl);
				downError = false;
			} else {
				dismiss();
			}
		}

	}

	UpdateUtils.UpdateCallback appUpdate = new UpdateUtils.UpdateCallback() {

		public void downloadProgressChanged(int progress) {
			mProgressDialog.setVisibility(View.VISIBLE);
			mProgressDialog.setProgress(progress);
		}

		public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
			mProgressDialog.setVisibility(View.GONE);
			if (sucess) {
				UpdateUtils.installApk(UpdateUtils.updateFile);
				dismiss();
			} else {
				downError = true;
				error.setVisibility(View.VISIBLE);
				setPositiveButtonText(R.string.update_try_downing);
			}
		}

		public void downloadCanceled() {
			updateManager.cancelDownload();
		}

		public void downloading() {
			mProgressDialog.setVisibility(View.VISIBLE);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(0);
			updateManager.downloadPackage();

		}
	};

}
