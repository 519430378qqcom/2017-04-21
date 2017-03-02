package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;

import com.umiwi.ui.adapter.updateadapter.ExpertListAdapter;
import com.umiwi.ui.beans.LecturerBean;

import com.umiwi.ui.beans.updatebeans.CelebrityBean;
import com.umiwi.ui.fragment.LecturerDetailFragment;

import com.umiwi.ui.fragment.LecturerListFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.MyCelebrityLetterListView;
import com.umiwi.ui.view.MyLetterListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.L;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail: 行家
 */

public class ExpertFragment  extends BaseConstantFragment {


    private PopupWindow popWindow;

    private View popViewBack;

    private TextView lastnameTextView;

    private MyCelebrityLetterListView letterListView;

    private PinnedHeaderListView lecturerListView;
    private ExpertListAdapter mListAdapter;

    private Runnable dissmiss;

    public LastnameHandler mHandler;

    public static final int POP_SHOW = 1;

    public static final int POP_DISMISS = 2;

    public static final int POP_MOVE = 3;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    public static String SIGN="key.sign";
    private List<CelebrityBean.CelebrityBeanRet> letterList;

    public static class LastnameHandler extends Handler{
        WeakReference<ExpertFragment> expertFragment;

        public LastnameHandler(ExpertFragment expertFragment) {
            this.expertFragment = new WeakReference<ExpertFragment>(expertFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POP_SHOW:
                    String lastname = (String) msg.obj;
                    Integer locationY = msg.arg1;

                    expertFragment.get().showLastname(lastname, locationY);
                    break;

                case POP_DISMISS:
                    expertFragment.get().dissmissPop();
                    break;

                case POP_MOVE:
                    String lastnameStr = (String) msg.obj;
                    Integer toLocationY = msg.arg1;

                    expertFragment.get().move(lastnameStr, toLocationY);
                    break;

                default:
                    break;

            }
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_expert_layout,null);
        //expert_context_item.layout 条目
//        View view =inflater.inflate(R.layout.lecturer_list_fragment,null);
        popViewBack = inflater.inflate(R.layout.lastname_popview, null);
        lecturerListView = (PinnedHeaderListView) view.findViewById(R.id.lecturer_listview);
        letterListView = (MyCelebrityLetterListView) view
                .findViewById(R.id.letter_listview);
        lastnameTextView = (TextView) popViewBack
                .findViewById(R.id.lastname_textview);
        mHandler = new LastnameHandler(this);

        popWindow = new PopupWindow(popViewBack, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.popwindow_style);
        popWindow.dismiss();

        mLoadingFooter = new LoadingFooter(getActivity());
        lecturerListView.addFooterView(mLoadingFooter.getView());
        mListAdapter = new ExpertListAdapter(getActivity());
        lecturerListView.setAdapter(mListAdapter);

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        mScrollLoader.onLoadFirstPage();
        lecturerListView.setOnScrollListener(mScrollLoader);

        lecturerListView
                .setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int section, int position, long id) {
                        if (letterList != null && letterList.size() > 0) {
                            List<CelebrityBean.CelebrityBeanRet.ContentBean> contents = letterList.get(section).getContent();
                            if (contents != null && contents.size() > 0) {
                                String tutoruid = contents.get(position).getTutoruid();
                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ExperDetailsFragment.class);
                                intent.putExtra(ExperDetailsFragment.KEY_DEFAULT_TUTORUID,tutoruid);
                                startActivity(intent);

                            }
                        }
                    }

                    @Override
                    public void onSectionClick(AdapterView<?> adapterView,
                                               View view, int section, long id) {
                        // TODO Auto-generated method stub
                    }
                });

        dissmiss = new Runnable() {
            @Override
            public void run() {
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        };


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
        if(popWindow != null && popWindow.isShowing()) {
            popWindow.dismiss();
        }
        MobclickAgent.onPageEnd(fragmentName);
    }


    public void move(String lastnameStr, Integer toLocationY) {
        lastnameTextView.setText(lastnameStr);
        popWindow.showAtLocation(lecturerListView, Gravity.CENTER, 0, 0);

    }

    public void dissmissPop() {
        mHandler.postDelayed(dissmiss, 1000);
    }

    // 显示姓名
    public void showLastname(String lastname, Integer locationY) {
        lastnameTextView.setText(lastname);
        popWindow.showAtLocation(letterListView, Gravity.CENTER, 0, 0);
    }

   /* private AbstractRequest.Listener<CelebrityBean.RBean> listener = new AbstractRequest.Listener<CelebrityBean.RBean>() {
        @Override
        public void onResult(AbstractRequest<CelebrityBean.RBean> reques,CelebrityBean.RBean t) {


            if (t != null) {

                lecturerWapper = t.getRecord();
                mListAdapter.setLecturers(lecturerWapper);

                initLetterView(lecturerWapper);
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
            }
        }

        @Override
        public void onError(AbstractRequest<CelebrityBean.RBean> requet,
                            int statusCode, String body) {
            mLoadingFooter.setState(LoadingFooter.State.Error);
            mLoadingFooter.getView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.e("data","数据进行请求了失败了");
                    mScrollLoader.onLoadErrorPage();
                }
            });
        }
    };*/

    @Override
    public void onLoadData() {
        Log.e("data","数据进行请求了");
    /*    GetRequest<CelebrityBean.RBean> request = new GetRequest<CelebrityBean.RBean>(
                UmiwiAPI.CELEBRTYY_LIST, GsonParser.class,
                CelebrityBean.RBean.class, listener);
        HttpDispatcher.getInstance().go(request);*/
       OkHttpUtils.get().url(UmiwiAPI.CELEBRTYY_LIST).build().execute(new CustomStringCallBack() {
           @Override
           public void onFaild() {
               Log.e("data","数据进行请求失败了");
               mScrollLoader.onLoadErrorPage();

           }

           @Override
           public void onSucess(String data) {
               Log.e("data","数据进行请求成功了"+data);
               CelebrityBean celebrityBean = JsonUtil.json2Bean(data, CelebrityBean.class);
               letterList = celebrityBean.record;

               Log.e("data","data"+celebrityBean.record.size());
               Log.e("data","data"+celebrityBean.record.get(0).getPinyinname());
               Log.e("data","data"+celebrityBean.record.get(0).getContent().size());
               Log.e("data","data"+celebrityBean.record.get(0).getContent().get(0).getName());
               mListAdapter.setLecturers(letterList);
               initLetterView(letterList);
               mLoadingFooter.setState(LoadingFooter.State.TheEnd);

           }
       });

    }

    // 显示字母view
    private void initLetterView(List<CelebrityBean.CelebrityBeanRet> letterList) {
        letterListView.setData(letterList);
        letterListView.setVisibility(View.VISIBLE);
       OnTouchetterChangedListener listener = new OnTouchetterChangedListener();

        letterListView.setOnTouchingLetterChangedListener(listener);
        letterListView.setHandler(mHandler);
      }

    class OnTouchetterChangedListener implements MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(String s, String order) {
            int groundPosition = 0;
            try {
                groundPosition = Integer.parseInt(order);
            } catch (NumberFormatException e) {
                groundPosition = 0;
            }

            int position = 0;
            for (int i = 0; i < groundPosition; i++) {
                position += mListAdapter.getCountForSection(i);
            }

            position += groundPosition;

            lastnameTextView.setText(s);
            lecturerListView.setSelection(position);
        }
    }

}
