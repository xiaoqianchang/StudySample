package com.changxiao.satelitemenudemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.changxiao.satelitemenudemo.widget.SateliteMenu;
import com.changxiao.satelitemenudemo.widget.SateliteMenuTwo;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.sateliteMenu)
    SateliteMenu sateliteMenu;
    @Bind(R.id.sateliteMenuTwo)
    SateliteMenuTwo sateliteMenuTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        sateliteMenu.setOnMenuItemClickListener(new SateliteMenu.onMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 可以使用标记来判断点击的Viwew，当然必须给ChildViewItem设置了Tag
                if ("1".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                } else if ("2".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                } else if ("3".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                } else if ("4".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                } else if ("5".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                } else if ("6".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                }

                //可以使用position来判断
                switch (position) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        /**
         * 自定义右上角菜单的弹出方式为向右上
         */
        sateliteMenuTwo.setOnMenuItemClickListener(new SateliteMenuTwo.onMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 可以使用标记来判断点击的Viwew，当然必须给ChildViewItem设置了Tag
                if ("1".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                } else if ("2".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                }

                //可以使用position来判断
                switch (position) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
