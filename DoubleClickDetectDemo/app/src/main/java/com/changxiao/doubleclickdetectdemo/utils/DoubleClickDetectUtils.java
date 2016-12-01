package com.changxiao.doubleclickdetectdemo.utils;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 检测双击Utils
 *
 * 参考：http://www.sixwolf.net/blog/2016/03/31/%E6%B5%85%E6%9E%90ReactiveX%E7%9A%84%E5%A4%9A%E6%92%AD/#reactivex
 * <p>
 * Created by Chang.Xiao on 2016/12/1.
 *
 * @version 1.0
 */

public class DoubleClickDetectUtils {

    private static long lastClickTime;
    private final static int SPACE_TIME = 1000;

    private DoubleClickDetectUtils() {}

    private static class LazyHolder {
        private static final DoubleClickDetectUtils INSTANCE = new DoubleClickDetectUtils();
    }

    public static DoubleClickDetectUtils getInstance() {
        return LazyHolder.INSTANCE;
    }

    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    /**
     * 两次点击时间对比判断是否为双击
     *
     * @return
     */
    public synchronized boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isDoubleClick;
        if (currentTime - lastClickTime > SPACE_TIME) {
            isDoubleClick = false;
        } else {
            isDoubleClick = true;
        }
        lastClickTime = currentTime;
        return isDoubleClick;
    }

    private boolean isDoubleClick;
    public boolean doubleClickDetect(View view) {
        isDoubleClick = false;
        Observable<Void> observable = RxView.clicks(view).share();
        observable.buffer(observable.debounce(SPACE_TIME, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Void>>() {
                    @Override
                    public void call(List<Void> voids) {
                        if (voids.size() >= 2) {
                            // double click detected
                            isDoubleClick = true;
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        return isDoubleClick;
    }
}
