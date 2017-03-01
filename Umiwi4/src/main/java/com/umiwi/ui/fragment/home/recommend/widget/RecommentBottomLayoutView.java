package com.umiwi.ui.fragment.home.recommend.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.BottomLunboAdapter;
import com.umiwi.ui.beans.updatebeans.RecommendBean;
import com.umiwi.ui.view.AutoViewPager;
import com.umiwi.ui.view.CirclePageIndicator;

import java.util.ArrayList;

import cn.youmi.framework.util.DimensionUtil;

/**
 * Created by jooper on 2017/3/1.
 */

public class RecommentBottomLayoutView extends LinearLayout {

    private RelativeLayout rl_botton_root;
    private Context mContext;
    private BottomLunboAdapter bottomLunboAdapter;
    private AutoViewPager mAutoViewPager;
    private CirclePageIndicator indicator_bottom;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_recomment_bottom, this);

        mAutoViewPager = (AutoViewPager) findViewById(R.id.vp_bottom);
        indicator_bottom = (CirclePageIndicator) findViewById(R.id.indicator_bottom);
        rl_botton_root = (RelativeLayout) findViewById(R.id.rl_botton_root);

    }

    public void setData(Activity activity, ArrayList<RecommendBean.RBean.BottomBean> bottomBeen) {

        bottomBeen.add(bottomBeen.get(0));
        if (null == bottomBeen || bottomBeen.size() <= 0)
            return;

        ViewGroup.LayoutParams para = mAutoViewPager.getLayoutParams();
        para.width = DimensionUtil.getScreenWidth(activity);
        para.height = (para.width * 300) / 640;
        mAutoViewPager.setLayoutParams(para);
        mAutoViewPager.setStopScrollWhenTouch(false);

        bottomLunboAdapter = new BottomLunboAdapter(mContext, bottomBeen);
        mAutoViewPager.setAdapter(bottomLunboAdapter);
        indicator_bottom.setViewPager(mAutoViewPager);

        mAutoViewPager.setInterval(5000);
        mAutoViewPager.setSlideBorderMode(AutoViewPager.SLIDE_BORDER_MODE_CYCLE);
        mAutoViewPager.setAnimation(new AlphaAnimation(1, (float) 0.2));
        mAutoViewPager.startAutoScroll(5000);
    }
}
