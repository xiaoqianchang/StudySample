package com.changxiao.downloadservicedemo.io;

import android.os.AsyncTask;

import com.zritc.colorfulfund.utils.ZRLog;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public final class ZRTaskDelegate {
    private static ExecutorService sExecutor = Executors.newCachedThreadPool();

    private static final int THREAD_SIZE = 5;
    private static ExecutorService sImageExecutor = Executors
            .newFixedThreadPool(THREAD_SIZE);

    public static final void execute(AsyncTask task, String... params)
            throws IOException {
        execute(task, false, params);
    }

    public static final void execute(AsyncTask task, boolean isImage,
                                     String... params) throws IOException {
        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                task.execute(params);
            } else {
                task.executeOnExecutor(isImage ? sImageExecutor : sExecutor,
                        params);
            }
        } catch (RejectedExecutionException e) {
            ZRLog.w("task " + task + " execute failed", e);
            throw new IOException();
        }
    }
}
