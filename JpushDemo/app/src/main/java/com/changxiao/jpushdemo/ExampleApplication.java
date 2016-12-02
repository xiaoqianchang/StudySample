package com.changxiao.jpushdemo;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2016/12/2.
 *
 * @version 1.0
 */

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
