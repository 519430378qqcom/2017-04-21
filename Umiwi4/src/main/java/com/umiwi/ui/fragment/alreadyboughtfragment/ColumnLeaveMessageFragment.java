package com.umiwi.ui.fragment.alreadyboughtfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umiwi.ui.R;
import com.umiwi.ui.beans.updatebeans.ColumnMessageCommitBean;
import com.umiwi.ui.main.BaseConstantFragment;
import com.umiwi.ui.main.UmiwiAPI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.parsers.GsonParser;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ColumnLeaveMessageFragment extends BaseConstantFragment {
    @InjectView(R.id.tv_cancle)
    TextView tv_cancle;
    @InjectView(R.id.tv_commit)
    TextView tv_commit;
    @InjectView(R.id.et_comment_content)
    EditText et_comment_content;
    @InjectView(R.id.tv_numbers)
    TextView tv_numbers;
//    @InjectView(R.id.tv_alreadyall)
//    TextView tv_alreadyall;
//    @InjectView(R.id.lv_my_comment)
//    ListView lv_my_comment;
    private String aid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_message,null);
        ButterKnife.inject(this,view);
        aid = getActivity().getIntent().getStringExtra("aid");
        et_comment_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = et_comment_content.getText().length();
                tv_numbers.setText("剩余" + (300-length) + "字");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_comment_content.getText().length() <= 0) {
                   Toast.makeText(getActivity(), "您还没有写留言呢!", Toast.LENGTH_SHORT).show();
                } else {
                    String url = String.format(UmiwiAPI.UMIWI_MESSAGE_COMMIT,aid,et_comment_content.getText().toString());
                    GetRequest<ColumnMessageCommitBean> request = new GetRequest<ColumnMessageCommitBean>(url, GsonParser.class, ColumnMessageCommitBean.class, new AbstractRequest.Listener<ColumnMessageCommitBean>() {
                        @Override
                        public void onResult(AbstractRequest<ColumnMessageCommitBean> request, ColumnMessageCommitBean columnMessageCommitBean) {
                            if ("9999".equals(columnMessageCommitBean.getE())) {
                                Toast.makeText(getActivity(), "留言提交成功!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "提交错误,请重新提交!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(AbstractRequest<ColumnMessageCommitBean> requet, int statusCode, String body) {

                        }
                    });
                    request.go();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}
