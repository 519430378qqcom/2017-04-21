package cn.youmi.framework.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.volley.VolleyUrlLoader;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.youmi.framework.debug.LogUtils;
import cn.youmi.framework.http.volley.MHurlStack;
import cn.youmi.framework.main.BaseApplication;

public class ImageLoader {
	
	private static RequestQueue mRequestQueue;
	private static Cache openCache() {
		return new DiskBasedCache(CacheUtils.getDiskCacheDir(),
				10 * 1024 * 1024);
	}
	
	CenterCrop mCenterCrop;
	Transformation<Bitmap> mNone;
	
	public static void registerListener(Context context) {
		mRequestQueue = new RequestQueue(openCache(), new BasicNetwork(new MHurlStack()));
		mRequestQueue.start();
		Glide.get(context).register(GlideUrl.class, InputStream.class,
		        new VolleyUrlLoader.Factory(mRequestQueue));
	}
	
	private final ModelCache<String, GlideUrl> modelCache = new ModelCache<String, GlideUrl>(500);
	
	private Context mContext;
	
	public ImageLoader(Context context) {
		// ImageModelLoader imageLoader = new ImageModelLoader(context,  modelCache);
		if (null == context) {
			this.mContext = BaseApplication.getApplication();
		} else {
			this.mContext = context;
		}

		mCenterCrop = new CenterCrop(Glide.get(context).getBitmapPool());
	}
	
	public void loadImage(String url, ImageView imageView) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Glide.with(mContext).load(url).crossFade().into(imageView);
	}
	
	public void loadImage(String url, ImageView imageView, int imageLoading) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Glide.with(mContext).load(url).crossFade().placeholder(imageLoading).into(imageView);
//		Glide.with(mContext).load(url).crossFade().into(imageView);
	}
	
	public void loadBitmap(String url,ImageView iv){
		Glide.with(mContext).load(url).asBitmap().into(iv);
	}
	
//	public void loadImage(String url, ImageView imageView, int errorResurceId) {
//		Glide.with(mContext).load(url).error(errorResurceId).into(imageView);
//	}
	public void loadImage(String url,ImageView iv, RequestListener<? super String, GlideDrawable> requestListener) {
		Glide.with(mContext).load("").listener(requestListener).into(iv);
	}
	
	public void loadImage(int resourceId, ImageView imageView) {
		Glide.with(mContext).load(resourceId).into(imageView);
	}
	public void loadImage(int resourceId, ImageView imageView, int imageLoading) {
		Glide.with(mContext).load(resourceId).placeholder(imageLoading).into(imageView);
	}

	public void loadImage(File file, ImageView imageView) {
		Glide.with(mContext).load(file).into(imageView);
	}
	
	public void loadImage(Uri uri, ImageView imageView) {
		Glide.with(mContext).load(uri).into(imageView);
	}
	
	public void loadImage(byte[] model, ImageView imageView) {
		Glide.with(mContext).load(model).into(imageView);
	}

	class ImageRequestListener<T> implements RequestListener<T, GlideDrawable> {

		@Override
		public boolean onException(Exception arg0, T arg1,
				Target<GlideDrawable> arg2, boolean arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onResourceReady(GlideDrawable arg0, T arg1,
				Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	

	private static class ImageModelLoader extends BaseGlideUrlLoader<String> {
		
		public ImageModelLoader(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}


		public ImageModelLoader(Context context,
				ModelCache<String, GlideUrl> modelCache) {
			super(context, modelCache);
		}

//	    public static class Factory implements ModelLoaderFactory<String, InputStream> {
//	    	private final ModelCache<String, GlideUrl> modelCache = new ModelCache<String, GlideUrl>(500);
//			@Override
//			public ModelLoader<String, InputStream> build(Context context,
//					GenericLoaderFactory factories) {
//				return new ImageModelLoader(context, modelCache);
//			}
//
//			@Override
//			public void teardown() {
//				
//			}
//	    	
//	    }

		private static final Pattern PATTERN = Pattern.compile("__w-((?:-?\\d+)+)__");


		@Override
		protected String getUrl(String model, int width, int height) {
			Matcher m = PATTERN.matcher(model);
			int bestBucket = 0;
			if (m.find()) {
				String[] found = m.group(1).split("-");
				for (String bucketStr : found) {
					bestBucket = Integer.parseInt(bucketStr);
					if (bestBucket >= width) {
						// the best bucket is the first immediately bigger than
						// the requested width
						break;
					}
				}
				if (bestBucket > 0) {
					model = m.replaceFirst("w" + bestBucket);
					LogUtils.e("imageloader", "width=" + width
							+ ", URL successfully replaced by " + model);
				}
			}
			return model;
		}
	}

}
