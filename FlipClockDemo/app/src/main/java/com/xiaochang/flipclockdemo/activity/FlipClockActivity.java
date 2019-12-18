package com.xiaochang.flipclockdemo.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;

import com.xiaochang.flipclockdemo.R;
import com.xiaochang.flipclockdemo.view.FlipSurface;

import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class FlipClockActivity extends Activity implements OnTouchListener {

    public static final String ACTION_START = "mobi.intuitit.flipclock.action.START";
    public static final String EXTRA_BACKGROUND = "extra.background";
    static final int GAP = 10;
    static final int MARGIN = 20;
    private boolean m24Hours = false;
    FlipSurface mAmPmSurface;
    View mBoard;
    Time mCalendar;
    private int mColorBackground;
    private int mColorStrings;
    FlipSurface mHourSurface;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.TIMEZONE_CHANGED")) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            onTimeChanged();
        }
    };
    boolean mKeepScreenOn = true;
    int mLastBrightnessLevel = -1;
    FlipSurface mMinuteSurface;
    MediaPlayer mMinuteTickPlayer;
    private boolean mOrientationOn = true;
    FlipSurface mSecondSurface;
    TimerTask mSecondTask;
    MediaPlayer mSecondTickPlayer;
    Timer mSecondTimer;
    SharedPreferences mSharedPrefs;
    private boolean mShowSecond = true;
    boolean mSoundOn = true;

    public static final class Background {
        public static final String SOLID = "solid";
        public static final String TRANSLUCENT = "translucent";
        public static final String TRANSLUCENT_BLUR = "translucent.blur";
        public static final String TRANSPARENT = "transparent";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_clock);
        this.mBoard = findViewById(R.id.board);
        this.mBoard.setOnTouchListener(this);
        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        this.mAmPmSurface = (FlipSurface) findViewById(R.id.ampm_surface);
        this.mHourSurface = (FlipSurface) findViewById(R.id.hour_surface);
        this.mMinuteSurface = (FlipSurface) findViewById(R.id.minute_surface);
        this.mSecondSurface = (FlipSurface) findViewById(R.id.second_surface);
        this.mCalendar = new Time();
//        this.mMinuteTickPlayer = MediaPlayer.create(this, R.raw.ticking);
//        this.mSecondTickPlayer = MediaPlayer.create(this, R.raw.ticking);
        onNewIntent(getIntent());
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        String action = intent.getAction();
        if (action != null && action.equals(ACTION_START)) {
            String background = intent.getExtras().getString(EXTRA_BACKGROUND);
            if (background != null) {
                switchBackground(background);
            }
        }
        super.onNewIntent(intent);
    }

    private void switchBackground(String background) {
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.mColorBackground = -16777216;
        this.mColorStrings = -1;
        if (this.mBoard != null) {
            this.mBoard.setBackgroundColor(this.mColorBackground);
        }
        this.m24Hours = this.mSharedPrefs.getBoolean(getString(R.string.key_24hours), false);
        this.mOrientationOn = this.mSharedPrefs.getBoolean(getString(R.string.key_orientation), true);
        this.mShowSecond = this.mSharedPrefs.getBoolean(getString(R.string.key_second), false);
        this.mKeepScreenOn = this.mSharedPrefs.getBoolean(getString(R.string.key_keepscreenon), true);
        this.mSoundOn = this.mSharedPrefs.getBoolean(getString(R.string.key_soundon), true);
        if (this.mOrientationOn) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
        this.mCalendar.setToNow();
        int hour = this.mCalendar.hour;
        if (this.m24Hours) {
            this.mAmPmSurface.clear();
            this.mAmPmSurface.setVisibility(View.GONE);
        } else {
            this.mAmPmSurface.init(hour > 12 ? "PM" : "AM", null, this.mColorBackground, this.mColorStrings);
        }
        this.mHourSurface.setKeepScreenOn(this.mKeepScreenOn);
        this.mHourSurface.setVisibility(View.VISIBLE);
        if (!this.m24Hours) {
            int hour2 = hour % 12;
            if (hour2 == 0) {
                hour2 = 12;
            }
            this.mHourSurface.init(new StringBuilder(String.valueOf(hour2)).toString(), this.mCalendar.hour < 12 ? "AM" : "PM", this.mColorBackground, this.mColorStrings);
        } else {
            this.mHourSurface.init(fill2(hour), null, this.mColorBackground, this.mColorStrings);
        }
        this.mMinuteSurface.setVisibility(View.VISIBLE);
        this.mMinuteSurface.init(fill2(this.mCalendar.minute), null, this.mColorBackground, this.mColorStrings);
        if (this.mShowSecond) {
            this.mSecondSurface.setVisibility(View.VISIBLE);
            this.mSecondSurface.init(fill2(this.mCalendar.second), null, this.mColorBackground, this.mColorStrings);
            if (this.mSecondTimer != null) {
                this.mSecondTimer.cancel();
            }
            this.mSecondTimer = new Timer(true);
            if (this.mSecondTask != null) {
                this.mSecondTask.cancel();
            }
            this.mSecondTask = new TimerTask() {
                public void run() {
                    mCalendar.setToNow();
                    mSecondSurface.flipTo(fill2(mCalendar.second), true);
                    if (mSecondTickPlayer != null && mSoundOn) {
                        mSecondTickPlayer.start();
                    }
                }
            };
            this.mSecondTimer.scheduleAtFixedRate(this.mSecondTask, 1000 - (System.currentTimeMillis() % 1000), 1000);
        } else {
            this.mSecondSurface.clear();
            this.mSecondSurface.setVisibility(View.GONE);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        registerReceiver(this.mIntentReceiver, filter);
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mHourSurface != null) {
            this.mHourSurface.setKeepScreenOn(false);
        }
        if (this.mSecondTimer != null) {
            this.mSecondTimer.cancel();
        }
        unregisterReceiver(this.mIntentReceiver);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mSecondTickPlayer != null) {
            this.mSecondTickPlayer.release();
            this.mSecondTickPlayer = null;
        }
        if (this.mMinuteTickPlayer != null) {
            this.mMinuteTickPlayer.release();
            this.mMinuteTickPlayer = null;
        }
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.clock_menu, menu);
//        menu.findItem(R.id.mi_settings).setIntent(new Intent(this, ClockSettings.class));
//        menu.findItem(R.id.mi_help).setIntent(new Intent("android.intent.action.VIEW", Uri.parse(getString(R.string.help_uri))));
//        return true;
//    }
//
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.mi_orientation).setEnabled(!this.mOrientationOn);
//        if (this.mOrientationOn) {
//            menu.findItem(R.id.mi_orientation).setTitle(R.string.auto);
//        } else {
//            menu.findItem(R.id.mi_orientation).setTitle(R.string.flip_orientation);
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.mi_orientation /*2131230732*/:
//                boolean isportrait = !TextUtils.equals(getString(R.string.orientation), "portrait");
//                this.mSharedPrefs.edit().putBoolean(getString(R.string.key_setorientation), isportrait);
//                if (isportrait) {
//                    setRequestedOrientation(1);
//                } else {
//                    setRequestedOrientation(0);
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    /* access modifiers changed from: 0000 */
    public void onTimeChanged() {
        String str;
        String str2;
        String str3 = "PM";
        String str4 = "AM";
        this.mCalendar.setToNow();
        int hour = this.mCalendar.hour;
        int minute = this.mCalendar.minute;
        FlipSurface flipSurface = this.mAmPmSurface;
        if (hour > 12) {
            String str5 = "PM";
            str = str3;
        } else {
            String str6 = "AM";
            str = str4;
        }
        flipSurface.flipTo(str, true);
        if (this.mHourSurface != null) {
            if (!this.m24Hours) {
                int hour2 = hour % 12;
                if (hour2 == 0) {
                    hour2 = 12;
                }
                FlipSurface flipSurface2 = this.mHourSurface;
                String sb = this.m24Hours ? fill2(hour2) : new StringBuilder(String.valueOf(hour2)).toString();
                if (this.mCalendar.hour < 12) {
                    String str7 = "AM";
                    str2 = str4;
                } else {
                    String str8 = "PM";
                    str2 = str3;
                }
                flipSurface2.flipTo(sb, str2, true);
            } else {
                this.mHourSurface.flipTo(fill2(this.mCalendar.hour), null, true);
            }
        }
        if (this.mMinuteSurface != null) {
            this.mMinuteSurface.flipTo(fill2(minute), true);
            if (this.mMinuteTickPlayer != null && this.mSoundOn) {
                this.mMinuteTickPlayer.start();
            }
        }
    }

    static String fill2(int i) {
        if (i > 9) {
            return Integer.toString(i);
        }
        return "0" + i;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (v == this.mBoard) {
            float y = event.getY();
            switch (event.getAction()) {
                case 0:
                    return true;
                case 1:
                    return true;
                case 2:
                    int level = (int) (10.0f * (1.0f - (y / ((float) this.mBoard.getMeasuredHeight()))));
                    if (this.mLastBrightnessLevel != level) {
                        float bright = (float) (0.03d * Math.exp(((double) level) * 0.35d));
                        LayoutParams lp = getWindow().getAttributes();
                        if (lp.screenBrightness != bright) {
                            lp.screenBrightness = bright;
                            getWindow().setAttributes(lp);
                        }
                        this.mLastBrightnessLevel = level;
                    }
                    return true;
            }
        }
        return false;
    }
}
