package com.umiwi.ui.fragment.home.alreadyshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.fragment.home.updatehome.indexfragment.AskQuestionFragment;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.managers.YoumiRoomUserManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.util.ImageLoader;
import cn.youmi.framework.view.CircleImageView;

/**
 * Created by Administrator on 2017/3/12.
 */

public class AnswerDetailsFragment extends BaseConstantFragment implements View.OnClickListener {

    @InjectView(R.id.back)
    ImageView back;
//    @InjectView(R.id.share)
//    ImageView share;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.tavatar)
    CircleImageView tavatar;
    @InjectView(R.id.buttontag)
    TextView buttontag;
    @InjectView(R.id.listennum)
    TextView listennum;
    @InjectView(R.id.goodnum)
    TextView goodnum;
    @InjectView(R.id.playtime)
    TextView playtime;
    @InjectView(R.id.header)
    CircleImageView header;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.duty)
    TextView duty;
    @InjectView(R.id.obligation)
    TextView obligation;
    @InjectView(R.id.all_q_a)
    TextView allQA;
    @InjectView(R.id.answertime)
    TextView answertime;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragmeny_answer_details, null);

        ButterKnife.inject(this, inflate);
        getIntentInfos();

        initOnClickLintenrs();
        return inflate;
    }

    private void initOnClickLintenrs() {
        back.setOnClickListener(this);
//        share.setOnClickListener(this);
        allQA.setOnClickListener(this);
    }

    private void getIntentInfos() {
        String titleinfo = getActivity().getIntent().getStringExtra("title");
        String buttontaginfo = getActivity().getIntent().getStringExtra("buttontag");
        String tavatarinfo = getActivity().getIntent().getStringExtra("tavatar");
        String playtimeinfo = getActivity().getIntent().getStringExtra("playtime");
        String answertimeinfo = getActivity().getIntent().getStringExtra("answertime");
        String goodnuminfo = getActivity().getIntent().getStringExtra("goodnum");
        String listennuminfo = getActivity().getIntent().getStringExtra("listennum");
        uid = getActivity().getIntent().getStringExtra("uid");
        UserModel user = YoumiRoomUserManager.getInstance().getUser();
        String usernameinfo = user.getUsername();
        buttontag.setText(buttontaginfo);
        title.setText(titleinfo);
        playtime.setText(playtimeinfo);
        listennum.setText(listennuminfo);
        goodnum.setText(goodnuminfo);
        answertime.setText(answertimeinfo);
        ImageLoader imageLoader = new ImageLoader(getActivity());
        imageLoader.loadImage(tavatarinfo,header);
        imageLoader.loadImage(tavatarinfo,tavatar);
        name.setText(usernameinfo);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                getActivity().finish();
                break;
//            case R.id.share:
//                break;
            case R.id.all_q_a:
                Intent intent = new Intent(getActivity(), UmiwiContainerActivity.class);
                intent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, AskQuestionFragment.class);
                intent.putExtra("uid",uid);
                startActivity(intent);

                break;
        }
    }
}
