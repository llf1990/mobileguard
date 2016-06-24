package com.android.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.ContactInfo;
import com.android.mobileguard.utils.ContactInfoUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ContactSelectActivity extends Activity {
	
	private ListView lv_contact_select; 
	private List<ContactInfo> list;
	private LinearLayout ll_loading;
	
	
	
	public ContactSelectActivity() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectcontact);
		
		lv_contact_select = (ListView) findViewById(R.id.lv_select_contact);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			@Override
			public void run() {
				list =  ContactInfoUtils.getAllContactInfos(ContactSelectActivity.this);
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						lv_contact_select.setAdapter(new ContactAdapter());
					}
					
				});
			}
			
		}.start();
		
		lv_contact_select.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phoneNum = list.get(position).getPhone();
				Intent intent = new Intent();
				intent.putExtra("phoneNum", phoneNum);
				setResult(0, intent);
				finish();
			}
		});
		
	}
	private class ContactAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(ContactSelectActivity.this, R.layout.item_contact, null);
			TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
			TextView tv_contact_phoneNum = (TextView) view.findViewById(R.id.tv_contact_phoneNum);
			tv_contact_name.setText(list.get(position).getName());
			tv_contact_phoneNum.setText(list.get(position).getPhone());
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		
		
	}
}
