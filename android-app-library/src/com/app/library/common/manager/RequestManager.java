package com.app.library.common.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Request Manager based on AsyncHttpClient
 * 
 * @author savant-pan
 * 
 */
public class RequestManager {
	private final AsyncHttpClient asyncHttpClient;
	private static RequestManager INSTANCE = null;

	protected RequestManager() {
		this.asyncHttpClient = new AsyncHttpClient();
	}

	public static RequestManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RequestManager();
		}
		return INSTANCE;
	}

	/**
	 * 清空缓存文件
	 * 
	 * @param context
	 */
	public static void clearHttpCache(Context context) {
		context.getSharedPreferences("cachefiles", Context.MODE_PRIVATE).edit().clear().commit();
		final String fl[] = context.fileList();
		try {
			for (String f : fl) {
				context.deleteFile(f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancel(Context context) {
		asyncHttpClient.cancelRequests(context, true);
	}

	/**
	 * 参数列表请求
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param requestListener
	 * @param actionId
	 */
	public void post(Context context, String url, RequestParams params, RequestListener requestListener, int actionId) {
		asyncHttpClient.post(context, url, params, new HttpRequestListener(requestListener, actionId));
	}

	/**
	 * JSON　参数请求
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param requestListener
	 * @param actionId
	 */
	public void post(Context context, String url, JSONObject params, RequestListener requestListener, int actionId) {
		asyncHttpClient.post(context, url, rpcToEntity(params.toString(), "application/json"), "application/json",
				new HttpRequestListener(requestListener, actionId));
	}

	/**
	 * JSON　参数请求
	 * 
	 * @param context
	 * @param url
	 * @param headers
	 * @param params
	 * @param requestListener
	 * @param actionId
	 */
	public void post(Context context, String url, Header[] headers, JSONObject params, RequestListener requestListener,
			int actionId) {
		asyncHttpClient.post(context, url, headers, rpcToEntity(params.toString(), "application/json"),
				"application/json", new HttpRequestListener(requestListener, actionId));
	}

	/**
	 * XML　参数请求
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param requestListener
	 * @param actionId
	 */
	public void post(Context context, String url, String params, RequestListener requestListener, int actionId) {
		asyncHttpClient.post(context, url, rpcToEntity(params, "application/xml"), "application/xml",
				new HttpRequestListener(requestListener, actionId));
	}

	/**
	 * XML　参数请求
	 * 
	 * @param context
	 * @param url
	 * @param headers
	 * @param params
	 * @param requestListener
	 * @param actionId
	 */
	public void post(Context context, String url, Header[] headers, String params, RequestListener requestListener,
			int actionId) {
		asyncHttpClient.post(context, url, headers, rpcToEntity(params, "application/xml"), "application/xml",
				new HttpRequestListener(requestListener, actionId));
	}

	/**
	 * get数据
	 * 
	 * @param context
	 * @param url
	 * @param requestListener
	 * @param actionId
	 */
	public void get(Context context, String url, RequestListener requestListener, int actionId) {
		get(context, url, requestListener, false, actionId);
	}

	/**
	 * get数据
	 * 
	 * @param context
	 * @param url
	 * @param requestListener
	 * @param cache
	 * @param actionId
	 */
	public void get(Context context, String url, RequestListener requestListener, boolean cache, int actionId) {
		final String encodeUrl = urlEncode(url);
		if (!cache) {
			asyncHttpClient.get(context, url, new HttpRequestListener(requestListener, actionId));
		} else {
			if (!hasCache(context, encodeUrl)) {
				loadAndSaveResource(context, encodeUrl, requestListener, 0l, actionId);
			} else {
				loadCache(context, encodeUrl, requestListener, actionId);
				if (!hasNetwork(context)) {
					return;
				} else {
					checkUpdate(context, encodeUrl, actionId);
				}
			}
		}
	}

	/**
	 * 加载并缓存网络数据
	 * 
	 * @param context
	 * @param url
	 * @param requestListener
	 * @param actionId
	 */
	private void loadAndSaveResource(final Context context, final String url, final RequestListener requestListener,
			final long lastModified, final int actionId) {
		asyncHttpClient.get(context, url, new HttpRequestListener(new CacheRequestListener(context, url,
				requestListener, lastModified), actionId));
	}

	/**
	 * 检测/更新缓存
	 * 
	 * @param context
	 * @param url
	 * @param actionId
	 */
	private void checkUpdate(final Context context, final String url, final int actionId) {
		final SharedPreferences pref = context.getSharedPreferences("cachefiles", Context.MODE_PRIVATE);
		final String fileName = encryptMD5(url);
		new AsyncTask<Void, Void, Long>() {
			@Override
			protected Long doInBackground(Void... params) {
				long lastModified = -1l;
				try {
					final URL u = new URL(url);
					final HttpURLConnection conn = (HttpURLConnection) u.openConnection();
					conn.setConnectTimeout(5 * 1000);
					conn.setRequestProperty("User-agent", "Mozilla/4.0");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Charset", "UTF-8");
					conn.setRequestMethod("GET");
					conn.connect();
					if (conn.getResponseCode() == 200) {
						lastModified = conn.getLastModified();
					}
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return lastModified;
			}

			protected void onPostExecute(Long result) {
				if (result != -1l && result != pref.getLong(fileName, 0l)) {
					loadAndSaveResource(context, url, null, result, actionId);// 不返回数据到接口
				}
			}
		}.execute();
	}

	/**
	 * 读缓存
	 * 
	 * @param context
	 * @param url
	 * @param requestListener
	 * @param actionId
	 */
	private void loadCache(final Context context, final String url, final RequestListener requestListener,
			final int actionId) {
		requestListener.onStart();
		new AsyncTask<Void, Void, byte[]>() {
			@Override
			protected byte[] doInBackground(Void... params) {
				try {
					InputStream is = context.openFileInput(encryptMD5(url));
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					byte[] bytes = new byte[4096];
					int len = 0;
					while ((len = is.read(bytes)) > 0) {
						bos.write(bytes, 0, len);
					}
					bos.flush();
					return bos.toByteArray();

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			protected void onPostExecute(byte[] result) {
				boolean flag = (result != null);
				requestListener.onCompleted(result, (flag ? RequestListener.OK : RequestListener.ERR),
						flag ? "load cache ok" : "load cache error", actionId);
			}
		}.execute();
	}

	/**
	 * 检测缓存
	 */
	private boolean hasCache(Context context, String url) {
		try {
			context.openFileInput(encryptMD5(url));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 将JSON/XML字符串转为HttpEntity(StringEntity)
	 * 
	 * @param params
	 * @param contentType
	 * @return
	 */
	public static HttpEntity rpcToEntity(String params, String contentType) {
		StringEntity entity = null;
		if (!TextUtils.isEmpty(params)) {
			try {
				entity = new StringEntity(params, HTTP.UTF_8);
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	/**
	 * 网络请求+缓存处理
	 */
	private class CacheRequestListener implements RequestListener {
		private Context context = null;
		private String url = "";
		private RequestListener requestListener = null;
		private long lastModified;

		public CacheRequestListener(Context context, String url, RequestListener requestListener, long lastModified) {
			this.context = context;
			this.url = url;
			this.requestListener = requestListener;
			this.lastModified = lastModified;
		}

		@Override
		public void onStart() {
			if (requestListener != null) {
				requestListener.onStart();
			}
		}

		@Override
		public void onCompleted(byte[] data, int statusCode, String description, int actionId) {
			if (requestListener != null) {
				requestListener.onCompleted(data, statusCode, description, actionId);
			}
			if (data != null && statusCode != RequestListener.ERR) {
				saveCache(context, url, data);
			}
		}

		/**
		 * 保存数据
		 */
		private void saveCache(Context context, String url, byte[] data) {
			try {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				FileOutputStream os = context.openFileOutput(encryptMD5(url), Context.MODE_PRIVATE);

				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(buffer)) > 0) {
					os.write(buffer, 0, len);
				}

				os.close();
				inputStream.close();
				saveLastModified();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void saveLastModified() {
			context.getSharedPreferences("cachefiles", Context.MODE_PRIVATE).edit()
					.putLong(encryptMD5(url), lastModified).commit();
		}
	}

	/**
	 * 网络请求处理
	 */
	private class HttpRequestListener extends AsyncHttpResponseHandler {
		private RequestListener requestListener;
		private int actionId;

		public HttpRequestListener(RequestListener requestListener, int actionId) {
			this.requestListener = requestListener;
			this.actionId = actionId;
		}

		@Override
		public void onStart() {
			super.onStart();
			requestListener.onStart();
		}

		@Override
		public void onFinish() {
			super.onFinish();
		}

		@Override
		public void onSuccess(int statusCode, byte[] binaryData) {
			super.onSuccess(statusCode, binaryData);
			requestListener.onCompleted(binaryData, RequestListener.OK, "server response ok", actionId);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			requestListener.onCompleted(null, RequestListener.ERR, content, actionId);
		}
	}

	/**
	 * 检验网络是否有连接，有则true，无则false
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 16位MD5
	 */
	public static String encryptMD5(String strInput) {
		return encryptMD532(strInput).substring(8, 24);
	}

	public static String encryptMD532(String strInput) {
		StringBuffer buf = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(strInput.getBytes("UTF-8"));
			byte b[] = md.digest();
			buf = new StringBuffer(b.length * 2);
			for (int i = 0; i < b.length; i++) {
				if (((int) b[i] & 0xff) < 0x10) { /* & 0xff转换无符号整型 */
					buf.append("0");
				}
				buf.append(Long.toHexString((int) b[i] & 0xff)); // 转换16进制,下方法同
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return buf.toString();
	}

	/**
	 * 网址汉字编码
	 */
	public static String urlEncode(String str) {
		StringBuffer buf = new StringBuffer();
		byte c;
		byte[] utfBuf;
		try {
			utfBuf = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("URLEncode: Failed to get UTF-8 bytes from string.");
			utfBuf = str.getBytes();
		}
		for (int i = 0; i < utfBuf.length; i++) {
			c = utfBuf[i];
			if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
					|| (c == '.' || c == '-' || c == '*' || c == '_')
					|| (c == ':' || c == '/' || c == '=' || c == '?' || c == '&' || c == '%')) {
				buf.append((char) c);
			} else {
				buf.append("%").append(Integer.toHexString((0x000000FF & c)));
			}
		}
		return buf.toString();
	}
}
