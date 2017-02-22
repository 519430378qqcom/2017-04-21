package com.umiwi.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.JokeBean;
import com.umiwi.ui.dao.JokeDao;
import com.umiwi.ui.fragment.down.DownloadedListFragment;

public class JokeDialog {

	private TextView tvJoke;
	private TextView tvReview;
	private static JokeDao dao;

	public void showDialog(Context mContext) {
		if (mContext == null) {
			return;
		}
		ViewHolder holder = new ViewHolder(R.layout.dailog_joke_fragment);
		DialogPlus dialogPlus = new DialogPlus.Builder(mContext)
				.setContentHolder(holder)
				.setBackgroundColorResourceId(R.color.transparent)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(DialogPlus dialog, View view) {
						switch (view.getId()) {
						case R.id.cancel:
							dialog.dismiss();
							break;
						case R.id.show_download:
							Intent i = new Intent(view.getContext(),
									UmiwiContainerActivity.class);
							i.putExtra(
									UmiwiContainerActivity.KEY_FRAGMENT_CLASS,
									DownloadedListFragment.class);
							view.getContext().startActivity(i);
							dialog.dismiss();
							break;
						case R.id.tv_retry:
							dialog.dismiss();
							break;
						default:
							break;
						}
					}
				}).setGravity(Gravity.CENTER).create();
		View rootView = dialogPlus.getHolderView();

		tvJoke = (TextView) rootView.findViewById(R.id.tv_joke);
		tvReview = (TextView) rootView.findViewById(R.id.tv_review);

		if (dao == null) {
			dao = new JokeDao();
		}
		JokeBean joke = dao.getJoke();
		if (joke != null) {
			tvJoke.setText(joke.getContent());
			tvReview.setText(joke.getReview());
			dao.update(joke);
		}

		dialogPlus.show();
	}

	public interface IResearch {
		void reTrySearch();
	}
}
