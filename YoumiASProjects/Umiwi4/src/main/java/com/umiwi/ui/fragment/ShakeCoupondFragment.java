package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.MineShakeCouponAdapter;
import com.umiwi.ui.beans.MineShakeCouponBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.view.LoadingFooter;

/**
 * @author tjie00
 * @version 2014年9月11日 下午2:17:59
 *          <p/>
 *          我摇到的页面
 */
public class ShakeCoupondFragment extends BaseConstantFragment {

    private ListView mListView;

    private MineShakeCouponAdapter mAdapter;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "我摇到的");

        mListView = (ListView) view.findViewById(R.id.listView);
        mAdapter = new MineShakeCouponAdapter();

        mLoadingFooter = new LoadingFooter(getActivity());// 加载更多的view
        mListView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);// 初始化加载更多监听

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(mScrollLoader);// 添加加载更多到listveiw

        mScrollLoader.onLoadFirstPage();// 初始化接口，加载第一页

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MineShakeCouponBean coupon = (MineShakeCouponBean) mAdapter.getItem(position);
                switch (Integer.parseInt(coupon.getType())) {
                    case MineShakeCouponBean.TYPE_COUPON:
                        ToastU.showShort(getActivity(), "优惠券购买时使用");
                        break;
                    case MineShakeCouponBean.TYPE_HUODONG:
                        ToastU.showShort(getActivity(), "若未领取请联系客服");
                        break;
                    case MineShakeCouponBean.TYPE_GIFT:
                        ToastU.showShort(getActivity(), "当前版本过低，请升级最新版本");
                        break;
                    case MineShakeCouponBean.TYPE_BOOK:
                        ToastU.showShort(getActivity(), "若未领取请联系客服");
                        break;
                    case MineShakeCouponBean.TYPE_LEAKS:
                        ToastU.showShort(getActivity(), "当前版本过低，请升级最新版本");
                        break;
                    case MineShakeCouponBean.TYPE_COURSE:
                    case MineShakeCouponBean.TYPE_COURSEFREE:
                        Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                        intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                        intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, coupon.getDetailurl());
                        startActivity(intent);
                        if (!"1".equalsIgnoreCase(coupon.getStatus())) {
                            ToastU.showShort(getActivity(), "课程已过期，请重新购买!");
                        }
                        break;
                    default:
                        ToastU.showShort(getActivity(), "当前版本过低，请升级最新版本");
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        GetRequest<MineShakeCouponBean> mineCouponRequest = new GetRequest<MineShakeCouponBean>(
                UmiwiAPI.MINE_SHAKE_COUPONS, GsonParser.class,
                MineShakeCouponBean.class, mineCouponListener);

        HttpDispatcher.getInstance().go(mineCouponRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    private Listener<MineShakeCouponBean> mineCouponListener = new Listener<MineShakeCouponBean>() {

        @Override
        public void onResult(AbstractRequest<MineShakeCouponBean> request,
                             MineShakeCouponBean t) {
            if (t == null) {// 主要用于防止服务器数据出错
                mScrollLoader.showLoadErrorView("未知错误,请重试");
                return;
            }

            final ArrayList<MineShakeCouponBean> coupons = t.getRecord();
            mAdapter.setCoupons(coupons);
            mScrollLoader.setEnd(true);
        }

        @Override
        public void onError(AbstractRequest<MineShakeCouponBean> requet,
                            int statusCode, String body) {
            mScrollLoader.showLoadErrorView();
        }
    };

}
