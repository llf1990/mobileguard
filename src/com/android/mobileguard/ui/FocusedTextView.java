package com.android.mobileguard.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
//�Զ���ؼ�д��
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
	
	//��д���෽����ֱ�ӷ���true����ƭtextviewֱ�ӻ�ȡ����
	public boolean isFocused() {
		return true;
	}
	
}
