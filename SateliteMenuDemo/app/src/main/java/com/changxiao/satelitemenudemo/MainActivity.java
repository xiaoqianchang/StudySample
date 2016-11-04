package com.changxiao.satelitemenudemo;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.changxiao.satelitemenudemo.widget.SateliteMenu;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.sateliteMenu)
    SateliteMenu sateliteMenu;

    @Bind(R.id.ll_container)
    LinearLayout llContainer;

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
    }

    @OnClick({R.id.ll_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_container:
                /*View maskView = LayoutInflater.from(this).inflate(R.layout.view_wish_completed, null);
                Dialog dialog = ZRPopupUtil.makeAddWishTipPopup(this, maskView);
                dialog.show();
                maskView.findViewById(R.id.img_add_wish).setOnClickListener(v -> dialog.dismiss());
                maskView.findViewById(R.id.img_wish_tip).setOnClickListener(v -> dialog.dismiss());*/
//                dialog.setOnDismissListener(dialogs -> ZRSharePreferenceKeeper.keepBooleanValue(this, ZRConstant.FIRST_TIME_USE_WISH, false));
                showPopupWindow(null);
                break;
        }
    }

    PopupWindow popupSearch;
    /**
     * 弹出搜索框
     * @param v
     */
    private void showPopupWindow(View v) {
        View view = null;
        if (popupSearch == null) {
            view = LayoutInflater.from(this).inflate(R.layout.view_wish_completed, null);

            // 创建一个PopuWidow对象
            popupSearch = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        // 使其聚集
        popupSearch.setFocusable(true);
        // 设置允许在外点击消失
        popupSearch.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupSearch.setBackgroundDrawable(new BitmapDrawable());

        // 主界面变暗
        backgroundAlpha(0.8f);
        //添加pop窗口关闭事件
        popupSearch.setOnDismissListener(new poponDismissListener());
        // 淡入淡出动画
//        popupSearch.setAnimationStyle(R.anim.slide_in_from_right);
//        popupSearch.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
        // 设置好参数之后再show
        popupSearch.showAsDropDown(llContainer, 0, -llContainer.getMeasuredHeight());
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        getWindow().setAttributes(lp);
        getWindow().getDecorView().setAlpha(bgAlpha);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     * @author xiaochang
     *
     */
    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }
}
