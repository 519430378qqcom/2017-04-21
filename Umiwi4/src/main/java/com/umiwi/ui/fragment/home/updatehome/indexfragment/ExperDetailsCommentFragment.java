package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.adapter.ExperDetailsCommentAdapter;
import com.umiwi.ui.beans.updatebeans.CommentBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.util.JsonUtil;
import com.umiwi.ui.view.NoScrollListview;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 行家详情评论
 * Created by Administrator on 2017/2/28.
 */

public class ExperDetailsCommentFragment extends BaseConstantFragment {
    @InjectView(R.id.noscroll_listview)
    NoScrollListview noscrollListview;
    @InjectView(R.id.no_data)
    TextView noData;
    private int page;
    private int totalpage;
    private boolean isBottom = false;
    private ExperDetailsCommentAdapter experDetailsCommentAdapter;
    private List<CommentBean.RecordBean> commentInfos = new ArrayList<>();
    private Handler handler;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exper_details_comment_layout, null);
        ButterKnife.inject(this, view);
        experDetailsCommentAdapter = new ExperDetailsCommentAdapter(getActivity());
        experDetailsCommentAdapter.setData(commentInfos);
        noscrollListview.setAdapter(experDetailsCommentAdapter);
        ExperDetailsFragment.setOnScrollListenerComment(new ExperDetailsFragment.OnScrollListenerComment() {
            @Override
            public void IsCommentBottom() {
                page++;
                isBottom = true;
                if (page <= totalpage) {
                    ExperDetailsFragment.tv_more.setVisibility(View.VISIBLE);
                    getInfos();
                }
            }
        });
        handler = new Handler();
        if (!TextUtils.isEmpty(ExperDetailsFragment.threadurl)){
            getInfos();
        }else {
            noData.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void getInfos() {
        String threadurl = ExperDetailsFragment.threadurl;
        if (!TextUtils.isEmpty(threadurl)) {
            String url = threadurl + "/?p=" + page;
            OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
                @Override
                public void onFaild() {
                    Log.e("data", "名人评论数据请求失败");
                }

                @Override
                public void onSucess(final String data) {
                    Log.e("data", "名人评论数据请求成功" + data);
                    if (!TextUtils.isEmpty(data)) {
                        if (isBottom == true) {
                            if (runnable == null) {
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ExperDetailsFragment.tv_more.setVisibility(View.GONE);
                                                ExperDetailsFragment.tv_more.setVisibility(View.GONE);
                                                CommentBean commentBean = JsonUtil.json2Bean(data, CommentBean.class);
                                                totalpage = commentBean.getPage().getTotalpage();
                                                List<CommentBean.RecordBean> record = commentBean.getRecord();
                                                if (record!=null&&record.size()>0){
                                                    noData.setVisibility(View.GONE);
                                                }else{
                                                    noData.setVisibility(View.VISIBLE);

                                                }
                                                commentInfos.addAll(record);
                                                experDetailsCommentAdapter.setData(commentInfos);
                                            }
                                        });
                                    }
                                };
                            }
                            handler.postDelayed(runnable, 1000);
                        } else {
                            ExperDetailsFragment.tv_more.setVisibility(View.GONE);
                            CommentBean commentBean = JsonUtil.json2Bean(data, CommentBean.class);
                            totalpage = commentBean.getPage().getTotalpage();
                            List<CommentBean.RecordBean> record = commentBean.getRecord();

                            commentInfos.addAll(record);
                            experDetailsCommentAdapter.setData(commentInfos);
                        }

                    }
                }
            });
        }


    }


    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
