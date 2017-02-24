package com.umiwi.ui.fragment.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.event.NoticeEvent;
import com.umiwi.ui.fragment.OfflineActivityFragment;
import com.umiwi.ui.fragment.ShakeFragment;
import com.umiwi.ui.fragment.UserTestInfoFragment;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.search.SearchFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.NoticeModel;
import com.umiwi.ui.util.LoginUtil;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.manager.ModelManager.ModelStatusListener;

/**
 * 发现
 *
 * @author tangxiyong
 * @version 2014年6月13日 下午4:27:16
 */
public class DiscoveryFragment extends BaseConstantFragment {

//	@SuppressLint("InflateParams")
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.fragment_smarttab_toolbar, null);
//		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
//		mActionBarToolbar.setTitle(null);
//		mActionBarToolbar.inflateMenu(R.menu.toolbar_search);
//		mActionBarToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//
//					@Override
//					public boolean onMenuItemClick(MenuItem arg0) {
//						startActivity(new Intent(getActivity(), SearchActivity.class));
//						return true;
//					}
//				});
//
//		SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
//		ViewPager rootPager = (ViewPager) view.findViewById(R.id.viewpager);
//
//		Bundle winBundle = new Bundle();
//		winBundle.putString(CourseListNoToolbarFragment.KEY_URL, String.format(UmiwiAPI.WIN_OF_CHINA_URL, URLCoderUtils.URLEncoder("赢在中国")));
//
//		Bundle openBundle = new Bundle();
//		openBundle.putString(CourseListNoToolbarFragment.KEY_URL, UmiwiAPI.CHARTSLIST_PUBLIC_URL);
//
//		FragmentPagerItems pages = new FragmentPagerItems(getActivity());
//		pages.add(FragmentPagerItem.of("赢在中国", CourseListNoToolbarFragment.class, winBundle));
//		pages.add(FragmentPagerItem.of("公开课", CourseListNoToolbarFragment.class, openBundle));
//		
//		FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), pages);
//		rootPager.setAdapter(adapter);
//		viewPagerTab.setViewPager(rootPager);
//
//		return view;
//	}

    private ListView mListView;
    private ArrayList<MineItem> mlist;
    private DiscoveryAdapte mAdapte;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mine, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("发现");
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        mActionBarToolbar.inflateMenu(R.menu.toolbar_search);
        mActionBarToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SearchFragment.class);
                startActivity(i);
                return true;
            }
        });

        mListView = (ListView) view.findViewById(R.id.listView);

        mlist = new ArrayList<MineItem>();
        mlist.clear();
        mlist.add(new MineItem(true));
        mlist.add(new MineItem(R.drawable.ic_discovery_shake, R.drawable.category_hot, "摇一摇", "", ""));
        mlist.add(new MineItem(true));

        mlist.add(new MineItem(R.drawable.ic_discovery_lottery, R.drawable.category_hot, "签到", "", ""));
        mlist.add(new MineItem(R.drawable.ic_discovery_store, R.drawable.category_hot, "积分商城", "", ""));
        mlist.add(new MineItem(R.drawable.ic_discovery_offline, R.drawable.category_hot, "线下活动", "", ""));
        mlist.add(new MineItem(R.drawable.ic_discovery_usertest, R.drawable.category_hot, "发现适合我的课程", "", ""));

        mAdapte = new DiscoveryAdapte();
        mListView.setAdapter(mAdapte);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (YoumiRoomUserManager.getInstance().isLogin()) {
                    Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                    switch (position) {
                        case 1://摇一摇
                            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ShakeFragment.class);
                            break;
                        case 3://签到
                            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                            i.putExtra(WebFragment.WEB_URL, UmiwiAPI.LOTTERY);
//                            i = new Intent(getActivity(), WebActivity.class);
//                            i.putExtra(WebActivity.WEB_URL, UmiwiAPI.LOTTERY);
                            break;
                        case 4://积分商城
//                            i = new Intent(getActivity(), WebActivity.class);
//                            i.putExtra(WebActivity.WEB_URL, UmiwiAPI.INTEGRAL_SHOP);
                            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                            i.putExtra(WebFragment.WEB_URL, UmiwiAPI.INTEGRAL_SHOP);
                            isWebNotice = true;
                            break;
                        case 5://线下活动
                            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, OfflineActivityFragment.class);
                            break;
                        case 6://发现适合我的课程
                            //TODO 5-6 切换了下
                            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, UserTestInfoFragment.class);
                            i.putExtra(UserTestInfoFragment.URL_CATEGORY, UmiwiAPI.USER_TEST_COURSE);

                            break;
                    }
                    startActivity(i);
                } else {
                    if (position < 6) {
                        LoginUtil.getInstance().showLoginView(getActivity());
                    } else {
                        Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, OfflineActivityFragment.class);
                        startActivity(i);
                    }
                }

            }
        });

        NoticeManager.getInstance().registerListener(noticeListener);
        return view;
    }

    private void setListItem() {
        mlist = new ArrayList<MineItem>();
        mlist.clear();
        mlist.add(new MineItem(true));
        if (Integer.valueOf(noticeModel.getShake()) > 0) {
            mlist.add(new MineItem(R.drawable.ic_discovery_shake, R.drawable.red_solid_round_normal, "摇一摇", "", ""));
        } else {
            mlist.add(new MineItem(R.drawable.ic_discovery_shake, R.drawable.category_hot, "摇一摇", "", ""));
        }
        mlist.add(new MineItem(true));

        mlist.add(new MineItem(R.drawable.ic_discovery_lottery, R.drawable.category_hot, "签到", "", ""));
        if (Integer.valueOf(noticeModel.getCoin_goods()) > 0) {
            mlist.add(new MineItem(R.drawable.ic_discovery_store, R.drawable.red_solid_round_normal, "积分商城", "", ""));
        } else {
            mlist.add(new MineItem(R.drawable.ic_discovery_store, R.drawable.category_hot, "积分商城", "", ""));
        }

//        mlist.add(new MineItem(R.drawable.ic_discovery_usertest, R.drawable.category_hot, "发现适合我的课程", "", ""));
        mlist.add(new MineItem(R.drawable.ic_discovery_offline, R.drawable.category_hot, "线下活动", "", ""));
        if (Integer.valueOf(noticeModel.getActivity()) > 0) {
//            mlist.add(new MineItem(R.drawable.ic_discovery_offline, R.drawable.red_solid_round_normal, "线下活动", "", ""));
            mlist.add(new MineItem(R.drawable.ic_discovery_usertest, R.drawable.red_solid_round_normal, "发现适合我的课程", "", ""));
        } else {
//            mlist.add(new MineItem(R.drawable.ic_discovery_offline, R.drawable.category_hot, "线下活动", "", ""));
            mlist.add(new MineItem(R.drawable.ic_discovery_usertest, R.drawable.category_hot, "发现适合我的课程", "", ""));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isWebNotice) {
            isWebNotice = false;
            NoticeManager.getInstance().loadNotice();
        }
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
        NoticeManager.getInstance().unregisterListener(noticeListener);
    }

    private class DiscoveryAdapte extends BaseAdapter {


        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            MineItem categoryItem = mlist.get(position);
            if (categoryItem.isSeperator) {
                convertView = inflater.inflate(R.layout.item_line_mine, null);
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                convertView = inflater.inflate(R.layout.item_myinfo, null);
                ImageView icon = (ImageView) convertView.findViewById(R.id.icon_iv);
                ImageView itemImage = (ImageView) convertView.findViewById(R.id.myinfo_iv_);
                TextView itemTitle = (TextView) convertView.findViewById(R.id.myinfo_tv_title);
                TextView itemContent = (TextView) convertView.findViewById(R.id.myinfo_tv_info);
                TextView itemMoney = (TextView) convertView.findViewById(R.id.myinfo_tv_money);

                icon.setImageResource(categoryItem.iconId);
                itemImage.setImageResource(categoryItem.imageResourseId);
                itemTitle.setText(categoryItem.item_Title);
                itemMoney.setText(categoryItem.item_money);
                itemContent.setText(categoryItem.item_Content);
            }
            return convertView;
        }


    }

    private class MineItem {
        /**
         * 标识图片
         */
        public int iconId;
        /**
         * 默认图标id
         */
        public int imageResourseId;
        /**
         * 分类的标题
         */
        public String item_Title;
        /**
         * 分类的介绍
         */
        public String item_Content;
        public String item_money;
        public boolean isSeperator = false;

        /**
         * @param imageResourseId 图片资源id
         * @param item_Title      分类的标题
         * @param item_Content    分类的介绍
         */
        public MineItem(int iconId, int imageResourseId, String item_Title, String item_money, String item_Content) {
            super();
            this.iconId = iconId;
            this.imageResourseId = imageResourseId;
            this.item_Title = item_Title;
            this.item_money = item_money;
            this.item_Content = item_Content;
        }

        public MineItem(boolean seperator) {
            this.isSeperator = seperator;
        }

    }

    private ModelStatusListener<NoticeEvent, NoticeModel> noticeListener = new ModelStatusListener<NoticeEvent, NoticeModel>() {
        @Override
        public void onModelGet(NoticeEvent key, NoticeModel models) {

        }

        @Override
        public void onModelUpdate(NoticeEvent key, NoticeModel model) {
            noticeModel = model;
            switch (key) {
                case ALL:
                    setListItem();
                    mAdapte.notifyDataSetChanged();
                    break;
                case SHAKE:
                    mAdapte.notifyDataSetChanged();
                    break;
                case COIN_GOODS:
                    mAdapte.notifyDataSetChanged();
                    break;
                case ACTIVITY:
                    mAdapte.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onModelsGet(NoticeEvent key, List<NoticeModel> models) {

        }
    };

    NoticeModel noticeModel;
    boolean isWebNotice;

}
