package com.android.mobileguard.activities;

import java.util.List;

import com.android.mobileguard.R;
import com.android.mobileguard.bean.AppInfo;
import com.android.mobileguard.engine.AppInfoProvider;
import com.android.mobileguard.utils.SystemInfoUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AppManagerActivity extends Activity {
	
	private static final String TAG = "AppManagerActivity";
	private TextView tv_internal_freesize;
	private TextView tv_external_freesize;
	private ListView lv_appmanger_applist;
	private List<AppInfo> list;
	private MyAdapter adapter;
	private ProgressBar pb;
	private TextView tv_appman_loading;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanager);
		
		tv_internal_freesize = (TextView) findViewById(R.id.tv_internal_freesize);
		tv_external_freesize = (TextView) findViewById(R.id.tv_external_freesize);
		lv_appmanger_applist = (ListView) findViewById(R.id.lv_appmanger_applist);
		pb = (ProgressBar) findViewById(R.id.pb_appman_loading);
		tv_appman_loading = (TextView) findViewById(R.id.tv_appman_loading);
		
		
		
		String internalsize = Formatter.formatFileSize(this, SystemInfoUtils.getInternalStorageAvailableSize());
		String externalsize = Formatter.formatFileSize(this, SystemInfoUtils.getExternalStorageAvailableSize());
		
		tv_internal_freesize.setText("内存可用："+internalsize);
		tv_external_freesize.setText("sd卡可用："+externalsize);
		adapter = new MyAdapter();
		new Thread(){
			public void run(){
				pb.setVisibility(View.VISIBLE);
				tv_appman_loading.setVisibility(View.VISIBLE);
				list = AppInfoProvider.getAllAppInfo(AppManagerActivity.this);
				runOnUiThread( new Runnable() {
					public void run() {
					lv_appmanger_applist.setAdapter(adapter);
					pb.setVisibility(View.INVISIBLE);
					tv_appman_loading.setVisibility(View.INVISIBLE);
				}
				
			});
		}
		}.start();
		
		
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder;
			if(convertView == null){
				view = View.inflate(AppManagerActivity.this, R.layout.item_appmanager, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_appicon);
				holder.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
				holder.tv_appsize= (TextView) view.findViewById(R.id.tv_appsize);
				holder.iv_install_location = (ImageView) view.findViewById(R.id.iv_appman_installloc);
				if(list.get(position).isInRom() == true){
					holder.iv_install_location.setImageResource(R.drawable.memory);
				}else{
					holder.iv_install_location.setImageResource(R.drawable.sd);
				}
				long tempsize =  list.get(position).getAppSize();
				String size = Formatter.formatFileSize(AppManagerActivity.this,tempsize);
				holder.tv_appsize.setText("程序大小："+size);
				holder.iv_icon.setImageDrawable(list.get(position).getAppIcon());
				holder.tv_appname.setText(list.get(position).getAppName());
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			
			return view;
		}
		private class ViewHolder{
			ImageView iv_icon;
			TextView tv_appname;
			TextView tv_appsize;
			ImageView iv_install_location;
		}
	}
}
