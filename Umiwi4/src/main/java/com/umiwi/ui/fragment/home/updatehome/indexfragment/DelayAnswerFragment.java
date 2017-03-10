package com.umiwi.ui.fragment.home.updatehome.indexfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.setting.FeedbackFragment;
import com.umiwi.ui.main.CustomStringCallBack;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.view.RefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.fragment.BaseFragment;

/**
 * Created by Administrator on 2017/3/3.
 */

//待回答页面
public class DelayAnswerFragment extends BaseFragment {


    @InjectView(R.id.lv_answer_answered)
    ListView lvAnswerAnswered;
    @InjectView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @InjectView(R.id.button)
    Button button;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delay_answer_fragment, null);
        ButterKnife.inject(this, view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, RecordVoiceFragment.class);
                startActivity(intent);
            }
        });
        getInfos();
        return view;
    }

    //    YoumiRoomUserManager.getInstance().isLogin()
    private void getInfos() {
        String cookie = YoumiRoomUserManager.getInstance().getUser().getCookie();

//        if ()

        OkHttpUtils.get().url(UmiwiAPI.Mine_Answer).addParams("p",page +"").addParams("answerstate","1").build().execute(new CustomStringCallBack() {
            @Override
            public void onFaild() {

            }

            @Override
            public void onSucess(String data) {
                Log.e("data", "待回答页面请求数据成功" + data);
                if (!TextUtils.isEmpty(data)) {

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
