package com.android.mobileguard.activities;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.BlackNumber;
import com.android.mobileguard.db.dao.InterceptDao;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class addBlackNumberActivity extends Activity {
	protected static final String TAG = "addBlackNumberActivity";
	private EditText et_addblacknumber_number;
	private RadioGroup rg_mode_select;
	private Button bt_addblack_confirm;
	private Button bt_addblack_cancel;
	private InterceptDao dao ;
	
	public addBlackNumberActivity() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addblacknumber);
		dao = new InterceptDao(addBlackNumberActivity.this);
		
		et_addblacknumber_number = (EditText) findViewById(R.id.et_addbalcknumber_number);
		rg_mode_select = (RadioGroup) findViewById(R.id.rg_mode_select);
		bt_addblack_confirm = (Button) findViewById(R.id.bt_addblack_confirm);
		bt_addblack_cancel = (Button) findViewById(R.id.bt_addblack_cancel);
		
		final int mode_id = rg_mode_select.getCheckedRadioButtonId();
		//设置点击确定事件添加数据库记录
		bt_addblack_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String blacknumber = et_addblacknumber_number.getText().toString().trim();
				String modecommit = "1";
				
				switch (mode_id) {
				case R.id.rb_mode_all://数据库mode写入1
					modecommit = "1" ;
					break;

				case R.id.rb_mode_msg://数据库mode写入2
					modecommit = "2" ;
					break;
				case R.id.rb_mode_tel://数据库mode写入3
					modecommit = "3" ;
					break;
				}
				
				if(TextUtils.isEmpty(blacknumber)){
					Toast.makeText(addBlackNumberActivity.this, "请输入要拦截的号码", 0).show();
					return;
				}
				Log.i(TAG,"blacknumber");
				if(dao.find(blacknumber) != null){
					Toast.makeText(addBlackNumberActivity.this, "已设置拦截", 0).show();
					return;
				}else{
					Intent data = new Intent();
					long flag = dao.add(blacknumber, modecommit);
					System.out.println(blacknumber+":"+modecommit);
					if(flag != -1){
						data.putExtra("blacknumber", blacknumber);
						data.putExtra("mode",modecommit);
						data.putExtra("isSuccess", true);
						setResult(0, data);
						finish();
					}else{
						data.putExtra("isSuccess", false);
						setResult(0, data);
						finish();
					}
				}
				
			}
		});
		//设置点击取消事件响应
		bt_addblack_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
