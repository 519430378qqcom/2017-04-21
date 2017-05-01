package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.updateadapter.ColumnMessageAdapter;
import com.umiwi.ui.adapter.updateadapter.ColumnReadDetailsAdapter;
import com.umiwi.ui.beans.AudioTmessageListBeans;
import com.umiwi.ui.beans.updatebeans.AudioResourceBean;
import com.umiwi.ui.beans.updatebeans.ColumnReadBean;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.VoiceDetailsFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.view.NoScrollListview;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.ToastU;

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
    TextView tv_play_title;
    TextView tv_play_time;

    NoScrollListview nsl_content;

    LinearLayout ll_leave_word;
    ImageView iv_leaveword;

    ListView nsl_message_list;

    RelativeLayout rl_buy_column;
    TextView tv_buy_column;

    //顶部悬浮框
    LinearLayout ll_leave_word1;
    ImageView iv_leaveword1;
    //底部悬浮框
    LinearLayout ll_leave_word2;
    ImageView iv_leaveword2;

    public static final String DETAIL_ID = "id";
    private String id;
    private ColumnReadBean.RColumnRead details;
    private int page = 1;
    private int totalpage = 0;
    private ColumnMessageAdapter columnMessageAdapter;
    private ArrayList<AudioTmessageListBeans.RecordX.Record> mList = new ArrayList<>();
    private AnimationDrawable background;
    private int height;
    private int floaty;
    private int height1;
    private String url;
    private String aid;

    private VoiceDetailsFragment voiceDetailsFragment;
    private String source;
    private String detailurl;
    private int screenHeight;
    private View footView;

    private boolean isLoading=false;
    private ProgressBar progressBar2;
    private int currentpage =1;
    private int totalItem;
    private int lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_column_read, null);
//        ButterKnife.inject(this,view);
        id = getActivity().getIntent().getStringExtra(DETAIL_ID);
        voiceDetailsFragment = new VoiceDetailsFragment();

        screenHeight = getScreenHeight(getActivity());
        Log.e("TAG", "screenHeight=" + screenHeight);

        initView(view);
        getDetailsData();
        getMessageList();

        return view;
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }
    private void initView(View view) {

        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tab_title = (TextView) view.findViewById(R.id.tab_title);
        iv_shared = (ImageView) view.findViewById(R.id.iv_shared);
        record = (ImageView) view.findViewById(R.id.record);
        rl_background = view.findViewById(R.id.rl_background);

        nsl_message_list = (ListView) view.findViewById(R.id.nsl_message_list);
        rl_buy_column = (RelativeLayout) view.findViewById(R.id.rl_buy_column);
        tv_buy_column = (TextView) view.findViewById(R.id.tv_buy_column);
        ll_leave_word1 = (LinearLayout) view.findViewById(R.id.ll_leave_word1);
        iv_leaveword1 = (ImageView) view.findViewById(R.id.iv_leaveword1);
        ll_leave_word2 = (LinearLayout) view.findViewById(R.id.ll_leave_word2);
        iv_leaveword2 = (ImageView) view.findViewById(R.id.iv_leaveword2);

        View inflate = View.inflate(getActivity(), R.layout.column_header_view, null);
        title = (TextView) inflate.findViewById(R.id.title);
        iv_play = (ImageView) inflate.findViewById(R.id.iv_play);
        tv_play_title = (TextView) inflate.findViewById(R.id.tv_play_title);
        tv_play_time = (TextView) inflate.findViewById(R.id.tv_play_time);
        nsl_content = (NoScrollListview) inflate.findViewById(R.id.nsl_content);
        ll_leave_word = (LinearLayout) inflate.findViewById(R.id.ll_leave_word);
        iv_leaveword = (ImageView) inflate.findViewById(R.id.iv_leaveword);

        //底部加载更多布局
        footView = View.inflate(getActivity(), R.layout.column_foot_view,null);
        progressBar2 = (ProgressBar) footView.findViewById(R.id.progressBar2);
        nsl_message_list.addHeaderView(inflate);
        nsl_message_list.addFooterView(footView);
        nsl_message_list.setVerticalScrollBarEnabled(false);
        nsl_message_list.setFastScrollEnabled(false);
        nsl_message_list.setOnScrollListener(mScrollListener);

        iv_back.setOnClickListener(this);
        iv_shared.setOnClickListener(this);
        record.setOnClickListener(this);
        iv_leaveword.setOnClickListener(this);
        iv_leaveword1.setOnClickListener(this);
        iv_leaveword2.setOnClickListener(this);
        tv_buy_column.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        ll_leave_word1.setVisibility(View.GONE);


        ViewTreeObserver vto = ll_leave_word.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_leave_word.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                floaty = (int) ll_leave_word.getY();
                Log.e("TAG", "y=" + floaty);

                if (floaty < screenHeight) {
                    ll_leave_word2.setVisibility(View.GONE);
                } else {
                    ll_leave_word2.setVisibility(View.VISIBLE);
                }
            }
        });


//
//        ViewTreeObserver observer = ll_leave_word2.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ll_leave_word2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                float y = ll_leave_word2.getY();
//                int top = ll_leave_word2.getTop();
//
//                int height = ll_leave_word2.getHeight();
//
////                Log.e("TAG", "底部悬浮=" + y);
////                Log.e("TAG", "底部悬浮top=" + top);
////                Log.e("TAG", "底部悬浮height=" + height);
//            }
//        });
    }



    private int mCurrentfirstVisibleItem = 0;
    private SparseArray recordSp = new SparseArray(0);
    private boolean isFloat;
    private AbsListView.OnScrollListener mScrollListener= new AbsListView.OnScrollListener() {


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(nsl_message_list.getLastVisiblePosition() == mList.size() +1
                    && scrollState == SCROLL_STATE_IDLE
                    && progressBar2.isShown()
                    && !isLoading ) {
                getDataMore();
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (isLoading) {
                footView.setVisibility(View.VISIBLE);
            }
//            lastVisibleItem = firstVisibleItem + visibleItemCount;
//            totalItem = totalItemCount;
//            Log.e("TAG", "totalItemCount=" + totalItemCount);
//            int[] location = new int[2];
//            ll_leave_word2.getLocationOnScreen(location);
//            int x = location[0];
//            int y = location[1];
//            Log.e("TAG", "top121=" + y);


            int top = ll_leave_word2.getTop();
//            Log.e("TAG", "top=" + top);
            mCurrentfirstVisibleItem = firstVisibleItem;

            View firstView  = view.getChildAt(0);
            if(null != firstView) {
                ItemRecod  itemRecord  = (ItemRecod) recordSp.get(firstVisibleItem);
                if (null == itemRecord) {
                    itemRecord = new ItemRecod();
                }
                itemRecord.height = firstView.getHeight();
                itemRecord.top = firstView.getTop();
                recordSp.append(firstVisibleItem,itemRecord);
                int h = getScrollY();
                //顶部悬浮
                if (h >= floaty) {
                    ll_leave_word1.setVisibility(View.VISIBLE);
                } else {
                    ll_leave_word1.setVisibility(View.GONE);
                }
//                Log.e("TAG", "h=" + h);
                //底部悬浮
                if(h==0) {
                    return;
                }
                if(h <= (floaty - top )) {
                   ll_leave_word2.setVisibility(View.VISIBLE);

                }else if(h > (floaty - top ) && h <floaty) {
                    ll_leave_word2.setVisibility(View.GONE);
                }
            }
        }
    };

    private void getDataMore() {
        isLoading = true;
        currentpage ++;
        Log.e("TAG", "currentpage=" + currentpage);
        if (currentpage <= totalpage) {
            footView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMore();
                }
            }, 1000);
        } else {
            ToastU.showLong(getActivity(), "没有更多了!");
            isLoading = false;
            footView.setVisibility(View.GONE);
        }
    }

    private void getMore() {
        String url = String.format(UmiwiAPI.UMIWI_COLUM_MESSAGE,id,currentpage);
        Log.e("TAG", "阅读界面url=" + url);
        GetRequest<AudioTmessageListBeans> request = new GetRequest<AudioTmessageListBeans>(url, GsonParser.class, AudioTmessageListBeans.class, new AbstractRequest.Listener<AudioTmessageListBeans>() {
            @Override
            public void onResult(AbstractRequest<AudioTmessageListBeans> request, AudioTmessageListBeans audioTmessageListBeans) {
                ArrayList<AudioTmessageListBeans.RecordX.Record> record = audioTmessageListBeans.getR().getRecord();
//                Log.e("TAG", "阅读界面record=" + record.toString());
                currentpage = audioTmessageListBeans.getR().getPage().getCurrentpage();

                isLoading = false;
                mList.addAll(record);
                columnMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(AbstractRequest<AudioTmessageListBeans> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            height += itemRecod.height;
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }
    class ItemRecod {
        int height = 0;
        int top = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        initMedia();
        getMessageList();
    }

    private void initMedia() {
        String url = String.format(UmiwiAPI.UMIWI_COLUMN_READ,id);
        GetRequest<ColumnReadBean> request = new GetRequest<ColumnReadBean>(url, GsonParser.class, ColumnReadBean.class, new AbstractRequest.Listener<ColumnReadBean>() {
            @Override
            public void onResult(AbstractRequest<ColumnReadBean> request, ColumnReadBean columnReadBean) {
                String detailurl = columnReadBean.getR().getDetailurl();
                if (UmiwiApplication.mainActivity != null) {
                    if (UmiwiApplication.mainActivity.service != null) {
                        background = (AnimationDrawable) record.getBackground();
                        try {
                            if (UmiwiApplication.mainActivity.service.isPlaying()) {
                                background.start();
                                if (UmiwiApplication.mainActivity.herfUrl.equals(detailurl) ) {
                                    iv_play.setBackgroundResource(R.drawable.image_pause);
                                } else {
                                    iv_play.setBackgroundResource(R.drawable.image_play);
                                }
//                                Log.e("TAG", "detailurl=" + detailurl + "UmiwiApplication.mainActivity.herfUrl=" + UmiwiApplication.mainActivity.herfUrl);
                            } else {
                                background.stop();
                                iv_play.setBackgroundResource(R.drawable.image_play);
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onError(AbstractRequest<ColumnReadBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

//    private void initMediaPlay() {
//
//    }

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
            case R.id.iv_leaveword1 :
                Intent intent1 = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent1.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnLeaveMessageFragment.class);
                intent1.putExtra("aid",details.getId());
                startActivity(intent1);
                break;
            case R.id.iv_leaveword2 :
                Intent intent2 = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent2.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ColumnLeaveMessageFragment.class);
                intent2.putExtra("aid",details.getId());
                startActivity(intent2);
                break;
            case R.id.tv_buy_column :

                break;
            case R.id.iv_play:

                UmiwiApplication.mainActivity.herfUrl = detailurl;
                Log.e("TAG", "detailurl=" + detailurl);

                if (UmiwiApplication.mainActivity.service != null) {

                    try {
                        if (url.equals(UmiwiApplication.mainActivity.musicUrl)) {
                            //如果再次进来当前音乐暂停，就继续播放
                            try {
                                if (UmiwiApplication.mainActivity.service.isPlaying()) {
                                    iv_play.setBackgroundResource(R.drawable.image_play);
                                    UmiwiApplication.mainActivity.service.pause();
                                    UmiwiApplication.mainActivity.isPause = true;
                                    AnimationDrawable background = (AnimationDrawable) record.getBackground();
                                    background.stop();
                                } else {
                                    iv_play.setBackgroundResource(R.drawable.image_pause);
                                    UmiwiApplication.mainActivity.service.play();
                                    AnimationDrawable background = (AnimationDrawable) record.getBackground();
                                    background.start();
                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        UmiwiApplication.mainActivity.service.openAudio(url);
                        UmiwiApplication.mainActivity.musicUrl = url;
//                        Log.e("TAG", "source=" + source);
//                        Log.e("TAG", "UmiwiApplication.mainActivity.musicUrl=" + UmiwiApplication.mainActivity.musicUrl);

                        iv_play.setBackgroundResource(R.drawable.image_pause);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    voiceDetailsFragment.bind(url);
                    UmiwiApplication.mainActivity.musicUrl = url;
//                    Log.e("TAG", "source1=" + source);
//                    Log.e("TAG", "UmiwiApplication.mainActivity.musicUrl1=" + UmiwiApplication.mainActivity.musicUrl);
                    iv_play.setBackgroundResource(R.drawable.image_pause);
                    AnimationDrawable background = (AnimationDrawable) record.getBackground();
                    background.start();
                }
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
                currentpage = audioTmessageListBeans.getR().getPage().getCurrentpage();
                ArrayList<AudioTmessageListBeans.RecordX.Record> record = audioTmessageListBeans.getR().getRecord();
//                Log.e("TAG", "阅读界面record=" + record.toString());
                mList.clear();
                mList.addAll(record);
                columnMessageAdapter = new ColumnMessageAdapter(getActivity(),mList);
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
                detailurl = details.getDetailurl();
                tab_title.setText(details.getTcolumntitle());
                tab_title.setTextColor(Color.BLACK);
                tab_title.setVisibility(View.VISIBLE);

                title.setText(details.getTitle());
                tv_play_title.setText(details.getAudiofile().getAtitle());
                tv_play_time.setText(details.getAudiofile().getAplaytime());

                ArrayList<ColumnReadBean.RColumnRead.ReadContentWord> content = details.getContent();
                nsl_content.setFocusable(false);
                nsl_content.setAdapter(new ColumnReadDetailsAdapter(getActivity(), content));

                aid = details.getAudiofile().getAid();
                source = details.getAudiofile().getSource();

//                UmiwiApplication.mainActivity.herfUrl = detailurl;
                getAudioSource(source);
            }

            @Override
            public void onError(AbstractRequest<ColumnReadBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void getAudioSource(String source) {
        GetRequest<AudioResourceBean> request = new GetRequest<AudioResourceBean>(source, GsonParser.class, AudioResourceBean.class, new AbstractRequest.Listener<AudioResourceBean>() {
            @Override
            public void onResult(AbstractRequest<AudioResourceBean> request, AudioResourceBean audioResourceBean) {
                url = audioResourceBean.getR().getSource();

            }

            @Override
            public void onError(AbstractRequest<AudioResourceBean> requet, int statusCode, String body) {

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
