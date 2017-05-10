package com.umiwi.ui.fragment.pay;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.activity.YMPayActivity;
import com.umiwi.ui.adapter.PaymentBankAdapter;
import com.umiwi.ui.adapter.PaymentSdkAdapter;
import com.umiwi.ui.beans.UmiwiPayDoingBeans;
import com.umiwi.ui.beans.UmiwiPayDoingBeans.PayDoingBeansRequestData;
import com.umiwi.ui.beans.UmiwiPayDoingPaymentBeans;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.view.MyListView;
import com.umiwi.ui.view.PayGridView;

import java.util.ArrayList;
import java.util.List;

import cn.youmi.account.event.UserEvent;
import cn.youmi.account.model.UserModel;
import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.manager.ModelManager;
import cn.youmi.framework.manager.ModelManager.ModelStatusListener;
import cn.youmi.framework.util.ToastU;

/**
 * @author tangxiyong
 * @version 2015-5-28 下午3:01:35
 */
public class PayingFragment extends BaseFragment {

    public static final String KEY_PAY_URL = "key.payurl";
    public String pay_url;
    public static final String PAY_ID="id";
    public String toolbarTitle = "";
    public boolean isMineRecharge = false;

    private ProgressDialog mLoadingDialog;
    /**
     * 订单价格
     */
    private TextView payment_price;
    /**
     * 帐户余额
     */
    private TextView payment_balance;
    /**
     * 还需支付
     */
    private TextView payment_amount;
    /**
     * 充值 点击进入充值
     */
    private TextView payment_recharge;

    /**
     * 账户余额充足时立即支付
     */
    private TextView payment_submit;
    private TextView payment_ing_sdk;
    private TextView payment_ing_bank;

    private ArrayList<UmiwiPayDoingPaymentBeans> mListSdk;
    private ArrayList<UmiwiPayDoingPaymentBeans> mListBank;

    private MyListView mListView;
    private PayGridView mGridView;

    private PaymentSdkAdapter mListAdapter;
    private PaymentBankAdapter mGridAdapter;

    private View lines_01;
    private View lines_02;
    private View lines_03;
    private View lines_04;

    private android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
    public static String payId;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_paying, null);
        mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        findViewById(view);

        mListSdk = new ArrayList<UmiwiPayDoingPaymentBeans>();
        mListBank = new ArrayList<UmiwiPayDoingPaymentBeans>();

        mListAdapter = new PaymentSdkAdapter(getActivity(), mListSdk);
        mListView.setAdapter(mListAdapter);

        mGridAdapter = new PaymentBankAdapter(getActivity(), mListBank);
        mGridView.setAdapter(mGridAdapter);

        Intent intent = getActivity().getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra(KEY_PAY_URL))) {
            pay_url = intent.getStringExtra(KEY_PAY_URL);
            payId = intent.getStringExtra(PAY_ID);
            payment(pay_url);
        } else {
            ToastU.show(getActivity(), "购买链接异常", Toast.LENGTH_SHORT);
        }

        YoumiRoomUserManager.getInstance().registerListener(userListener);

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
        progressDissmiss();
        MobclickAgent.onPageEnd(fragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        YoumiRoomUserManager.getInstance().unregisterListener(userListener);
    }

    private void findViewById(View view) {
        payment_price = (TextView) view.findViewById(R.id.tv_pay_payment_price);
        payment_balance = (TextView) view.findViewById(R.id.tv_pay_payment_balance);
        payment_amount = (TextView) view.findViewById(R.id.tv_pay_payment_amount);
        payment_recharge = (TextView) view.findViewById(R.id.tv_pay_payment_recharge);

        payment_recharge.setText(Html.fromHtml(
                "<font color='#666666'>" + "支付有限额问题可多次充值再支付    " + "</font>" +
                        "<font color='#000000'><u><big>" + "充值" + "</big></u></font>"));

        payment_submit = (TextView) view.findViewById(R.id.tv_pay_payment_submit);
        payment_ing_sdk = (TextView) view.findViewById(R.id.tv_pay_payment_ing_sdk);
        payment_ing_bank = (TextView) view.findViewById(R.id.tv_pay_payment_ing_bank);

        mListView = (MyListView) view.findViewById(R.id.listView);
        mGridView = (PayGridView) view.findViewById(R.id.gridview);

        lines_01 = view.findViewById(R.id.lines_01);
        lines_02 = view.findViewById(R.id.lines_02);
        lines_03 = view.findViewById(R.id.lines_03);
        lines_04 = view.findViewById(R.id.lines_04);

        mLoadingDialog = new ProgressDialog(getActivity());
        mLoadingDialog.setMessage("加载中,请稍候...");
        mLoadingDialog.setIndeterminate(true);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.show();

        onClickListener();
    }

    private void onClickListener() {
        payment_recharge.setOnClickListener(new RechargeListener());

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UmiwiPayDoingPaymentBeans mListBeans = (UmiwiPayDoingPaymentBeans) mListAdapter.getItem(position - mListView.getHeaderViewsCount());
                Intent i = new Intent(getActivity(), YMPayActivity.class);
                if ("alipay".equals(mListBeans.getMethod())) {//支付宝客户端
                    //判断支付宝
                    i.putExtra(YMPayActivity.PAY_TYPE, YMPayActivity.PAY_ALIPAY);
                    i.putExtra(YMPayActivity.PYA_URL, mListBeans.getUrl());
                } else if ("unionpay".equals(mListBeans.getMethod())) {
                    //判断银联
                    i.putExtra(YMPayActivity.PAY_TYPE, YMPayActivity.PAY_UNIONPAY);
                    i.putExtra(YMPayActivity.PYA_URL, mListBeans.getUrl());
                } else if ("weixinpay".equals(mListBeans.getMethod())) {//微信支付
                    i.putExtra(YMPayActivity.PAY_TYPE, YMPayActivity.PAY_WEIXIN);
                    i.putExtra(YMPayActivity.PYA_URL, mListBeans.getUrl());
                } else if ("wap".equals(mListBeans.getMethod())) {//网页
                    i.putExtra(YMPayActivity.PAY_TYPE, YMPayActivity.PAY_WEB);
                    i.putExtra(YMPayActivity.PYA_URL, mListBeans.getUrl());
                } else {
                    ToastU.showShort(getActivity(), "不支持，请选择其他支付或更新优米创业");
                }
                if (i.getExtras() != null) {
                    startActivity(i);
                }
            }
        });

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UmiwiPayDoingPaymentBeans mGirdBeans = (UmiwiPayDoingPaymentBeans) mGridAdapter.getItem(position);
                if ("wap".equals(mGirdBeans.getMethod())) {
                    Intent i = new Intent(getActivity(), YMPayActivity.class);
                    i.putExtra(YMPayActivity.PAY_TYPE, YMPayActivity.PAY_WEB);
                    i.putExtra(YMPayActivity.PYA_URL, mGirdBeans.getUrl());
                    startActivity(i);
                } else {
                    ToastU.showShort(getActivity(), "不支持，请选择其他支付或更新优米创业");
                }
            }
        });
    }

    /**
     * 充值
     *
     * @author tangxiong
     * @version 2014年6月9日 上午10:07:18
     */
    private class RechargeListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (!isMineRecharge) {
                YoumiRoomUserManager.getInstance().unregisterListener(userListener);//
            }

            PayRechargeFragment courseDetailFragment = new PayRechargeFragment();
            UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();
            ca.getNavigationController().pushFragment(courseDetailFragment);
        }
    }


    /**
     * 支付界面解析
     */
    private void payment(String pay_url) {
        mListBank.clear();
        mListSdk.clear();
        mListView.removeAllViewsInLayout();
        mGridView.removeAllViewsInLayout();
        GetRequest<UmiwiPayDoingBeans.PayDoingBeansRequestData> request = new GetRequest<UmiwiPayDoingBeans.PayDoingBeansRequestData>(
                pay_url, GsonParser.class,
                UmiwiPayDoingBeans.PayDoingBeansRequestData.class,
                LoadPaymentListener);
        HttpDispatcher.getInstance().go(request);

    }

    private Listener<UmiwiPayDoingBeans.PayDoingBeansRequestData> LoadPaymentListener = new Listener<UmiwiPayDoingBeans.PayDoingBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<PayDoingBeansRequestData> request,
                             final PayDoingBeansRequestData requestData) {
            Log.e("requestData------------", requestData.toString());
            if (null != requestData) {
                if ("1611".equals(requestData.getDoing_e())) {//订单支付过了 用于判断网页返回键
//					paySuccRefresh();
                    YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.PAY_SUCC);
                    return;
                }
                if (!"9999".equals(requestData.getDoing_e())) {
                    ToastU.showShort(getActivity(), requestData.getDoing_m());
                    progressDissmiss();
                    return;
                }
                if (!requestData.getPayment().isIsenough()) {// 判断是否有足够的余额支付
                    ArrayList<UmiwiPayDoingPaymentBeans> pay_sdk_list = requestData.getPayment().getPay_sdk();
                    ArrayList<UmiwiPayDoingPaymentBeans> pay_bank_list = requestData.getPayment().getPay_bank();

                    if (null != pay_sdk_list) {
                        mListSdk.addAll(pay_sdk_list);
                    }

                    if (null != pay_bank_list) {
                        mListBank.addAll(pay_bank_list);
                    }
                }

                payment_price.setText(Html.fromHtml(
                        "<font color='#666666'>" + "订单价格：" + "</font>" +
                                "<font color='#000000'>" + requestData.getPayment().getOrder_amt() + "元" + "</font>"));
                payment_balance.setText(Html.fromHtml(
                        "<font color='#666666'>" + "帐户余额：" + "</font>" +
                                "<font color='#000000'>" + requestData.getPayment().getBalance() + "元" + "</font>"));

                if (!requestData.getPayment().isIsenough()) {//走sdk
                    payment_amount.setText(Html.fromHtml(requestData.getPayment().getAmount()));

                    payment_ing_bank.setVisibility(View.VISIBLE);
                    payment_ing_sdk.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.VISIBLE);
                    mGridView.setVisibility(View.VISIBLE);

                    lines_03.setVisibility(View.VISIBLE);
                    lines_04.setVisibility(View.VISIBLE);

                    if (mListAdapter == null) {
                        mListAdapter = new PaymentSdkAdapter(getActivity(), mListSdk);
                        mListView.setAdapter(mListAdapter);// 解析成功 播放列表
                    } else {
                        mListAdapter.notifyDataSetChanged();
                    }

                    if (mGridAdapter == null) {
                        mGridAdapter = new PaymentBankAdapter(getActivity(), mListBank);
                        mGridView.setAdapter(mGridAdapter);
                    } else {
                        mGridAdapter.notifyDataSetChanged();
                    }

                } else {//直接
                    payment_submit.setVisibility(View.VISIBLE);
                    payment_submit.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            progressShow();
                            Confirmurl(requestData.getPayment().getConfirmurl());
                        }
                    });
                }

                if (TextUtils.isEmpty(toolbarTitle)) {//非充值
                    lines_01.setVisibility(View.VISIBLE);
                    lines_02.setVisibility(View.VISIBLE);
                    payment_price.setVisibility(View.VISIBLE);
                    payment_balance.setVisibility(View.VISIBLE);
                    payment_recharge.setVisibility(View.VISIBLE);
                    mActionBarToolbar.setTitle("订单付款");
                } else {
                    mActionBarToolbar.setTitle("充值");
                }

                progressDissmiss();

            }
        }

        @Override
        public void onError(AbstractRequest<PayDoingBeansRequestData> requet,
                            int statusCode, String body) {
            showToast(body);
        }
    };

    /**
     * 帐户有余额直接支付解析
     */
    private void Confirmurl(String confirmurl) {

        GetRequest<UmiwiPayDoingBeans.PayDoingBeansRequestData> request = new GetRequest<UmiwiPayDoingBeans.PayDoingBeansRequestData>(
                confirmurl, GsonParser.class,
                UmiwiPayDoingBeans.PayDoingBeansRequestData.class,
                ConfirmurlListener);
        HttpDispatcher.getInstance().go(request);

    }

    private Listener<UmiwiPayDoingBeans.PayDoingBeansRequestData> ConfirmurlListener = new Listener<UmiwiPayDoingBeans.PayDoingBeansRequestData>() {

        @Override
        public void onResult(AbstractRequest<PayDoingBeansRequestData> request,
                             PayDoingBeansRequestData t) {
            if (null != t) {
                if ("9999".equals(t.getDoing_e())) {//支付成功
//					paySuccRefresh();
//                  YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.PAY_SUCC);

                    String ok = t.getDoing_m();
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(getActivity());
                    normalDialog.setCancelable(false);
                    normalDialog.setMessage(ok);
                    normalDialog.setTitle("支付成功");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    YoumiRoomUserManager.getInstance().getUserInfoSave(UserEvent.PAY_SUCC);
//                                    Intent intent = new Intent();
//                                    getActivity().setResult(Activity.RESULT_OK,intent);
                                }
                            });
                    normalDialog.show();

                } else {
                    progressDissmiss();
                    ToastU.showShort(getActivity(), t.getDoing_m());
                }
            }
        }

        @Override
        public void onError(AbstractRequest<PayDoingBeansRequestData> requet,
                            int statusCode, String body) {
            showToast(body);
        }
    };



    private void progressDissmiss() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Looper.prepare();
                    mLoadingDialog.hide();

                }
            });
        }
    }

    private void showToast(String str) {
        progressDissmiss();
        ToastU.showShort(getActivity(), str);
    }

    private void progressShow() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Looper.prepare();
                    mLoadingDialog.show();
                    Looper.loop();
                }
            }).start();

        }
    }

    ModelStatusListener<UserEvent, UserModel> userListener = new ModelStatusListener<UserEvent, UserModel>() {

        @Override
        public void onModelGet(UserEvent key, UserModel models) {
        }

        @Override
        public void onModelUpdate(UserEvent key, UserModel model) {
            switch (key) {
                case PAY_SUCC://支付成功处理
                    progressDissmiss();
                    UmiwiContainerActivity ca = (UmiwiContainerActivity) getActivity();

                    if (ca.getNavigationController().getStack().size() >= 3) {
                        ca.getNavigationController().popFragment();
                        ca.getNavigationController().popFragment();
                    } else {
                        ca.slideToFinishActivity();
                    }
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onModelsGet(UserEvent key, List<UserModel> models) {
        }
    };

    private ModelManager.CustomerListener customerListener;
    public void setCustomerListener(ModelManager.CustomerListener customerListener){
        this.customerListener = customerListener;
    }
}
