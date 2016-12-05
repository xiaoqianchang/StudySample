package com.changxiao.doubleclickdetectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.btn_click)
    Button btnClick;

    @Bind(R.id.btn_double_click)
    Button btnDoubleClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        doubleClickDetect(btnDoubleClick);
    }

    /**
     * 这里rx的双击检测会在debounce 1000毫秒之后再才执行call方法(也就是间隔debounce执行)
     *
     * 总结：这里要做到双击检测不适合，感觉有延迟的效果。
     *
     * @param view
     */
    public void doubleClickDetect(View view) {
        Observable<Void> observable = RxView.clicks(view).share();
        observable.buffer(observable.debounce(1000, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Void>>() {
                    @Override
                    public void call(List<Void> voids) {
                        if (voids.size() >= 2) {
                            // double click detected
                            Log.i(TAG, "doubleClick");
                        } else {
                            Log.i(TAG, "click");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
