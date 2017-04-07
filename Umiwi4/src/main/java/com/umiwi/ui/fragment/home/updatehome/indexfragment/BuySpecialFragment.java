package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.updateadapter.BuySpecialAdapter;
import com.umiwi.ui.beans.updatebeans.BuySpecialBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.view.RefreshLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;


/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class BuySpecialFragment extends BaseConstantFragment {
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.listview)
    ListView listview;
    private int page = 1;
    private int totalpage;
    private boolean isla = false;
    private boolean isload = false;
    private ArrayList<BuySpecialBean.RBuySpecial.BuySpecialRecord> mList = new ArrayList<>();
    private BuySpecialAdapter buySpecialAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyspecial_layout, null);
        ButterKnife.inject(this,view);
        initrefreshLayout();
        buySpecialAdapter = new BuySpecialAdapter(getActivity());
        buySpecialAdapter.setData(mList);
        listview.setAdapter(buySpecialAdapter);
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
                listview.setEnabled(false);
                isla = true;
                page = 1;
//                mList.clear();
                getInfos();
            }
        });
    }

    private void getInfos() {
        String uid = YoumiRoomUserManager.getInstance().getUid();
        String url = String.format(UmiwiAPI.UMIWI_BUYSPECIAL,page);
        Log.e("TAG", "UMIWI_BUYSPECIAL=" + url + ","+uid);
        GetRequest<BuySpecialBean> request = new GetRequest<BuySpecialBean>(url, GsonParser.class, BuySpecialBean.class, new AbstractRequest.Listener<BuySpecialBean>() {
            @Override
            public void onResult(AbstractRequest<BuySpecialBean> request, BuySpecialBean buySpecialBean) {
                totalpage= buySpecialBean.getR().getPage().getTotalpage();
                ArrayList<BuySpecialBean.RBuySpecial.BuySpecialRecord> record = buySpecialBean.getR().getRecord();
                if(record!= null) {
                    mList.clear();
                    mList.addAll(record);
                    buySpecialAdapter.setData(mList);
                    if (isla) {
                        listview.setEnabled(true);
                        refreshLayout.setRefreshing(false);
                        isla = false;
                    } else if (isload) {
                        refreshLayout.setLoading(false);
                        isload = false;
                    }
                }
            }

            @Override
            public void onError(AbstractRequest<BuySpecialBean> requet, int statusCode, String body) {

            }
        });
        request.go();
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
