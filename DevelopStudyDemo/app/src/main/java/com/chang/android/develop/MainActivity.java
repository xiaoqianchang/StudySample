package com.chang.android.develop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chang.android.develop.locationaware.LocationAwareActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLocationAwareClick(View view) {
        startActivity(new Intent(this, LocationAwareActivity.class));
    }
}
