package com.android.mobileguard.ui;

import com.android.mobileguard.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 自定义开关类，继承ImageView
 * @author feng
 *
 */
public class SwitchImageView extends ImageView {
	/**
	 * true 代表打开状态，false表示关闭状态
	 */
	private boolean switchStatus = true;
	
	
	public SwitchImageView(Context context) {
		super(context);
	}

	public SwitchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SwitchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public boolean getSwitchStatus() {
		return switchStatus;
	}

	public void setSwitchStatus(boolean switchStatus) {
		this.switchStatus = switchStatus;
		if(switchStatus){
			setImageResource(R.drawable.on);
		}else{
			setImageResource(R.drawable.off);
		}
	}
	
	public  void changeStatus(){
		setSwitchStatus(!switchStatus);
	}
}
