package com.xiaochang.flipclockdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaochang.flipclockdemo.activity.FlipClockActivity;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 说明：FlipClock
 * <p>
 * Created by Chang.Xiao on 2019-12-18.
 *
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFlipClockClick(View view) {
        Intent intent = new Intent(this, FlipClockActivity.class);
        startActivity(intent);
    }
}
