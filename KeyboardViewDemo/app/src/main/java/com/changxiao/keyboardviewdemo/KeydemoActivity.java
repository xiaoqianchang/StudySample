package com.changxiao.keyboardviewdemo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class KeydemoActivity extends Activity implements OnTouchListener {
	private Context ctx;
	private Activity act;
	private EditText edit;
	private EditText edit1;
	private int  sdkInt;
	private KeyboardUtil keyboardUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key_demo);
		sdkInt=Build.VERSION.SDK_INT;
		ctx = this;
		act = this;

		edit = (EditText) this.findViewById(R.id.edit);
		edit1 = (EditText) this.findViewById(R.id.edit1);
		edit.setOnTouchListener(this);
		edit1.setOnTouchListener(this);
	}

	/**
	 * editview的touch事件
	 * @param v
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		//隐藏输入法，显示光标
		EditText et=(EditText)v;
		int inType = et.getInputType(); // back up the input type
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
		keyboardUtil=null;
		keyboardUtil = new KeyboardUtil(act, ctx, et);
		et.onTouchEvent(event);// call native handler
		// restore input type
		keyboardUtil.showKeyboard();

		return true;
	}
}