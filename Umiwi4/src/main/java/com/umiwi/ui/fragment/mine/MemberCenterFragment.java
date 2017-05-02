package com.umiwi.ui.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.view.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.util.ImageLoader;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class MemberCenterFragment extends BaseConstantFragment {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.iv_back1)
    ImageView ivBack1;
    @InjectView(R.id.rl_top_bar)
    LinearLayout rlTopBar;
    @InjectView(R.id.iv_big_shot_image)
    CircleImageView ivBigShotImage;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_monthly_pay)
    TextView tvMonthlyPay;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.tv_expire_time)
    TextView tvExpireTime;
    @InjectView(R.id.tv_lianxubaoyue)
    TextView tvLianxubaoyue;
    @InjectView(R.id.tv_baoyueprice)
    TextView tvBaoyueprice;
    @InjectView(R.id.tv_baoyueyuan)
    TextView tvBaoyueyuan;
    @InjectView(R.id.tv_baoyueold)
    TextView tvBaoyueold;
    @InjectView(R.id.tv_baoyuebutton)
    TextView tvBaoyuebutton;
    @InjectView(R.id.tv_onemonth)
    TextView tvOnemonth;
    @InjectView(R.id.tv_onemonthprice)
    TextView tvOnemonthprice;
    @InjectView(R.id.tv_onemonthyuan)
    TextView tvOnemonthyuan;
    @InjectView(R.id.tv_onemonthold)
    TextView tvOnemonthold;
    @InjectView(R.id.tv_onemonthbutton)
    TextView tvOnemonthbutton;
    @InjectView(R.id.tv_threemonth)
    TextView tvThreemonth;
    @InjectView(R.id.tv_threemonthprice)
    TextView tvThreemonthprice;
    @InjectView(R.id.tv_threemonthyuan)
    TextView tvThreemonthyuan;
    @InjectView(R.id.tv_threemonthold)
    TextView tvThreemonthold;
    @InjectView(R.id.tv_threemonthbutton)
    TextView tvThreemonthbutton;
    @InjectView(R.id.tv_sixmonthprice)
    TextView tvSixmonthprice;
    @InjectView(R.id.tv_sixmonthyuan)
    TextView tvSixmonthyuan;
    @InjectView(R.id.tv_sixmonthold)
    TextView tvSixmonthold;
    @InjectView(R.id.tv_sixmonthbutton)
    TextView tvSixmonthbutton;
    @InjectView(R.id.tv_sixmonth)
    TextView tvSixmonth;
    @InjectView(R.id.tv_zuanshi)
    TextView tvZuanshi;
    @InjectView(R.id.tv_zuanshiprice)
    TextView tvZuanshiprice;
    @InjectView(R.id.tv_zuanshiyuan)
    TextView tvZuanshiyuan;
    @InjectView(R.id.tv_zuanshiold)
    TextView tvZuanshiold;
    @InjectView(R.id.tv_zuanshibutton)
    TextView tvZuanshibutton;
    @InjectView(R.id.tv_huiyuan)
    TextView tvHuiyuan;
    @InjectView(R.id.tv_baoyuexieyi)
    TextView tvBaoyuexieyi;
    private ImageLoader imageLoader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_center, null);
        ButterKnife.inject(this, view);
        imageLoader = new ImageLoader(getActivity());
        getUserInfo();

        return view;
    }

    //获取用户信息
    private void getUserInfo() {
        UserModel mUser = YoumiRoomUserManager.getInstance().getUser();
        String username = mUser.getUsername();
        String useridentity = mUser.getIdentity();
        String userphoto = mUser.getAvatar();
        String usergrade = mUser.getGrade();
        String usertime = mUser.getIdentity_expire();

        tvName.setText(username);
        imageLoader.loadImage(userphoto, ivBigShotImage, R.drawable.fragment_mine_photo);
        // 加载会员有效期
        if (!TextUtils.isEmpty(usertime)) {
            tvExpireTime.setVisibility(View.VISIBLE);
            if (usertime.length() > 10) {
                tvExpireTime.setText("到期时间： " + usertime.substring(0, 10));
            } else {
                tvExpireTime.setText("到期时间： " + usertime);
            }
        } else {
            tvExpireTime.setVisibility(View.INVISIBLE);
        }
        if (!"".equals(usergrade) && !"".equals(useridentity)) {
            tvStatus.setVisibility(View.VISIBLE);

            switch (Integer.parseInt(useridentity)) {
                case 1:// 普通未续费会员
                    tvStatus.setVisibility(View.GONE);
                    break;
                case 23:// 钻石会员
//                    tvStatus.setImageResource(R.drawable.mine_user_diamond);
                    tvStatus.setText("当前身份:" + "钻石会员");
                    break;
                case 20:// 白银会员
//                    tvStatus.setImageResource(R.drawable.mine_user_silvery);
                    tvStatus.setText("当前身份:" + "白银会员");
                    break;
                case 22:// 黄金会员
//                    user_grade.setImageResource(R.drawable.mine_user_gold);
                    tvStatus.setText("当前身份:" + "黄金会员");
                    break;
                case 24:// 皇冠会员
//                    user_grade.setImageResource(R.drawable.mine_user_crown);
                    tvStatus.setText("当前身份:" + "皇冠会员");
                    break;
            }
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_monthly_pay, R.id.tv_baoyuebutton, R.id.tv_onemonthbutton, R.id.tv_threemonthbutton, R.id.tv_sixmonthbutton, R.id.tv_zuanshibutton, R.id.tv_huiyuan, R.id.tv_baoyuexieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.tv_monthly_pay:
                break;
            case R.id.tv_baoyuebutton:
                break;
            case R.id.tv_onemonthbutton:
                break;
            case R.id.tv_threemonthbutton:
                break;
            case R.id.tv_sixmonthbutton:
                break;
            case R.id.tv_zuanshibutton:
                break;
            case R.id.tv_huiyuan:

                break;
            case R.id.tv_baoyuexieyi:
                break;
        }
    }
}
