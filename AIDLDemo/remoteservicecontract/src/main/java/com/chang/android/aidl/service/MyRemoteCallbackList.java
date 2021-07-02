package com.chang.android.aidl.service;

import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.util.Log;

/**
 * Description: $
 * <p>
 * Created by Nicholas Sean on 2021/7/2 7:27 PM.
 *
 * @version 1.0
 */
public class MyRemoteCallbackList<T extends IInterface> extends RemoteCallbackList<T> {
    private static final String TAG = "RemoteService";

    @Override
    public void onCallbackDied(T callback, Object cookie) {
        super.onCallbackDied(callback, cookie);
        this.unregister(callback);
        if (cookie != null && cookie instanceof ProcessCookie) {
            ProcessCookie p = (ProcessCookie) cookie;
            Log.e(TAG, "Process " + p.pid + " died");
        }
    }

    public static class ProcessCookie {
        public int pid;
        public int uid;

        public ProcessCookie(int pid, int uid) {
            this.pid = pid;
            this.uid = uid;
        }
    }
}
