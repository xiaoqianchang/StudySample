package com.changxiao.runtimepermissionsdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.changxiao.runtimepermissionsdemo.utils.PermissionHelper;

/**
 * $desc$
 * <p>
 * Created by Chang.Xiao on 2016/11/22.
 *
 * @version 1.0
 */

public class PermissionActivity extends AppCompatActivity {
    private static final String TAG = PermissionActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PermissionsFragment fragment = new PermissionsFragment();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();

    }

    /**
     * Called when the 'show camera' button is clicked.
     * Callback is defined in resource layout definition.
     */
    public void showCamera(View view) {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_CAMERA, mPermissionGrant);
    }

    public void getAccounts(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_GET_ACCOUNTS, mPermissionGrant);
    }

    public void callPhone(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_CALL_PHONE, mPermissionGrant);
    }

    public void readPhoneState(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_READ_PHONE_STATE, mPermissionGrant);
    }

    public void accessFineLocation(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
    }

    public void accessCoarseLocation(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);
    }

    public void readExternalStorage(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
    }

    public void writeExternalStorage(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
    }

    public void recordAudio(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_RECORD_AUDIO, mPermissionGrant);
    }


    private PermissionHelper.OnPermissionListener mPermissionGrant = new PermissionHelper.OnPermissionListener() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionHelper.CODE_RECORD_AUDIO:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_GET_ACCOUNTS:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_READ_PHONE_STATE:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_CALL_PHONE:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_CAMERA:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPremissionDenied(int requestCode) {

        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionHelper.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }
}
