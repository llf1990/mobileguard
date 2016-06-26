package com.android.mobileguard.activities;


import com.android.mobileguard.R;
import com.android.mobileguard.db.dao.AddressDBDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CallerLocQueryActivity extends Activity {
	
	protected static final String TAG = "CallerLocQueryActivity";
	private EditText et_locaitonquery_number;
	private Button bt_locationquery_query;
	private TextView tv_callerloc_result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callerloc_query);
		
		et_locaitonquery_number = (EditText) findViewById(R.id.et_locaitonquery_number);
		tv_callerloc_result = (TextView) findViewById(R.id.tv_callerloc_result);
		bt_locationquery_query = (Button) findViewById(R.id.bt_locationquery_query);
		
		
		bt_locationquery_query.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String queryNumber = et_locaitonquery_number.getText().toString().trim();
				if(TextUtils.isEmpty(queryNumber)){
					Toast.makeText(CallerLocQueryActivity.this, "请输入号码", 0).show();
					return;
				}else{
					String result = AddressDBDao.findLocation(queryNumber);
					Log.i(TAG, "查询结果："+result);
					tv_callerloc_result.setText(result);
				}
			}
		});
	}
}
