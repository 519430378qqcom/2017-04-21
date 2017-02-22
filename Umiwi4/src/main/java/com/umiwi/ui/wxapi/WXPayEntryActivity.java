package com.umiwi.ui.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.youmi.pay.event.PayResultEvent;
import cn.youmi.pay.manager.PayResultManager;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umiwi.ui.main.UmiwiAPI;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
    	api = WXAPIFactory.createWXAPI(this, UmiwiAPI.WEIXIN_APP_ID_PAY);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {/*

		Toast.makeText(this, "openid = " + req.openId, Toast.LENGTH_SHORT).show();
		
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
			Toast.makeText(this, R.string.launch_from_wx, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	
	*/}

	@Override
	public void onResp(BaseResp resp) {
//		Intent intent = new Intent("WXPAY");
//		intent.putExtra("data", String.valueOf(resp.errCode));
//		sendBroadcast(intent);
		
//		Message msg = new Message();
//		msg.what = PayDoingActivity.RQF_WEIXIN;
//		Bundle bundle = new Bundle();    
//        bundle.putString("errCode", String.valueOf(resp.errCode));
//        msg.setData(bundle);
//        PayDoingActivity.instance.mHandler.sendMessage(msg);
		PayResultManager.getInstance().setPayResult(PayResultEvent.RQF_WEIXIN, String.valueOf(resp.errCode));
		finish();
		
		finish();
	}
	
}