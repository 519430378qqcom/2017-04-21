package com.umiwi.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.LecturerDetailFragment;
import com.umiwi.ui.fragment.course.BigZTListFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.course.CourseSequenceListFragment;
import com.umiwi.ui.fragment.course.JPZTDetailFragment;
import com.umiwi.ui.fragment.course.JPZTListFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.model.HomeRecommend;
import com.umiwi.ui.model.HomeRecommendCourseListModel;
import com.umiwi.ui.model.HomeRecommendTagListModel;

import java.util.ArrayList;

import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

/**
 * @author tangxiyong
 * @version 2015-6-9 下午6:52:54
 */
public class HomeRecommendAdapter extends BaseAdapter {
	
	private LayoutInflater mLayoutInflater;

	private ArrayList<HomeRecommend> mList;
	
	private Activity mActivity;

	public HomeRecommendAdapter(Context context) {
		super();
		mLayoutInflater = LayoutInflater.from(UmiwiApplication.getInstance());
		this.mActivity = (Activity) context;
	}

	public HomeRecommendAdapter(Context context, ArrayList<HomeRecommend> mList) {
		mLayoutInflater  = LayoutInflater.from(UmiwiApplication.getInstance());
		this.mActivity = (Activity) context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mList != null && position < mList.size() - 1) {
			return mList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final HomeRecommend listBeans = mList.get(position);
		ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
		
		if ("jiangshi".equals(listBeans.recommendType)) {
			convertView = mLayoutInflater.inflate(R.layout.item_home_recommend_jiangshi, null);
			TextView typeTitle = (TextView) convertView.findViewById(R.id.type_title);
			typeTitle.setText(listBeans.recommendName);
			LinearLayout jiangshiContainer = (LinearLayout) convertView.findViewById(R.id.jiangshi_container);
			if (jiangshiContainer.getChildCount() >= 1) {
				jiangshiContainer.removeAllViews();
			}
			for (int i = 0; i < listBeans.recommendCourseList.size(); i++) {
				HomeRecommendCourseListModel hm = listBeans.recommendCourseList.get(i);
				
				View item = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.item_home_recommend_jiangshi_image, null);
				item.setTag(hm);
				item.setOnClickListener(jiangshiClickListener);
				
				TextView tagTitle = (TextView) item.findViewById(R.id.name);
				tagTitle.setText(hm.name);
				
				CircleImageView photo = (CircleImageView) item.findViewById(R.id.photo);
				mImageLoader.loadImage(hm.image, photo);
				
				LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
				jiangshiContainer.addView(item, p);
			}
			
		} else {
			convertView = mLayoutInflater.inflate(R.layout.item_home_recommend, null);
			Holder holder = getHolder(convertView);
			View[] views = new View[] { holder.topLeftView, holder.topRightView, holder.bottomLeftView, holder.bottomRightView };
			
			holder.typeTitle.setText(listBeans.recommendName);
			for(int i = 0;i < 4;i++){
				HomeRecommendCourseListModel ti = listBeans.recommendCourseList.get(i);
				
				View vv = views[i];
				vv.setOnClickListener(recommendClickListener);
				vv.setTag(ti);
				
				ImageView coverImageView = (ImageView) vv.findViewById(R.id.cover_image_view);
				int w = (parent.getWidth() == 0) ? DimensionUtil.getScreenWidth(mActivity) : parent.getWidth();
				int h = (9 * w) / 16;
				
				if ("recommend".equals(listBeans.recommendType)) {
					w = (parent.getWidth() == 0) ? DimensionUtil.getScreenWidth(mActivity) : parent.getWidth();
					h = (3 * w) / 4;
				}
				LayoutParams para = coverImageView.getLayoutParams();
				para.width = w / 2 ;
				para.height = h / 2 ;
				coverImageView.setLayoutParams(para);
				
				mImageLoader.loadImage(ti.image, coverImageView);
				
				TextView courseTitle = (TextView) vv.findViewById(R.id.course_title);
				courseTitle.setText(ti.courseTitle + "");
				TextView subTitle = (TextView) vv.findViewById(R.id.sub_title);
				if (!TextUtils.isEmpty(ti.subtitle)) {
					subTitle.setVisibility(View.VISIBLE);
					subTitle.setText(ti.subtitle);
				}
				TextView subscriptName = (TextView) vv.findViewById(R.id.subscript_name);
				if (!TextUtils.isEmpty(ti.description)) {
					subscriptName.setVisibility(View.VISIBLE);
					subscriptName.setText(ti.description);
				}
				
			}
			
			if (holder.tagContainer.getChildCount() >= 1 ) {
				holder.tagContainer.removeAllViews();
			}
			
			for (int i = 0; i < listBeans.recommendTagList.size(); i++) {
				HomeRecommendTagListModel tagModel = listBeans.recommendTagList.get(i);
				
				View item = LayoutInflater.from(UmiwiApplication.getInstance()).inflate(R.layout.item_home_recommend_tag, null);
				TextView tagTitle = (TextView) item.findViewById(R.id.tag_title);
				tagTitle.setText(tagModel.title);
				tagTitle.setTag(tagModel);
				
				if (i < listBeans.recommendTagList.size() -1) {
					Drawable drawable = UmiwiApplication.getContext().getResources().getDrawable(R.drawable.line_vertical_small);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					tagTitle.setCompoundDrawables(null, null, drawable, null);
					tagTitle.setCompoundDrawablePadding(14);
					tagTitle.setPadding(8, 8, 8, 8);
				} else {
					tagTitle.setPadding(8, 8, 24, 8);
					tagTitle.setRight(8);
				}
				
				tagTitle.setOnClickListener(tagClickListener);
				holder.tagContainer.addView(item);
			}
		}
		return convertView;
	}
	
	View.OnClickListener jiangshiClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			HomeRecommendCourseListModel hm = (HomeRecommendCourseListModel) v.getTag();
			Intent intent = new Intent(mActivity, UmiwiContainerActivity.class);
			intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LecturerDetailFragment.class);
			intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL, hm.courseUrl);
			mActivity.startActivity(intent);
		}
	};
	
	View.OnClickListener recommendClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			HomeRecommendCourseListModel recommendCourseModel = (HomeRecommendCourseListModel) v.getTag();
			Intent intent = new Intent(mActivity, UmiwiContainerActivity.class);
//			intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailLayoutFragments.class);
//			intent.putExtra(CourseDetailLayoutFragments.KEY_DETAIURL, recommendCourseModel.detailUrl);
			intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
			intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, recommendCourseModel.detailUrl);
			mActivity.startActivity(intent);
		}
	};
	
	View.OnClickListener tagClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			HomeRecommendTagListModel tagModel = (HomeRecommendTagListModel) v.getTag();
			
	 		Intent intent = new Intent(mActivity, UmiwiContainerActivity.class);
			switch (tagModel.tagType()) {
			case COURSE:
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
				intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, tagModel.detailUrl);
				break;
			case NEWCOURSE:
			case CATEGORY:
				intent.putExtra(CourseSequenceListFragment.KEY_URL, tagModel.detailUrl);
				intent.putExtra(CourseSequenceListFragment.KEY_ACTION_TITLE, tagModel.title);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseSequenceListFragment.class);
				break;
			case ZHUANTI2:
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
				intent.putExtra(JPZTDetailFragment.KEY_URL, tagModel.detailUrl);
				break;
			case ZHUANTILIST://JPZTListFragment
				intent.putExtra(JPZTListFragment.KEY_URL, tagModel.detailUrl);
				intent.putExtra(JPZTListFragment.KEY_ACTION_TITLE, tagModel.title);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTListFragment.class);
				break;
			case ZHUANTI3://大专题
				intent.putExtra(BigZTListFragment.KEY_URL, tagModel.detailUrl);
				intent.putExtra(BigZTListFragment.KEY_ACTION_TITLE, tagModel.title);
				intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, BigZTListFragment.class);
				break;

			default:
				break;
			}
			mActivity.startActivity(intent);
		}};
	
	private Holder getHolder(final View view) {
		Holder holder = (Holder) view.getTag();
		if (holder == null) {
			holder = new Holder(view);
			view.setTag(holder);
		}
		return holder;
	}

	private class Holder {

		public View topLeftView, topRightView, bottomLeftView, bottomRightView;
		public TextView typeTitle;
		public LinearLayout tagContainer;

		public Holder(View view) {
			topLeftView = view.findViewById(R.id.top_left_container);
			topRightView = view.findViewById(R.id.top_right_container);
			bottomLeftView = view.findViewById(R.id.bottom_left_container);
			bottomRightView = view.findViewById(R.id.bottom_right_container);
			
			typeTitle = (TextView) view.findViewById(R.id.type_title);
			tagContainer = (LinearLayout) view.findViewById(R.id.tag_container);
			LinearLayout title_layout = (LinearLayout) view.findViewById(R.id.title_layout);
			title_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			LinearLayout item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
			item_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
		}
	}

}
