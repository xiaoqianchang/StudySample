package com.changxiao.downloadservicedemo.io;

import android.os.AsyncTask;
import android.os.Bundle;

import com.zritc.colorfulfund.utils.ZRConstant;
import com.zritc.colorfulfund.utils.ZRErrors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.net.ssl.SSLHandshakeException;

/**
 * Task to send HTTP request. 4 params should be set when
 * {@link #execute(String...)} is called. The first parameter is the URL of the
 * server, the second parameter is {@link ZRHttpManager#POST} or
 * {@link ZRHttpManager#GET}, the third parameter is "true" or "false" to
 * indicate whether we need to encrypt the message or not, and the last
 * parameter is the content of the request message.
 * 
 * @author eric
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public class ZRHttpTask extends AsyncTask<Object, Integer, Bundle> {
	public static final String ENCRYPT_NONE = "0";
	public static final String ENCRYPT_SESSION = "1";

	private static HashMap<String, String> sHeaderNormal = new HashMap<String, String>();

	static {
		sHeaderNormal.put(ZRConstant.KEY_LOCALE, "zh-CN");
		// sHeaderNormal.put(UPConstant.KEY_ACCEPT_ENCODING,
		// ZRHttpManager.GZIP);
	}

	private ZRRequestID mRequestID;
	private ZRTaskCallback mCallback;

	public ZRHttpTask(ZRRequestID requestID, ZRTaskCallback callback) {
		mRequestID = requestID;
		mCallback = callback;
	}

	@Override
	protected Bundle doInBackground(Object... params) {
		Bundle result = new Bundle();
		result.putString(ZRConstant.KEY_RESP_CODE, ZRErrors.SUCCESS);
		String serverUrl = (String) params[0];
		String method = (String) params[1];
		String encryption = (String) params[2];
		String msg = (String) params[3];

		HashMap<String, String> headers = new HashMap<String, String>();
		Set<String> keys = sHeaderNormal.keySet();
		for (String key : keys) {
			headers.put(key, sHeaderNormal.get(key));
		}

		if (!ENCRYPT_NONE.equals(encryption)) {
			headers.put(ZRConstant.KEY_HTTP_ENCRYPT, encryption);
		}

		try {
			String resp = (String) ZRHttpManager.sendMessage(serverUrl, msg,
					method, headers);
			if (null != resp) {
				result.putString(ZRConstant.KEY_RESP_DESC, resp);
			} else {
				result.putString(ZRConstant.KEY_RESP_CODE,
						ZRErrors.ERROR_NETWORK);
			}
		} catch (SSLHandshakeException e) {
			e.printStackTrace();
			result.putString(ZRConstant.KEY_RESP_CODE,
					ZRErrors.ERROR_HTTPS_TIME_SETTINGS);
		} catch (IOException e) {
			e.printStackTrace();
			result.putString(ZRConstant.KEY_RESP_CODE, ZRErrors.ERROR_NETWORK);
		}
		return result;
	}

	@Override
	protected void onPostExecute(Bundle result) {
		try {
			String respCode = result.getString(ZRConstant.KEY_RESP_CODE);
			String respMsg = result.getString(ZRConstant.KEY_RESP_DESC);
			if (ZRErrors.SUCCESS.equals(respCode)) {
				onResult(respMsg);
			} else {
				onError(respCode, respMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onResult(String respMsg) {
		mCallback.onResult(mRequestID, respMsg);
	}

	protected void onError(String respCode, String respMsg) {
		mCallback.onError(mRequestID, respCode, respMsg);
	}
}
