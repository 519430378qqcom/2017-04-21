package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.AnswerAdapter;
import com.umiwi.ui.adapter.AudioAdapter;
import com.umiwi.ui.adapter.VideoAdapter;
import com.umiwi.ui.beans.AudioBean;
import com.umiwi.ui.beans.VideoBean;
import com.umiwi.ui.beans.VideoHeadBean;
import com.umiwi.ui.fragment.course.CourseDetailPlayFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.util.ListViewScrollLoader;
import cn.youmi.framework.view.LoadingFooter;


/**
 * Created by LvDabing on 2017/2/16.
 * Email：lvdabing@lvshandian.com
 * Detail:视频
 */

public class VideoFragment extends BaseConstantFragment {

    private TextView tv_all_one;
    private TextView tv_all_two;
    private TextView tv_all_three;
    private TextView tv_new;
    private TextView tv_host;
    private TextView tv_good;
    private TextView tv_free;
    private TextView tv_pay;

    private ListView listView;
    private LoadingFooter mLoadingFooter;
    private ListViewScrollLoader mScrollLoader;

    private String catid = "";//分类ID
    private String price = "";//free-免费，charge-收费
    private String orderby = "ctime";//ctime-最新，watchnum-最热,usefulnum-好评

    private GridView gv_one;
    private GridView gv_two;
    private List<VideoHeadBean.SubtreeBean> subtree;//二级数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout, null);

        onLoadData(1);
        initView(view);

        getData();

        return view;
    }

    private void initView(View view) {
        gv_one = (GridView) view.findViewById(R.id.gv_one);
        gv_two = (GridView) view.findViewById(R.id.gv_two);

        //最新
        tv_new = (TextView) view.findViewById(R.id.tv_new);
        tv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_new.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_host.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_good.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                orderby = "ctime";

                onLoadData(1);
            }
        });

        //最热
        tv_host = (TextView) view.findViewById(R.id.tv_host);
        tv_host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_new.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_host.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_good.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                orderby = "watchnum";

                onLoadData(1);
            }
        });
        //好评
        tv_good = (TextView) view.findViewById(R.id.tv_good);
        tv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_good.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_new.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_host.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                orderby = "usefulnum";

                onLoadData(1);
            }
        });

        tv_all_three = (TextView) view.findViewById(R.id.tv_all_three);
        tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
        tv_all_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_free.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_pay.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                price = "";
                onLoadData(1);
            }
        });
        //免费
        tv_free = (TextView) view.findViewById(R.id.tv_free);
        tv_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_free.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_pay.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

                price = "free";
                onLoadData(1);
            }
        });
        //付费
        tv_pay = (TextView) view.findViewById(R.id.tv_pay);
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_pay.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                tv_free.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                tv_all_three.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

                price = "charge";
                onLoadData(1);
            }
        });

        tv_all_one = (TextView) view.findViewById(R.id.tv_all_one);
        tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
        tv_all_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int childCount = gv_one.getChildCount();
                Log.e("MZX", childCount + "");
                for (int i = 0; i < childCount; i++) {
                    TextView textView = (TextView) gv_one.getChildAt(i).findViewById(R.id.textview);
                    textView.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                }
                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                catid = "";
                onLoadData(1);
            }
        });

        tv_all_two = (TextView) view.findViewById(R.id.tv_all_two);
        tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
        tv_all_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int childCount = gv_two.getChildCount();
                Log.e("MZX", childCount + "");
                for (int i = 0; i < childCount; i++) {
                    TextView textView = (TextView) gv_two.getChildAt(i).findViewById(R.id.textview);
                    textView.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                }
                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
                catid = "";
                onLoadData(1);
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
////					intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailLayoutFragments.class);
////					intent.putExtra(CourseDetailLayoutFragments.KEY_DETAIURL, listBeans.getDetailurl());
////                  intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, CourseDetailPlayFragment.class);
//////                intent.putExtra(CourseDetailPlayFragment.KEY_DETAIURL, mList.get();
//
//                getActivity().startActivity(intent);
            }
        });

        mLoadingFooter = new LoadingFooter(getActivity());
        listView.addFooterView(mLoadingFooter.getView());

        mScrollLoader = new ListViewScrollLoader(this, mLoadingFooter);
        listView.setOnScrollListener(mScrollLoader);

        mScrollLoader.onLoadFirstPage();
    }

    private ArrayList<VideoHeadBean.InfoBean> oneList;
    private ArrayList<VideoHeadBean.SubtreeBean> twoList;

    private void getData() {
        String URL = UmiwiAPI.video_head;
        OkHttpUtils.get().url(URL).build().execute(new CustomStringCallBack() {

            @Override
            public void onFaild() {
            }

            @Override
            public void onSucess(String data) {
                Log.e("TAG---", data);
                try {
                    JSONArray jsonArray = new JSONArray(data);
                    oneList = new ArrayList();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject temp = (JSONObject) jsonArray.get(i);
                        VideoHeadBean videoHeadBean = new Gson().fromJson(temp.toString(), VideoHeadBean.class);
                        oneList.add(i, videoHeadBean.getInfo());
                        subtree = videoHeadBean.getSubtree();
                        Log.e("TAG", videoHeadBean.getInfo().getName());
                    }
                    Log.e("TAG", oneList.size() + "");
                    final OneAdapter oneAdapter = new OneAdapter(getActivity(), oneList);
                    gv_one.setAdapter(oneAdapter);
                    gv_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            oneAdapter.setSeclection(position);
                            oneAdapter.notifyDataSetChanged();
                            tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

                            catid = oneList.get(position).getId();
                            onLoadData(1);

                        }
                    });

                    final TwoAdapter twoAdapter = new TwoAdapter(getActivity(), subtree);
                    gv_two.setAdapter(twoAdapter);
                    gv_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            twoAdapter.setSeclection(position);
                            tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
                            twoAdapter.notifyDataSetChanged();

                            catid = subtree.get(position).getId();
                            onLoadData(1);

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private int p = 1;
    private List<VideoBean.RecordBean> mList = new ArrayList<>();

    public void onLoadData(final int page) {
        String url = UmiwiAPI.Login_Video + "?orderby=" + orderby;

        if (price != null && price != "") {
            url += "&price=" + price;
        }
        if (catid != null && catid != "") {
            url += "&catid=" + catid;
        }

        if (String.valueOf(p) != null && String.valueOf(p) != "") {
            url += "&p=" + page;
        }

        Log.e("MZX", url);
        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            private VideoAdapter videoAdapter;

            @Override
            public void onFaild() {
//                mScrollLoader.onLoadErrorPage();
            }

            @Override
            public void onSucess(String data) {
                Log.e("MZX", "数据进行请求成功了--x-" + data);

                VideoBean audioBean = new Gson().fromJson(data, VideoBean.class);
                VideoBean.PageBean pagebean = audioBean.getPage();

                if (audioBean.getPage().getCurrentpage() >= audioBean.getPage().getTotalpage()) {
                    mLoadingFooter.setState(LoadingFooter.State.NoMore);
                    mScrollLoader.setEnd(true);
                    return;
                }


                mScrollLoader.setPage(pagebean.getCurrentpage());
                mScrollLoader.setloading(false);

                if (page == 1) {
                    mList.clear();
                }

                if (mList == null) {
                    mList = audioBean.getRecord();
                } else {
                    if (page == 1) {
                        mList.clear();
                    }
                    mList.addAll(audioBean.getRecord());
                }

                videoAdapter = new VideoAdapter(getActivity(), mList);
                if (page == 1) {
                    listView.setAdapter(videoAdapter);
                } else {
                    videoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private class OneAdapter extends BaseAdapter {
        private FragmentActivity activity;
        private ArrayList<VideoHeadBean.InfoBean> headList;

        private int clickTemp = -1;//标识被选择的item
        private int[] clickedList;//这个数组用来存放item的点击状态

        public OneAdapter(FragmentActivity activity, ArrayList<VideoHeadBean.InfoBean> oneList) {
            this.activity = activity;
            this.headList = oneList;
            clickedList = new int[headList.size()];
            for (int i = 0; i < headList.size(); i++) {
                clickedList[i] = 0;      //初始化item点击状态的数组
            }
        }

        public void setSeclection(int posiTion) {
            clickTemp = posiTion;
        }

        @Override
        public int getCount() {
            return headList.size();
        }

        @Override
        public Object getItem(int i) {
            return headList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoler viewHoler;
            if (convertView == null) {
                viewHoler = new ViewHoler();
                convertView = View.inflate(activity, R.layout.one_item, null);

                viewHoler.textview = (TextView) convertView.findViewById(R.id.textview);

                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }

            if (clickTemp == position) {    //根据点击的Item当前状态设置背景
                if (clickedList[position] == 0) {
                    viewHoler.textview.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
//                    tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

                    for (int i = 0; i < headList.size(); i++) {
                        clickedList[position] = 0;
                    }
                } else {
//                    viewHoler.textview.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
//                    clickedList[position]=0;
                }
            } else {
                viewHoler.textview.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
//                tv_all_one.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }

            viewHoler.textview.setText(headList.get(position).getName());
            return convertView;
        }

        class ViewHoler {
            private TextView textview;
        }
    }

    private class TwoAdapter extends BaseAdapter {
        private FragmentActivity activity;
        private List<VideoHeadBean.SubtreeBean> headList;

        private int clickTemp = -1;//标识被选择的item
        private int[] clickedList;//这个数组用来存放item的点击状态

        public TwoAdapter(FragmentActivity activity, List<VideoHeadBean.SubtreeBean> oneList) {
            this.activity = activity;
            this.headList = oneList;
            clickedList = new int[headList.size()];
            for (int i = 0; i < headList.size(); i++) {
                clickedList[i] = 0;      //初始化item点击状态的数组
            }
        }

        public void setSeclection(int posiTion) {
            clickTemp = posiTion;
        }

        @Override
        public int getCount() {
            return headList.size();
        }

        @Override
        public Object getItem(int i) {
            return headList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoler viewHoler;
            if (convertView == null) {
                viewHoler = new ViewHoler();
                convertView = View.inflate(activity, R.layout.one_item, null);
                viewHoler.textview = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }

            if (clickTemp == position) {    //根据点击的Item当前状态设置背景
                if (clickedList[position] == 0) {
                    viewHoler.textview.setTextColor(getActivity().getResources().getColor(R.color.umiwi_orange));
//                    tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));

                    for (int i = 0; i < headList.size(); i++) {
                        clickedList[position] = 0;
                    }
                } else {
//                    viewHoler.textview.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
//                    clickedList[position]=0;
                }
            } else {
                viewHoler.textview.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
//                tv_all_two.setTextColor(getActivity().getResources().getColor(R.color.umiwi_gray_6));
            }

            viewHoler.textview.setText(headList.get(position).getName());
            return convertView;
        }

        class ViewHoler {
            private TextView textview;
        }
    }
}