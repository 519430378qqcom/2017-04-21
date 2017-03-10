package com.umiwi.ui.fragment.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.event.NoticeEvent;
import com.umiwi.ui.fragment.MyAnswerFragment;
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.down.DownloadedListFragment;
import com.umiwi.ui.fragment.mine.MyCardFragment;
import com.umiwi.ui.fragment.mine.MyCouponFragment;
import com.umiwi.ui.fragment.mine.MyFavListFragment;
import com.umiwi.ui.fragment.mine.MyMessageFragment;
import com.umiwi.ui.fragment.mine.RecordFragment;
import com.umiwi.ui.fragment.pay.PayRechargeFragment;
import com.umiwi.ui.fragment.setting.FeedbackFragment;
import com.umiwi.ui.fragment.setting.SettingFragment;
import com.umiwi.ui.fragment.user.UserSettingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.model.NoticeModel;
import com.umiwi.ui.util.LoginUtil;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

import static com.umiwi.ui.R.id.myinfo_tv_info;
import static com.umiwi.ui.R.id.user_photo_iv_login;

/**
 * Created by Administrator on 2017/3/3.
 */
public class NewMineFragment extends BaseConstantFragment implements ActivityCompat.OnRequestPermissionsResultCallback  {

    private ImageLoader imageLoader;
    
    private ListView mListView;
    private ArrayList<MineItem> mlist;
    private NewMineFragment.MyInfoAdapte myInfoAdapte;

    private LinearLayout ll_header;
    private LinearLayout ll_header_login;

    //用户头像
    private CircleImageView user_photo;
    private TextView user_name;
    private TextView user_time;
    private TextView user_answer;
    private TextView user_study_time;
    private String balance;
    private ImageView user_grade;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mine_new, null);

        imageLoader = new ImageLoader(getActivity());
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("我的");
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        mListView = (ListView) view.findViewById(R.id.listView);
        setMineList();

        myInfoAdapte = new NewMineFragment.MyInfoAdapte();
        mListView.setAdapter(myInfoAdapte);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                switch (i){
                    case 0:
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, UserSettingFragment.class);
                        break;
                    case 2://意见
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, FeedbackFragment.class);
                        break;
                    case 4://积分
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                        intent.putExtra(WebFragment.WEB_URL, UmiwiAPI.INTEGRAL);
                        isWebNotice = true;
                        break;
                    case 5://余额
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayRechargeFragment.class);
                        break;
                    case 7://私信
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyMessageFragment.class);
                        break;
                    case 8://学习周报
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                        intent.putExtra(WebFragment.WEB_URL, UmiwiAPI.WEEK_REPORT);
                        break;
                    case 10://我答
//                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyAnswerFragment.class);
                        break;
                    case 11://下载
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, DownloadedListFragment.class);
                        break;
                    case 12://浏览记录
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RecordFragment.class);
                        break;
                    case 13://我的收藏
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyFavListFragment.class);
                        break;
                    case 15://优惠券
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyCouponFragment.class);
                        break;
                    case 16://开卡
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyCardFragment.class);
                        break;
                    case 17://设置
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SettingFragment.class);
                        break;
                }
                if (YoumiRoomUserManager.getInstance().isLogin()) {
                    startActivity(intent);
                } else {
//                    if (i <= 17) {
                        showLogin();
//                    } else {
//                        startActivity(intent);
//                    }
                }
            }
        });

        YoumiRoomUserManager.getInstance().registerListener(userListener);
        NoticeManager.getInstance().registerListener(noticeListener);

        return view;
    }

    ModelManager.ModelStatusListener<UserEvent, UserModel> userListener = new ModelManager.ModelStatusListener<UserEvent, UserModel>() {

        @Override
        public void onModelGet(UserEvent key, UserModel models) {
        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {

            switch (key) {
                case LOGGED_KICK_OUT:
                    NoticeManager.getInstance().loadNoticeLoginOut();
                    updateShowView();
                    break;
                case HOME_LOGIN:
                    NoticeManager.getInstance().loadNotice();
                    updateShowView();
                    break;
                case HOME_TESTINFO:
                    updateShowView();
                    break;
                case HOME_CHANGE_PASSWORD:
                    updateShowView();
                    break;
                case HOME_RESET_PASSWORD:
                    updateShowView();
                    break;
                case HOME_CHANGE_NAME_AND_PASSWORD:
                    updateShowView();
                    break;
                case HOME_LOGIN_OUT:
                    NoticeManager.getInstance().loadNoticeLoginOut();
                    updateShowView();
                    break;
                case HOME_USER_REGISTE_SUCC_AND_COMMIT_INFO:
                    updateShowView();
                    break;
                case PAY_MINE_RECHARGE:
                    updateShowView();
                    break;
                case PAY_MINEANDORDER_RECHARGE:
                    updateShowView();
                    break;
                case HOME_WEBVIEW:
                    updateShowView();
                    break;
                case USER_MOBILE_REGISTE_SUCC_NO_INFO:
                    updateShowView();
                    break;
                case AVATAR:
                    imageLoader.loadImage(YoumiRoomUserManager.getInstance().getUser().getAvatar(), user_photo, R.drawable.fragment_mine_photo);
                    break;
                case PAY_SUCC:
                    updateShowView();
                    NoticeManager.getInstance().loadNotice();
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {
        }
    };

    private void configUserItme(View view) {
        user_photo = (CircleImageView) view.findViewById(R.id.user_photo_iv);
        user_name = (TextView) view.findViewById(R.id.user_name_tv);
        user_time = (TextView) view.findViewById(R.id.user_time_tv);
        user_grade = (ImageView) view.findViewById(R.id.user_grade_image_iv);

        if (YoumiRoomUserManager.getInstance().isLogin()) {
            UserModel mUser = YoumiRoomUserManager.getInstance().getUser();
            String username = mUser.getUsername();
            String useridentity = mUser.getIdentity();
            String userphoto = mUser.getAvatar();
            String usergrade = mUser.getGrade();
            String usertime = mUser.getIdentity_expire();

            // 加载用户名
            user_name.setText(username);
            imageLoader.loadImage(userphoto, user_photo, R.drawable.fragment_mine_photo);
            // 加载会员有效期
            if (!TextUtils.isEmpty(usertime)) {
                user_time.setVisibility(View.VISIBLE);
                if (usertime.length() > 10) {
                    user_time.setText("会员有效期至： " + usertime.substring(0, 10));
                } else {
                    user_time.setText("会员有效期至： " + usertime);
                }
            } else {
                user_time.setVisibility(View.INVISIBLE);
            }
            if (!"".equals(usergrade) && !"".equals(useridentity)) {
                user_grade.setVisibility(View.VISIBLE);

                switch (Integer.parseInt(useridentity)) {
                    case 1:// 普通未续费会员
                        user_grade.setVisibility(View.GONE);
                        break;
                    case 23:// 钻石会员
                        user_grade.setImageResource(R.drawable.mine_user_diamond);
                        break;
                    case 20:// 白银会员
                        user_grade.setImageResource(R.drawable.mine_user_silvery);
                        break;
                    case 22:// 黄金会员
                        user_grade.setImageResource(R.drawable.mine_user_gold);
                        break;
                    case 24:// 皇冠会员
                        user_grade.setImageResource(R.drawable.mine_user_crown);
                        break;
                }
            }

        } else {
            user_photo.setImageResource(R.drawable.fragment_mine_photo);
            user_grade.setVisibility(View.GONE);
            user_name.setText(getString(R.string.home_mine_user_name));
            user_time.setText(getString(R.string.home_mine_user_time));
            user_time.setVisibility(View.VISIBLE);

        }
    }



    public void updateShowView() {
        setMineList();
        myInfoAdapte.notifyDataSetChanged();
    }

    private void showLogin() {
        LoginUtil.getInstance().showLoginView(getActivity());
    }

    private void setMineList() {
        mlist = new ArrayList<MineItem>();
        mlist.clear();
        if (YoumiRoomUserManager.getInstance().isLogin() && !TextUtils.isEmpty(YoumiRoomUserManager.getInstance().getUser().getBalance())) {
            //已登录

        } else {
           //未登录

        }
        mlist.add(new NewMineFragment.MineItem(true));
        mlist.add(new NewMineFragment.MineItem(R.drawable.feed_back, R.drawable.category_hot, "", "意见反馈", ""));
        mlist.add(new NewMineFragment.MineItem(true));
        mlist.add(new NewMineFragment.MineItem(R.drawable.integral, R.drawable.category_hot, "", "积分", ""));
        mlist.add(new NewMineFragment.MineItem(R.drawable.balance, R.drawable.category_hot, "", "我的余额", "0.0"));
        mlist.add(new NewMineFragment.MineItem(true));
        mlist.add(new NewMineFragment.MineItem(R.drawable.mine_message, R.drawable.category_hot, "", "我的私信", ""));
//        if (Integer.valueOf(noticeModel.getMessage()) > 0) {
//            mlist.add(new NewMineFragment.MineItem(R.drawable.mine_message, R.drawable.red_solid_round_normal, "", "我的私信", ""));
//        } else {
//            mlist.add(new NewMineFragment.MineItem(R.drawable.mine_message, R.drawable.category_hot, "", "我的私信", ""));
//        }
        mlist.add(new NewMineFragment.MineItem(R.drawable.study, R.drawable.category_hot, "", "我的学习周报", ""));
        mlist.add(new NewMineFragment.MineItem(true));
        mlist.add(new NewMineFragment.MineItem(R.drawable.answer, R.drawable.category_hot, "", "我答", ""));
        mlist.add(new NewMineFragment.MineItem(R.drawable.download, R.drawable.category_hot, "", "下载", ""));
        mlist.add(new NewMineFragment.MineItem(R.drawable.history, R.drawable.category_hot, "", "浏览记录", ""));
        mlist.add(new NewMineFragment.MineItem(R.drawable.favorite, R.drawable.category_hot, "", "我的收藏", ""));
        mlist.add(new NewMineFragment.MineItem(true));
        mlist.add(new NewMineFragment.MineItem(R.drawable.mine_discount, R.drawable.category_hot, "", "我的优惠券", ""));
        mlist.add(new NewMineFragment.MineItem(R.drawable.mine_card, R.drawable.category_hot, "", "开卡/兑换课程", ""));
        mlist.add(new NewMineFragment.MineItem(R.drawable.mine_setting, R.drawable.category_hot, "", "设置", ""));
        mlist.add(new NewMineFragment.MineItem(true));
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

        public MineItem() {
            super();
        }

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

    private class MyInfoAdapte extends BaseAdapter {

        public static final int ITEM_TYPE_USER = 0;
        public static final int ITEM_TYPE_LIST = 1;

        @Override
        public int getCount() {
            return mlist.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return ITEM_TYPE_USER;
            }
            return ITEM_TYPE_LIST;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            switch (getItemViewType(position)){
                case ITEM_TYPE_USER:
                    convertView = inflater.inflate(R.layout.item_myinfo_user, null);
                    configUserItme(convertView);
                    break;
                case ITEM_TYPE_LIST:
                    NewMineFragment.MineItem categoryItem = mlist.get(position-1);
                    if (categoryItem.isSeperator) {
                        convertView = inflater.inflate(R.layout.item_line_mine_new, null);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    } else {
                        convertView = inflater.inflate(R.layout.item_myinfo, null);
                        ImageView icon = (ImageView) convertView.findViewById(R.id.icon_iv);
                        ImageView itemImage = (ImageView) convertView.findViewById(R.id.myinfo_iv_);
                        TextView itemTitle = (TextView) convertView.findViewById(R.id.myinfo_tv_title);
                        TextView itemContent = (TextView) convertView.findViewById(myinfo_tv_info);
                        TextView itemMoney = (TextView) convertView.findViewById(R.id.myinfo_tv_money);
                        icon.setImageResource(categoryItem.iconId);
                        if (categoryItem.imageResourseId != 0) {
                            itemImage.setImageResource(categoryItem.imageResourseId);
                        }
                        itemTitle.setText(categoryItem.item_Title);
                        if (position == 2) {
                            itemTitle.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                        }
                        itemMoney.setText(categoryItem.item_money);
                        if (position == 9) {
                            itemContent.setTextColor(getActivity().getResources().getColor(R.color.umiwi_red));
                        }
                        if(position == 5){
                            if(balance != null && balance != ""){
                                itemContent.setText(balance);
                            }
                        }
                    }
            }
            return convertView;
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
    public void onDestroyView() {
        super.onDestroyView();
        YoumiRoomUserManager.getInstance().unregisterListener(userListener);
        NoticeManager.getInstance().unregisterListener(noticeListener);
    }

    private ModelManager.ModelStatusListener<NoticeEvent, NoticeModel> noticeListener = new ModelManager.ModelStatusListener<NoticeEvent, NoticeModel>() {
        @Override
        public void onModelGet(NoticeEvent key, NoticeModel models) {
        }
        @Override
        public void onModelUpdate(NoticeEvent key, NoticeModel model) {
            noticeModel = model;
            switch (key) {
                case ALL:
//                    setMineListNotice();
                    myInfoAdapte.notifyDataSetChanged();
                    break;
                case SHAKE:
                    myInfoAdapte.notifyDataSetChanged();
                    break;
                case COIN_GOODS:
                    myInfoAdapte.notifyDataSetChanged();
                    break;
                case ACTIVITY:
                    myInfoAdapte.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onModelsGet(NoticeEvent key, List<NoticeModel> models) {

        }
    };

    boolean isWebNotice;
    NoticeModel noticeModel;
}
