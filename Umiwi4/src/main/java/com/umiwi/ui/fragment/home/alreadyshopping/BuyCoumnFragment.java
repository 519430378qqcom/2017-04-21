package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.BuyColumnAdapter;
import com.umiwi.ui.beans.updatebeans.AlreadShopColumnBean;
import com.umiwi.ui.beans.updatebeans.BuyCoumnBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.view.LoadingFooter;

/**
 * 类描述：已购-专栏
 * Created by Gpsi on 2017-03-13.
 */

public class BuyCoumnFragment extends BaseConstantFragment {

    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<AlreadShopColumnBean.RalreadyColumn.RecordColumn> mList;
    private BuyColumnAdapter buyColumnAdapter;
    private int page = 1;
    private int totalpage;
    private boolean isla = false;
    private boolean isload = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alread_column_layout, null);
        mList = new ArrayList<>();



        ButterKnife.inject(this, view);
        buyColumnAdapter = new BuyColumnAdapter(getActivity());
        buyColumnAdapter.setData(mList);
        listview.setAdapter(buyColumnAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
//                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnDetailsFragment.class);//详情页
//                intent.putExtra("columnurl", mList.get(i).getColumnurl());
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LogicalThinkingFragment.class);
                intent.putExtra("id", mList.get(i).getId());
                intent.putExtra("title",mList.get(i).getTitle());
                startActivity(intent);
            }
        });
        initrefreshLayout();
        getInfos();
        return view;
    }

    private void initrefreshLayout() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.main_color));
        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                isload = true;
                page++;
                if (page <= totalpage) {
                    refreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getInfos();
                        }
                    }, 1000);
                } else {
                    refreshLayout.setLoading(false);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isla = true;
                page = 1;
                mList.clear();
                getInfos();
            }
        });
    }

    private void getInfos() {
        String uid = YoumiRoomUserManager.getInstance().getUid();
        String url = String.format(UmiwiAPI.ALREADY_COLUMN, page, uid);
        GetRequest<AlreadShopColumnBean> req = new GetRequest<AlreadShopColumnBean>(url, GsonParser.class, AlreadShopColumnBean.class, new AbstractRequest.Listener<AlreadShopColumnBean>() {
            @Override
            public void onResult(AbstractRequest<AlreadShopColumnBean> request, AlreadShopColumnBean alreadyVideoBean) {
                AlreadShopColumnBean.RalreadyColumn infos = alreadyVideoBean.getR();
                AlreadShopColumnBean.RalreadyColumn.PageBean page = infos.getPage();
                totalpage = page.getTotalpage();
                ArrayList<AlreadShopColumnBean.RalreadyColumn.RecordColumn> record = infos.getRecord();
                mList.addAll(record);
                buyColumnAdapter.setData(mList);
                if (isla) {
                    refreshLayout.setRefreshing(false);
                     isla = false;
                } else if (isload) {
                    refreshLayout.setLoading(false);
                    isload = false;
                }

            }

            @Override
            public void onError(AbstractRequest<AlreadShopColumnBean> requet, int statusCode, String body) {

            }
        });

        req.go();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
