package com.chang.android.develop.locationaware;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.chang.android.develop.R;

public class LocationAwareActivity extends AppCompatActivity {

    private static final String TAG = "LocationAwareActivity";

    private static final int PERMISSION_REQUESTCODE = 1;
    private static final int UPDATE_LATLNG = 1;

    private LocationManager mLocationManager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg == null) {
                return;
            }
            int what = msg.what;
            if (what == UPDATE_LATLNG) {
                Object obj = msg.obj;
                Log.d(TAG, obj.toString());
                Toast.makeText(LocationAwareActivity.this, obj.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_aware);

        // Get a Reference to LocationManager
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Pick a Location Provider
//        LocationProvider provider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER); // 需要ACCESS_FINE_LOCATION权限

        // Retrieve a list of location providers that have fine accuracy, no monetary cost, etc
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setCostAllowed(false);
        String providerName = mLocationManager.getBestProvider(criteria, true);
        // If no suitable provider is found, null is returned.
        if (providerName != null) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // This verification should be done during onStart() because the system calls
        // this method when the user returns to the activity, which ensures the desired
        // location provider is enabled each time the activity resumes from the stopped state.
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Verify the Location Provider is Enabled
        boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
        }

        requestLocation();
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }

    private void requestLocation() {
        // 这里需要权限 ACCESS_COARSE_LOCATION ACCESS_FINE_LOCATION 中的任何一个
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUESTCODE);
            return;
        }
        // 每10秒、设备移动距离大于10米 回调
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                10000, // 10-second interval.
                0.01f, // 10 meters.
                locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUESTCODE) {
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            }
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  In this case,
            // we're sending the update to a handler which then updates the UI with the new
            // location.
            Message.obtain(mHandler,
                    UPDATE_LATLNG,
                    location.getLatitude() + ", " +
                            location.getLongitude()).sendToTarget();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        // Terminate Location Updates
        mLocationManager.removeUpdates(locationListener);
    }
}
