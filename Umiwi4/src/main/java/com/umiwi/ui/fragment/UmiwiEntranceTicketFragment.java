package com.umiwi.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.beans.UmiwiResultBeans;
import com.umiwi.ui.beans.UmiwiResultBeans.ResultBeansRequestData;
import com.umiwi.ui.main.UmiwiAPI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.KeyboardUtils;

/**
 * 我摇到的门票信息页面
 */
public class UmiwiEntranceTicketFragment extends BaseFragment {

//    AddressDao addressDao = new AddressDao();
//    AddressModel addressModel = new AddressModel();

    private EditText etUserName;
    private EditText etPhoneNum;
    private EditText etAddress;

    private TextView tvCommit;

    private String hiddenCode;

    private ProgressBar pbWaiting;

    public static final String KEY_LOTTERY_USER_ID = "lotteryuserid";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrance_ticket, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBarAndToolbarTitle(mActionBarToolbar, "快递信息");

        hiddenCode = getActivity().getIntent().getStringExtra(KEY_LOTTERY_USER_ID);
        onPostInflateView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(fragmentName);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(fragmentName);
    }

    protected void onPostInflateView(View view) {

        pbWaiting = (ProgressBar) view.findViewById(R.id.loading);

        etUserName = (EditText) view.findViewById(R.id.entrance_ticket_username_et);
        etPhoneNum = (EditText) view.findViewById(R.id.entrance_ticket_phone_et);
        etAddress = (EditText) view.findViewById(R.id.entrance_ticket_address_et);

        tvCommit = (TextView) view.findViewById(R.id.commit_textview);
        tvCommit.setOnClickListener(commitListener);

//        addressModel = addressDao.getAddress();
//        if (null != addressModel && !TextUtils.isEmpty(addressModel.getAddress()))
//            etAddress.setText(addressModel.getAddress());
//        if (null != addressModel && !TextUtils.isEmpty(addressModel.getMoblie()))
//            etAddress.setText(addressModel.getMoblie());
//        if (null != addressModel && !TextUtils.isEmpty(addressModel.getUserName()))
//            etAddress.setText(addressModel.getUserName());
    }


    private View.OnClickListener commitListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            commitMenpiaoInfo();
        }

    };


    private void commitMenpiaoInfo() {
        String strUserName = etUserName.getText().toString();
        if (TextUtils.isEmpty(strUserName)) {
            Toast.makeText(getActivity(), "请填写姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        String strPhoneNum = etPhoneNum.getText().toString();
        if (TextUtils.isEmpty(strPhoneNum)) {
            Toast.makeText(getActivity(), "请填写电话号码", Toast.LENGTH_SHORT).show();
            return;
        }

        String strAddress = etAddress.getText().toString();
        if (TextUtils.isEmpty(strAddress)) {
            Toast.makeText(getActivity(), "请填写地址", Toast.LENGTH_SHORT).show();
            return;
        }

        tvCommit.setClickable(false);

        String usernameU8 = null;
        String addressU8 = null;

        try {
            usernameU8 = URLEncoder.encode(strUserName, "UTF-8");
            addressU8 = URLEncoder.encode(strAddress, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String commitStr = String.format(UmiwiAPI.MENPIAO_SUBMIT, hiddenCode, usernameU8, strPhoneNum, addressU8);


        GetRequest<UmiwiResultBeans.ResultBeansRequestData> mineCouponRequest = new GetRequest<UmiwiResultBeans.ResultBeansRequestData>(
                commitStr, GsonParser.class,
                UmiwiResultBeans.ResultBeansRequestData.class, menpiaoStatusListener);

        HttpDispatcher.getInstance().go(mineCouponRequest);

//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                addressModel.setUserName(etUserName.getText().toString());
//                addressModel.setAddress(etAddress.getText().toString());
//                addressModel.setMoblie(etPhoneNum.getText().toString());
//                addressDao.save(addressModel);
//            }
//        });

        KeyboardUtils.hideKeyboard(getActivity());

        pbWaiting.setVisibility(View.VISIBLE);
    }

//    private Handler handler = new Handler();

    // 提交信息
    private Listener<UmiwiResultBeans.ResultBeansRequestData> menpiaoStatusListener = new Listener<UmiwiResultBeans.ResultBeansRequestData>() {

        @Override
        public void onResult(
                AbstractRequest<ResultBeansRequestData> request,
                ResultBeansRequestData t) {
            pbWaiting.setVisibility(View.GONE);
            tvCommit.setClickable(true);

            if (t != null && t.isSucc()) {
                //TODO 正常提示
                Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                //TODO 错误提示
                Toast.makeText(getActivity(), "信息提交失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(AbstractRequest<ResultBeansRequestData> requet,
                            int statusCode, String body) {
            tvCommit.setClickable(true);
            pbWaiting.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "网络发生问题,请稍后重试", Toast.LENGTH_SHORT).show();
        }


    };

}
