package com.umiwi.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.main.UmiwiApplication;

import java.util.ArrayList;

import cn.youmi.framework.util.ImageLoader;

/**
 * 首页-推荐-底部轮播图adapter
 */
public class BottomLunboAdapter extends PagerAdapter {

    private Context mContext;
    public Context context;
    public ArrayList<RecommendBean.RBean.BottomBean> data;
    private BottomLunboItemClickListener bottomLunboItemClickListener;

    public BottomLunboAdapter(Context context, ArrayList<RecommendBean.RBean.BottomBean>
            arrayList) {
        this.context = context;
        this.mContext = context;
        this.data = arrayList;
    }

    public interface BottomLunboItemClickListener {
        void OnBottomLunboItemClick(View view, int position);
    }

    public void setOnBottomLunboItemClickListener(BottomLunboItemClickListener
                                                          bottomLunboItemClickListener) {
        this.bottomLunboItemClickListener = bottomLunboItemClickListener;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
        final ImageView imageView = new ImageView(context);

        final RecommendBean.RBean.BottomBean listBeans = (RecommendBean.RBean.BottomBean) getItem
                (position % data.size());


        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        mImageLoader.loadImage(listBeans.getImage(), imageView, R.drawable.image_loader_big);


        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(para);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomLunboItemClickListener.OnBottomLunboItemClick(imageView, position);
//                if ("video".equals(listBeans.getTypes())) {
//                    Log.i("youmilog", "轮播图开始播放。。。。" + listBeans.getDetailurl());
//                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
////					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,
// CourseDetailLayoutFragments.class);
////					intent.putExtra(CourseDetailLayoutFragments.KEY_DETAIURL, listBeans
// .getDetailurl());
//                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,
// CourseDetailPlayFragment.class);
//                    intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, listBeans
// .getDetailurl());
//                    mContext.startActivity(intent);
//                } else if ("article".equals(listBeans.getTypes())) {
//                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
//                    intent.putExtra(WebFragment.WEB_URL, listBeans.getDetailurl());
//                    mContext.startActivity(intent);
//                } else if ("zhuanti".equals(listBeans.getTypes())) {
//                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,
// JPZTDetailFragment.class);
//                    intent.putExtra(JPZTDetailFragment.KEY_URL, listBeans.getUrl());
//                    mContext.startActivity(intent);
//                } else if ("share".equals(listBeans.getTypes())) {
//                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,
// ShareArticleFragment.class);
//                    intent.putExtra(ShareArticleFragment.KEY_URL, listBeans.getDetailurl());
//                    mContext.startActivity(intent);
//                } else if ("tutor".equals(listBeans.getTypes())) {
//                    Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
//                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS,
// LecturerDetailFragment.class);
//                    intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL, listBeans
// .getDetailurl());
//                    mContext.startActivity(intent);
//
//                } else {
//                    ToastU.showLong(mContext, "版本不支持，请更新版本");
//                }
                MobclickAgent.onEvent(mContext, "首页VI", "轮播");

//                StatisticsManager.getInstance().getResultInfo(listBeans.getSpmurl());
                Log.e("TAG", "推荐底部轮播图");
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