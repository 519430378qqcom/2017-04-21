package com.umiwi.ui.fragment.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.TextUtils;
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
import com.umiwi.ui.fragment.WebFragment;
import com.umiwi.ui.fragment.course.CourseListFragment;
import com.umiwi.ui.fragment.down.DownloadedListFragment;
import com.umiwi.ui.fragment.mine.MyCardFragment;
import com.umiwi.ui.fragment.mine.MyCouponFragment;
import com.umiwi.ui.fragment.mine.MyFavListFragment;
import com.umiwi.ui.fragment.mine.MyMessageFragment;
import com.umiwi.ui.fragment.mine.MyNoteFragment;
import com.umiwi.ui.fragment.mine.MyOrderFragment;
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
import com.umiwi.ui.util.PermissionUtil;
import com.umiwi.ui.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.view.CircleImageView;

/**
 * 我的
 *
 * @author tangxiyong 2014-4-24下午1:34:26
 */
public class MineFragment extends BaseConstantFragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private ImageLoader imageLoader;

    private ListView mListView;
    private ArrayList<MineItem> mlist;
    private MyInfoAdapte myInfoAdapte;
    private String balance = "";
    private String integration = "";
    private String balance_str;

    /**
     * 用户头像
     */
    private CircleImageView user_photo;
    private TextView user_name;
    private TextView user_time;
    private ImageView user_grade;
    private TextView downloadImg, myCourse, myRecord, myFav;


    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mine, null);

        imageLoader = new ImageLoader(getActivity());

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("我的");
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        mActionBarToolbar.inflateMenu(R.menu.toolbar_setting);
        mActionBarToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, SettingFragment.class);
                MobclickAgent.onEvent(getActivity(), "我VI", "设置");
                getActivity().startActivity(i);
                return true;
            }
        });

        mListView = (ListView) view.findViewById(R.id.listView);

        setMineList();

        myInfoAdapte = new MyInfoAdapte();
        mListView.setAdapter(myInfoAdapte);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
                switch (position) {
                    case 0:// 个人信息
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, UserSettingFragment.class);
                        break;
//				case 1:
//					return;
                    case 2://积分
//                        i = new Intent(getActivity(), WebActivity.class);
//                        i.putExtra(WebActivity.WEB_URL, UmiwiAPI.INTEGRAL);
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                        i.putExtra(WebFragment.WEB_URL, UmiwiAPI.INTEGRAL);
                        isWebNotice = true;
                        break;
                    case 4://我的私信
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyMessageFragment.class);
                        break;
                    case 5://周报
//                        i = new Intent(getActivity(), WebActivity.class);
//                        i.putExtra(WebActivity.WEB_URL, UmiwiAPI.WEEK_REPORT);
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, WebFragment.class);
                        i.putExtra(WebFragment.WEB_URL, UmiwiAPI.WEEK_REPORT);
                        break;
                    case 7:// 我的余额
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayRechargeFragment.class);
                        break;
                    case 8:// 我的订单
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyOrderFragment.class);
                        break;
                    case 9:// 我的优惠劵
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyCouponFragment.class);
                        break;
                    case 10:// 我的笔记
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyNoteFragment.class);
                        break;
//				case 7:
//					return;
                    case 12:// 网站登录 打开扫描界面扫描条形码或二维码
                        goCaptureActivity();
                        return;
                    case 13:// 开卡/兑换课程
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyCardFragment.class);
                        break;
                    case 14:// 吐槽
                        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, FeedbackFragment.class);
                        break;
                }
                if (YoumiRoomUserManager.getInstance().isLogin()) {
                    startActivity(i);
                } else {
                    if (position < 14) {
                        showLogin();
                    } else {
                        startActivity(i);
                    }
                }
            }
        });

        YoumiRoomUserManager.getInstance().registerListener(userListener);
        NoticeManager.getInstance().registerListener(noticeListener);

        return view;
    }

    private String[] REQUEST_CAMERA_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private String[] REQUEST_STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION_CODE_TAKE_CAMERA = 6;
    private static final int REQUEST_PERMISSION_CODE_TAKE_STORAGE = 7;

    void goCaptureActivity() {
        if (!YoumiRoomUserManager.getInstance().isLogin()) {
            showLogin();
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                Snackbar.make(mListView, "需要授权使用摄像头", Snackbar.LENGTH_INDEFINITE)
                        .setAction("授权", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(getActivity(), REQUEST_CAMERA_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_CAMERA);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), REQUEST_CAMERA_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_CAMERA);
            }
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(mListView, "需要授权读取存储卡", Snackbar.LENGTH_INDEFINITE)
                        .setAction("授权", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
            }
            return;
        }
        Intent i = new Intent(getActivity(), CaptureActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE_TAKE_CAMERA:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Snackbar.make(mListView, "需要授权读取存储卡", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("授权", new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                                        }
                                    })
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), REQUEST_STORAGE_PERMISSIONS, REQUEST_PERMISSION_CODE_TAKE_STORAGE);
                        }
                        return;
                    }
                    Intent i = new Intent(getActivity(), CaptureActivity.class);
                    startActivity(i);
                } else {
                    ToastU.showShort(getActivity(), "没有权限");
                }
                break;
            case REQUEST_PERMISSION_CODE_TAKE_STORAGE:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    Intent i = new Intent(getActivity(), CaptureActivity.class);
                    startActivity(i);
                } else {
                    ToastU.showShort(getActivity(), "没有权限");
                }
                break;
        }
    }


    private void setMineList() {
        mlist = new ArrayList<MineItem>();
        mlist.clear();
        if (YoumiRoomUserManager.getInstance().isLogin() && !TextUtils.isEmpty(YoumiRoomUserManager.getInstance().getUser().getBalance())) {
            balance = String.valueOf(Float.parseFloat(YoumiRoomUserManager.getInstance().getUser().getBalance()) / 100) + " 元";
            balance_str = "我的余额：";
            integration = YoumiRoomUserManager.getInstance().getUser().getMycoin() + "积分";
        } else {
            balance_str = "我的余额";
            balance = "";
            integration = "我的积分";
        }

        mlist.add(new MineItem(R.drawable.ic_mine_integration, R.drawable.category_hot, integration, "", "观看课程得积分，积分当钱花"));
        mlist.add(new MineItem(true));
        mlist.add(new MineItem(R.drawable.mine_messages, R.drawable.category_hot, "我的私信", "", ""));
        mlist.add(new MineItem(R.drawable.ic_mine_learning_weekly, 0, "我的学习周报", "", ""));
        mlist.add(new MineItem(true));
        mlist.add(new MineItem(R.drawable.mine_money, R.drawable.category_hot, balance_str, balance, "充值"));
        mlist.add(new MineItem(R.drawable.mine_order, R.drawable.category_hot, "我的订单", "", ""));
        mlist.add(new MineItem(R.drawable.mine_coupon, R.drawable.category_hot, "我的优惠券", "", ""));
        mlist.add(new MineItem(R.drawable.mine_note, R.drawable.category_hot, "我的笔记", "", ""));
        mlist.add(new MineItem(true));

        mlist.add(new MineItem(R.drawable.mine_capture, R.drawable.category_hot, "网站登录/扫描课程", "", ""));
        mlist.add(new MineItem(R.drawable.mine_card, R.drawable.category_hot, "开卡/兑换课程", "", ""));
        mlist.add(new MineItem(R.drawable.mine_feedback, R.drawable.category_hot, "意见反馈", "", ""));
        mlist.add(new MineItem(true));
    }

    private void setMineListNotice() {
        mlist = new ArrayList<MineItem>();
        mlist.clear();

        if (YoumiRoomUserManager.getInstance().isLogin() && !TextUtils.isEmpty(YoumiRoomUserManager.getInstance().getUser().getBalance())) {
            balance = String.valueOf(Float.parseFloat(YoumiRoomUserManager.getInstance().getUser().getBalance()) / 100) + " 元";
            balance_str = "我的余额：";
            integration = YoumiRoomUserManager.getInstance().getUser().getMycoin() + "积分";
        } else {
            balance_str = "我的余额";
            balance = "";
            integration = "我的积分";
        }

        mlist.add(new MineItem(R.drawable.ic_mine_integration, R.drawable.category_hot, integration, "", "观看课程得积分，积分当钱花"));
        mlist.add(new MineItem(true));
        if (Integer.valueOf(noticeModel.getMessage()) > 0) {
            mlist.add(new MineItem(R.drawable.mine_messages, R.drawable.red_solid_round_normal, "我的私信", "", ""));
        } else {
            mlist.add(new MineItem(R.drawable.mine_messages, R.drawable.category_hot, "我的私信", "", ""));
        }
        mlist.add(new MineItem(R.drawable.ic_mine_learning_weekly, 0, "我的学习周报", "", ""));
        mlist.add(new MineItem(true));
        mlist.add(new MineItem(R.drawable.mine_money, R.drawable.category_hot, balance_str, balance, "充值"));
        mlist.add(new MineItem(R.drawable.mine_order, R.drawable.category_hot, "我的订单", "", ""));
        if (Integer.valueOf(noticeModel.getCoupon()) > 0) {
            mlist.add(new MineItem(R.drawable.mine_coupon, R.drawable.red_solid_round_normal, "我的优惠券", "", noticeModel.getCoupon() + "张优惠券将到期"));
        } else {
            mlist.add(new MineItem(R.drawable.mine_coupon, R.drawable.category_hot, "我的优惠券", "", ""));
        }
        mlist.add(new MineItem(R.drawable.mine_note, R.drawable.category_hot, "我的笔记", "", ""));
        mlist.add(new MineItem(true));

        mlist.add(new MineItem(R.drawable.mine_capture, R.drawable.category_hot, "网站登录/扫描课程", "", ""));
        mlist.add(new MineItem(R.drawable.mine_card, R.drawable.category_hot, "开卡/兑换课程", "", ""));
        mlist.add(new MineItem(R.drawable.mine_feedback, R.drawable.category_hot, "意见反馈", "", ""));
        mlist.add(new MineItem(true));
    }

    OnClickListener downloadListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
            i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, DownloadedListFragment.class);
            startActivity(i);
            MobclickAgent.onEvent(getActivity(), "我VI", "下载");
        }
    };

    OnClickListener recordListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (YoumiRoomUserManager.getInstance().isLogin()) {
                Intent intent = null;
                intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RecordFragment.class);
                startActivity(intent);
                MobclickAgent.onEvent(getActivity(), "我VI", "播放记录");
            } else {
                showLogin();
            }
        }
    };

    OnClickListener myCourseListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (YoumiRoomUserManager.getInstance().isLogin()) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(CourseListFragment.KEY_URL, UmiwiAPI.VIDEO_VIP_MYCOURSE);
                intent.putExtra(CourseListFragment.KEY_ACTION_TITLE, "我的课程");
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseListFragment.class);
                startActivity(intent);
                MobclickAgent.onEvent(getActivity(), "我VI", "我的课程");
            } else {
                showLogin();
            }
        }
    };

    OnClickListener favListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (YoumiRoomUserManager.getInstance().isLogin()) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MyFavListFragment.class);
                startActivity(intent);
                MobclickAgent.onEvent(getActivity(), "我VI", "我的收藏");
            } else {
                showLogin();
            }
        }
    };

    private void showLogin() {
        LoginUtil.getInstance().showLoginView(getActivity());
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
        YoumiRoomUserManager.getInstance().unregisterListener(userListener);
        NoticeManager.getInstance().unregisterListener(noticeListener);
    }

    public void updateShowView() {
        setMineList();
        myInfoAdapte.notifyDataSetChanged();
    }

    private class MyInfoAdapte extends BaseAdapter {

        public static final int ITEM_TYPE_USER = 0;
        public static final int ITEM_TYPE_USERGRID = 1;
        public static final int ITEM_TYPE_LIST = 2;

        @Override
        public int getCount() {
            return mlist.size() + 2;
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
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return ITEM_TYPE_USER;
            }
            if (position == 1) {
                return ITEM_TYPE_USERGRID;
            }
            return ITEM_TYPE_LIST;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            switch (getItemViewType(position)) {
                case ITEM_TYPE_USER:
                    convertView = inflater.inflate(R.layout.item_myinfo_user, null);
                    configUserItme(convertView);
                    break;
                case ITEM_TYPE_USERGRID:
                    convertView = inflater.inflate(R.layout.item_myinfo_usergrid, null);
                    configUserGridItme(convertView);
                    break;
                case ITEM_TYPE_LIST:
                    MineItem categoryItem = mlist.get(position - 2);
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
                        itemContent.setText(categoryItem.item_Content);
                    }
                    break;
            }
            return convertView;
        }

        private void configUserGridItme(View view) {
            downloadImg = (TextView) view.findViewById(R.id.download_button);
            myRecord = (TextView) view.findViewById(R.id.myrecord_button);
            myCourse = (TextView) view.findViewById(R.id.mycourse_button);
            myFav = (TextView) view.findViewById(R.id.myfav_button);

            downloadImg.setOnClickListener(downloadListener);
            myRecord.setOnClickListener(recordListener);
            myCourse.setOnClickListener(myCourseListener);
            myFav.setOnClickListener(favListener);
        }

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

    ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

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

    private ModelStatusListener<NoticeEvent, NoticeModel> noticeListener = new ModelStatusListener<NoticeEvent, NoticeModel>() {
        @Override
        public void onModelGet(NoticeEvent key, NoticeModel models) {

        }

        @Override
        public void onModelUpdate(NoticeEvent key, NoticeModel model) {
            noticeModel = model;
            switch (key) {
                case ALL:
                    setMineListNotice();
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

    NoticeModel noticeModel;
    boolean isWebNotice;

}