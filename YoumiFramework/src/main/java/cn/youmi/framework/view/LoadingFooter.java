package cn.youmi.framework.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.youmi.framework.R;
import cn.youmi.framework.main.BaseApplication;
import cn.youmi.framework.main.ContextProvider;
import cn.youmi.framework.util.ImageLoader;

/**
 * 列表加载动画
 * 
 * @author tangxiyong 2013-11-21下午6:21:07
 * 
 */
public class LoadingFooter {
	protected Context mContext;
	protected View mLoadingFooter;

	protected TextView mLoadingText;

	protected State mState = State.Idle;

	private ProgressBar mProgress;
	private ImageView emptyImage;
	private TextView emptyText;

	/**
	 * @param TheEnd
	 * @param NoMore
	 * @param Loading
	 * @param Error
	 * @author tangxiong
	 * @version 2014年6月18日 下午4:37:29
	 */
	public enum State {
		Idle, Loading, NoMore, TheEnd, Error, ShowContent, ShowImage, TheEndHint
	}

	public LoadingFooter(Context context) {
		mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.loading_footer, null);
		mContext = context;
		imageLoader = new ImageLoader(BaseApplication.getApplication());
		mProgress = (ProgressBar) mLoadingFooter.findViewById(R.id.progressBar);
		mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.textView);
		
		emptyImage = (ImageView) mLoadingFooter.findViewById(R.id.empty_imageview);
		emptyText = (TextView) mLoadingFooter.findViewById(R.id.empty_text);
		setState(State.Idle);
	}

	public View getView() {
		return mLoadingFooter;
	}

	public State getState() {
		return mState;
	}

	/**
	 * 
	 * @param state
	 * @param delay
	 *            如果需要进度条显示一定时间，调用此方法
	 */
	public void setState(final State state, long delay) {
		mLoadingFooter.postDelayed(new Runnable() {

			@Override
			public void run() {
				setState(state);
			}
		}, delay);
	}

	/**
	 * 
	 * @param status LoadingFooter.State.*
	 */
	public void setState(State status) {
		if (mState == status) {
			return;
		}
		mState = status;

		mLoadingFooter.setVisibility(View.VISIBLE);
		
		switch (status) {
		case Loading:
			mLoadingText.setVisibility(View.INVISIBLE);
			mProgress.setVisibility(View.VISIBLE);
			emptyImage.setVisibility(View.GONE);
			emptyText.setVisibility(View.GONE);
			mLoadingFooter.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			break;

		case NoMore:
			mLoadingText.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.GONE);
			emptyImage.setVisibility(View.GONE);
			emptyText.setVisibility(View.GONE);
			mLoadingText.setText("亲，没有更多内容了...");
			mLoadingText.postDelayed(new Runnable() {
				@Override
				public void run() {
					mLoadingText.setVisibility(View.INVISIBLE);
				}
			}, 3000);
			mLoadingFooter.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			break;

		case TheEnd:
			mLoadingText.setVisibility(View.INVISIBLE);
			mProgress.setVisibility(View.GONE);
			emptyImage.setVisibility(View.GONE);
			emptyText.setVisibility(View.GONE);
			mLoadingFooter.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		case TheEndHint:
			mLoadingText.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.GONE);
			emptyImage.setVisibility(View.GONE);
			emptyText.setVisibility(View.GONE);
			mLoadingText.setText("亲，没有更多内容了...");
			mLoadingText.postDelayed(new Runnable() {
				@Override
				public void run() {
					mLoadingText.setVisibility(View.GONE);
				}
			}, 3000);
			mLoadingFooter.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
			break;

		case Error:
			mLoadingText.setVisibility(View.INVISIBLE);
			mProgress.setVisibility(View.GONE);
			emptyImage.setVisibility(View.VISIBLE);
			emptyImage.setBackgroundResource(R.drawable.content_empty);
//			imageLoader.loadImage(R.drawable.content_empty, emptyImage);
			emptyText.setVisibility(View.VISIBLE);
			emptyText.setText(loadingStr);
			break;
		case ShowContent:
			emptyImage.setVisibility(View.VISIBLE);
			emptyText.setVisibility(View.VISIBLE);
			emptyText.setText(emptyStr);
			mLoadingText.setVisibility(View.GONE);
			mProgress.setVisibility(View.GONE);
			emptyImage.setBackgroundResource(R.drawable.content_empty);
//			imageLoader.loadImage(R.drawable.content_empty, emptyImage);
			
//			mLoadingFooter.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
			break;
		case ShowImage:
			emptyImage.setVisibility(View.VISIBLE);
			
			imageLoader.loadImage(emptyImageId, emptyImage);
//			emptyImage.setBackgroundResource(emptyImageId);
			emptyText.setVisibility(View.VISIBLE);
			emptyText.setText(emptyStr);
			mLoadingText.setVisibility(View.GONE);
			mProgress.setVisibility(View.GONE);
			break;

		default:
			mLoadingText.setVisibility(View.GONE);
			mLoadingFooter.setVisibility(View.GONE);
			mProgress.setVisibility(View.GONE);
			emptyImage.setVisibility(View.GONE);
			emptyText.setVisibility(View.GONE);
			break;
		}
	}
	
	private String loadingStr = "加载失败,点击重试";
	
	private String emptyStr;
	
	private int emptyImageId;
	private ImageLoader imageLoader;

	public void setContentStr(String emptyStr) {
		this.emptyStr = emptyStr;
	}

	public void setContentImageId(int emptyImageId) {
		this.emptyImageId = emptyImageId;
	}
	
	public void setLoadinHint() {
		mLoadingText.setVisibility(View.GONE);
		mLoadingFooter.setVisibility(View.GONE);
		mProgress.setVisibility(View.GONE);
		emptyImage.setVisibility(View.GONE);
		emptyText.setVisibility(View.GONE);
	}
	
	
	
}
