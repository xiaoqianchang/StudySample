package com.changxiao.circularimageviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.changxiao.circularimageviewdemo.widget.CircleImageView;
import com.changxiao.circularimageviewdemo.widget.CircularImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.img_avatar1)
    CircularImageView imgAvatar1;

    @Bind(R.id.img_avatar2)
    CircleImageView imgAvatar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Picasso.with(this)
                .load(R.mipmap.avatat)
                .placeholder(R.mipmap.ic_launcher)
                .into(imgAvatar1);

        Picasso.with(this)
                .load(R.mipmap.avatat)
                .placeholder(R.mipmap.ic_launcher)
                .into(imgAvatar2);
    }
}
