package com.umiwi.ui.fragment.home;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.fragment.alreadyboughtfragment.AlreadyBoughtFragment;
import com.umiwi.ui.fragment.home.updatehome.NewHomeRecommendFragment;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;
import com.umiwi.ui.view.SmartTabLayout;

import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;


/**
 * 首页 //抽屉
 *
 * @author tangxiyong 2014-4-15下午3:14:14
 */
@SuppressLint("ValidFragment")
public class ContainerFragment extends BaseFragment {

    private ViewPager mViewPager;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_content_home, null);

        final SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            final Resources res = getActivity().getResources();

            @Override
            public View createTabView(ViewGroup container, int position,
                                      PagerAdapter adapter) {
                ImageView icon = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_icon, container, false);

                switch (position) {
                    case 0:
                        icon.setImageDrawable(res.getDrawable(R.drawable.selector_ic_bottom_home));
                        break;
                    case 1:
//                        icon.setImageDrawable(res.getDrawable(R.drawable.selector_ic_bottom_category));
                        icon.setImageDrawable(res.getDrawable(R.drawable.selector_ic_bottom_alreadybought));

                        break;
                    case 2:
                        icon.setImageDrawable(res.getDrawable(R.drawable.selector_ic_bottom_discovery));
                        break;
                    case 3:
                        icon.setImageDrawable(res.getDrawable(R.drawable.selector_ic_bottom_mine));
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return icon;
            }
        });

        FragmentPagerItems pages = new FragmentPagerItems(getActivity());
//		pages.add(FragmentPagerItem.of("首页", HomeRecommendFragment.class));
        pages.add(FragmentPagerItem.of("首页", NewHomeRecommendFragment.class));
//		pages.add(FragmentPagerItem.of("分类",StageSectionCategoryFragment.class));
        pages.add(FragmentPagerItem.of("已购", AlreadyBoughtFragment.class));
        pages.add(FragmentPagerItem.of("发现", DiscoveryFragment.class));
//        pages.add(FragmentPagerItem.of("发现", OldYoumiFragment.class));
//		pages.add(FragmentPagerItem.of("我的",MineFragment.class));
        pages.add(FragmentPagerItem.of("我的", NewMineFragment.class));

        mViewPager.setOffscreenPageLimit(4);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
        mViewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(mViewPager, new SmartTabLayout.OnTabClickCallback() {
            @Override
            public void onTabClick(int pos) {
                if (1 == pos) {
                    if (YoumiRoomUserManager.getInstance().getUser().isLogout()) {
                        LoginUtil.getInstance().showLoginView(getContext());
                        return;
                    }
                }
                viewPagerTab.setCurPage(pos);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        YoumiRoomUserManager.getInstance().registerListener(userListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        YoumiRoomUserManager.getInstance().unregisterListener(userListener);
    }

    ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

        @Override
        public void onModelGet(UserEvent key, UserModel models) {
        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {
            switch (key) {
                case HOME_LOGIN_OUT:
                    mViewPager.setCurrentItem(0);
                    break;
                case HOME_TESTINFO:
                    mViewPager.setCurrentItem(0);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {
        }
    };

}
