package com.umiwi.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.UmiwiListBeans;
import com.umiwi.ui.fragment.AudioSpecialDetailFragment;
import com.umiwi.ui.fragment.ShareArticleFragment;
import com.umiwi.ui.fragment.VideoSpecialDetailFragment;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.audiolive.AudioLiveDetailsFragment;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.fragment.home.alreadyshopping.LogicalThinkingFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ColumnDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.ExperDetailsFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.StatisticsManager;

import java.util.ArrayList;

import cn.youmi.framework.util.ToastU;

public class LunboAdapter extends PagerAdapter {

    private Context mContext;
    public ArrayList<UmiwiListBeans> data;

    public LunboAdapter(Context context, ArrayList<UmiwiListBeans> arrayList) {
        this.mContext = context;
        this.data = arrayList;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size() * 400000000;
    }

    public Object getItem(int position) {
        return data.get(position % data.size());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutParams para = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final UmiwiListBeans listBeans = (UmiwiListBeans) getItem(position % data.size());

//        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
//		mImageLoader.loadImage(listBeans.getImage(), imageView);//, R.drawable.image_loader_big
//		Log.e("TAG", "listBeans.getImage()=" + listBeans.getImage());
        Glide.with(UmiwiApplication.getApplication()).load(listBeans.getImage()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setLayoutParams(para);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				Log.e("Detailurl", "轮播Detailurl="+listBeans.getDetailurl() + ",轮播Url" + listBeans.getUrl());
                if ("video".equals(listBeans.getTypes())) {
//					Log.i("youmilog","轮播图开始播放。。。。"+listBeans.getDetailurl());
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailLayoutFragments.class);
//					intent.putExtra(CourseDetailLayoutFragments.KEY_DETAIURL, listBeans.getDetailurl());
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, listBeans.getDetailurl());
                    mContext.startActivity(intent);
                } else if ("article".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                    intent.putExtra(WebFragment.WEB_URL, listBeans.getDetailurl());
                    mContext.startActivity(intent);
                } else if ("zhuanti".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, JPZTDetailFragment.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VideoSpecialDetailFragment.class);
                    intent.putExtra("detailurl", listBeans.getDetailurl());
                    mContext.startActivity(intent);
                } else if ("share".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ShareArticleFragment.class);
                    intent.putExtra(ShareArticleFragment.KEY_URL, listBeans.getDetailurl());
                    mContext.startActivity(intent);
                } else if ("tutor1".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ExperDetailsFragment.class);
                    intent.putExtra(ExperDetailsFragment.KEY_DEFAULT_TUTORUID, listBeans.getAlbumid());
                    mContext.startActivity(intent);
                } else if ("column".equals(listBeans.getTypes())) {
                    if (listBeans.isbuy()) {
                        Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                        intent.putExtra("id", listBeans.getAlbumid());
                        intent.putExtra("title", listBeans.getTitle());
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);
//					intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL, listBeans.getDetailurl());
                        intent.putExtra("columnurl", listBeans.getDetailurl());
                        mContext.startActivity(intent);
                    }
                } else if ("audio".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                    intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, listBeans.getUrl());
                    mContext.startActivity(intent);
                } else if ("asker".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                    intent.putExtra("uid", listBeans.getId());
                    mContext.startActivity(intent);

                } else if ("audiozhuanti".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioSpecialDetailFragment.class);
                    intent.putExtra("typeId", listBeans.getAlbumid());
                    mContext.startActivity(intent);
                } else if("telecast".equals(listBeans.getTypes())) {
                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AudioLiveDetailsFragment.class);
                    intent.putExtra(AudioLiveDetailsFragment.LIVEID, listBeans.getAlbumid());
                    mContext.startActivity(intent);
                }
                
                
                else {
                    ToastU.showLong(mContext, "版本不支持，请更新版本");
                }
                MobclickAgent.onEvent(mContext, "首页VI", "轮播");

                StatisticsManager.getInstance().getResultInfo(listBeans.getSpmurl());
            }
        });
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}