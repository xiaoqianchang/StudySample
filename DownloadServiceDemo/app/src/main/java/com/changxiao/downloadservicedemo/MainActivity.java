package com.changxiao.downloadservicedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.changxiao.downloadservicedemo.service.ZRDownloadService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(getApplicationContext(),
                        ZRDownloadService.class));
                ZRDownloadAppInfo info = new ZRDownloadAppInfo();
                info.setName(ZRStrings.get(this, "app_name"));
                info.setDownloadUrl(updateInfo.getUpdateUrl());
                info.setType(ZRDownloadAppInfo.APP_TYPE_CLIENT);
                intent.putExtra(ZRConstant.KEY_INFO, info);
                startService(intent);
            }
        });
    }
}
