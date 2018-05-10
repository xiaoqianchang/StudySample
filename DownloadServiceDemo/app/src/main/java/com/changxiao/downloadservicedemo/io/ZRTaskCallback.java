package com.changxiao.downloadservicedemo.io;

/**
 * Callback for AsyncTask.
 * 
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public interface ZRTaskCallback {
	/**
	 * This function will be called when error happened during task execution.
	 * 
	 * @param requestID ID of the request.
	 * @param errorCode Error code.
	 * @param errorDesc Error description, may be empty string.
	 */
	public void onError(ZRRequestID requestID, String errorCode, String errorDesc);

	/**
	 * This function will be called when task has finished.
	 * 
	 * @param requestID ID of the request.
	 * @param result result of the task, for {@link ZRHttpTask}, this is the
	 *            response from server.
	 */
	public void onResult(ZRRequestID requestID, String result);
}
