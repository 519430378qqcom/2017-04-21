package com.umiwi.ui.fragment.home.recommend.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.BottomLunboAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.HotListFragment;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.AutoViewPager;
import com.umiwi.ui.view.CirclePageIndicator;

import java.util.ArrayList;

import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;

/**
 * Created by jooper on 2017/3/1.
 */

public class RecommentBottomLayoutView extends LinearLayout implements BottomLunboAdapter
        .BottomLunboItemClickListener {

    private RelativeLayout rl_botton_root;
    private Context mContext;
    private BottomLunboAdapter bottomLunboAdapter;
    private ImageView mAutoViewPager;
//    private CirclePageIndicator indicator_bottom;

    public RecommentBottomLayoutView(Context context) {
        super(context);
        init(context);
    }

    public RecommentBottomLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_recomment_bottom, this);

        mAutoViewPager = (ImageView) findViewById(R.id.vp_bottom);
//        indicator_bottom = (CirclePageIndicator) findViewById(R.id.indicator_bottom);
        rl_botton_root = (RelativeLayout) findViewById(R.id.rl_botton_root);

    }

    public void setData(Activity activity, ArrayList<RecommendBean.RBean.BottomBean> bottomBeen) {

        if (null == bottomBeen || bottomBeen.size() <= 0)
            return;

        ImageLoader mImageLoader = new ImageLoader(UmiwiApplication.getApplication());
        for (int i = 0; i < bottomBeen.size(); i++) {
            mImageLoader.loadImage(bottomBeen.get(i).getImage(), mAutoViewPager, R.drawable.image_loader_big);
        }

//        ViewGroup.LayoutParams para = mAutoViewPager.getLayoutParams();
//        para.width = DimensionUtil.getScreenWidth(activity);
//        para.height = (para.width * 300) / 640;
//        mAutoViewPager.setLayoutParams(para);
//        mAutoViewPager.setStopScrollWhenTouch(false);
//
//        bottomLunboAdapter = new BottomLunboAdapter(mContext, bottomBeen);
//        mAutoViewPager.setAdapter(bottomLunboAdapter);
//        bottomLunboAdapter.setOnBottomLunboItemClickListener(this);
////        indicator_bottom.setViewPager(mAutoViewPager);
//
//        mAutoViewPager.setInterval(5000);
//        mAutoViewPager.setSlideBorderMode(AutoViewPager.SLIDE_BORDER_MODE_CYCLE);
//        mAutoViewPager.setAnimation(new AlphaAnimation(1, (float) 0.2));
//        mAutoViewPager.startAutoScroll(5000);

        mAutoViewPager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, HotListFragment.class);
//        intent.putExtra("id",experDetailsAlbumbean.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void OnBottomLunboItemClick(View view, int position) {
        Intent intent = new Intent(mContext, UmiwiContainerActivity.class);
        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, HotListFragment.class);
//        intent.putExtra("id",experDetailsAlbumbean.getId());
        mContext.startActivity(intent);
    }
}
