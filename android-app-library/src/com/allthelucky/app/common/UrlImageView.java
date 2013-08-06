package com.allthelucky.app.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.allthelucky.http.RequestListener;
import com.allthelucky.http.RequestManager;


/**
 * ImageView with a network image RequestManager
 * 
 * @author savant-pan
 */
public class UrlImageView extends ImageView {
	private static final int DEFAULT_RESID = -1;

	private int imageResId = DEFAULT_RESID;
	private String imageUrl = "";
	private boolean hasRetry = false;

	/**
	 * @param context
	 */
	public UrlImageView(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public UrlImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public UrlImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * set a url
	 * 
	 * @param url
	 *            network resource address
	 */
	public void setURLAsync(String url) {
		this.setURLAsync(url, DEFAULT_RESID);
	}

	/**
	 * set a url and default drawable
	 * 
	 * @param url
	 *            network resource address
	 * @param imageResId
	 *            drawable id
	 */
	public void setURLAsync(String url, int imageResId) {
		this.imageUrl = url;
		this.imageResId = imageResId;
		this.firstLoad();
	}

	/**
	 * first load image
	 */
	private void firstLoad() {
		this.setImageResource(imageResId);
		this.loadResource();
	}

	/**
	 * retry cache when failed first time
	 */
	private void retryCache() {
		this.loadResource();
	}

	private void loadResource() {
		if (TextUtils.isEmpty(imageUrl)) {
			setImageResource(imageResId);
		} else {
			RequestManager.getInstance().get(getContext(), imageUrl, null, requestListener, true, 0);
		}
	}

	/**
	 * load callback for RequestManager
	 */
	private RequestListener requestListener = new RequestListener() {

		@Override
		public void onStart() {

		}

		@Override
		public void onCompleted(int statusCode, byte[] data, long lastModified, String description, int actionId) {
			if (RequestListener.ERR == statusCode) {
				if (!hasRetry) {
					hasRetry = true;
					retryCache();
				} else {
					setImageResource(imageResId);
				}
			} else {
				if (null != data) {
					// decode image size
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					BitmapFactory.decodeByteArray(data, 0, data.length, o);
					// Find the correct scale value. It should be the power of
					final int REQUIRED_SIZE = 100;
					int width_tmp = o.outWidth, height_tmp = o.outHeight;
					int scale = 1;
					while (true) {
						if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
							break;
						width_tmp /= 2;
						height_tmp /= 2;
						scale *= 2;
					}
					// decode with inSampleSize
					BitmapFactory.Options options = new Options();
					options.inSampleSize = scale;
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
					if (bitmap != null) {
						setImageBitmap(bitmap);
					} else {
						setImageResource(imageResId);
					}
				}
			}
		}
	};

}
