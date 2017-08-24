package com.changxiao.runtimepermissionsdemo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.changxiao.runtimepermissionsdemo.R;
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
    }

    public void contacts(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_CONTACTS, mPermissionGrant);
    }

    public void phone(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_PHONE, mPermissionGrant);
    }

    public void calendar(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_CALENDAR, mPermissionGrant);
    }

    public void camera(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_CAMERA, mPermissionGrant);
    }

    public void sensors(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_SENSORS, mPermissionGrant);
    }

    public void location(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_LOCATION, mPermissionGrant);
    }

    public void storage(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_STORAGE, mPermissionGrant);
    }

    public void microphone(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_MICROPHONE, mPermissionGrant);
    }

    public void sms(View view) {
        PermissionHelper.requestPermission(this, PermissionHelper.CODE_SMS, mPermissionGrant);
    }


    private PermissionHelper.OnPermissionListener mPermissionGrant = new PermissionHelper.OnPermissionListener() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionHelper.CODE_CONTACTS:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_PHONE:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_CALENDAR:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_CAMERA:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_SENSORS:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_LOCATION:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_STORAGE:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_MICROPHONE:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionHelper.CODE_SMS:
                    Toast.makeText(PermissionActivity.this, "Result Permission Grant Success", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPremissionDenied(int requestCode) {
            Toast.makeText(PermissionActivity.this, "Result Permission Grant Failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPremissionNeverAskAgain(int requestCode) {

        }
    };

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }
}
