package com.changxiao.downloadservicedemo.io;

import android.os.AsyncTask;
import android.os.Bundle;

import com.zritc.colorfulfund.utils.ZRConstant;
import com.zritc.colorfulfund.utils.ZRErrors;
import com.zritc.colorfulfund.utils.ZRFileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Task to download a file from URL. 2 params should be set when
 * {@link #execute(String...)} is called. The first parameter is the URL of the
 * target file, the second parameter is the path to save this file.
 * 
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public class ZRFileDownloadTask extends AsyncTask<String, Integer, Bundle> {

	private ZRTaskCallback mTaskCallback;
	private TFDownloadProgressCallback mDownloadProgressCallback;
	private ZRRequestID mRequestID;
	private boolean mIsUTF8;

	public ZRFileDownloadTask(ZRRequestID requestID, boolean isUTF8,
			ZRTaskCallback taskCallback,
			TFDownloadProgressCallback progressCallback) {
		mRequestID = requestID;
		mIsUTF8 = isUTF8;
		mTaskCallback = taskCallback;
		mDownloadProgressCallback = progressCallback;
	}

	public interface TFDownloadProgressCallback {
		void onDownloadProgress(ZRRequestID requestID, int progress);
	}

	@Override
	protected Bundle doInBackground(String... params) {
		Bundle result = new Bundle();
		result.putString(ZRConstant.KEY_RESP_CODE, ZRErrors.SUCCESS);
		String strUrl = params[0];
		String target = params[1];

		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(ZRHttpManager.HTTP_TIME_OUT);
			conn.setReadTimeout(ZRHttpManager.HTTP_TIME_OUT);
			conn.setRequestMethod("GET");
			conn.addRequestProperty(ZRHttpManager.ACCPET_KEY,
					ZRHttpManager.ACCPET_CONTENT);
			// conn.setDoOutput(true);
			conn.connect();
			int length = conn.getContentLength();

			InputStream is = conn.getInputStream();

			int path = ZRFileUtils.PATH_SDCARD;
			if (target.startsWith(ZRFileUtils
					.getWorkFolder(ZRFileUtils.PATH_DATA))) {
				path = ZRFileUtils.PATH_DATA;
			}
			if (length >= ZRFileUtils.getStorageFreeSize(path)) {
				result.putString(ZRConstant.KEY_RESP_CODE,
						ZRErrors.ERROR_STORAGE_NOT_ENOUGHT);
				return result;
			}

			boolean isSuccess = false;
			if (mIsUTF8) {
				isSuccess = saveFile(is, new File(target));
			} else {
				isSuccess = saveFile(is, new File(target), length);
			}
			if (!isSuccess) {
				result.putString(ZRConstant.KEY_RESP_CODE,
						ZRErrors.ERROR_DOWNLOAD_FILE);
			}
		} catch (IOException e) {
			e.printStackTrace();
			result.putString(ZRConstant.KEY_RESP_CODE,
					ZRErrors.ERROR_DOWNLOAD_FILE);
		}
		return result;
	}

	@Override
	protected void onPostExecute(Bundle result) {
		String respCode = result.getString(ZRConstant.KEY_RESP_CODE);
		if (ZRErrors.SUCCESS.equals(respCode)) {
			mTaskCallback.onResult(mRequestID, null);
		} else {
			mTaskCallback.onError(mRequestID, respCode, null);
		}
	}

	public boolean saveFile(InputStream is, File file, int total) {
		if (!checkFile(file) || 0 == total) {
			return false;
		}

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length = 0;
			int readTotal = 0;
			int readLast = 0;
			while (-1 != (length = is.read(buffer))) {
				fos.write(buffer, 0, length);
				readTotal += length;
				readLast += length;
				if ((0 != total) && (readLast * 100 >= total)) {
					if (null != mDownloadProgressCallback) {
						float percent = (float) readTotal / total;
						mDownloadProgressCallback.onDownloadProgress(
								mRequestID, (int) (100 * percent));
					}
					readLast = 0;
				}
			}
			fos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public boolean saveFile(InputStream is, File file) {
		if (!checkFile(file)) {
			return false;
		}

		OutputStreamWriter osw = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(is, "UTF-8");
			osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			char[] buffer = new char[1024];
			int length = -1;
			while (-1 != (length = isr.read(buffer))) {
				osw.write(buffer, 0, length);
			}
			osw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != osw) {
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != isr) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	private boolean checkFile(File file) {
		File parent = file.getParentFile();
		if (null != parent && !parent.exists() && !parent.mkdirs()) {
			return false;
		}

		if (file.exists() && !file.delete()) {
			return false;
		}

		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
