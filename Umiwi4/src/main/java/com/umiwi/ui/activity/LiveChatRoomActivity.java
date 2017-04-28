package com.umiwi.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.LiveDetailsBean;
import com.umiwi.ui.beans.NIMAccountBean;
import com.umiwi.ui.fragment.audiolive.LiveDetailsFragment;
import com.umiwi.ui.main.UmiwiAPI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

public class LiveChatRoomActivity extends AppCompatActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.iv_more)
    ImageView ivMore;
    @InjectView(R.id.et_input)
    EditText etInput;
    @InjectView(R.id.btn_comfirm)
    Button btnComfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat_room);
        ButterKnife.inject(this);
        loginNIM();
        initData();
    }

    /**
     * 登录网易云信服务端
     */
    private void loginNIM() {
        GetRequest<NIMAccountBean> request = new GetRequest<NIMAccountBean>(UmiwiAPI.NIM_ACCOUNT, GsonParser.class, NIMAccountBean.class, new AbstractRequest.Listener<NIMAccountBean>() {
            @Override
            public void onResult(AbstractRequest<NIMAccountBean> request, NIMAccountBean nimAccountBean) {
                if (nimAccountBean != null) {
                    final LoginInfo loginInfo = new LoginInfo(nimAccountBean.getR().getAccid(), nimAccountBean.getR().getToken());
                    RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            Toast.makeText(LiveChatRoomActivity.this, loginInfo.getAccount() + "登录成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                        }

                        @Override
                        public void onException(Throwable exception) {
                        }
                    };
                    NIMClient.getService(AuthService.class).login(loginInfo).setCallback(callback);
                }
            }

            @Override
            public void onError(AbstractRequest<NIMAccountBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    private void initData() {
        String id = getIntent().getStringExtra(LiveDetailsFragment.DETAILS_ID);
        GetRequest<LiveDetailsBean> request = new GetRequest<>(
                UmiwiAPI.LIVE_DETAILS + id, GsonParser.class, LiveDetailsBean.class, new AbstractRequest.Listener<LiveDetailsBean>() {
            @Override
            public void onResult(AbstractRequest<LiveDetailsBean> request, LiveDetailsBean liveDetailsBean) {
                if (liveDetailsBean != null) {
                    LiveDetailsBean.RBean.RecordBean record = liveDetailsBean.getR().getRecord();
                    tvTitle.setText(record.getTitle());
                    tvStatus.setText(record.getStatus()+"("+record.getPartakenum()+"人)");
                }
            }

            @Override
            public void onError(AbstractRequest<LiveDetailsBean> requet, int statusCode, String body) {

            }
        });
        request.go();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.iv_more, R.id.btn_comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.iv_more:
                break;
            case R.id.btn_comfirm:
                break;
        }
    }
}
