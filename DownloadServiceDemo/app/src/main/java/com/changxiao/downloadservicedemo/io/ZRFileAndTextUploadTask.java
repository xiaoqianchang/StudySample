package com.changxiao.downloadservicedemo.io;

import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Task to upload a file from URL. 2 params should be set when
 * {@link #execute(String...)} is called. The first parameter is the URL of the
 * server, the second parameter is the path of which what to upload.
 * 文字和文件或图片混合一起上传
 * 
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-02
 * @lastUpdate 2014-07-02
 */
public class ZRFileAndTextUploadTask extends AsyncTask<Object, Integer, Bundle> {

	private static final String TAG = "ZRFileUploadTask";

	private ZRTaskCallback mTaskCallback;
	private ZRUploadFileAndTextProgressCallback mUploadProgressCallback;
	private ZRRequestID mRequestID;

	public ZRFileAndTextUploadTask(ZRRequestID requestID,
			ZRTaskCallback taskCallback,
			ZRUploadFileAndTextProgressCallback progressCallback) {
		mRequestID = requestID;
		mTaskCallback = taskCallback;
		mUploadProgressCallback = progressCallback;
	}

	public interface ZRUploadFileAndTextProgressCallback {
		void onUploadProgress(ZRRequestID requestID, int progress);
	}

	@Override
	protected Bundle doInBackground(Object... params) {
		Bundle result = new Bundle();
		result.putString(ZRConstant.KEY_RESP_CODE, ZRErrors.SUCCESS);
		String serverUrl = (String) params[0];
		Map<String, String> param = (Map<String, String>) params[1];
		Map<String, File> fileMap = (Map<String, File>) params[2];
		ZRLog.d(TAG, serverUrl + "params:" + param + " filePath:" + fileMap);
		HttpPost post = new HttpPost(serverUrl);
		try {
			// HttpClient httpClient = new DefaultHttpClient();
			MultipartEntity entity = new MultipartEntity();
			if (param != null && !param.isEmpty()) {
				for (Map.Entry<String, String> entry : param.entrySet()) {
					if (entry.getValue() != null
							&& entry.getValue().trim().length() > 0) {
						entity.addPart(
								entry.getKey(),
								new StringBody(
										entry.getValue(),
										Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
					}
				}
			}
			// 文件资源
			if (fileMap != null && !fileMap.isEmpty()) {
				for (Map.Entry<String, File> entry : fileMap.entrySet()) {
					if (entry.getValue() != null && entry.getValue().exists()) {
						entity.addPart(entry.getKey(),
								new FileBody(entry.getValue()));
					}
				}
			}
			post.setEntity(entity);
			HttpResponse response = ZRHttpManager.client.execute(post);
			int stateCode = response.getStatusLine().getStatusCode();
			ZRLog.d(TAG, " stateCode:" + stateCode);
			StringBuffer sb = new StringBuffer();
			if (HttpStatus.SC_OK == stateCode) {
				HttpEntity resEntity = response.getEntity();
				if (result != null) {
					InputStream is = resEntity.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String tempLine;
					while ((tempLine = br.readLine()) != null) {
						sb.append(tempLine);
					}
				}
				result.putString(ZRConstant.KEY_RESP_DESC, sb.toString());
				ZRLog.d(TAG, " response:" + result.toString());
			} else {
				result.putString(ZRConstant.KEY_RESP_CODE,
						ZRErrors.ERROR_UPLOAD_FILE);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			result.putString(ZRConstant.KEY_RESP_CODE,
					ZRErrors.ERROR_UPLOAD_FILE);
		} catch (ProtocolException e) {
			e.printStackTrace();
			result.putString(ZRConstant.KEY_RESP_CODE,
					ZRErrors.ERROR_UPLOAD_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result.putString(ZRConstant.KEY_RESP_CODE,
					ZRErrors.ERROR_UPLOAD_FILE);
		} catch (IOException e) {
			e.printStackTrace();
			result.putString(ZRConstant.KEY_RESP_CODE,
					ZRErrors.ERROR_UPLOAD_FILE);
		} finally {
			post.abort();
		}
		return result;
	}

	@Override
	protected void onPostExecute(Bundle result) {
		String respCode = result.getString(ZRConstant.KEY_RESP_CODE);
		String respMsg = result.getString(ZRConstant.KEY_RESP_DESC);
		if (ZRErrors.SUCCESS.equals(respCode)) {
			mTaskCallback.onResult(mRequestID, respMsg);
		} else {
			mTaskCallback.onError(mRequestID, respCode, null);
		}
	}
}
