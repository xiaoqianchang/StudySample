package com.changxiao.downloadservicedemo.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.zritc.colorfulfund.R;
import com.zritc.colorfulfund.base.ZRServiceBase;
import com.zritc.colorfulfund.data.ZRDownloadAppInfo;
import com.zritc.colorfulfund.io.ZRRequestID;
import com.zritc.colorfulfund.utils.ZRAppConfig;
import com.zritc.colorfulfund.utils.ZRConstant;
import com.zritc.colorfulfund.utils.ZRErrors;
import com.zritc.colorfulfund.utils.ZRLog;
import com.zritc.colorfulfund.utils.ZRStrings;

import org.json.JSONException;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * ZRDownloadService.
 * 
 * @author gufei
 * @version 1.0
 * @createDate 2014-05-08
 * @lastUpdate 2014-05-08
 */
public class ZRDownloadService extends ZRServiceBase implements Callback {

	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotificationManager;
	private ZRDownloadAppInfo mDownloadAppInfo;
	private IDownloadAppCallback mDownloadCallback;
	private String mPercentFormat;

	private DownloadBinder mBinder = new DownloadBinder(this);

	private Handler mProgressHandler = new Handler(this);

	public interface IDownloadService {
		public void downloadApp(ZRDownloadAppInfo appInfo);

		public void setDownloadProgressCallback(IDownloadAppCallback callback);

		public ZRDownloadAppInfo getDownloadAppInfo();
	}

	public interface IDownloadAppCallback {
		public void onAppDownloadStarted();

		public void onAppProgress(int progress);

		public void onAppDownloadSuccess();

		public void onAppDownloadError();
	}

	private static class DownloadBinder extends Binder implements
			IDownloadService {

		private WeakReference<ZRDownloadService> mService;

		public DownloadBinder(ZRDownloadService service) {
			mService = new WeakReference<ZRDownloadService>(service);
		}

		@Override
		public void downloadApp(ZRDownloadAppInfo appInfo) {
			ZRDownloadService service = mService.get();
			if (null != service) {
				service.downloadApp(appInfo);
			} else {
				ZRLog.e("downloadApp error, service = null");
			}
		}

		@Override
		public void setDownloadProgressCallback(IDownloadAppCallback callback) {
			ZRDownloadService service = mService.get();
			if (null != service) {
				service.mDownloadCallback = callback;
			} else {
				ZRLog.e("setDownloadProgressCallback error, service = null");
			}
		}

		@Override
		public ZRDownloadAppInfo getDownloadAppInfo() {
			ZRDownloadService service = mService.get();
			if (null != service) {
				return service.mDownloadAppInfo;
			} else {
				ZRLog.e("getDownloadAppInfo error, service = null");
				return null;
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPercentFormat = ZRStrings.get(this, "text_percent_format");
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public void onDestroy() {
		mBuilder = null;
		mNotificationManager = null;
		mDownloadAppInfo = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ZRDownloadAppInfo info = (ZRDownloadAppInfo) intent
				.getSerializableExtra(ZRConstant.KEY_INFO);
		if (null != info) {
			downloadApp(info);
		}
		return START_NOT_STICKY;
	}

	private void downloadApp(ZRDownloadAppInfo appInfo) {
		if (null == mDownloadAppInfo) {
			mDownloadAppInfo = appInfo;
			mDownloadAppInfo
					.setProgress(ZRDownloadAppInfo.DOWNLOAD_PROGRESS_MIN);
			resetNotificationView();
			downloadFile(ZRConstant.REQUEST_DOWNLOAD_APP, false,
					appInfo.getDownloadUrl(), ZRAppConfig.APP_DOWNLOAD_DIR
							+ appInfo.getFileName());
			if (null != mDownloadCallback) {
				mDownloadCallback.onAppDownloadStarted();
			}
			showToast(ZRStrings.get(this, "toast_download_start"));
		}
	}

	@Override
	public void onError(ZRRequestID requestID, String errorCode,
			String errorDesc) {
		super.onError(requestID, errorCode, errorDesc);
		switch (requestID.getRequestID()) {
		case ZRConstant.REQUEST_DOWNLOAD_APP:
			showToast(errorDesc);
			onDownloadAppError();
			break;
		}
	}

	@Override
	public void onResult(ZRRequestID requestID, String result) {
		super.onResult(requestID, result);
		try {
			switch (requestID.getRequestID()) {
			case ZRConstant.REQUEST_DOWNLOAD_APP:
				onDownloadAppFinish();
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			onError(requestID, ZRErrors.ERROR_RESPONSE_FORMAT);
		}
	}

	@Override
	public void onDownloadProgress(ZRRequestID requestID, int progress) {
		switch (requestID.getRequestID()) {
		case ZRConstant.REQUEST_DOWNLOAD_APP:
			mBuilder.setProgress(ZRDownloadAppInfo.DOWNLOAD_PROGRESS_MAX,
					progress, false).setContentText(
					String.format(mPercentFormat, progress));
			mDownloadAppInfo.setProgress(progress);
			mNotificationManager.notify(ZRConstant.NOTIFICATION_ID,
					mBuilder.build());
			if (null != mDownloadCallback) {
				// 不在UI线程，需要切换线程
				mProgressHandler.sendEmptyMessage(progress);
			}
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		mDownloadCallback.onAppProgress(msg.what);
		return true;
	}

	private void resetNotificationView() {
		int drawable = -1;
		// 程序更新下载时的图标设置
		drawable = R.mipmap.icon_share_logo;
		mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(drawable)
				.setContentTitle(mDownloadAppInfo.getName())
				.setContentText(
						String.format(mPercentFormat,
								ZRDownloadAppInfo.DOWNLOAD_PROGRESS_MIN))
				.setWhen(System.currentTimeMillis())
				.setProgress(ZRDownloadAppInfo.DOWNLOAD_PROGRESS_MAX,
						ZRDownloadAppInfo.DOWNLOAD_PROGRESS_MIN, false)
				.setTicker(
						String.format(ZRStrings.get(this,
								"notification_download_app"), mDownloadAppInfo
								.getName())).setAutoCancel(false);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, ZRDownloadService.class), 0);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager.notify(ZRConstant.NOTIFICATION_ID,
				mBuilder.build());
	}

	private void onDownloadAppFinish() throws JSONException {
		String msg = String.format(
				ZRStrings.get(this, "toast_download_success"),
				mDownloadAppInfo.getName());
		showToast(msg);
		mBuilder.setProgress(ZRDownloadAppInfo.DOWNLOAD_PROGRESS_MAX,
				ZRDownloadAppInfo.DOWNLOAD_PROGRESS_MAX, false)
				.setContentText(ZRStrings.get(this, "tip_download_install"))
				.setAutoCancel(true);
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		File file = new File(ZRAppConfig.APP_DOWNLOAD_DIR
				+ mDownloadAppInfo.getFileName());
		installIntent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		mBuilder.setContentIntent(PendingIntent.getActivity(this, 0,
				installIntent, PendingIntent.FLAG_UPDATE_CURRENT));
		mNotificationManager.notify(ZRConstant.NOTIFICATION_ID,
				mBuilder.build());
		startActivity(installIntent);
		mDownloadAppInfo = null;
		if (null != mDownloadCallback) {
			mDownloadCallback.onAppDownloadSuccess();
		}
	}

	private void onDownloadAppError() {
		mNotificationManager.cancel(ZRConstant.NOTIFICATION_ID);
		mDownloadAppInfo = null;
		if (null != mDownloadCallback) {
			mDownloadCallback.onAppDownloadError();
		}
	}

}
