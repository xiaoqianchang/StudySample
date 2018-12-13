package com.changxiao.runtimepermissionsdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.changxiao.runtimepermissionsdemo.normal.activity.ContactsActivity;
import com.changxiao.runtimepermissionsdemo.normal.activity.PermissionActivity;
import com.changxiao.runtimepermissionsdemo.permissionsdispatcher.activity.PermissionsDispatcherActivity;
import com.changxiao.runtimepermissionsdemo.rxpermissions.activity.RxPermissionsActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        Toast.makeText(this, "widthPixels：" + widthPixels + ", heightPixels：" + heightPixels, Toast.LENGTH_LONG).show();
        Log.d(TAG, "widthPixels：" + widthPixels + ", heightPixels：" + heightPixels);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = null;
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_normal:
                intent = new Intent(this, PermissionActivity.class);
                break;
            case R.id.action_contacts:
                intent = new Intent(this, ContactsActivity.class);
                break;
            case R.id.action_permissions_dispatcher:
                intent = new Intent(this, PermissionsDispatcherActivity.class);
                break;
            case R.id.action_rx_permissions:
                intent = new Intent(this, RxPermissionsActivity.class);
                break;
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
        }

        return super.onOptionsItemSelected(item);
    }
}
