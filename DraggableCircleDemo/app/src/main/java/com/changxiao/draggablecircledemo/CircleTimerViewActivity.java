package com.changxiao.draggablecircledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.changxiao.draggablecircledemo.widget.CircleTimerView;

public class CircleTimerViewActivity extends AppCompatActivity implements CircleTimerView.CircleTimerListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CircleTimerView mTimer;
    private EditText mTimerSet;
    private EditText mHintSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_timer_view);

        mTimer = (CircleTimerView) findViewById(R.id.ctv);
        mTimer.setCircleTimerListener(this);
        mTimerSet = (EditText) findViewById(R.id.time_set_et);
        mHintSet = (EditText) findViewById(R.id.hint_set_et);
    }

    public void setTime(View v) {
        try {
            mTimer.setCurrentTime(Integer.parseInt(mTimerSet.getText().toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setHint(View v) {
        mTimer.setHintText(mHintSet.getText().toString());
    }

    public void start(View v) {
        mTimer.startTimer();
    }

    public void pause(View v) {
        mTimer.pauseTimer();
    }

    @Override
    public void onTimerStop() {
        Toast.makeText(this, "onTimerStop", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerStart(int time) {
        Toast.makeText(this, "onTimerStart", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerPause(int time) {
        Toast.makeText(this, "onTimerPause", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimerTimingValueChanged(int time) {
        Log.d(TAG, "onTimerTimingValueChanged");
    }

    @Override
    public void onTimerSetValueChanged(int time) {
        Log.d(TAG, "onTimerSetValueChanged");
    }

    @Override
    public void onTimerSetValueChange(int time) {
        Log.d(TAG, "onTimerSetValueChange");
    }
}
