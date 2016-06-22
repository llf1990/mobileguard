package com.android.mobileguard.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
//自定义控件写法
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context) {
		super(context);
	}

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	@ExportedProperty(category = "focus")
	
	//重写父类方法，直接返回true，欺骗textview直接获取焦点
	public boolean isFocused() {
		return true;
	}
	
}
