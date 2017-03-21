package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.LogicalThinkingAdapter;
import com.umiwi.ui.beans.LogincalThinkingBean;
import com.umiwi.ui.beans.updatebeans.AttemptBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;


/**
 * Created by Administrator on 2017/2/24.
 */

public class LogicalThinkingFragment extends BaseConstantFragment {
    @InjectView(R.id.orderby)
    TextView orderby;
    @InjectView(R.id.update_count)
    TextView update_count;
    @InjectView(R.id.listview)
    ListView listView;
    @InjectView(R.id.iv_back)
    ImageView iv_back;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.ll_orderby)
    LinearLayout ll_orderby;
    @InjectView(R.id.iv_sort)
    ImageView iv_sort;

    private String id;
    private LogicalThinkingAdapter logicalThinkingAdapter;
    private List<LogincalThinkingBean.RecordBean> thinkingBeanList = new ArrayList<>();

    private String orderbyId = "new";
    private ArrayList<AttemptBean.RAttenmpInfo.RecordsBean> record = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logical_thinking, null);
        ButterKnife.inject(this, view);
        id = getActivity().getIntent().getStringExtra("id");
        String intentTitile = getActivity().getIntent().getStringExtra("title");
        title.setText(intentTitile);
        logicalThinkingAdapter = new LogicalThinkingAdapter(getActivity(), record);
        listView.setAdapter(logicalThinkingAdapter);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        getdataInfo();
        changeOrder();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, String.format(UmiwiAPI.MIANFEI_YUEDU, record.get(position).getId()));
                intent.putExtra("isTry", true);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    private void changeOrder() {
        ll_orderby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderby.getText().toString().equals("正序")) {
                    orderby.setText("倒序");
                    orderbyId = "old";
                    getdataInfo();
                    rotateImpl();
                } else {
                    orderby.setText("正序");
                    orderbyId = "new";
                    getdataInfo();
                    rotateImpl();
                }
            }
        });
    }

    public void rotateImpl() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.sort_anim);
        iv_sort.startAnimation(animation);
    }

    private void getdataInfo() {
        String url = String.format(UmiwiAPI.Logincal_thinking, id, orderbyId);
        Log.e("试读页面的url", "试读页面的url=" + url);
        GetRequest<AttemptBean> request = new GetRequest<AttemptBean>(url, GsonParser.class, AttemptBean.class, new AbstractRequest.Listener<AttemptBean>() {
            @Override
            public void onResult(AbstractRequest<AttemptBean> request, AttemptBean attemptBean) {
                ArrayList<AttemptBean.RAttenmpInfo.RecordsBean> recordsBeen = attemptBean.getR().getRecord();
                Log.e("TAG", "recordsBean=" + recordsBeen.toString());
                record.clear();
                update_count.setText(String.format("已更新%s条", recordsBeen.size()));
                Log.e("TAG", "recordsBeen.size()=" + recordsBeen.size());
                record.addAll(recordsBeen);
                logicalThinkingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(AbstractRequest<AttemptBean> requet, int statusCode, String body) {

            }
        });
        request.go();
//        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
//            @Override
//            public void onFaild() {
//
//            }
//
//            @Override
//            public void onSucess(String data) {
//                thinkingBeanList.clear();
//                LogincalThinkingBean thinkingBean = JsonUtil.json2Bean(data, LogincalThinkingBean.class);
//                update_count.setText(String.format("已更新%s条", thinkingBean.getRecord().size()));
//                thinkingBeanList.addAll(thinkingBean.getRecord());
//                logicalThinkingAdapter.notifyDataSetChanged();
//            }
//        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
