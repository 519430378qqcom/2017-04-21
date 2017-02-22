package com.umiwi.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.AndroidSDK;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewUtils;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.NoteDetailBeans;
import com.umiwi.ui.fragment.mine.NoteImageFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.CourseListModel;

/**
 * 我的笔记详情
 * @author tangxiong
 * @version 2014年6月16日 下午5:56:34
 */
public class MineNoteDetailAdapter extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater;
	private ArrayList<NoteDetailBeans> mList;
	private BaseConstantFragment fragment;
	
	private Context mContext;
	
	public MineNoteDetailAdapter(BaseConstantFragment homeMineNoteDetailFragment, Context context, ArrayList<NoteDetailBeans> mList, CourseListModel noteList) {
		mLayoutInflater = ((Activity) context).getLayoutInflater();
		this.mContext = context;
		this.mList = mList;
		this.fragment = homeMineNoteDetailFragment;
	}


	public void setData(ArrayList<NoteDetailBeans> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return ListViewUtils.getSize(mList);
	}

	@Override
	public Object getItem(int position) {
		if(mList != null && position < mList.size() - 1) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if(view == null) {
			view = mLayoutInflater.inflate(R.layout.item_note_mine, null, false);
		}
		final Holder holder = getHolder(view);
		final NoteDetailBeans listBeans = mList.get(position);

		if (mList != null && mList.size() > 0) {
			if (listBeans.getImage()!=null) {
				holder.shotScreenListener.setImageURL(listBeans.getImage());
				ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
				mImageLoader.loadImage(listBeans.getImage(), holder.image);
			} else {

			}
			if (listBeans.getContent()!=null) {
				
				holder.shareListener.setShareContent(listBeans.getContent());
				
				holder.contentTextView.setText(listBeans.getContent());

				holder.contentTextView.getViewTreeObserver()
					.addOnGlobalLayoutListener(
						new OnGlobalLayoutListener() {
							int lines = 0;

							@Override
							public void onGlobalLayout() {
								lines = holder.contentTextView
										.getLineCount();
								if (AndroidSDK.isJELLY_BEAN()) {
									holder.contentTextView
											.getViewTreeObserver()
											.removeOnGlobalLayoutListener(
													this);
								} else {
									holder.contentTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
								}

								if (lines != 0 && lines > 2) {

									holder.contentTextView.setMaxLines(2);
									holder.contentTextView
											.setEllipsize(TextUtils.TruncateAt.END);

									holder.moreView
											.setVisibility(View.VISIBLE);
									holder.moreView.setText("全文");
								} else {
									holder.moreView
											.setVisibility(View.GONE);
								}
							}
						});
				
			}
			
			
			
			holder.ctime.setText("发表于"+listBeans.getCtime());
			
			holder.nameTextView.setText(YoumiRoomUserManager.getInstance().getUser().getUsername());
			
		} else {
			throw new IllegalStateException(view.getClass().getName() + "data is null or to is null");
		}
		return view;
//		}
	}

	private Holder getHolder(final View view) {
		Holder holder = (Holder) view.getTag();
		if (holder == null) {
			holder = new Holder(view);
			view.setTag(holder);
		}
		return holder;
	}
	private class Holder {
		ClickListener shotScreenListener = new ClickListener();

		MoreClickListener moreListener = new MoreClickListener();
		
		ShareClickListener shareListener = new ShareClickListener();
		
		public ImageView image;

		public TextView contentTextView;
		public TextView ctime;
		public TextView moreView;
		public TextView shareView;
		public TextView nameTextView;
		
		public Holder(View view) {
			image = (ImageView) view.findViewById(R.id.content_image_view);
			contentTextView = (TextView) view.findViewById(R.id.content_text_view);
			ctime = (TextView) view.findViewById(R.id.date_text_view);
			moreView = (TextView) view.findViewById(R.id.moreView);
			nameTextView = (TextView) view.findViewById(R.id.username);
			shareView = (TextView) view.findViewById(R.id.share_note_textview);
			shareView.setOnClickListener(shareListener);
			moreListener.setContentView(contentTextView);
			view.setOnClickListener(moreListener);
			image.setOnClickListener(shotScreenListener);
			
		}
		
		private class ClickListener implements View.OnClickListener {
			private String imageURL;

			public void setImageURL(String imageURL) {
				this.imageURL = imageURL;
			}

			@Override
			public void onClick(View v) {
				NoteImageFragment.getInstance().showDialog(mContext, imageURL);
			}

		}
		
		private class ShareClickListener implements View.OnClickListener {
			private String shareContent;

			public void setShareContent(String shareContent) {
				this.shareContent = shareContent;
			}

			@Override
			public void onClick(View v) {
				//TODO
				
			}

		}
		
		
		private class MoreClickListener implements View.OnClickListener {

			private TextView contentView;

			@Override
			public void onClick(View v) {
				
				if (contentView.getLineCount() == 2) {
					contentView.setLines(Integer.MAX_VALUE);
					contentView.setMinLines(0);
					moreView.setText("收起");
				} else {
					contentView.setMaxLines(2);
					moreView.setText("全文");
				}
				
			}

			public void setContentView(TextView contentTextView) {
				this.contentView = contentTextView;
			}
		}
	}
	
}
