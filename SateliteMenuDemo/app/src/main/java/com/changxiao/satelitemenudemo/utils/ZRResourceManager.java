package com.changxiao.satelitemenudemo.utils;


import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Resource util used for application,help to get resource id from resource
 * name.
 * 
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public class ZRResourceManager {
	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
	}

	/**
	 * Get resouce id from resource name and type.
	 * 
	 * @param name The name of the desired resource.
	 * @param type The type of the desired resource.
	 * @return <b>int</b> The associated resource identifier. Returns 0 if no
	 *         such resource was found.
	 */
	public static final int getResourceID(String name, String type) {
		try {
			return mContext.getResources().getIdentifier(name, type,
			        mContext.getPackageName());
		}
		catch (Exception e) {
			e.printStackTrace();
			Log.e("DFResourceManager.getResourceID", e.getMessage());
		}
		return -1;
	}

	/**
	 * Get styleable array by given name.
	 * 
	 * @param name The name of the desired styleable array.
	 * @return <b>int[]</b> The associated styleable array.
	 */
	public static final int[] getStyleableArray(String name) {
		try {
			Field[] fields2 = Class.forName(
			        mContext.getPackageName() + ".R$styleable").getFields();
			for (Field f : fields2) {
				if (f.getName().equals(name)) {
					return (int[]) f.get(null);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static final int getStyleable(String name) {
		try {
			Field[] fields2 = Class.forName(
			        mContext.getPackageName() + ".R$styleable").getFields();
			for (Field f : fields2) {
				if (f.getName().equals(name)) {
					return f.getInt(null);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
}
