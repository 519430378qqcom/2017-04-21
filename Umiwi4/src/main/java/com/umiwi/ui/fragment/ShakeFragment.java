package com.umiwi.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umiwi.ui.R;
import com.umiwi.ui.activity.UmiwiContainerActivity;
import com.umiwi.ui.adapter.UmiwiShakeCouponGridViewAdapter;
import com.umiwi.ui.beans.UmiwiShakeBean;
import com.umiwi.ui.beans.UmiwiShakeCouponBean;
import com.umiwi.ui.dialog.ShakeResultDialog;
import com.umiwi.ui.dialog.ShareDialog;
import com.umiwi.ui.main.UmiwiAPI;
import com.umiwi.ui.main.UmiwiApplication;
import com.umiwi.ui.managers.NoticeManager;
import com.umiwi.ui.managers.YoumiRoomUserManager;
import com.umiwi.ui.util.CommonHelper;
import com.umiwi.ui.util.LoginUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.youmi.framework.fragment.BaseFragment;
import cn.youmi.framework.http.AbstractRequest;
import cn.youmi.framework.http.AbstractRequest.Listener;
import cn.youmi.framework.http.GetRequest;
import cn.youmi.framework.http.HttpDispatcher;
import cn.youmi.framework.http.parsers.GsonParser;
import cn.youmi.framework.util.NetworkManager;
import cn.youmi.framework.util.ToastU;

/**
 * @author tjie00
 * @version 2014年9月11日 下午2:17:59 TODO
 */
public class ShakeFragment extends BaseFragment {

	private GridView gvShakeCoupon;
	private TextView tvMineCoupons;
	private TextView tvShakeAD;
	private SensorManager mSensorManager;
	private Vibrator vibrator;
	private SoundPool soundPool;
	private int soundID = -1;

	private UmiwiSensorEventListener uSensorEventListener;
	private UmiwiShakeCouponGridViewAdapter couponAdapter;
	private Animation shakeAnimation;

	private ShakeResultDialog resultDialog;
	private boolean isLogined = false;
	private boolean isShakeable = false;
	public boolean isShaking = true;

	private ProgressBar loading;

	private CouponBeanListener couponBeanListener;
	private GetRequest<UmiwiShakeCouponBean> couponRequest;
	private ShakeBeanListener shakeBeanListener;
	private GetRequest<UmiwiShakeBean> shakeRequest;


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.toolbar_share, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.share:
			ShareDialog.getInstance().showDialog(getActivity(),
					getString(R.string.share_app_title),
					"@优米网 手机客户端的摇一摇功能棒棒哒，免费看付费课程。 http://m.youmi.cn/m.shtml",
					getString(R.string.share_app_weburl),
					getString(R.string.share_app_image));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			umiwiShakeBean = (UmiwiShakeBean) savedInstanceState
					.getSerializable("umiwiShakeBean");
		}
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}


	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shake, null);
		mActionBarToolbar = (Toolbar) view.findViewById(R.id.toolbar_actionbar);
		setSupportActionBarAndToolbarTitle(mActionBarToolbar, "摇一摇");
		mSensorManager = (SensorManager) getActivity().getSystemService(Service.SENSOR_SERVICE);

		isLogined = checkLoginState();

		if (!isLogined) {
			ToastU.showShort(getActivity(), "登录之后才可以摇优惠哦！");// 开线程
			LoginUtil.getInstance().showLoginView(getActivity());
		}

		isShakeable = isShakeAvailble();
		if (isShakeable) {

			getCouponsList();
		} else {
			ToastU.showShort(getActivity(),"么么哒，网络出错了，过会再试试！");// 开线程
		}
		initView(view);
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (uSensorEventListener != null) {
			mSensorManager.unregisterListener(uSensorEventListener);
			uSensorEventListener = null;
		}
		isShaking = true;
		MobclickAgent.onPageEnd(fragmentName);
	}

	@Override
	public void onResume() {
		super.onResume();
		activateSendor();
		MobclickAgent.onPageStart(fragmentName);
	}



	// 初始化优惠列表出现问题
	public void getCouponListError() {
		Toast.makeText(getActivity(), "网络有问题,请稍后重试.", Toast.LENGTH_SHORT).show();
		isShaking = true;
	}

	// 获取摇奖结果异常
	// 要改为默认没摇到
	public void getCouponResultError() {
		Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
		isShaking = false;
	}

	public void initCouponAdapter(ArrayList<String> couponURLs) {
		if (couponAdapter == null) {
			couponAdapter = new UmiwiShakeCouponGridViewAdapter(getActivity(), couponURLs);
			gvShakeCoupon.setAdapter(couponAdapter);
		} else {
			couponAdapter.setData(couponURLs);
			couponAdapter.notifyDataSetChanged();
		}

		isShaking = false;

	}

	public void upDateShakeAD(String shakeOtherAD) {
		tvShakeAD.setText(shakeOtherAD);
	}

	// check is the user login or not
	private boolean checkLoginState() {

		return YoumiRoomUserManager.getInstance().isLogin();
	}

	private boolean getNetState() {
		return NetworkManager.getInstance().checkNet(UmiwiApplication.getContext());
	}

	private boolean isShakeAvailble() {
		return checkLoginState() && getNetState();
	}

	// find the ui
	public void initView(View view) {

		gvShakeCoupon = (GridView) view.findViewById(R.id.gv_shake_coupon);
		gvShakeCoupon.setSelector(new ColorDrawable(Color.TRANSPARENT));
		tvMineCoupons = (TextView) view.findViewById(R.id.tv_mine_coupons);
		tvMineCoupons.setClickable(true);
		tvMineCoupons.setOnClickListener(mineCouponClickListener);

		tvShakeAD = (TextView) view.findViewById(R.id.tv_shake_couponad);

		uSensorEventListener = new UmiwiSensorEventListener();
		// 震动
		vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
		// soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundPool = new SoundPool(1, 0, 0);

		loading = (ProgressBar) view.findViewById(R.id.loading);

		loadShakeAudio();

	}

	private void loadShakeAudio() {
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = UmiwiApplication.getContext().getAssets().openFd("shake_reward.wav");
			soundID = soundPool.load(fileDescriptor, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * request for the coupons' content, get the rest of the shake times;
	 */
	private void getCouponsList() {

		if (couponBeanListener == null)
			couponBeanListener = new CouponBeanListener();

		if (couponRequest == null)
			couponRequest = new GetRequest<UmiwiShakeCouponBean>(UmiwiAPI.COUPON_LIST_URL, GsonParser.class, UmiwiShakeCouponBean.class, couponBeanListener);

		HttpDispatcher.getInstance().go(couponRequest);
	}

	private void activateSendor() {
		if (isShakeAvailble()) {

			isShaking = false;

			if (uSensorEventListener == null) {
				uSensorEventListener = new UmiwiSensorEventListener();
			}

			List<Sensor> sensor = mSensorManager.getSensorList(Sensor.TYPE_ALL);
			for (Sensor sen : sensor) {
				switch (sen.getType()) {
				case Sensor.TYPE_ACCELEROMETER:

					mSensorManager.registerListener(uSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
					break;
				case Sensor.TYPE_GYROSCOPE:

					mSensorManager.registerListener(uSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
					break;

				}

			}

		}

	}

	private void onSensorEvenChange(float rocking, float[] values) {

		vibrator.vibrate(500);
		if (-1 != soundID) {
			soundPool.play(soundID, 0.4f, 0.4f, 0, 0, 1);
		}
		rocking = values[0];
		if (!isShaking) {
			if (isShakeAvailble()) {
				getCouponResult();
				getCouponsList();
			}
		}

		if (uSensorEventListener != null) {
			mSensorManager.unregisterListener(uSensorEventListener);
		}

	}

	private class UmiwiSensorEventListener implements SensorEventListener {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			int sensorType = event.sensor.getType();
			if (sensorType != Sensor.TYPE_ACCELEROMETER && sensorType != Sensor.TYPE_GYROSCOPE) {
				return;
			}
			float[] values = event.values;
			float rocking = 0;
			if (sensorType == Sensor.TYPE_ACCELEROMETER) {
				if (Math.abs(values[0]) > 13.0 || Math.abs(values[1]) > 13.0 || Math.abs(values[2]) > 13.0) {
					onSensorEvenChange(rocking, values);
				}
				else {
					rocking = 0;
				}
			} else {

				if (Math.abs(values[0]) > 10.0 || Math.abs(values[1]) > 10.0 || Math.abs(values[2]) > 10.0) {
					onSensorEvenChange(rocking, values);
				}
				else {
					rocking = 0;
				}

			}
			shakeAnimation = new RotateAnimation(values[0], rocking, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
			shakeAnimation.setDuration(400);

			if (gvShakeCoupon != null) {
				int size = gvShakeCoupon.getChildCount();
				for (int i = 0; i < size; i++) {
					gvShakeCoupon.getChildAt(i).startAnimation(shakeAnimation);
				}
			}
		}
	}

	public void getCouponResult() {
		if (shakeBeanListener == null)
			shakeBeanListener = new ShakeBeanListener();
		if (shakeRequest == null)
			shakeRequest = new GetRequest<UmiwiShakeBean>(UmiwiAPI.COUPON_CONTENT_URL + "?" + CommonHelper.getChannelModelViesion(), GsonParser.class, UmiwiShakeBean.class, shakeBeanListener);
		HttpDispatcher.getInstance().go(shakeRequest);
	}

	private UmiwiShakeBean umiwiShakeBean;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("umiwiShakeBean", umiwiShakeBean);
	}

	// show the coupon content in pop up window
	public void showCouponResult(final UmiwiShakeBean bean) {
		if (resultDialog == null || !resultDialog.isVisible()) {
			resultDialog = new ShakeResultDialog();
		}

		resultDialog.setData(bean);
		resultDialog.shakeFragment = this;
		if (!resultDialog.isVisible() && null != getActivity()) {
			try {
				resultDialog.show(this.getActivity().getSupportFragmentManager(), ShakeResultDialog.TAG);
			} catch (Exception e) {
				e.printStackTrace();
			}



		}

		resultDialog.dismissListener = new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (resultDialog != null) {
					resultDialog = null;
				}
				activateSendor();
			}
		};

	}

	@Override
	public void onStop() {
		super.onStop();
		if (uSensorEventListener != null) {
			mSensorManager.unregisterListener(uSensorEventListener);
			uSensorEventListener = null;
		}

	}

	class CouponBeanListener implements Listener<UmiwiShakeCouponBean> {
		@Override
		public void onResult(AbstractRequest<UmiwiShakeCouponBean> request, UmiwiShakeCouponBean bean) {
			loading.setVisibility(View.GONE);

			if (bean == null) {
				return;
			}
			if (!"nologin".equalsIgnoreCase(bean.getStatus())) {

				String shakeOtherAD = bean.getLotteryuser();

				if (!TextUtils.isEmpty(shakeOtherAD)) {
					tvShakeAD.setText(shakeOtherAD);
				} else {
					tvShakeAD.setText("");
				}

				if (couponAdapter == null) {
					ArrayList<String> couponURLs = bean.getRecord();
					couponAdapter = new UmiwiShakeCouponGridViewAdapter(getActivity(), couponURLs);
					gvShakeCoupon.setAdapter(couponAdapter);
				} else {
					couponAdapter.notifyDataSetChanged();
				}

				isShaking = false;
			} else {
				ToastU.showShort(getActivity(),"登录之后才可以摇优惠哦！");
				LoginUtil.getInstance().showLoginView(getActivity());
			}
			if (YoumiRoomUserManager.getInstance().isLogin())
				NoticeManager.getInstance().loadNotice();
		}

		@Override
		public void onError(AbstractRequest<UmiwiShakeCouponBean> requet, int statusCode, String body) {
			loading.setVisibility(View.GONE);

			ToastU.showShort(getActivity(),"么么哒，网络出错了，过会再试试！");
			isShaking = true;
		}
	}

	class ShakeBeanListener implements Listener<UmiwiShakeBean> {

		@Override
		public void onResult(AbstractRequest<UmiwiShakeBean> request, UmiwiShakeBean bean) {
			if (bean != null && !"nologin".equalsIgnoreCase(bean.getStatus())) {
				umiwiShakeBean = bean;
				showCouponResult(umiwiShakeBean);
			} else {
				ToastU.showShort(getActivity(),"登录之后才可以摇优惠哦！");
				LoginUtil.getInstance().showLoginView(getActivity());
			}
		}

		@Override
		public void onError(AbstractRequest<UmiwiShakeBean> requet, int statusCode, String body) {
			ToastU.showShort(getActivity(),"么么哒，网络出错了，过会再试试！");
			isShaking = false;
		}
	}

	private View.OnClickListener mineCouponClickListener = new View.OnClickListener() {
		long lastCommit = 0;

		@Override
		public void onClick(View v) {
			if (System.currentTimeMillis() - lastCommit < 1000) {
				return;
			}
			lastCommit = System.currentTimeMillis();

			Intent couponIntent = new Intent(getActivity(), UmiwiContainerActivity.class);
			couponIntent.putExtra(UmiwiContainerActivity.KEY_FRAGMENT_CLASS, ShakeCoupondFragment.class);
			getActivity().startActivity(couponIntent);
		}
	};

}
