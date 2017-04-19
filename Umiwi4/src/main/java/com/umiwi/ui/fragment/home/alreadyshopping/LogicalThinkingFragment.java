package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.LogicalThinkingAdapter;
import com.umiwi.ui.beans.LogincalThinkingBean;
import com.umiwi.ui.beans.updatebeans.AttemptBean;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.util.CacheUtil;

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
    @InjectView(R.id.record)
    ImageView record1;
    private AnimationDrawable background;
    public static final String READ_ARRAY_ID = "read_array_id";
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
//                Log.e("TAG", "String.format(UmiwiAPI.MIANFEI_YUEDU, record.get(position).getId())=" +
//                        String.format(UmiwiAPI.MIANFEI_YUEDU, record.get(position).getId()));
                intent.putExtra("isTry", true);
                getActivity().startActivity(intent);


                AttemptBean.RAttenmpInfo.RecordsBean recordsBean = record.get(position);
                String readIdArray = CacheUtil.getStringFile(getActivity(), READ_ARRAY_ID);
                if(!readIdArray.contains(recordsBean.getId())) {

                    CacheUtil.putStringFile(getActivity(),READ_ARRAY_ID,readIdArray + recordsBean.getId() +",");
                    //
                    logicalThinkingAdapter.notifyDataSetChanged();
                }
            }
        });
        initMediaPlay();
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
                if(recordsBeen != null) {
//                    Log.e("TAG", "recordsBean=" + recordsBeen.toString());
                    record.clear();
                    update_count.setText(String.format("已更新%s条", recordsBeen.size()));
//                    Log.e("TAG", "recordsBeen.size()=" + recordsBeen.size());
                    record.addAll(recordsBeen);
                    logicalThinkingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(AbstractRequest<AttemptBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(UmiwiApplication.mainActivity != null) {
            if(UmiwiApplication.mainActivity.service != null) {
                background = (AnimationDrawable) record1.getBackground();
                try {
                    if (UmiwiApplication.mainActivity.service.isPlaying()) {
                        background.start();
                    } else {
                        background.stop();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 播放按钮
     */
    private void initMediaPlay() {
        record1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UmiwiApplication.mainActivity.service != null) {
                    try {

                        if (UmiwiApplication.mainActivity.service.isPlaying() || UmiwiApplication.mainActivity.isPause) {
                            if (UmiwiApplication.mainActivity.herfUrl != null) {
                                Log.e("TAG", "UmiwiApplication.mainActivity.herfUrl=" + UmiwiApplication.mainActivity.herfUrl);
                                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, VoiceDetailsFragment.class);
                                intent.putExtra(VoiceDetailsFragment.KEY_DETAILURL, UmiwiApplication.mainActivity.herfUrl);
                                getActivity().startActivity(intent);
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(getActivity(), "没有正在播放的音频", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
