package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.umiwi.ui.R;
import com.umiwi.ui.adapter.AnswerAdapter;
import com.umiwi.ui.adapter.AudioAdapter;
import com.umiwi.ui.adapter.VideoAdapter;
import com.umiwi.ui.beans.AddFavBeans;
import com.umiwi.ui.beans.AudioBean;
import com.umiwi.ui.beans.MyanswerBean;
import com.umiwi.ui.beans.VideoBean;
import com.umiwi.ui.beans.updatebeans.HomeCoumnBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.JsonUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.view.LoadingFooter;

import static android.R.attr.data;
import static com.umiwi.ui.R.id.listView;
import static com.umiwi.ui.R.id.lv_expert_dwon_answer;
import static u.aly.x.S;
import static u.aly.x.V;

/**
 * Created by Administrator on 2017/3/3.
 */

//已回答页面
public class AnsweredFragment extends BaseConstantFragment {

    private ListView lv_answer_answered;
    private String url;
    private String p = "1";
    private ArrayList mList = new ArrayList();
    private String  answerstate = "2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answered_fragment, null);

        if(YoumiRoomUserManager.getInstance().isLogin()){
            getData();
        }

        initView(view) ;
        return view;
    }

    private void getData() {
        mList = new ArrayList();
        url = UmiwiAPI.Mine_Answer+"?answerstate=1";

        OkHttpUtils.get().url(url).build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {
                Log.e("MZX", "数据进行请求失败");
            }

            @Override
            public void onSucess(String data) {
                Log.e("MZX", "数据进行请求成功了" + data);
            }
        });
    }

    private void initView(View view) {
        lv_answer_answered = (ListView) view.findViewById(R.id.lv_answer_answered);
        lv_answer_answered.setAdapter(new AnsweredAdapter(getActivity(),mList));
    }

    public class AnsweredAdapter extends BaseAdapter{

        private ArrayList mList;
        private FragmentActivity activity;

        public AnsweredAdapter(FragmentActivity activity, ArrayList mList) {
            this.activity = activity;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(activity, R.layout.answered_item, null);



                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }

        class ViewHolder{

        }
    }
}
