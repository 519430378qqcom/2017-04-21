package com.umiwi.ui.fragment.course;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.UmiwiJPZTDetailAdapter;
import com.umiwi.ui.beans.UmiwiJPZTBetailBeans;
import com.umiwi.ui.beans.UmiwiJPZTBetailBeans.UmiwiJPZTDetailRequestData;
import com.umiwi.ui.fragment.pay.PayOrderDetailFragment;
import com.umiwi.ui.fragment.pay.PayTypeEvent;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.managers.StatisticsUrl;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.LoginUtil;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.DimensionUtil;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;

/**
 * 精品专题 内容页 专题详情
 *
 * @author tangxiyong 2013-12-19下午4:24:07
 */
public class JPZTDetailFragment extends BaseConstantFragment implements ModelStatusListener<UserEvent, UserModel> {

    public static final String KEY_URL = "key.url";

    private String detailurl;

    private UmiwiJPZTDetailAdapter mAdapter;

    private ListView mListView;
    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    private ImageView image;

    private TextView introduce;

    private String imageurl_big = null;
    private String tv_introduce = null;
    private ArrayList<UmiwiJPZTBetailBeans> mList;

    public JPZTDetailFragment newInstance(String str) {
        JPZTDetailFragment f = new JPZTDetailFragment();
        Bundle b = new Bundle();
        b.putString(KEY_URL, str);
        f.setArguments(b);
        return f;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_jpzt_list_view, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        View header = inflater.inflate(R.layout.fragment_jpzt_detail_header, null);
        parseArgument();

        mImageLoader = new ImageLoader(getActivity());
        mList = new ArrayList<UmiwiJPZTBetailBeans>();

        mListView = (ListView) view.findViewById(R.id.listView);
        buyzhuanti = (TextView) view.findViewById(R.id.buyzhuanti);
        buyprice = (TextView) view.findViewById(R.id.buyprice);
        discount_price = (TextView) view.findViewById(R.id.discount_price);
        buyLayout = (LinearLayout) view.findViewById(R.id.buyzhuanti_layout);
        buyprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mLoadingFooter = new LoadingFooter(getActivity());
        mListView.addHeaderView(header, null, false);
        mListView.addFooterView(mLoadingFooter.getView());

        image = (ImageView) header.findViewById(R.id.jpzt_iv_image);

        introduce = (TextView) header.findViewById(R.id.jpzt_tv_introduce);


        // 设置图片显示大小
        LayoutParams para = image.getLayoutParams();
        para.width = DimensionUtil.getScreenWidth(getActivity());
        para.height = (DimensionUtil.getScreenWidth(getActivity()) * 15) / 32;
        image.setLayoutParams(para);

        mAdapter = new UmiwiJPZTDetailAdapter(getActivity(), mList);
        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);

        mListView.setAdapter(mAdapter);

        mListView.setOnScrollListener(mScrollLoader);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mListView.clearChoices();
                UmiwiJPZTBetailBeans mListBeans = (UmiwiJPZTBetailBeans) mAdapter.getItem(position - mListView.getHeaderViewsCount());
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mListBeans.getDetailurl());
                startActivity(intent);
            }
        });
        mScrollLoader.onLoadFirstPage();

        buyzhuanti.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (YoumiRoomUserManager.getInstance().isLogin()) {
                    Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                    intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, PayOrderDetailFragment.class);
                    intent.putExtra(PayOrderDetailFragment.KEY_ORDER_ID, buyId);
                    intent.putExtra(PayOrderDetailFragment.KEY_ORDER_TYPE, PayTypeEvent.ZHUANTI);
                    intent.putExtra(PayOrderDetailFragment.KEY_SPM, String.format(StatisticsUrl.ORDER_JPZT_DETAIL, buyId, price));
                    startActivity(intent);
                } else {
                    LoginUtil.getInstance().showLoginView(getActivity());
                }

            }

        });
        YoumiRoomUserManager.getInstance().registerListener(this);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        YoumiRoomUserManager.getInstance().unregisterListener(this);
    }

    private String buyId;


    private void parseArgument() {
        Bundle bundle = getArguments();
        detailurl = bundle.getString(KEY_URL);
    }

    private Listener<UmiwiJPZTDetailRequestData> listener = new Listener<UmiwiJPZTBetailBeans.UmiwiJPZTDetailRequestData>() {

        @Override
        public void onResult(AbstractRequest<UmiwiJPZTDetailRequestData> request,
                             UmiwiJPZTDetailRequestData t) {
            if (t != null) {
                mScrollLoader.setEnd(true);
                imageurl_big = t.getImageBig();
                tv_introduce = t.getIntroduce();
                if (t.isIsbuy()) {//true 需要购买
                    buyLayout.setVisibility(View.VISIBLE);
                    buyprice.setText("原价：" + t.getRaw_price() + "元");
                    discount_price.setText(Html.fromHtml(
                            "<font color='#666666'>" + "优惠价：" + "</font>" +
                                    "<font color='#ff6600'>" + t.getDiscount_price() + "元" + "</font>"));
//					discount_price.setText("优惠："+t.getDiscount_price() + "元");
                    buyprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    price = t.getDiscount_price() + "";
                    buyzhuanti.setText("立即购买");
                }

                buyId = t.getSectionid();
                ArrayList<UmiwiJPZTBetailBeans> charts = t.getRecord();
                mList.addAll(charts);

                mActionBarToolbar.setTitle(t.getTitle());
                if (!isRemoving()) {
                    mImageLoader.loadImage(imageurl_big, image, R.drawable.image_loader_big);
                }

                introduce.setText(tv_introduce);
                introduce.setVisibility(View.VISIBLE);
                if (mAdapter == null) {
                    mAdapter = new UmiwiJPZTDetailAdapter(getActivity(), mList);
                    mListView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                mLoadingFooter.setState(LoadingFooter.State.TheEndHint);
                mActionBarToolbar.setTitle(t.getTitle());
            }
        }

        @Override
        public void onError(AbstractRequest<UmiwiJPZTDetailRequestData> requet,
                            int statusCode, String body) {

            mLoadingFooter.setState(LoadingFooter.State.Error);
            mLoadingFooter.getView().setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    mScrollLoader.onLoadErrorPage();
                }
            });

        }
    };

    private String price;

    private TextView buyzhuanti;//PAY_ZHUANTI

    private TextView buyprice;
    private TextView discount_price;

    private LinearLayout buyLayout;

    private ImageLoader mImageLoader;

    public void onLoadData() {
        GetRequest<UmiwiJPZTBetailBeans.UmiwiJPZTDetailRequestData> request = new GetRequest<UmiwiJPZTBetailBeans.UmiwiJPZTDetailRequestData>(
                detailurl, GsonParser.class,
                UmiwiJPZTBetailBeans.UmiwiJPZTDetailRequestData.class, listener);
        HttpDispatcher.getInstance().go(request);
    }

    @Override
    public void onModelGet(UserEvent key, UserModel models) {
    }

    @Override
    public void onModelUpdate(UserEvent key, UserModel model) {
        switch (key) {
//		case PAY_ZHUANTI:
//			buyLayout.setVisibility(View.GONE);
////			buyzhuanti.setText("购买成功");
//			break;
            case PAY_SUCC:
                mScrollLoader.onLoadFirstPage();
//			buyLayout.setVisibility(View.GONE);
//			buyzhuanti.setText("购买成功");
                break;
            default:
                break;
        }

    }

    @Override
    public void onModelsGet(UserEvent key, List<UserModel> models) {
    }

}
