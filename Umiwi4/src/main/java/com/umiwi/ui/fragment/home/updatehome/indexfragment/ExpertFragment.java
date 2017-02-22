package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
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
import com.umiwi.ui.adapter.LecturerListAdapter;
import com.umiwi.ui.beans.LecturerBean;
import com.umiwi.ui.fragment.LecturerDetailFragment;
import com.umiwi.ui.fragment.home.updatehome.newtext.MyLetterListViewTwo;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;
import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;

/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail: 行家
 */

public class ExpertFragment  extends BaseConstantFragment {
 /*   @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_expert_layout,null);
//        View view =inflater.inflate(R.layout.lecturer_list_fragment,null);

        return view;
    }*/
    private PopupWindow popWindow;

    private View popViewBack;

    private TextView lastnameTextView;

    private MyLetterListViewTwo letterListView;

    private PinnedHeaderListView lecturerListView;
    private LecturerListAdapter mListAdapter;

    private Runnable dissmiss;
    private ArrayList<LecturerBean.LecturerBeanWapper> lecturerWapper;

    private ExpertFragment.LastnameHandler mHandler;

    public static final int POP_SHOW = 1;

    public static final int POP_DISMISS = 2;

    public static final int POP_MOVE = 3;

    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    public static String SIGN="key.sign";
    public static class LastnameHandler extends Handler {

        WeakReference<ExpertFragment> lecturerListFragment;

        public LastnameHandler(ExpertFragment lecturerListFragment) {
            this.lecturerListFragment = new WeakReference<ExpertFragment>(
                    lecturerListFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POP_SHOW:
                    String lastname = (String) msg.obj;
                    Integer locationY = msg.arg1;

                    lecturerListFragment.get().showLastname(lastname, locationY);
                    break;

                case POP_DISMISS:
                    lecturerListFragment.get().dissmissPop();
                    break;

                case POP_MOVE:
                    String lastnameStr = (String) msg.obj;
                    Integer toLocationY = msg.arg1;

                    lecturerListFragment.get().move(lastnameStr, toLocationY);
                    break;

                default:
                    break;

            }
        }

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.gpsi_lecturer_list_fragment, null);

//        mActionBarToolbar = (Toolbar) contentView.findViewById(R.id.toolbar_actionbar);
//        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "名人大佬");
        popViewBack = inflater.inflate(R.layout.lastname_popview, null);
        lecturerListView = (PinnedHeaderListView) contentView.findViewById(R.id.lecturer_listview);
        letterListView = (MyLetterListViewTwo) contentView
                .findViewById(R.id.letter_listview);
        lastnameTextView = (TextView) popViewBack
                .findViewById(R.id.lastname_textview);

        mHandler = new ExpertFragment.LastnameHandler(this);

        popWindow = new PopupWindow(popViewBack, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.popwindow_style);
        popWindow.dismiss();

        mLoadingFooter = new LoadingFooter(getActivity());
        lecturerListView.addFooterView(mLoadingFooter.getView());

        mListAdapter = new LecturerListAdapter(getActivity());

        lecturerListView.setAdapter(mListAdapter);

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        mScrollLoader.onLoadFirstPage();
        lecturerListView.setOnScrollListener(mScrollLoader);

        lecturerListView
                .setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int section, int position, long id) {
                        if (lecturerWapper != null && lecturerWapper.size() > 0) {
                            LecturerBean.LecturerBeanWapper wapper = lecturerWapper
                                    .get(section);
                            ArrayList<LecturerBean> lecturers = wapper
                                    .getLecturers();
                            if (lecturers != null && lecturers.size() > 0) {
                                LecturerBean lecturer = lecturers.get(position);

                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                //讲师详细页面
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, LecturerDetailFragment.class);
                                intent.putExtra(LecturerDetailFragment.KEY_DEFAULT_DETAILURL,lecturer.getCourseurl());
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

        return contentView;
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

    private AbstractRequest.Listener<LecturerBean.LecturerBeanRequestData> listener = new AbstractRequest.Listener<LecturerBean.LecturerBeanRequestData>() {

        @Override
        public void onResult(AbstractRequest<LecturerBean.LecturerBeanRequestData> request,
                             LecturerBean.LecturerBeanRequestData t) {
            if (t != null) {
                lecturerWapper = t.getRecord();
                mListAdapter.setLecturers(lecturerWapper);

                initLetterView(lecturerWapper);
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
            }
        }

        @Override
        public void onError(AbstractRequest<LecturerBean.LecturerBeanRequestData> requet,
                            int statusCode, String body) {
            mLoadingFooter.setState(LoadingFooter.State.Error);
            mLoadingFooter.getView().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    mScrollLoader.onLoadErrorPage();
                }
            });
        }
    };

    @Override
    public void onLoadData() {
        GetRequest<LecturerBean.LecturerBeanRequestData> request = new GetRequest<LecturerBean.LecturerBeanRequestData>(
                UmiwiAPI.LECTURER_LIST, GsonParser.class,
                LecturerBean.LecturerBeanRequestData.class, listener);
        HttpDispatcher.getInstance().go(request);
    }

    // 显示字母view
    private void initLetterView(ArrayList<LecturerBean.LecturerBeanWapper> lecturerWapper) {
        letterListView.setData(lecturerWapper);
        letterListView.setVisibility(View.VISIBLE);

        ExpertFragment.OnTouchetterChangedListener listener = new ExpertFragment.OnTouchetterChangedListener();

        letterListView.setOnTouchingLetterChangedListener(listener);
        letterListView.setHandler(mHandler);
    }

    class OnTouchetterChangedListener implements
            MyLetterListViewTwo.OnTouchingLetterChangedListener {
        @Override
        public void onTouchingLetterChanged(String s, String order) {
            if (lecturerListView != null) {

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


}
