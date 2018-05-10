package com.changxiao.downloadservicedemo.io;

/**
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public class ZRRequestID {
	private int mRequestID;
	private String mAdditionalData;

	public ZRRequestID(int requestID) {
		mRequestID = requestID;
	}

	public ZRRequestID(int requestID, String data) {
		mRequestID = requestID;
		mAdditionalData = data;
	}

	public int getRequestID() {
		return mRequestID;
	}

	public String getAdditionalData() {
		return mAdditionalData;
	}

	@Override
	public String toString() {
		return "{requestID:" + mRequestID + ", data:" + mAdditionalData + "}";
	}
}
