package com.changxiao.downloadservicedemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.zritc.colorfulfund.data.ZRDataEngine;
import com.zritc.colorfulfund.io.ZRFileDownloadTask;
import com.zritc.colorfulfund.io.ZRFileDownloadTask.TFDownloadProgressCallback;
import com.zritc.colorfulfund.io.ZRHttpTask;
import com.zritc.colorfulfund.io.ZRRequestID;
import com.zritc.colorfulfund.io.ZRTaskCallback;
import com.zritc.colorfulfund.io.ZRTaskDelegate;
import com.zritc.colorfulfund.utils.ZRConstant;
import com.zritc.colorfulfund.utils.ZRErrors;
import com.zritc.colorfulfund.utils.ZRToastFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * The base service in application.
 * 
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public abstract class ZRServiceBase extends Service {

	protected ZRDataEngine mDataEngine;

	protected ZRTaskCallback mTaskCallback = new ZRTaskCallback() {

		@Override
		public void onResult(ZRRequestID requestID, String result) {
			ZRServiceBase.this.onResult(requestID, result);
		}

		@Override
		public void onError(ZRRequestID requestID, String errorCode,
				String errorDesc) {
			if (TextUtils.isEmpty(errorDesc)) {
				ZRServiceBase.this.onError(requestID, errorCode);
			} else {
				ZRServiceBase.this.onError(requestID, errorCode, errorDesc);
			}
		}
	};

	protected TFDownloadProgressCallback mProgressCallback = new TFDownloadProgressCallback() {

		@Override
		public void onDownloadProgress(ZRRequestID requestID, int progress) {
			ZRServiceBase.this.onDownloadProgress(requestID, progress);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mDataEngine = ZRDataEngine.getInstance();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void postMessage(int requestID, String server, String method,
                            String encryption, String msg) {
		postMessage(new ZRRequestID(requestID), server, method, encryption, msg);
	}

	public void postMessage(ZRRequestID requestID, String server,
                            String method, String encryption, String msg) {
		ZRHttpTask task = new ZRHttpTask(requestID, mTaskCallback);
		try {
			ZRTaskDelegate.execute(task, server, method, encryption, msg);
		} catch (IOException e) {
			onError(requestID, ZRErrors.ERROR_NETWORK);
		}
	}

	protected void downloadFile(int requestID, boolean isUTF8, String url,
			String target) {
		downloadFile(new ZRRequestID(requestID), isUTF8, url, target);
	}

	protected void downloadFile(ZRRequestID requestID, boolean isUTF8,
                                String url, String target) {
		ZRFileDownloadTask task = new ZRFileDownloadTask(requestID, isUTF8,
				mTaskCallback, mProgressCallback);
		try {
			ZRTaskDelegate.execute(task, url, target);
		} catch (IOException e) {
			onError(requestID, ZRErrors.ERROR_NETWORK);
		}
	}

	protected void onError(ZRRequestID requestID, String errorCode) {
		String errorDesc = ZRErrors.getLocalErrorMsg(this, errorCode);
		onError(requestID, errorCode, errorDesc);
	}

	protected void onError(ZRRequestID requestID, String errorCode,
			String errorDesc) {

	}

	protected void onResult(ZRRequestID requestID, String result) {
	}

	protected void onDownloadProgress(ZRRequestID requestID, int progress) {

	}

	protected JSONObject getParamsFromResp(ZRRequestID requestID, String result)
			throws JSONException {
		JSONObject json = new JSONObject(result);
		String respCode = json.getString(ZRConstant.KEY_RESP_CODE);
		if (ZRErrors.SUCCESS.equals(respCode)) {
			return json.getJSONObject(ZRConstant.KEY_PARAMS);
		} else {
			String respDesc = json.getString(ZRConstant.KEY_RESP_DESC);
			onError(requestID, respCode, respDesc);
			return null;
		}
	}

	protected void showToast(String msg) {
		ZRToastFactory.getToast(this, msg).show();
	}

	protected void cancelToast() {
		ZRToastFactory.cancelToast();
	}
}
