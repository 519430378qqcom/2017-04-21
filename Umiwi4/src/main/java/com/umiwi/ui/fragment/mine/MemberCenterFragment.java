package com.umiwi.ui.fragment.mine;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.beans.UmiwiBuyCreateOrderBeans;
import com.umiwi.ui.beans.updatebeans.MemberCenterBean;
import com.umiwi.ui.fragment.pay.PayingFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.view.CircleImageView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ImageLoader;

import static com.umiwi.ui.main.YoumiConfiguration.context;

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
    //    @InjectView(R.id.tv_monthly_pay)
//    TextView tvMonthlyPay;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.tv_expire_time)
    TextView tvExpireTime;
    //    @InjectView(R.id.tv_lianxubaoyue)
//    TextView tvLianxubaoyue;
//    @InjectView(R.id.tv_baoyueprice)
//    TextView tvBaoyueprice;
//    @InjectView(R.id.tv_baoyueyuan)
//    TextView tvBaoyueyuan;
//    @InjectView(R.id.tv_baoyueold)
//    TextView tvBaoyueold;
//    @InjectView(R.id.tv_baoyuebutton)
//    TextView tvBaoyuebutton;
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
    //    @InjectView(R.id.tv_baoyuexieyi)
//    TextView tvBaoyuexieyi;
    private ImageLoader imageLoader;
    private MemberCenterBean.RMemberCenter.DiamondBean diamondBean;
    private MemberCenterBean.RMemberCenter.PlatinumBean platinumBean;
    private MemberCenterBean.RMemberCenter.PlatinumBean platinumBean1;
    private MemberCenterBean.RMemberCenter.PlatinumBean platinumBean2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_center, null);
        ButterKnife.inject(this, view);
        imageLoader = new ImageLoader(getActivity());
//        getUserInfo();
        getInfo();
        return view;
    }

    private void getInfo() {
        GetRequest<MemberCenterBean> request = new GetRequest<MemberCenterBean>(UmiwiAPI.UMIWI_MEMBERCENTER, GsonParser.class, MemberCenterBean.class, new AbstractRequest.Listener<MemberCenterBean>() {
            @Override
            public void onResult(AbstractRequest<MemberCenterBean> request, MemberCenterBean memberCenterBean) {
                if (memberCenterBean != null) {
                    MemberCenterBean.RMemberCenter memberCenter = memberCenterBean.getR();
                    getUserInfo(memberCenter);
                    initPrice(memberCenter);
                }
            }

            @Override
            public void onError(AbstractRequest<MemberCenterBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }

    private void initPrice(MemberCenterBean.RMemberCenter memberCenter) {
        ArrayList<MemberCenterBean.RMemberCenter.DiamondBean> diamond = memberCenter.getDiamond();
        for (int i = 0; i < diamond.size(); i++) {
            diamondBean = diamond.get(0);
        }
        //钻石会员
        tvZuanshi.setText(diamondBean.getName());
        tvZuanshiprice.setText(diamondBean.getPrice());
        if (!diamondBean.getCostprice().equals(diamondBean.getPrice())) {
            tvZuanshiold.setVisibility(View.VISIBLE);
//            bottomCourseOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            tvZuanshiold.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvZuanshiold.setText(diamondBean.getCostprice());
        } else {
            tvZuanshiold.setVisibility(View.GONE);
        }

        ArrayList<MemberCenterBean.RMemberCenter.PlatinumBean> platinum = memberCenter.getPlatinum();
        for (int i=0;i < platinum.size(); i++){
            platinumBean = platinum.get(0);
            platinumBean1 = platinum.get(1);
            platinumBean2 = platinum.get(2);
        }
        //一个月会员
        tvOnemonth.setText(platinumBean.getName());
        tvOnemonthprice.setText(platinumBean.getPrice());
        if (!platinumBean.getCostprice().equals(platinumBean.getPrice()) ) {
            tvOnemonthold.setVisibility(View.VISIBLE);
//            bottomCourseOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            tvOnemonthold.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvOnemonthold.setText(platinumBean.getCostprice());
        } else {
            tvOnemonthold.setVisibility(View.GONE);
        }
        //三个月会员
        tvThreemonth.setText(platinumBean1.getName());
        tvThreemonthprice.setText(platinumBean1.getPrice());
        if (!platinumBean1.getCostprice().equals(platinumBean1.getPrice())) {
            tvThreemonthold.setVisibility(View.VISIBLE);
//            bottomCourseOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            tvThreemonthold.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvThreemonthold.setText(platinumBean1.getCostprice());
        } else {
            tvThreemonthold.setVisibility(View.GONE);
        }
        //一年会员
        tvSixmonth.setText(platinumBean2.getName());
        tvSixmonthprice.setText(platinumBean2.getPrice());
        if (!platinumBean2.getCostprice().equals(platinumBean2.getPrice())) {
            tvSixmonthold.setVisibility(View.VISIBLE);
//            bottomCourseOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
            tvSixmonthold.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            tvSixmonthold.setText(platinumBean2.getCostprice());
        } else {
            tvSixmonthold.setVisibility(View.GONE);
        }
    }

    //获取用户信息
    private void getUserInfo(MemberCenterBean.RMemberCenter memberCenter) {

        tvName.setText(memberCenter.getUsername());
        Glide.with(context).load(memberCenter.getAvatar()).into(ivBigShotImage);
        // 加载会员有效期
        if (!TextUtils.isEmpty(memberCenter.getExpire())) {
            tvExpireTime.setVisibility(View.VISIBLE);
            if (memberCenter.getExpire().length() > 10) {
                tvExpireTime.setText("到期时间:" + memberCenter.getExpire().substring(0, 10));
            } else {
                tvExpireTime.setText("到期时间:" + memberCenter.getExpire());
            }
        } else {
            tvExpireTime.setVisibility(View.INVISIBLE);
        }
        if (!"".equals(memberCenter.getIdentity())) {
            tvStatus.setVisibility(View.VISIBLE);

            switch (Integer.parseInt(memberCenter.getIdentity())) {
                case 1:// 普通未续费会员
                    tvStatus.setVisibility(View.GONE);
                    break;
                case 23:// 钻石会员
//                    tvStatus.setImageResource(R.drawable.mine_user_diamond);
                    tvStatus.setText("当前身份:" + "钻石会员");
                    tvOnemonthbutton.setBackgroundResource(R.drawable.gray_probation_bg);
                    tvOnemonthbutton.setClickable(false);
                    tvThreemonthbutton.setBackgroundResource(R.drawable.gray_probation_bg);
                    tvThreemonthbutton.setClickable(false);
                    tvSixmonthbutton.setBackgroundResource(R.drawable.gray_probation_bg);
                    tvSixmonthbutton.setClickable(false);
                    break;
                case 26:// 白金会员
//                    tvStatus.setImageResource(R.drawable.mine_user_silvery);
                    tvStatus.setText("当前身份:" + "白金会员");
                    break;
            }
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_onemonthbutton, R.id.tv_threemonthbutton, R.id.tv_sixmonthbutton, R.id.tv_zuanshibutton, R.id.tv_huiyuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.tv_onemonthbutton:
                String buyurl1 = platinumBean.getBuyurl();
                getPayUrl(buyurl1);
                break;
            case R.id.tv_threemonthbutton:
                String buyurl2 = platinumBean1.getBuyurl();
                getPayUrl(buyurl2);
                break;
            case R.id.tv_sixmonthbutton:
                String buyurl3 = platinumBean2.getBuyurl();
                getPayUrl(buyurl3);
                break;
            case R.id.tv_zuanshibutton:
                String buyurl = diamondBean.getBuyurl();
                getPayUrl(buyurl);
                break;
            case R.id.tv_huiyuan:
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, MemberAgreementFragment.class);
                startActivity(intent);
                break;
        }
    }

    private void getPayUrl(String buyurl) {
        GetRequest<UmiwiBuyCreateOrderBeans> request = new GetRequest<UmiwiBuyCreateOrderBeans>(
                buyurl, GsonParser.class,
                UmiwiBuyCreateOrderBeans.class,
                addListener);
        request.go();
    }
    private AbstractRequest.Listener<UmiwiBuyCreateOrderBeans> addListener = new AbstractRequest.Listener<UmiwiBuyCreateOrderBeans>() {
        @Override
        public void onResult(AbstractRequest<UmiwiBuyCreateOrderBeans> request, UmiwiBuyCreateOrderBeans umiwiBuyCreateOrderBeans) {
            String payurl = umiwiBuyCreateOrderBeans.getR().getPayurl();
            goToPayFragment(payurl);
        }

        @Override
        public void onError(AbstractRequest<UmiwiBuyCreateOrderBeans> requet, int statusCode, String body) {

        }
    };

    private void goToPayFragment(String payurl) {
        Intent i = new Intent(getActivity(), UmiwiContainerActivity.class);
        i.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayingFragment.class);
        i.putExtra(PayingFragment.KEY_PAY_URL, payurl);
        startActivity(i);
    }
}
