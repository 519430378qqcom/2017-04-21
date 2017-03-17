package com.umiwi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.umiwi.ui.R;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.AnsweredFragment;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.DelayAnswerFragment;
import java.util.ArrayList;
import cn.youmi.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/3/3.
 */

//我答页面
public class MyAnswerFragment extends BaseFragment {

    private TextView tab_delay_answer;
    private TextView tab_answered;
    private View line;
    public static ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private int line_width;
    private ImageView back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_answer_fragment, null);

//        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
//        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "下载");
        onPostInflateView(view);//添加view
        return view;
    }

    private void onPostInflateView(View view) {
        tab_delay_answer = (TextView) view.findViewById(R.id.tab_delay_answer);
        tab_answered = (TextView) view.findViewById(R.id.tab_answered);
        line = (View) view.findViewById(R.id.line);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        back = (ImageView) view.findViewById(R.id.back);
        tab_delay_answer.setTextColor(getResources().getColor(R.color.main_color));
        tab_answered.setTextColor(getResources().getColor(R.color.black));
        ViewPropertyAnimator.animate(tab_delay_answer).scaleX(1.1f).setDuration(0);
        ViewPropertyAnimator.animate(tab_answered).scaleY(1.1f).setDuration(0);

        fragments = new ArrayList<Fragment>();
        fragments.add(new DelayAnswerFragment());
        fragments.add(new AnsweredFragment());

        line_width = getActivity().getWindowManager().getDefaultDisplay().getWidth()
                / fragments.size();
        line.getLayoutParams().width = line_width-360;
        line.requestLayout();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new FragmentStatePagerAdapter(
                getChildFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                changeState(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                float tagerX = arg0 * line_width + arg2 / fragments.size();
                ViewPropertyAnimator.animate(line).translationX(tagerX+150)
                        .setDuration(0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        tab_delay_answer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewPager.setCurrentItem(0);
            }
        });
        tab_answered.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewPager.setCurrentItem(1);
            }
        });

    }

    private void changeState(int position) {
        if (position == 0) {
            tab_delay_answer.setTextColor(getResources().getColor(R.color.main_color));
            tab_answered.setTextColor(getResources().getColor(R.color.black));

            ViewPropertyAnimator.animate(tab_delay_answer).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tab_delay_answer).scaleY(1.1f).setDuration(200);

            ViewPropertyAnimator.animate(tab_answered).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_answered).scaleY(1.0f).setDuration(200);

        } else if (position==1){
            tab_delay_answer.setTextColor(getResources().getColor(R.color.black));
            tab_answered.setTextColor(getResources().getColor(R.color.main_color));

            ViewPropertyAnimator.animate(tab_delay_answer).scaleX(1.0f).setDuration(200);
            ViewPropertyAnimator.animate(tab_delay_answer).scaleY(1.0f).setDuration(200);

            ViewPropertyAnimator.animate(tab_answered).scaleX(1.1f).setDuration(200);
            ViewPropertyAnimator.animate(tab_answered).scaleY(1.1f).setDuration(200);
        }
    }
}
