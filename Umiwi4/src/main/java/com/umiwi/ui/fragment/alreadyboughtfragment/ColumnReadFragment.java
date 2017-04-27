package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.ColumnMessageAdapter;
import com.umiwi.ui.adapter.updateadapter.ColumnReadDetailsAdapter;
import com.umiwi.ui.beans.AudioTmessageListBeans;
import com.umiwi.ui.beans.updatebeans.ColumnReadBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ColumnReadFragment extends BaseConstantFragment implements View.OnClickListener {

    ImageView iv_back;
    TextView tab_title;
    ImageView iv_shared;
    ImageView record;
    View rl_background;


    TextView title;
    ImageView iv_play;
    @InjectView(R.id.tv_play_title)
    TextView tv_play_title;
    @InjectView(R.id.tv_play_time)
    TextView tv_play_time;

    @InjectView(R.id.nsl_content)
    NoScrollListview nsl_content;

    @InjectView(R.id.ll_leave_word)
    LinearLayout ll_leave_word;
    @InjectView(R.id.iv_leaveword)
    ImageView iv_leaveword;

    @InjectView(R.id.nsl_message_list)
    ListView nsl_message_list;

    @InjectView(R.id.rl_buy_column)
    RelativeLayout rl_buy_column;
    @InjectView(R.id.tv_buy_column)
    TextView tv_buy_column;



    //顶部悬浮框
    @InjectView(R.id.ll_leave_word1)
    LinearLayout ll_leave_word1;
    @InjectView(R.id.iv_leaveword1)
    ImageView iv_leaveword1;

    public static final String DETAIL_ID = "id";
    private String id;
    private ColumnReadBean.RColumnRead details;
    private int page = 1;
    private int totalpage;
    private ColumnMessageAdapter columnMessageAdapter;
    private ArrayList<AudioTmessageListBeans.RecordX.Record> mList = new ArrayList<>();
    private AnimationDrawable background;
    private int height;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_column_read, null);
//        ButterKnife.inject(this,view);
        id = getActivity().getIntent().getStringExtra(DETAIL_ID);


        initView(view);
        getDetailsData();
        getMessageList();
        initMediaPlay();
        View inflate = View.inflate(getActivity(), R.layout.column_header_view, null);
        nsl_message_list.addHeaderView(inflate);

        iv_back.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        record.setOnClickListener(this);
        iv_leaveword.setOnClickListener(this);
        tv_buy_column.setOnClickListener(this);
        iv_play.setOnClickListener(this);




        return view;
    }

    private void initView(View view) {
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tab_title = (TextView) view.findViewById(R.id.tab_title);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        record = (ImageView) view.findViewById(R.id.record);
        rl_background = view.findViewById(R.id.rl_background);
        title = (TextView) view.findViewById(R.id.title);
        iv_play = (ImageView) view.findViewById(R.id.iv_play);
    }



    private void initMediaPlay() {
        if (UmiwiApplication.mainActivity != null) {
            if (UmiwiApplication.mainActivity.service != null) {
                background = (AnimationDrawable) record.getBackground();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back :
                getActivity().finish();
                break;
            case R.id.iv_shared :
                ShareDialog.getInstance().showDialog(getActivity(), details.getShare().getSharetitle(),
                        details.getShare().getSharecontent(), details.getShare().getShareurl(), details.getShare().getShareimg());
                break;
            case R.id.record :
                initPlay();
                break;
            case R.id.iv_leaveword :
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnLeaveMessageFragment.class);
                intent.putExtra("aid",details.getId());
                startActivity(intent);
                break;
            case R.id.tv_buy_column :

                break;
            case R.id.iv_play:

                break;
        }
    }

    private void initPlay() {
        if (UmiwiApplication.mainActivity.service != null) {
            try {

                if (UmiwiApplication.mainActivity.service.isPlaying() || UmiwiApplication.mainActivity.isPause) {
                    if (UmiwiApplication.mainActivity.herfUrl != null) {
//                        Log.e("TAG", "UmiwiApplication.mainActivity.herfUrl=" + UmiwiApplication.mainActivity.herfUrl);
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
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void getMessageList() {
        String url = String.format(UmiwiAPI.UMIWI_COLUM_MESSAGE,id,page);
        Log.e("TAG", "阅读界面url=" + url);
        GetRequest<AudioTmessageListBeans> request = new GetRequest<AudioTmessageListBeans>(url, GsonParser.class, AudioTmessageListBeans.class, new AbstractRequest.Listener<AudioTmessageListBeans>() {
            @Override
            public void onResult(AbstractRequest<AudioTmessageListBeans> request, AudioTmessageListBeans audioTmessageListBeans) {
                totalpage = audioTmessageListBeans.getR().getPage().getTotalpage();
                ArrayList<AudioTmessageListBeans.RecordX.Record> record = audioTmessageListBeans.getR().getRecord();
//                Log.e("TAG", "阅读界面record=" + record.toString());
                mList.clear();
                mList.addAll(record);
                columnMessageAdapter = new ColumnMessageAdapter(getActivity(),mList);
                nsl_message_list.setFocusable(false);
                nsl_message_list.setAdapter(columnMessageAdapter);
            }

            @Override
            public void onError(AbstractRequest<AudioTmessageListBeans> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    //获取页面数据
    private void getDetailsData() {
        String url = String.format(UmiwiAPI.UMIWI_COLUMN_READ,id);
        GetRequest<ColumnReadBean> request = new GetRequest<ColumnReadBean>(url, GsonParser.class, ColumnReadBean.class, new AbstractRequest.Listener<ColumnReadBean>() {
            @Override
            public void onResult(AbstractRequest<ColumnReadBean> request, ColumnReadBean columnReadBean) {
                details = columnReadBean.getR();
                tab_title.setText(details.getTcolumntitle());
                tab_title.setTextColor(Color.BLACK);
                tab_title.setVisibility(View.VISIBLE);

                title.setText(details.getTitle());
                tv_play_title.setText(details.getAudiofile().getAtitle());
                tv_play_time.setText(details.getAudiofile().getAplaytime());

                ArrayList<ColumnReadBean.RColumnRead.ReadContentWord> content = details.getContent();
                nsl_content.setFocusable(false);
                nsl_content.setAdapter(new ColumnReadDetailsAdapter(getActivity(), content));


            }

            @Override
            public void onError(AbstractRequest<ColumnReadBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }


}
