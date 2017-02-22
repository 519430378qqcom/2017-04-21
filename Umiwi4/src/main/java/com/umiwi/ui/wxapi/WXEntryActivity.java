package com.umiwi.ui.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.youmi.account.manager.WeiXinTokenKeyManager;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umiwi.ui.main.UmiwiAPI;


/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	private SendAuth.Resp authResp;
	
	@Override
	protected void onCreate(Bundle arg0) {
		api = WXAPIFactory.createWXAPI(WXEntryActivity.this, UmiwiAPI.WEIXIN_APP_ID,false);
        api.registerApp(UmiwiAPI.WEIXIN_APP_ID); 
//        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID,false);
//        api.registerApp(Constants.APP_ID); 
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api.handleIntent(getIntent(), WXEntryActivity.this);	
		super.onCreate(arg0);
	}

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	
	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		
	}
	
	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
//		Toast.makeText(this, "openid = " + resp.openId, Toast.LENGTH_SHORT).show();
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//oauth授权
//			Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code, Toast.LENGTH_SHORT).show();
			authResp = (SendAuth.Resp)resp;
		}
		switch (resp.errCode) {
		
		case BaseResp.ErrCode.ERR_OK://发送成功
			if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
				Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
			} else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//				Message msg = new Message();
//				msg.what = LoginActivity.WX_HANDLER_CODE;
//				Bundle bundle = new Bundle();    
//		        bundle.putString("wxtoken", authResp.code);
//		        msg.setData(bundle);
//		        LoginActivity.instance.wxHandler.sendMessage(msg);
				WeiXinTokenKeyManager.getInstance().setWXTokenKey(authResp.code);
			} 
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL://发送取消
			Toast.makeText(this, "发送取消", Toast.LENGTH_LONG).show();
			WeiXinTokenKeyManager.getInstance().setWXTokenKey(resp.errCode + "");
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED://发送被拒绝
			Toast.makeText(this, "授权取消", Toast.LENGTH_LONG).show();
			WeiXinTokenKeyManager.getInstance().setWXTokenKey(resp.errCode + "");
			break;
		default:
			Toast.makeText(this, "服务器繁忙", Toast.LENGTH_LONG).show();
			WeiXinTokenKeyManager.getInstance().setWXTokenKey(resp.errCode + "");
			break;
		}
		
		WXEntryActivity.this.finish();	
	}	
	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
		startActivity(iLaunchMyself);
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}
}