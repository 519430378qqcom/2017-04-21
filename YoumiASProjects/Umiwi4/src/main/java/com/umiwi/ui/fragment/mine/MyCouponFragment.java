
package com.umiwi.ui.fragment.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.MyCouponAdapter;
import com.umiwi.ui.beans.UmiwiMyCouponBeans;
import com.umiwi.ui.beans.UmiwiResultBeans;
import com.umiwi.ui.beans.UmiwiResultBeans.ResultBeansRequestData;
import com.umiwi.ui.fragment.course.CourseListFragment;
import com.umiwi.ui.fragment.course.CourseSequenceListFragment;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.http.parsers.ListOldParser;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.http.parsers.ListResult;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.util.ToastU;
import cn.youmi.framework.view.LoadingFooter;

/**
 * 优惠券
 *
 * @author tangxiyong
 *         2013-12-6下午3:07:06
 */
public class MyCouponFragment extends BaseConstantFragment {

    private MyCouponAdapter mAdapter;

    private ListView mListView;
    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    private ArrayList<UmiwiMyCouponBeans> mList;

    public static MyCouponFragment newInstance() {
        MyCouponFragment f = new MyCouponFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frame_listview_layout, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "我的优惠券");
        mListView = (ListView) view.findViewById(R.id.listView);
        mList = new ArrayList<UmiwiMyCouponBeans>();

        mAdapter = new MyCouponAdapter(getActivity(), mList);

        mLoadingFooter = new LoadingFooter(getActivity());
//        mListView.addHeaderView(emptyView);
        mListView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);

        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mScrollLoader);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UmiwiMyCouponBeans mListBeans = (UmiwiMyCouponBeans) mAdapter.getItem(position - mListView.getHeaderViewsCount());
                switch (mListBeans.getStatusnum()){
                    case 1:
                        switch (mListBeans.getTypeNumber()) {
                            case 2: {//holder.couponType.setText("(限专辑使用)");
                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                intent.putExtra(CourseSequenceListFragment.KEY_URL, mListBeans.getDetailUrl());
                                intent.putExtra(CourseSequenceListFragment.KEY_ACTION_TITLE, "推荐课程");
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseListFragment.class);
                                startActivity(intent);
                                break;
                            }
                            case 3: {//holder.couponType.setText("(限购买会员)");
                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
                                intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, "23");
                                intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.VIP);
                                startActivity(intent);
                                break;
                            }
                            default: {//holder.couponType.setText("(请更新版本)");
                                ToastU.showLong(getActivity(), "当前版本不支持此卷，请更新到最新版本");
                                break;
                            }
                        }
                        break;
                    case 2:
                        ToastU.showLong(getActivity(), "已绑定");
                        break;
                    case 3:
                        ToastU.showLong(getActivity(), "已消费");
                        break;
                    default:

                        break;
                }

            }
        });
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub
                UmiwiMyCouponBeans mListBeans = (UmiwiMyCouponBeans) mAdapter.getItem(position - mListView.getHeaderViewsCount());
//				1;//状态未使用
//				2;//状态使用
//				3;//已经消费
//				4;//停用
                if (2 == mListBeans.getStatusnum()) {
                    closeCouponDialog(mListBeans.getCloseurl());
                }
                return false;
            }
        });
        mScrollLoader.onLoadFirstPage();

        mAdapter.setOpenCouponListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCouponDialog(v.getTag().toString());
            }
        });
        return view;
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

    /**
     * 关闭一个订单
     */
    private void closeCouponDialog(final String closeurl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("释放优惠券");
        builder.setMessage("你确定要释放优惠券并关闭订单？");
        // 设置点击按钮 “删除”
        builder.setPositiveButton("释放", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeCoupon(closeurl);
            }

        });
        // 设置点击按钮“取消”
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        AlertDialog alt = builder.create();
        alt.show();
    }


    private Listener<ResultBeansRequestData> listener = new Listener<UmiwiResultBeans.ResultBeansRequestData>() {
        @Override
        public void onResult(AbstractRequest<ResultBeansRequestData> request,
                             ResultBeansRequestData t) {
            if ("succ".equals(t.getCode())) {
//				mList.remove(close_position);//刷新界面
//				mAdapter.notifyDataSetChanged();
                mList.clear();
                mScrollLoader.onLoadFirstPage();
                mListView.invalidate();
                showMsg("优惠券释放成功");
            } else {
                showMsg(t.getMsg());
            }
        }

        @Override
        public void onError(AbstractRequest<ResultBeansRequestData> requet,
                            int statusCode, String body) {
            showMsg("网络异常,请重试");
        }
    };


    /**
     * 关闭订单
     */
    private void closeCoupon(String closeurl) {
        GetRequest<UmiwiResultBeans.ResultBeansRequestData> request =
                new GetRequest<UmiwiResultBeans.ResultBeansRequestData>(closeurl, GsonParser.class, UmiwiResultBeans.ResultBeansRequestData.class, listener);
        HttpDispatcher.getInstance().go(request);
    }


    private Listener<ListResult<UmiwiMyCouponBeans>> dataListener = new Listener<ListResult<UmiwiMyCouponBeans>>() {

        @Override
        public void onResult(AbstractRequest<ListResult<UmiwiMyCouponBeans>> request,
                             ListResult<UmiwiMyCouponBeans> t) {

            if (t == null) {// 主要用于防止服务器数据出错
                mScrollLoader.showLoadErrorView("未知错误,请重试");
                return;
            }
            if (t.isLoadsEnd()) {// 判断是否是最后一页
                mScrollLoader.setEnd(true);
            }

            if (t.isEmptyData()) {// 当前列表没有数据
                mScrollLoader.showContentView("暂无优惠卷");
                return;
            }
            mScrollLoader.setPage(t.getCurrentPage());// 用于分页请求
            mScrollLoader.setloading(false);//

            // 数据加载
            ArrayList<UmiwiMyCouponBeans> charts = t.getItems();
            mList.addAll(charts);

            if (mAdapter == null) {
                mAdapter = new MyCouponAdapter(getActivity().getApplication(), mList);
                mListView.setAdapter(mAdapter);// 解析成功 播放列表
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(AbstractRequest<ListResult<UmiwiMyCouponBeans>> requet,
                            int statusCode, String body) {
            mScrollLoader.showLoadErrorView();
        }
    };

    @Override
    public void onLoadData(int page) {
        super.onLoadData(page);
        GetRequest<ListResult<UmiwiMyCouponBeans>> request = new GetRequest<ListResult<UmiwiMyCouponBeans>>(
                String.format(UmiwiAPI.UMIWI_MY_COUPON, page), ListOldParser.class, UmiwiMyCouponBeans.class, dataListener);
        request.go();
    }
}
