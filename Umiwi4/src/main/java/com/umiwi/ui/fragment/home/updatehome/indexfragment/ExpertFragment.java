package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.umiwi.ui.fragment.home.updatehome.newtest.MyLetterListViewTwo;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_expert_layout,null);
        //expert_context_item.layout 条目
//        View view =inflater.inflate(R.layout.lecturer_list_fragment,null);

        return view;
    }


}
