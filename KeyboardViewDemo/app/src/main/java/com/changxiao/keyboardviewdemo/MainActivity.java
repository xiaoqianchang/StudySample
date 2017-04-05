package com.changxiao.keyboardviewdemo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.changxiao.keyboardviewdemo.dialog.PasswordDialog;
import com.changxiao.keyboardviewdemo.fragment.PasswordFragment;
import com.changxiao.keyboardviewdemo.fragment.PasswordFragmentTwo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private EditText edtInput;
    private EditText edtInput2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btn_show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordFragment.newInstace().show(getSupportFragmentManager(), "PassWordFragment");
            }
        });

        Button btnShowPassword = (Button) findViewById(R.id.btn_show_password);
        btnShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PasswordDialog(MainActivity.this).show();
            }
        });

        findViewById(R.id.btn_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, KeydemoActivity.class));
            }
        });

        edtInput = (EditText) findViewById(R.id.edt_input);
        edtInput2 = (EditText) findViewById(R.id.edt_password);
        edtInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //隐藏输入法，显示光标
                    EditText et=(EditText)v;
                    int inType = et.getInputType(); // back up the input type
                    int sdkInt= Build.VERSION.SDK_INT;
                    if (sdkInt>=11) {
                        Class<EditText> cls=EditText.class;
                        try {
                            Method setShowSoftInputOnFocus=cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                            setShowSoftInputOnFocus.setAccessible(false);
                            setShowSoftInputOnFocus.invoke(et, false);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else {
                        et.setInputType(android.text.InputType.TYPE_NULL); // disable soft input
                        et.setInputType(inType);

                    }

                    et.onTouchEvent(event);// call native handler
                    PasswordFragmentTwo.newInstace().show(getSupportFragmentManager(), "PassWordFragmentTwo");
                }
                return true;
            }
        });
    }
}
